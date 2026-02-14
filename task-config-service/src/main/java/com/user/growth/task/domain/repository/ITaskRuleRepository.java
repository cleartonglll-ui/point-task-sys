package com.user.growth.task.domain.repository;

import com.user.growth.task.domain.aggregate.TaskRule;

import java.util.List;

/**
 * 任务规则仓储接口
 * 领域层：定义仓储契约
 */
public interface ITaskRuleRepository {
    
    /**
     * 保存任务规则
     */
    TaskRule save(TaskRule taskRule);
    
    /**
     * 根据ID查询
     */
    TaskRule findById(Long id);
    
    /**
     * 查询所有启用的任务
     */
    List<TaskRule> findEnabledTasks();
    
    /**
     * 根据事件类型查询任务
     */
    List<TaskRule> findByEventType(String eventType);
}
