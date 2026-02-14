package com.vivo.pointtask.stage1.service;

import com.vivo.pointtask.stage1.entity.Task;
import com.vivo.pointtask.stage1.entity.UserTask;
import com.vivo.pointtask.stage1.repository.TaskRepository;
import com.vivo.pointtask.stage1.repository.UserTaskRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 【演化阶段一】任务服务类 - 贫血模型下的服务层
 * 
 * 演化说明：
 * 在贫血模型中，所有的业务逻辑都集中在Service层。
 * 这导致：
 * 1. Service类变得臃肿，包含大量过程式代码
 * 2. 业务逻辑与数据分离，无法体现面向对象设计
 * 3. 跨项目协作时，业务方需要重复实现类似的判定逻辑
 * 
 * 核心问题：
 * - 任务是否有效的判定逻辑散落在各个方法中
 * - 任务完成判定需要业务方自行实现
 * - 无法复用业务逻辑
 * 
 * @author vivo积分任务系统
 * @version 1.0.0
 */
public class TaskService {
    
    private TaskRepository taskRepository;
    private UserTaskRepository userTaskRepository;
    
    /**
     * 构造函数注入依赖
     */
    public TaskService(TaskRepository taskRepository, UserTaskRepository userTaskRepository) {
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
    }
    
    /**
     * 获取所有有效任务
     * 
     * 演化说明：
     * 这里包含了任务有效性的判定逻辑，但这种判定是过程式的，
     * 没有封装在Task实体中，导致逻辑无法复用。
     * 
     * @return 有效任务列表
     */
    public List<Task> getValidTasks() {
        List<Task> tasks = taskRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        
        // 演化痛点：任务有效性判定逻辑散落在Service中
        // 业务方如果需要判断任务是否有效，需要重复这段代码
        return tasks.stream()
                .filter(task -> task.getStatus() == 1)  // 状态启用
                .filter(task -> task.getStartTime() == null || !now.isBefore(task.getStartTime()))  // 已开始
                .filter(task -> task.getEndTime() == null || !now.isAfter(task.getEndTime()))  // 未结束
                .toList();
    }
    
    /**
     * 检查任务是否有效
     * 
     * 演化痛点：
     * 这个方法是过程式的辅助方法，没有与Task实体绑定。
     * 如果业务方直接操作Task，很容易忘记调用这个方法进行校验。
     * 
     * @param task 任务
     * @return 是否有效
     */
    public boolean isTaskValid(Task task) {
        if (task == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        // 演化痛点：同样的判定逻辑在多个地方重复
        return task.getStatus() == 1 &&
               (task.getStartTime() == null || !now.isBefore(task.getStartTime())) &&
               (task.getEndTime() == null || !now.isAfter(task.getEndTime()));
    }
    
    /**
     * 用户领取任务
     * 
     * 演化说明：
     * 任务领取的初始化逻辑由Service完成，UserTask实体只是被动接收数据。
     * 
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return 用户任务记录
     */
    public UserTask claimTask(Long userId, Long taskId) {
        Task task = taskRepository.findById(taskId);
        
        // 演化痛点：每次都需要手动调用校验方法
        if (!isTaskValid(task)) {
            throw new RuntimeException("任务不存在或已失效");
        }
        
        // 检查是否已领取
        UserTask existing = userTaskRepository.findByUserIdAndTaskId(userId, taskId);
        if (existing != null) {
            throw new RuntimeException("任务已领取");
        }
        
        // 演化痛点：UserTask的初始化逻辑完全由Service控制，实体无法自我保护
        UserTask userTask = new UserTask();
        userTask.setUserId(userId);
        userTask.setTaskId(taskId);
        userTask.setStatus(0);  // 未完成
        userTask.setProgress(0);
        userTask.setTargetValue(1);  // 默认目标值为1
        userTask.setCreateTime(LocalDateTime.now());
        userTask.setUpdateTime(LocalDateTime.now());
        
        userTaskRepository.save(userTask);
        return userTask;
    }
    
    /**
     * 完成任务（由业务方调用）
     * 
     * 演化痛点：
     * 这是最大的问题！任务完成的判定逻辑需要业务方自行实现。
     * 系统只提供了基础的配置能力，具体的"行为采集"和"达成判定"都由业务方完成。
     * 导致：
     * 1. 跨项目协作周期长
     * 2. 业务方逻辑重、开发成本高
     * 3. 一个季度上线不了几个任务
     * 
     * @param userId 用户ID
     * @param taskId 任务ID
     */
    public void completeTask(Long userId, Long taskId) {
        UserTask userTask = userTaskRepository.findByUserIdAndTaskId(userId, taskId);
        if (userTask == null) {
            throw new RuntimeException("用户未领取该任务");
        }
        
        if (userTask.getStatus() != 0) {
            throw new RuntimeException("任务状态异常");
        }
        
        // 更新为已完成
        userTask.setStatus(1);
        userTask.setProgress(userTask.getTargetValue());
        userTask.setCompleteTime(LocalDateTime.now());
        userTask.setUpdateTime(LocalDateTime.now());
        
        userTaskRepository.update(userTask);
    }
    
    /**
     * 领取任务奖励
     * 
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return 奖励积分
     */
    public Integer claimReward(Long userId, Long taskId) {
        UserTask userTask = userTaskRepository.findByUserIdAndTaskId(userId, taskId);
        if (userTask == null || userTask.getStatus() != 1) {
            throw new RuntimeException("任务未完成");
        }
        
        if (userTask.getStatus() == 2) {
            throw new RuntimeException("奖励已领取");
        }
        
        Task task = taskRepository.findById(taskId);
        
        // 更新为已领取
        userTask.setStatus(2);
        userTask.setRewardTime(LocalDateTime.now());
        userTask.setUpdateTime(LocalDateTime.now());
        userTaskRepository.update(userTask);
        
        return task.getRewardPoints();
    }
}
