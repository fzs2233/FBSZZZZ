package csu.edu.fbszzzz.controller;

import csu.edu.fbszzzz.entity.Member;
import csu.edu.fbszzzz.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 成员控制器
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 成员登录接口
     * POST /api/members/login
     * 
     * @param loginRequest 登录请求体（包含username和password）
     * @return 登录结果（包含token和memberId）
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Member member = memberService.login(loginRequest.getUsername(), loginRequest.getPassword());
            if (member == null) {
                return ResponseEntity.status(401).body("用户名或密码错误");
            }

            // 生成 token（实际项目中应使用 JWT）
            String token = UUID.randomUUID().toString();

            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("memberId", member.getId());
            result.put("member", member);

            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 成员注册接口
     * POST /api/members/register
     * 
     * @param member 成员信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Member member) {
        try {
            Member registeredMember = memberService.register(member);
            // 清除密码，不返回给前端
            registeredMember.setPassword(null);
            return ResponseEntity.ok(registeredMember);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 登录请求体
     */
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * 根据ID查询成员
     * GET /api/members/{id}
     * 
     * @param id 成员ID
     * @return 成员信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Member> getById(@PathVariable Long id) {
        Member member = memberService.findById(id);
        if (member != null) {
            member.setPassword(null);
            return ResponseEntity.ok(member);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 查询所有成员
     * GET /api/members
     * 
     * @return 成员列表
     */
    @GetMapping
    public ResponseEntity<List<Member>> getAll() {
        List<Member> members = memberService.findAll();
        // 清除所有成员的密码
        members.forEach(m -> m.setPassword(null));
        return ResponseEntity.ok(members);
    }

    /**
     * 更新成员信息
     * PUT /api/members/{id}
     * 
     * @param id     成员ID
     * @param member 更新的成员信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Member member) {
        member.setId(id);
        boolean success = memberService.update(member);
        if (success) {
            return ResponseEntity.ok("更新成功");
        }
        return ResponseEntity.badRequest().body("更新失败");
    }

    /**
     * 删除成员
     * DELETE /api/members/{id}
     * 
     * @param id 成员ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        boolean success = memberService.delete(id);
        if (success) {
            return ResponseEntity.ok("删除成功");
        }
        return ResponseEntity.badRequest().body("删除失败");
    }

    /**
     * 修改用户角色
     * PUT /api/members/{id}/role
     * 
     * @param id         成员ID
     * @param request    角色请求体
     * @param operatorId 操作者ID（从请求头获取）
     * @return 修改结果
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @RequestBody RoleUpdateRequest request,
            @RequestHeader(value = "X-Member-Id", required = true) Long operatorId) {
        try {
            boolean success = memberService.updateRole(id, request.getRoleId(), operatorId);
            if (success) {
                return ResponseEntity.ok("角色修改成功");
            }
            return ResponseEntity.badRequest().body("角色修改失败");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 角色更新请求体
     */
    public static class RoleUpdateRequest {
        private Long roleId;

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }
    }
}