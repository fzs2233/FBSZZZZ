package csu.edu.fbszzzz.mapper;

import csu.edu.fbszzzz.entity.VoteRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 投票记录Mapper接口
 */
@Mapper
public interface VoteRecordMapper {

    /**
     * 插入新投票记录
     */
    @Insert("INSERT INTO vote_record (proposal_id, member_id, option_id, vote_time) " +
            "VALUES (#{proposalId}, #{memberId}, #{optionId}, #{voteTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VoteRecord record);

    /**
     * 根据提案ID和成员ID查询投票记录
     */
    @Select("SELECT id, proposal_id, member_id, option_id, vote_time " +
            "FROM vote_record WHERE proposal_id = #{proposalId} AND member_id = #{memberId}")
    @Results({
        @Result(property = "proposalId", column = "proposal_id"),
        @Result(property = "memberId", column = "member_id"),
        @Result(property = "optionId", column = "option_id"),
        @Result(property = "voteTime", column = "vote_time")
    })
    List<VoteRecord> findByProposalAndMember(@Param("proposalId") Long proposalId, @Param("memberId") Long memberId);

    /**
     * 根据提案ID查询所有投票记录
     */
    @Select("SELECT id, proposal_id, member_id, option_id, vote_time " +
            "FROM vote_record WHERE proposal_id = #{proposalId}")
    @Results({
        @Result(property = "proposalId", column = "proposal_id"),
        @Result(property = "memberId", column = "member_id"),
        @Result(property = "optionId", column = "option_id"),
        @Result(property = "voteTime", column = "vote_time")
    })
    List<VoteRecord> findByProposalId(Long proposalId);

    /**
     * 根据选项ID查询投票记录
     */
    @Select("SELECT id, proposal_id, member_id, option_id, vote_time " +
            "FROM vote_record WHERE option_id = #{optionId}")
    @Results({
        @Result(property = "proposalId", column = "proposal_id"),
        @Result(property = "memberId", column = "member_id"),
        @Result(property = "optionId", column = "option_id"),
        @Result(property = "voteTime", column = "vote_time")
    })
    List<VoteRecord> findByOptionId(Long optionId);

    /**
     * 统计提案的总投票人数
     */
    @Select("SELECT COUNT(DISTINCT member_id) FROM vote_record WHERE proposal_id = #{proposalId}")
    int countVotersByProposalId(Long proposalId);
}
