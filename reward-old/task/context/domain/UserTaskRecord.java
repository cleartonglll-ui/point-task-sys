package com.user.growth.reward.task.context.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户任务完成记录领域模型
 *
 * 记录用户完成任务的情况
 *
 * @author system
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID（对应数据库主键）
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 完成日期
     */
    private LocalDate completeDate;

    /**
     * 完成次数
     */
    private Integer completeCount;

    /**
     * 已奖励积分
     */
    private Integer pointAwarded;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 增加完成次数
     *
     * @param count 增加数量
     */
    public void incrementCompleteCount(Integer count) {
        if (this.completeCount == null) {
            this.completeCount = 0;
        }
        this.completeCount += count;
    }

    /**
     * 增加奖励积分
     *
     * @param points 增加积分数量
     */
    public void addAwardedPoints(Integer points) {
        if (this.pointAwarded == null) {
            this.pointAwarded = 0;
        }
        this.pointAwarded += points;
    }
}
