package com.user.growth.reward.task.context.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户任务完成记录数据对象
 *
 * 对应数据库表 user_task_record
 *
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_task_record")
public class UserTaskRecordDO extends com.user.growth.reward.common.BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
}
