package csu.edu.fbszzzz.mapper;

import csu.edu.fbszzzz.entity.VoteOption;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 投票选项Mapper接口
 */
@Mapper
public interface VoteOptionMapper {

    /**
     * 插入新选项
     */
    @Insert("INSERT INTO vote_option (proposal_id, option_text, option_order, vote_count) " +
            "VALUES (#{proposalId}, #{optionText}, #{optionOrder}, #{voteCount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(VoteOption option);

    /**
     * 根据ID查询选项
     */
    @Select("SELECT id, proposal_id, option_text, option_order, vote_count, created_at " +
            "FROM vote_option WHERE id = #{id}")
    @Results({
        @Result(property = "proposalId", column = "proposal_id"),
        @Result(property = "optionText", column = "option_text"),
        @Result(property = "optionOrder", column = "option_order"),
        @Result(property = "voteCount", column = "vote_count"),
        @Result(property = "createdAt", column = "created_at")
    })
    VoteOption findById(Long id);

    /**
     * 根据提案ID查询所有选项
     */
    @Select("SELECT id, proposal_id, option_text, option_order, vote_count, created_at " +
            "FROM vote_option WHERE proposal_id = #{proposalId} ORDER BY option_order ASC")
    @Results({
        @Result(property = "proposalId", column = "proposal_id"),
        @Result(property = "optionText", column = "option_text"),
        @Result(property = "optionOrder", column = "option_order"),
        @Result(property = "voteCount", column = "vote_count"),
        @Result(property = "createdAt", column = "created_at")
    })
    List<VoteOption> findByProposalId(Long proposalId);

    /**
     * 更新选项信息
     */
    @Update("UPDATE vote_option SET option_text = #{optionText}, option_order = #{optionOrder} " +
            "WHERE id = #{id}")
    int update(VoteOption option);

    /**
     * 增加得票数
     */
    @Update("UPDATE vote_option SET vote_count = vote_count + 1 WHERE id = #{id}")
    int incrementVoteCount(Long id);

    /**
     * 删除提案的所有选项
     */
    @Delete("DELETE FROM vote_option WHERE proposal_id = #{proposalId}")
    int deleteByProposalId(Long proposalId);

    /**
     * 删除选项
     */
    @Delete("DELETE FROM vote_option WHERE id = #{id}")
    int deleteById(Long id);
}
