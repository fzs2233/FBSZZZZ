package csu.edu.fbszzzz.controller;

import csu.edu.fbszzzz.entity.Proposal;
import csu.edu.fbszzzz.entity.VoteOption;
import csu.edu.fbszzzz.entity.VoteRecord;
import csu.edu.fbszzzz.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提案控制器
 */
@RestController
@RequestMapping("/api/proposals")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    /**
     * 创建提案
     * POST /api/proposals
     * 请求体示例：
     * {
     * "title": "提案标题",
     * "description": "提案描述",
     * "voteType": 1, // 1-单选，2-多选
     * "maxChoices": 1, // 多选时的最大可选数量
     * "winnersCount": 1, // 获胜选项数量（取票数前N名作为最终结果）
     * "options": ["选项1", "选项2", "选项3"]
     * }
     * 注意：需要在请求头中传递 proposerId (实际项目中应从Session/JWT获取)
     */
    @PostMapping
    public ResponseEntity<?> createProposal(
            @RequestBody ProposalCreateRequest request,
            @RequestHeader(value = "X-Member-Id", required = true) Long proposerId) {
        try {
            Proposal proposal = new Proposal();
            proposal.setTitle(request.getTitle());
            proposal.setDescription(request.getDescription());
            proposal.setVoteType(request.getVoteType());
            proposal.setMaxChoices(request.getMaxChoices() != null ? request.getMaxChoices() : 1);
            proposal.setWinnersCount(request.getWinnersCount() != null ? request.getWinnersCount() : 1);

            Proposal created = proposalService.createProposal(proposal, request.getOptions(), proposerId);

            Map<String, Object> result = new HashMap<>();
            result.put("id", created.getId());
            result.put("title", created.getTitle());
            result.put("status", created.getStatus());
            result.put("winnersCount", created.getWinnersCount());
            result.put("message", "提案创建成功，投票已开始");

            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 结束投票
     * POST /api/proposals/{id}/end
     */
    @PostMapping("/{id}/end")
    public ResponseEntity<?> endVoting(
            @PathVariable Long id,
            @RequestHeader(value = "X-Member-Id", required = true) Long proposerId) {
        try {
            proposalService.endVoting(id, proposerId);
            return ResponseEntity.ok("投票已结束");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 查询提案列表
     * GET /api/proposals
     * 可选参数：status (状态筛选)
     */
    @GetMapping
    public ResponseEntity<?> listProposals(
            @RequestParam(required = false) Integer status) {
        List<Proposal> proposals;
        if (status != null) {
            proposals = proposalService.findByStatus(status);
        } else {
            proposals = proposalService.findAll();
        }
        return ResponseEntity.ok(proposals);
    }

    /**
     * 查询提案详情
     * GET /api/proposals/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProposalDetail(@PathVariable Long id) {
        Proposal proposal = proposalService.findById(id);
        if (proposal == null) {
            return ResponseEntity.notFound().build();
        }

        List<VoteOption> options = proposalService.getOptions(id);

        Map<String, Object> result = new HashMap<>();
        result.put("proposal", proposal);
        result.put("options", options);

        return ResponseEntity.ok(result);
    }

    /**
     * 投票
     * POST /api/proposals/{id}/vote
     * 请求体示例：
     * {
     * "optionIds": [1, 2] // 选择的选项ID列表
     * }
     */
    @PostMapping("/{id}/vote")
    public ResponseEntity<?> vote(
            @PathVariable Long id,
            @RequestBody VoteRequest request,
            @RequestHeader(value = "X-Member-Id", required = true) Long memberId) {
        try {
            proposalService.vote(id, memberId, request.getOptionIds());
            return ResponseEntity.ok("投票成功");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 查看投票结果
     * GET /api/proposals/{id}/result
     */
    @GetMapping("/{id}/result")
    public ResponseEntity<?> getVoteResult(@PathVariable Long id) {
        Proposal proposal = proposalService.findById(id);
        if (proposal == null) {
            return ResponseEntity.notFound().build();
        }

        List<VoteOption> results = proposalService.getVoteResult(id);
        List<VoteOption> winners = proposalService.getWinners(id);

        Map<String, Object> result = new HashMap<>();
        result.put("proposal", proposal);
        result.put("results", results);
        result.put("winners", winners);

        return ResponseEntity.ok(result);
    }

    /**
     * 查看自己的投票记录
     * GET /api/proposals/{id}/my-vote
     */
    @GetMapping("/{id}/my-vote")
    public ResponseEntity<?> getMyVote(
            @PathVariable Long id,
            @RequestHeader(value = "X-Member-Id", required = true) Long memberId) {
        List<VoteRecord> records = proposalService.getMyVotes(id, memberId);
        return ResponseEntity.ok(records);
    }

    /**
     * 创建提案请求体
     */
    public static class ProposalCreateRequest {
        private String title;
        private String description;
        private Integer voteType;
        private Integer maxChoices;
        private Integer winnersCount;
        private List<String> options;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getVoteType() {
            return voteType;
        }

        public void setVoteType(Integer voteType) {
            this.voteType = voteType;
        }

        public Integer getMaxChoices() {
            return maxChoices;
        }

        public void setMaxChoices(Integer maxChoices) {
            this.maxChoices = maxChoices;
        }

        public Integer getWinnersCount() {
            return winnersCount;
        }

        public void setWinnersCount(Integer winnersCount) {
            this.winnersCount = winnersCount;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }
    }

    /**
     * 投票请求体
     */
    public static class VoteRequest {
        private List<Long> optionIds;

        public List<Long> getOptionIds() {
            return optionIds;
        }

        public void setOptionIds(List<Long> optionIds) {
            this.optionIds = optionIds;
        }
    }
}
