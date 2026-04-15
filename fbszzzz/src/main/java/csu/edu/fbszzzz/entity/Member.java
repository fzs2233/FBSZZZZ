package csu.edu.fbszzzz.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 成员实体类
 */
@Data
public class Member {
    /**
     * 成员ID（主键，自动生成）
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 注册时间
     */
    private LocalDateTime registerTime;
    
    /**
     * 状态（0-禁用，1-正常）
     */
    private Integer status;
}