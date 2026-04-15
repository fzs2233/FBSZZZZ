package csu.edu.fbszzzz.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 投票选项实体类
 */
@Data
public class VoteOption {
    /**
     * 选项ID（主键，自动生成）
     */
    private Long id;

    /**
     * 所属提案ID
     */
    private Long proposalId;

    /**
     * 选项文本
     */
    private String optionText;

    /**
     * 选项排序
     */
    private Integer optionOrder;

    /**
     * 得票数
     */
    private Integer voteCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
