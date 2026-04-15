package csu.edu.fbszzzz.service;

import csu.edu.fbszzzz.entity.Proposal;
import csu.edu.fbszzzz.entity.VoteOption;
import csu.edu.fbszzzz.entity.VoteRecord;
import csu.edu.fbszzzz.mapper.ProposalMapper;
import csu.edu.fbszzzz.mapper.VoteOptionMapper;
import csu.edu.fbszzzz.mapper.VoteRecordMapper;
import csu.edu.fbszzzz.mapper.MemberMapper;
import csu.edu.fbszzzz.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提案服务层
 */
@Service
public class ProposalService {

    @Autowired
    private ProposalMapper proposalMapper;

    @Autowired
    private VoteOptionMapper voteOptionMapper;

    @Autowired
    private VoteRecordMapper voteRecordMapper;

    @Autowired
    private MemberMapper memberMapper;

    /**
     * 创建提案（直接进入投票状态）
     * @param proposal 提案信息
     * @param options 投票选项列表
     * @param proposerId 提案人ID
     * @return 创建成功后的提案
     */
    @Transactional
    public Proposal createProposal(Proposal proposal, List<String> options, Long proposerId) {
        // 检查用户权限（是否是管理员）
        Member proposer = memberMapper.findById(proposerId);
        if (proposer == null) {
            throw new RuntimeException("用户不存在");
        }
        if (proposer.getRoleId() == null || proposer.getRoleId() != 1L) {
            throw new RuntimeException("只有管理员可以发起提案");
        }

        proposal.setProposerId(proposerId);
        proposal.setStatus(1); // 直接进入投票状态
        proposal.setCreateTime(LocalDateTime.now());
        proposal.setStartTime(LocalDateTime.now()); // 设置投票开始时间

        // 设置默认值
        if (proposal.getVoteType() == null) {
            proposal.setVoteType(1); // 默认单选
        }
        if (proposal.getMaxChoices() == null) {
            proposal.setMaxChoices(1);
        }

        proposalMapper.insert(proposal);

        // 创建投票选项
        if (options == null || options.isEmpty()) {
            throw new RuntimeException("提案至少需要一个投票选项");
        }
        for (int i = 0; i < options.size(); i++) {
            VoteOption option = new VoteOption();
            option.setProposalId(proposal.getId());
            option.setOptionText(options.get(i));
            option.setOptionOrder(i + 1);
            option.setVoteCount(0);
            voteOptionMapper.insert(option);
        }

        return proposal;
    }

    /**
     * 结束投票
     * @param proposalId 提案ID
     * @param proposerId 提案人ID（用于权限校验）
     */
    @Transactional
    public void endVoting(Long proposalId, Long proposerId) {
        Proposal proposal = proposalMapper.findById(proposalId);
        if (proposal == null) {
            throw new RuntimeException("提案不存在");
        }
        if (!proposal.getProposerId().equals(proposerId)) {
            // 检查是否是管理员
            Member member = memberMapper.findById(proposerId);
            if (member == null || member.getRoleId() == null || member.getRoleId() != 1L) {
                throw new RuntimeException("只有提案人或管理员可以结束投票");
            }
        }
        if (proposal.getStatus() != 1) {
            throw new RuntimeException("只有进行中的提案可以结束投票");
        }

        proposal.setStatus(2); // 已结束
        proposal.setEndTime(LocalDateTime.now());
        proposalMapper.update(proposal);
    }

    /**
     * 用户投票
     * @param proposalId 提案ID
     * @param memberId 投票人ID
     * @param optionIds 选项ID列表
     */
    @Transactional
    public void vote(Long proposalId, Long memberId, List<Long> optionIds) {
        Proposal proposal = proposalMapper.findById(proposalId);
        if (proposal == null) {
            throw new RuntimeException("提案不存在");
        }
        if (proposal.getStatus() != 1) {
            throw new RuntimeException("提案不在投票状态");
        }

        // 检查投票时间范围
        LocalDateTime now = LocalDateTime.now();
        if (proposal.getStartTime() != null && now.isBefore(proposal.getStartTime())) {
            throw new RuntimeException("投票尚未开始");
        }
        if (proposal.getEndTime() != null && now.isAfter(proposal.getEndTime())) {
            throw new RuntimeException("投票已结束");
        }

        // 检查选项数量
        if (optionIds == null || optionIds.isEmpty()) {
            throw new RuntimeException("请选择投票选项");
        }

        // 单选检查
        if (proposal.getVoteType() == 1 && optionIds.size() > 1) {
            throw new RuntimeException("单选提案只能选择一个选项");
        }

        // 多选检查
        if (proposal.getVoteType() == 2 && optionIds.size() > proposal.getMaxChoices()) {
            throw new RuntimeException("超过最多可选数量，最多可选 " + proposal.getMaxChoices() + " 个");
        }

        // 单选：检查是否已投票
        if (proposal.getVoteType() == 1) {
            List<VoteRecord> existingRecords = voteRecordMapper.findByProposalAndMember(proposalId, memberId);
            if (!existingRecords.isEmpty()) {
                throw new RuntimeException("您已参与该提案的投票");
            }
        }

        // 多选：检查已投票数量
        if (proposal.getVoteType() == 2) {
            List<VoteRecord> existingRecords = voteRecordMapper.findByProposalAndMember(proposalId, memberId);
            int alreadyVotedCount = existingRecords.size();
            if (alreadyVotedCount + optionIds.size() > proposal.getMaxChoices()) {
                throw new RuntimeException("您已投 " + alreadyVotedCount + " 票，最多还可投 " +
                    (proposal.getMaxChoices() - alreadyVotedCount) + " 票");
            }
        }

        // 验证选项ID属于该提案，并记录已处理的选项防止重复
        java.util.Set<Long> processedOptionIds = new java.util.HashSet<>();
        for (Long optionId : optionIds) {
            if (processedOptionIds.contains(optionId)) {
                throw new RuntimeException("不能重复选择同一选项");
            }
            processedOptionIds.add(optionId);

            VoteOption option = voteOptionMapper.findById(optionId);
            if (option == null || !option.getProposalId().equals(proposalId)) {
                throw new RuntimeException("选项ID " + optionId + " 不属于该提案");
            }

            // 记录投票
            VoteRecord record = new VoteRecord();
            record.setProposalId(proposalId);
            record.setMemberId(memberId);
            record.setOptionId(optionId);
            record.setVoteTime(LocalDateTime.now());
            voteRecordMapper.insert(record);

            // 更新选项得票数
            voteOptionMapper.incrementVoteCount(optionId);
        }
    }

    /**
     * 查询提案详情
     */
    public Proposal findById(Long id) {
        return proposalMapper.findById(id);
    }

    /**
     * 查询所有提案
     */
    public List<Proposal> findAll() {
        return proposalMapper.findAll();
    }

    /**
     * 根据状态查询提案
     */
    public List<Proposal> findByStatus(Integer status) {
        return proposalMapper.findByStatus(status);
    }

    /**
     * 获取提案的投票选项
     */
    public List<VoteOption> getOptions(Long proposalId) {
        return voteOptionMapper.findByProposalId(proposalId);
    }

    /**
     * 获取投票结果
     */
    public List<VoteOption> getVoteResult(Long proposalId) {
        List<VoteOption> options = voteOptionMapper.findByProposalId(proposalId);
        // 按得票数降序排序
        options.sort((a, b) -> b.getVoteCount().compareTo(a.getVoteCount()));
        return options;
    }

    /**
     * 查看用户自己的投票记录
     */
    public List<VoteRecord> getMyVotes(Long proposalId, Long memberId) {
        return voteRecordMapper.findByProposalAndMember(proposalId, memberId);
    }
}
