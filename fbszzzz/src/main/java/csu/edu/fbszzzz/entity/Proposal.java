package csu.edu.fbszzzz.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 提案实体类
 */
@Data
public class Proposal {
    /**
     * 提案ID（主键，自动生成）
     */
    private Long id;

    /**
     * 提案标题
     */
    private String title;

    /**
     * 提案描述
     */
    private String description;

    /**
     * 提案人ID
     */
    private Long proposerId;

    /**
     * 投票类型：1-单选，2-多选
     */
    private Integer voteType;

    /**
     * 多选时最多可选数量
     */
    private Integer maxChoices;

    /**
     * 状态：0-草稿，1-进行中，2-已结束
     */
    private Integer status;

    /**
     * 投票开始时间
     */
    private LocalDateTime startTime;

    /**
     * 投票结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
