package csu.edu.fbszzzz.service;

import csu.edu.fbszzzz.entity.Member;
import csu.edu.fbszzzz.mapper.MemberMapper;
import csu.edu.fbszzzz.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 成员服务层
 */
@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RoleService roleService;
    
    /**
     * 成员注册
     * @param member 成员信息
     * @return 注册成功后的成员（包含生成的ID）
     */
    @Transactional
    public Member register(Member member) {
        // 检查用户名是否已存在
        Member existingMember = memberMapper.findByUsername(member.getUsername());
        if (existingMember != null) {
            throw new RuntimeException("用户名已存在: " + member.getUsername());
        }
        
        // 设置注册时间和默认状态
        member.setRegisterTime(LocalDateTime.now());
        member.setStatus(1); // 默认正常状态
        if (member.getRoleId() == null) {
            member.setRoleId(2L); // 默认为普通成员角色
        }
        
        // 插入数据库
        memberMapper.insert(member);
        
        return member;
    }
    
    /**
     * 根据ID查询成员
     * @param id 成员ID
     * @return 成员信息
     */
    public Member findById(Long id) {
        return memberMapper.findById(id);
    }
    
    /**
     * 根据用户名查询成员
     * @param username 用户名
     * @return 成员信息
     */
    public Member findByUsername(String username) {
        return memberMapper.findByUsername(username);
    }
    
    /**
     * 查询所有成员
     * @return 成员列表
     */
    public List<Member> findAll() {
        return memberMapper.findAll();
    }
    
    /**
     * 更新成员信息
     * @param member 成员信息
     * @return 更新结果
     */
    @Transactional
    public boolean update(Member member) {
        return memberMapper.update(member) > 0;
    }
    
    /**
     * 删除成员
     * @param id 成员ID
     * @return 删除结果
     */
    @Transactional
    public boolean delete(Long id) {
        return memberMapper.deleteById(id) > 0;
    }

    /**
     * 修改用户角色
     * @param memberId 要修改的用户ID
     * @param roleId 新角色ID
     * @param operatorId 操作者ID
     * @return 修改结果
     */
    @Transactional
    public boolean updateRole(Long memberId, Long roleId, Long operatorId) {
        // 检查操作者是否是管理员
        Member operator = memberMapper.findById(operatorId);
        if (operator == null) {
            throw new RuntimeException("操作者不存在");
        }
        if (!roleService.isAdmin(operator.getRoleId())) {
            throw new RuntimeException("只有管理员可以修改用户角色");
        }

        // 检查目标用户是否存在
        Member member = memberMapper.findById(memberId);
        if (member == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查角色是否存在
        if (roleService.findById(roleId) == null) {
            throw new RuntimeException("角色不存在");
        }

        return memberMapper.updateRole(memberId, roleId) > 0;
    }
}