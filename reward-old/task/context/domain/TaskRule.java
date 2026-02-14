package com.user.growth.reward.task.context.domain;

import com.user.growth.reward.task.context.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 任务规则领域模型
 *
 * 描述任务的配置规则，包括奖励积分、完成限制等
 *
 * @author system
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID（对应数据库主键）
     */
    private Long id;

    /**
     * 任务编码
     */
    private String taskCode;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务类型
     */
    private TaskType taskType;

    /**
     * 奖励积分数
     */
    private Integer pointAward;

    /**
     * 每日完成次数限制
     */
    private Integer dailyLimit;

    /**
     * 状态 0:禁用 1:启用
     */
    private Integer status;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 优先级 数字越大优先级越高
     */
    private Integer priority;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 目标用户标签（逗号分隔）
     */
    private String targetTags;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 判断任务是否启用
     */
    public boolean isEnabled() {
        return this.status != null && this.status == 1;
    }

    /**
     * 判断任务是否在有效期内
     */
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        if (this.startTime != null && now.isBefore(this.startTime)) {
            return false;
        }
        if (this.endTime != null && now.isAfter(this.endTime)) {
            return false;
        }
        return true;
    }

    /**
     * 获取目标用户标签列表
     */
    public List<String> getTargetTagList() {
        if (this.targetTags == null || this.targetTags.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(this.targetTags.split(","));
    }

    /**
     * 判断任务是否适用于指定用户标签
     */
    public boolean matchesUserTags(List<String> userTags) {
        List<String> targetTags = getTargetTagList();
        if (targetTags.isEmpty()) {
            return true; // 无标签限制，所有用户都适用
        }
        return userTags != null && userTags.stream().anyMatch(targetTags::contains);
    }
}
