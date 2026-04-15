package csu.edu.fbszzzz.mapper;

import csu.edu.fbszzzz.entity.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 角色Mapper接口
 */
@Mapper
public interface RoleMapper {

    /**
     * 插入新角色
     */
    @Insert("INSERT INTO role (role_name, description) VALUES (#{roleName}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Role role);

    /**
     * 根据ID查询角色
     */
    @Select("SELECT id, role_name, description, created_at, updated_at FROM role WHERE id = #{id}")
    @Results({
        @Result(property = "roleName", column = "role_name"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    Role findById(Long id);

    /**
     * 根据角色名称查询角色
     */
    @Select("SELECT id, role_name, description, created_at, updated_at FROM role WHERE role_name = #{roleName}")
    @Results({
        @Result(property = "roleName", column = "role_name"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    Role findByRoleName(String roleName);

    /**
     * 查询所有角色
     */
    @Select("SELECT id, role_name, description, created_at, updated_at FROM role")
    @Results({
        @Result(property = "roleName", column = "role_name"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<Role> findAll();

    /**
     * 更新角色信息
     */
    @Update("UPDATE role SET role_name = #{roleName}, description = #{description} WHERE id = #{id}")
    int update(Role role);

    /**
     * 删除角色
     */
    @Delete("DELETE FROM role WHERE id = #{id}")
    int deleteById(Long id);
}
