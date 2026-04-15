package csu.edu.fbszzzz.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 投票记录实体类
 */
@Data
public class VoteRecord {
    /**
     * 记录ID（主键，自动生成）
     */
    private Long id;

    /**
     * 提案ID
     */
    private Long proposalId;

    /**
     * 投票人ID
     */
    private Long memberId;

    /**
     * 选项ID
     */
    private Long optionId;

    /**
     * 投票时间
     */
    private LocalDateTime voteTime;
}
