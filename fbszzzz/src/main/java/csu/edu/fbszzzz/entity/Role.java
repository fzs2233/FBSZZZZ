package csu.edu.fbszzzz.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色实体类
 */
@Data
public class Role {
    /**
     * 角色ID（主键，自动生成）
     */
    private Long id;

    /**
     * 角色名称（如 ADMIN, MEMBER）
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
