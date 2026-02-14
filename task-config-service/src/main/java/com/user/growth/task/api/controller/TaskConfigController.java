package com.user.growth.task.api.controller;

import com.user.growth.task.application.service.TaskConfigService;
import com.user.growth.task.domain.aggregate.TaskRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 任务配置接口
 * 接口层：处理HTTP请求
 * 
 * 该控制器提供任务规则的管理接口，包括任务创建、查询等功能，
 * 接收前端请求参数并转发给应用服务处理，确保任务配置功能的正常运作。
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskConfigController {

    @Autowired
    private TaskConfigService taskConfigService;

    /**
     * 创建任务
     * 接收任务配置信息并创建新的任务规则
     * 
     * @param taskRule 任务规则数据对象
     * @return 创建结果状态
     */
    @PostMapping
    public String createTask(@RequestBody TaskRule taskRule) {
        taskConfigService.createTask(taskRule);
        return "success";
    }

    /**
     * 查询任务列表
     * 获取所有可用的任务规则列表
     * 
     * @return 任务规则列表
     */
    @GetMapping
    public List<TaskRule> getTasks() {
        return taskConfigService.getAllTasks();
    }

    /**
     * 健康检查
     * 检查服务运行状态
     * 
     * @return 服务健康状态
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}