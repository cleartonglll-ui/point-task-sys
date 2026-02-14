package com.vivo.pointtask.stage2.service;

import com.vivo.pointtask.stage2.entity.Behavior;
import com.vivo.pointtask.stage2.entity.Task;
import com.vivo.pointtask.stage2.entity.TrackingEvent;
import com.vivo.pointtask.stage2.entity.UserTask;
import com.vivo.pointtask.stage2.repository.BehaviorRepository;
import com.vivo.pointtask.stage2.repository.TaskRepository;
import com.vivo.pointtask.stage2.repository.UserTaskRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 【演化阶段二】行为服务 - 核心服务
 * 
 * 演化逻辑（对应文章阶段二）：
 * 行为服务负责处理SDK上报的行为事件，进行任务判定和奖励发放。
 * 
 * 核心职责：
 * 1. 管理行为定义
 * 2. 接收SDK上报的事件
 * 3. 匹配关联的任务
 * 4. 更新任务进度
 * 5. 发放奖励
 * 
 * 业务价值：
 * - 业务方无需自行实现行为采集和判定
 * - 系统统一处理，逻辑集中可复用
 * - 上线周期大幅缩短
 * 
 * @author vivo积分任务系统
 * @version 2.0.0
 */
public class BehaviorService {
    
    private BehaviorRepository behaviorRepository;
    private TaskRepository taskRepository;
    private UserTaskRepository userTaskRepository;
    
    public BehaviorService(BehaviorRepository behaviorRepository, 
                          TaskRepository taskRepository,
                          UserTaskRepository userTaskRepository) {
        this.behaviorRepository = behaviorRepository;
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
    }
    
    /**
     * 【核心方法】获取所有有效行为配置
     * 
     * 演化说明：
     * SDK通过此方法拉取行为配置，用于端侧匹配
     * 
     * @return 有效行为列表
     */
    public List<Behavior> getActiveBehaviors() {
        List<Behavior> behaviors = behaviorRepository.findAll();
        
        // 使用充血模型的自我验证能力
        return behaviors.stream()
                .filter(Behavior::isValid)
                .toList();
    }
    
    /**
     * 【核心方法】处理行为事件
     * 
     * 演化说明：
     * SDK上报事件后，此方法负责：
     * 1. 查找关联的任务
     * 2. 检查任务投放策略（标签、实验）
     * 3. 更新任务进度
     * 4. 发放奖励
     * 
     * @param userId 用户ID
     * @param behaviorId 行为ID
     * @param event 埋点事件
     */
    public void processBehaviorEvent(Long userId, Long behaviorId, TrackingEvent event) {
        // 查找关联的任务
        List<Task> tasks = taskRepository.findByBehaviorId(behaviorId);
        
        for (Task task : tasks) {
            // 使用充血模型验证任务有效性
            if (!task.isValid()) {
                continue;
            }
            
            // 【演化新增】检查用户标签投放
            // 简化实现：实际应从用户服务获取用户标签
            List<String> userTags = getUserTags(userId);
            if (!task.matchesUserTags(userTags)) {
                System.out.println("[BehaviorService] 用户不在任务投放标签范围内: userId=" + userId + ", taskId=" + task.getId());
                continue;
            }
            
            // 【演化新增】检查实验组
            // 简化实现：实际应从实验平台获取用户实验组
            String userExpGroup = getUserExperimentGroup(userId);
            if (!task.isInExperimentGroup(userExpGroup)) {
                System.out.println("[BehaviorService] 用户不在实验组: userId=" + userId + ", taskId=" + task.getId());
                continue;
            }
            
            // 处理任务进度
            processTaskProgress(userId, task);
        }
    }
    
    /**
     * 【内部方法】处理任务进度
     * 
     * @param userId 用户ID
     * @param task 任务
     */
    private void processTaskProgress(Long userId, Task task) {
        UserTask userTask = userTaskRepository.findByUserIdAndTaskId(userId, task.getId());
        
        if (userTask == null) {
            // 自动领取任务（简化实现）
            userTask = autoClaimTask(userId, task);
        }
        
        if (userTask == null || userTask.getStatus() != 0) {
            // 任务不存在或已完成
            return;
        }
        
        // 更新进度
        int newProgress = userTask.getProgress() + 1;
        userTask.setProgress(newProgress);
        userTask.setUpdateTime(LocalDateTime.now());
        
        // 检查是否完成
        if (newProgress >= userTask.getTargetValue()) {
            userTask.setStatus(1); // 已完成
            userTask.setCompleteTime(LocalDateTime.now());
            System.out.println("[BehaviorService] 任务完成: userId=" + userId + ", taskId=" + task.getId());
            
            // 自动发放奖励（简化实现）
            grantReward(userId, task);
        }
        
        userTaskRepository.update(userTask);
    }
    
    /**
     * 【内部方法】自动领取任务
     * 
     * 演化说明：
     * 某些任务可以设置为自动领取，用户无需手动领取
     * 
     * @param userId 用户ID
     * @param task 任务
     * @return 用户任务记录
     */
    private UserTask autoClaimTask(Long userId, Task task) {
        // 简化实现：所有任务都自动领取
        UserTask userTask = new UserTask();
        userTask.setUserId(userId);
        userTask.setTaskId(task.getId());
        userTask.setStatus(0); // 未完成
        userTask.setProgress(0);
        userTask.setTargetValue(1); // 默认目标值
        userTask.setCreateTime(LocalDateTime.now());
        userTask.setUpdateTime(LocalDateTime.now());
        
        userTaskRepository.save(userTask);
        System.out.println("[BehaviorService] 自动领取任务: userId=" + userId + ", taskId=" + task.getId());
        
        return userTask;
    }
    
    /**
     * 【内部方法】发放奖励
     * 
     * @param userId 用户ID
     * @param task 任务
     */
    private void grantReward(Long userId, Task task) {
        // 简化实现：实际应调用积分服务发放奖励
        System.out.println("[BehaviorService] 发放奖励: userId=" + userId + ", taskId=" + task.getId() + ", points=" + task.getRewardPoints());
        
        // 触达用户（简化实现）
        notifyUser(userId, task);
    }
    
    /**
     * 【内部方法】通知用户
     * 
     * 演化说明：
     * 阶段二只支持简单的Toast和Snackbar通知
     * 阶段三会扩展为支持自定义弹窗
     * 
     * @param userId 用户ID
     * @param task 任务
     */
    private void notifyUser(Long userId, Task task) {
        // 简化实现：打印日志代替实际通知
        System.out.println("[BehaviorService] 通知用户: userId=" + userId + ", message=恭喜完成任务[" + task.getTaskName() + "],获得" + task.getRewardPoints() + "积分");
    }
    
    /**
     * 【模拟方法】获取用户标签
     * 
     * @param userId 用户ID
     * @return 用户标签列表
     */
    private List<String> getUserTags(Long userId) {
        // 简化实现：实际应从用户标签服务获取
        return List.of("new_user", "active");
    }
    
    /**
     * 【模拟方法】获取用户实验组
     * 
     * @param userId 用户ID
     * @return 实验组ID
     */
    private String getUserExperimentGroup(Long userId) {
        // 简化实现：实际应从实验平台获取
        return "exp_001";
    }
}
