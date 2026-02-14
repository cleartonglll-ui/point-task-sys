package com.user.growth.reward.task.context.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 任务规则数据对象
 *
 * 对应数据库表 task_rule
 *
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("task_rule")
public class TaskRuleDO extends com.user.growth.reward.common.BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
     * 任务类型 1:签到 2:作业 3:视频 4:互动 5:答题 6:邀请
     */
    private Integer taskType;

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
}
