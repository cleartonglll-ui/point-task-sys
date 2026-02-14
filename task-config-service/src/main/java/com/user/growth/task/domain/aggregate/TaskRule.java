package com.user.growth.task.domain.aggregate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务规则聚合根
 * 领域层：核心业务逻辑，管理任务规则的完整生命周期
 * 
 * 该聚合根负责管理任务规则的创建、配置、状态变更等核心业务逻辑，
 * 确保任务规则配置的完整性和一致性，包括时间范围、奖励配置、状态管理等。
 */
@Data
@TableName("t_task_rule")
public class TaskRule {
    
    @TableId(type = IdType.AUTO)
    private Long id;              // 任务规则ID
    
    private String taskCode;      // 任务编码（唯一标识）
    private String taskName;      // 任务名称
    private String eventType;     // 关联的事件类型
    private String taskExpression; // Aviator表达式（用于复杂条件判断）
    private Integer rewardType;   // 奖励类型（积分、金币、成就等）
    private Integer rewardAmount; // 奖励数量
    private Integer status;       // 任务状态：0-禁用, 1-启用
    private LocalDateTime startTime; // 任务开始时间
    private LocalDateTime endTime;   // 任务结束时间
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间

    /**
     * 领域方法：校验任务是否有效
     * 验证任务规则的基本配置是否完整有效
     * 
     * @return 任务配置是否有效
     */
    public boolean isValid() {
        return taskCode != null && !taskCode.isEmpty() 
            && eventType != null && !eventType.isEmpty()
            && rewardAmount != null && rewardAmount > 0;
    }

    /**
     * 领域方法：判断任务是否在有效期内
     * 检查当前时间是否在任务的有效时间段内
     * 
     * @return 任务是否在有效期内
     */
    public boolean isWithinTimeRange() {
        LocalDateTime now = LocalDateTime.now();
        if (startTime != null && now.isBefore(startTime)) {
            return false;
        }
        if (endTime != null && now.isAfter(endTime)) {
            return false;
        }
        return true;
    }
}