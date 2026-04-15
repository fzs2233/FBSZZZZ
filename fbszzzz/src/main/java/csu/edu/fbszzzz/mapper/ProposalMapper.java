package csu.edu.fbszzzz.mapper;

import csu.edu.fbszzzz.entity.Proposal;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 提案Mapper接口
 */
@Mapper
public interface ProposalMapper {

    /**
     * 插入新提案
     */
    @Insert("INSERT INTO proposal (title, description, proposer_id, vote_type, max_choices, status, start_time, end_time, create_time) " +
            "VALUES (#{title}, #{description}, #{proposerId}, #{voteType}, #{maxChoices}, #{status}, #{startTime}, #{endTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Proposal proposal);

    /**
     * 根据ID查询提案
     */
    @Select("SELECT id, title, description, proposer_id, vote_type, max_choices, status, start_time, end_time, create_time " +
            "FROM proposal WHERE id = #{id}")
    @Results({
        @Result(property = "proposerId", column = "proposer_id"),
        @Result(property = "voteType", column = "vote_type"),
        @Result(property = "maxChoices", column = "max_choices"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "createTime", column = "create_time")
    })
    Proposal findById(Long id);

    /**
     * 查询所有提案
     */
    @Select("SELECT id, title, description, proposer_id, vote_type, max_choices, status, start_time, end_time, create_time " +
            "FROM proposal ORDER BY create_time DESC")
    @Results({
        @Result(property = "proposerId", column = "proposer_id"),
        @Result(property = "voteType", column = "vote_type"),
        @Result(property = "maxChoices", column = "max_choices"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "createTime", column = "create_time")
    })
    List<Proposal> findAll();

    /**
     * 根据状态查询提案
     */
    @Select("SELECT id, title, description, proposer_id, vote_type, max_choices, status, start_time, end_time, create_time " +
            "FROM proposal WHERE status = #{status} ORDER BY create_time DESC")
    @Results({
        @Result(property = "proposerId", column = "proposer_id"),
        @Result(property = "voteType", column = "vote_type"),
        @Result(property = "maxChoices", column = "max_choices"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "createTime", column = "create_time")
    })
    List<Proposal> findByStatus(Integer status);

    /**
     * 根据提案人ID查询提案
     */
    @Select("SELECT id, title, description, proposer_id, vote_type, max_choices, status, start_time, end_time, create_time " +
            "FROM proposal WHERE proposer_id = #{proposerId} ORDER BY create_time DESC")
    @Results({
        @Result(property = "proposerId", column = "proposer_id"),
        @Result(property = "voteType", column = "vote_type"),
        @Result(property = "maxChoices", column = "max_choices"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "createTime", column = "create_time")
    })
    List<Proposal> findByProposerId(Long proposerId);

    /**
     * 更新提案信息
     */
    @Update("UPDATE proposal SET title = #{title}, description = #{description}, " +
            "vote_type = #{voteType}, max_choices = #{maxChoices}, status = #{status}, " +
            "start_time = #{startTime}, end_time = #{endTime} WHERE id = #{id}")
    int update(Proposal proposal);

    /**
     * 更新提案状态
     */
    @Update("UPDATE proposal SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 删除提案
     */
    @Delete("DELETE FROM proposal WHERE id = #{id}")
    int deleteById(Long id);
}
