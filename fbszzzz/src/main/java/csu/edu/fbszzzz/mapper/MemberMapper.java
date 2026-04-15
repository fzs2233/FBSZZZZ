package csu.edu.fbszzzz.mapper;

import csu.edu.fbszzzz.entity.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 成员Mapper接口
 */
@Mapper
public interface MemberMapper {
    
    /**
     * 插入新成员
     */
    @Insert("INSERT INTO member (username, password, real_name, email, phone, role_id, register_time, status) " +
            "VALUES (#{username}, #{password}, #{realName}, #{email}, #{phone}, #{roleId}, #{registerTime}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Member member);
    
    /**
     * 根据ID查询成员
     */
    @Select("SELECT id, username, password, real_name, email, phone, role_id, register_time, status " +
            "FROM member WHERE id = #{id}")
    @Results({
        @Result(property = "realName", column = "real_name"),
        @Result(property = "roleId", column = "role_id"),
        @Result(property = "registerTime", column = "register_time")
    })
    Member findById(Long id);

    /**
     * 根据用户名查询成员
     */
    @Select("SELECT id, username, password, real_name, email, phone, role_id, register_time, status " +
            "FROM member WHERE username = #{username}")
    @Results({
        @Result(property = "realName", column = "real_name"),
        @Result(property = "roleId", column = "role_id"),
        @Result(property = "registerTime", column = "register_time")
    })
    Member findByUsername(String username);

    /**
     * 查询所有成员
     */
    @Select("SELECT id, username, password, real_name, email, phone, role_id, register_time, status FROM member")
    @Results({
        @Result(property = "realName", column = "real_name"),
        @Result(property = "roleId", column = "role_id"),
        @Result(property = "registerTime", column = "register_time")
    })
    List<Member> findAll();
    
    /**
     * 更新成员信息
     */
    @Update("UPDATE member SET username = #{username}, password = #{password}, " +
            "real_name = #{realName}, email = #{email}, phone = #{phone}, " +
            "role_id = #{roleId}, status = #{status} WHERE id = #{id}")
    int update(Member member);
    
    /**
     * 删除成员
     */
    @Delete("DELETE FROM member WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 更新成员角色
     */
    @Update("UPDATE member SET role_id = #{roleId} WHERE id = #{memberId}")
    int updateRole(@Param("memberId") Long memberId, @Param("roleId") Long roleId);
}