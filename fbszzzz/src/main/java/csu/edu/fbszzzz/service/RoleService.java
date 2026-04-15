package csu.edu.fbszzzz.service;

import csu.edu.fbszzzz.entity.Role;
import csu.edu.fbszzzz.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务层
 */
@Service
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 创建角色
     * @param role 角色信息
     * @return 创建成功后的角色
     */
    @Transactional
    public Role create(Role role) {
        Role existingRole = roleMapper.findByRoleName(role.getRoleName());
        if (existingRole != null) {
            throw new RuntimeException("角色名称已存在: " + role.getRoleName());
        }
        roleMapper.insert(role);
        return role;
    }

    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色信息
     */
    public Role findById(Long id) {
        return roleMapper.findById(id);
    }

    /**
     * 根据角色名称查询角色
     * @param roleName 角色名称
     * @return 角色信息
     */
    public Role findByRoleName(String roleName) {
        return roleMapper.findByRoleName(roleName);
    }

    /**
     * 查询所有角色
     * @return 角色列表
     */
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

    /**
     * 更新角色信息
     * @param role 角色信息
     * @return 更新结果
     */
    @Transactional
    public boolean update(Role role) {
        return roleMapper.update(role) > 0;
    }

    /**
     * 删除角色
     * @param id 角色ID
     * @return 删除结果
     */
    @Transactional
    public boolean delete(Long id) {
        return roleMapper.deleteById(id) > 0;
    }

    /**
     * 检查用户是否是管理员
     * @param roleId 角色ID
     * @return 是否是管理员
     */
    public boolean isAdmin(Long roleId) {
        Role role = roleMapper.findById(roleId);
        return role != null && "ADMIN".equals(role.getRoleName());
    }
}
