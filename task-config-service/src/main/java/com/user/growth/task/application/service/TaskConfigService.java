package com.user.growth.task.application.service;

import com.user.growth.task.domain.aggregate.TaskRule;
import com.user.growth.task.domain.repository.ITaskRuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务配置应用服务
 * 应用层：编排领域服务
 * 
 * 该应用服务负责协调任务规则的配置流程，包括任务创建、查询等功能，
 * 通过调用领域层的聚合根和仓储接口，确保任务配置业务逻辑的正确执行。
 */
@Slf4j
@Service
public class TaskConfigService {

    @Autowired
    private ITaskRuleRepository taskRuleRepository;

    /**
     * 创建任务
     * 处理新任务规则的创建流程，包括校验和保存
     * 
     * @param taskRule 要创建的任务规则对象
     */
    public void createTask(TaskRule taskRule) {
        log.info("创建任务: taskCode={}, eventType={}", taskRule.getTaskCode(), taskRule.getEventType());

        // 领域校验
        if (!taskRule.isValid()) {
            throw new IllegalArgumentException("任务配置无效");
        }

        // 保存到仓储
        taskRuleRepository.save(taskRule);
        log.info("任务创建成功: id={}", taskRule.getId());
    }

    /**
     * 获取所有任务
     * 查询所有启用状态的任务规则
     * 
     * @return 启用的任务规则列表
     */
    public List<TaskRule> getAllTasks() {
        return taskRuleRepository.findEnabledTasks();
    }

    /**
     * 根据事件类型获取任务
     * 查询指定事件类型对应的任务规则
     * 
     * @param eventType 事件类型
     * @return 匹配的任务规则列表
     */
    public List<TaskRule> getTasksByEventType(String eventType) {
        return taskRuleRepository.findByEventType(eventType);
    }
}