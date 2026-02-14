package com.vivo.pointtask.stage3.service;

import com.vivo.pointtask.stage3.collector.DataSourceConfig;
import com.vivo.pointtask.stage3.collector.DataSourceType;
import com.vivo.pointtask.stage3.engine.ExpressionEngine;
import com.vivo.pointtask.stage3.entity.*;
import com.vivo.pointtask.stage3.repository.TaskRepository;
import com.vivo.pointtask.stage3.repository.UserTaskRepository;
import com.vivo.pointtask.stage3.rule.RuleEngine;
import com.vivo.pointtask.stage3.rule.RuleResult;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 【演化阶段三】任务服务 - 完整充血模型
 * 
 * 演化逻辑（对应文章阶段三）：
 * 这是积分任务系统的最终形态，具备以下能力：
 * 1. 多源数据采集：埋点、数据库、MQ、API
 * 2. 表达式引擎：动态规则配置，无需修改代码
 * 3. 复杂行为计算：支持根据事件数据动态计算奖励
 * 4. 丰富触达形式：Toast、Snackbar、弹窗、消息推送
 * 
 * 架构特点：
 * - 充血模型：业务逻辑封装在实体中
 * - 配置化：通过配置而非代码实现业务逻辑
 * - 可扩展：新增数据源和规则无需修改核心代码
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public class TaskService {
    
    private TaskRepository taskRepository;
    private UserTaskRepository userTaskRepository;
    private RuleEngine ruleEngine;
    private ExpressionEngine expressionEngine;
    
    public TaskService(TaskRepository taskRepository, 
                      UserTaskRepository userTaskRepository,
                      RuleEngine ruleEngine,
                      ExpressionEngine expressionEngine) {
        this.taskRepository = taskRepository;
        this.userTaskRepository = userTaskRepository;
        this.ruleEngine = ruleEngine;
        this.expressionEngine = expressionEngine;
    }
    
    /**
     * 【核心方法】处理行为事件（支持多源数据）
     * 
     * 演化说明（对应文章阶段三核心改进）：
     * 这是系统的核心入口，处理来自不同数据源的行为事件：
     * 1. 埋点数据（阶段二已支持）
     * 2. 数据库数据（阶段三新增）
     * 3. 消息队列数据（阶段三新增）
     * 4. API数据（阶段三新增）
     * 
     * 处理流程：
     * 1. 数据预处理（表达式过滤）
     * 2. 数据归一化
     * 3. 规则计算
     * 4. 任务进度更新
     * 5. 奖励发放
     * 6. 用户触达
     * 
     * @param dataSource 数据源配置
     * @param rawEventData 原始事件数据
     */
    public void processEvent(DataSourceConfig dataSource, Map<String, Object> rawEventData) {
        // 1. 数据预处理 - 使用表达式过滤
        // 演化说明：对应文章中的"数据清洗过滤"表达式
        if (!preprocessData(dataSource, rawEventData)) {
            System.out.println("[TaskService] 数据预处理未通过，跳过处理");
            return;
        }
        
        // 2. 数据归一化
        Map<String, Object> normalizedData = dataSource.normalizeData(rawEventData);
        
        // 3. 查找关联的任务
        List<Task> tasks = findMatchingTasks(dataSource, normalizedData);
        
        for (Task task : tasks) {
            processTaskWithEvent(task, normalizedData);
        }
    }
    
    /**
     * 【核心方法】处理任务（使用表达式引擎）
     * 
     * 演化说明（对应文章阶段三核心改进）：
     * 使用表达式引擎进行规则计算，支持复杂业务逻辑。
     * 
     * 示例场景：
     * - 消费返积分：根据消费金额计算奖励
     * - 游戏充值：根据充值金额和产品类型计算不同奖励
     * 
     * @param task 任务
     * @param eventData 事件数据
     */
    private void processTaskWithEvent(Task task, Map<String, Object> eventData) {
        Long userId = extractUserId(eventData);
        if (userId == null) {
            System.out.println("[TaskService] 无法获取用户ID，跳过处理");
            return;
        }
        
        // 检查任务有效性（充血模型）
        if (!task.isValid()) {
            System.out.println("[TaskService] 任务无效: taskId=" + task.getId());
            return;
        }
        
        // 检查用户标签投放
        List<String> userTags = getUserTags(userId);
        if (!task.matchesUserTags(userTags)) {
            System.out.println("[TaskService] 用户不在投放标签范围内: userId=" + userId);
            return;
        }
        
        // 检查实验组
        String userExpGroup = getUserExperimentGroup(userId);
        if (!task.isInExperimentGroup(userExpGroup)) {
            System.out.println("[TaskService] 用户不在实验组: userId=" + userId);
            return;
        }
        
        // 获取或创建用户任务记录
        UserTask userTask = userTaskRepository.findByUserIdAndTaskId(userId, task.getId());
        if (userTask == null) {
            userTask = autoClaimTask(userId, task);
        }
        
        if (userTask.isCompleted()) {
            System.out.println("[TaskService] 任务已完成: userId=" + userId + ", taskId=" + task.getId());
            return;
        }
        
        // 【演化核心】使用表达式引擎计算规则
        // 演化说明：对应文章中的"规则计算"表达式
        int rewardPoints = calculateRewardWithExpression(task, eventData);
        
        // 更新任务进度
        updateTaskProgress(userTask, rewardPoints);
        
        // 发放奖励
        grantReward(userId, task, rewardPoints);
        
        // 触达用户
        if (task.shouldNotifyUser()) {
            notifyUser(userId, task, rewardPoints);
        }
    }
    
    /**
     * 【演化核心】数据预处理
     * 
     * 演化说明（对应文章阶段三）：
     * 使用Aviator表达式进行数据过滤。
     * 
     * 示例表达式：
     * "originEvent.pay_status == 1 && string.contains('11,12,13', originEvent.product_type)"
     * 
     * @param dataSource 数据源配置
     * @param rawData 原始数据
     * @return 是否通过预处理
     */
    private boolean preprocessData(DataSourceConfig dataSource, Map<String, Object> rawData) {
        String preprocessExpression = dataSource.getPreprocessExpression();
        
        if (preprocessExpression == null || preprocessExpression.isEmpty()) {
            return true; // 无预处理表达式，直接通过
        }
        
        Map<String, Object> context = new HashMap<>();
        context.put("originEvent", rawData);
        
        return expressionEngine.evaluateBoolean(preprocessExpression, context);
    }
    
    /**
     * 【演化核心】使用表达式计算奖励
     * 
     * 演化说明（对应文章阶段三）：
     * 支持动态奖励计算，通过表达式引擎根据事件数据计算奖励。
     * 
     * 示例：
     * - 固定奖励：rewardPoints = 10
     * - 动态奖励：rewardExpression = "event.amount / 100"
     *   消费100元返1积分，消费500元返5积分
     * 
     * @param task 任务
     * @param eventData 事件数据
     * @return 奖励积分
     */
    private int calculateRewardWithExpression(Task task, Map<String, Object> eventData) {
        // 构建奖励计算上下文
        RewardContext context = RewardContext.builder()
                .userId(extractUserId(eventData))
                .taskId(task.getId())
                .rawEventData(eventData)
                .build();
        
        // 提取金额信息
        Object amount = eventData.get("amount");
        if (amount instanceof Number) {
            context.setEventAmount(((Number) amount).doubleValue());
        }
        
        // 使用Task的充血模型方法计算奖励
        return task.calculateReward(context);
    }
    
    /**
     * 【内部方法】查找匹配的任务
     * 
     * @param dataSource 数据源
     * @param eventData 事件数据
     * @return 任务列表
     */
    private List<Task> findMatchingTasks(DataSourceConfig dataSource, Map<String, Object> eventData) {
        // 简化实现：根据数据源类型查找任务
        // 实际应根据行为ID关联
        return taskRepository.findAll().stream()
                .filter(Task::isValid)
                .toList();
    }
    
    /**
     * 【内部方法】自动领取任务
     */
    private UserTask autoClaimTask(Long userId, Task task) {
        UserTask userTask = new UserTask();
        userTask.setUserId(userId);
        userTask.setTaskId(task.getId());
        userTask.setStatus(0);
        userTask.setProgress(0);
        userTask.setTargetValue(1);
        userTask.setCreateTime(LocalDateTime.now());
        userTask.setUpdateTime(LocalDateTime.now());
        
        userTaskRepository.save(userTask);
        System.out.println("[TaskService] 自动领取任务: userId=" + userId + ", taskId=" + task.getId());
        
        return userTask;
    }
    
    /**
     * 【内部方法】更新任务进度
     */
    private void updateTaskProgress(UserTask userTask, int rewardPoints) {
        int newProgress = userTask.getProgress() + 1;
        userTask.setProgress(newProgress);
        userTask.setUpdateTime(LocalDateTime.now());
        
        if (newProgress >= userTask.getTargetValue()) {
            userTask.setStatus(1);
            userTask.setCompleteTime(LocalDateTime.now());
        }
        
        userTaskRepository.update(userTask);
    }
    
    /**
     * 【内部方法】发放奖励
     */
    private void grantReward(Long userId, Task task, int points) {
        System.out.println("[TaskService] 发放奖励: userId=" + userId + 
                          ", taskId=" + task.getId() + 
                          ", points=" + points);
        // 实际应调用积分服务
    }
    
    /**
     * 【演化改进】触达用户（支持多种方式）
     * 
     * 演化说明（对应文章阶段三）：
     * 支持Toast、Snackbar、弹窗、消息推送等多种触达方式。
     * 
     * @param userId 用户ID
     * @param task 任务
     * @param points 奖励积分
     */
    private void notifyUser(Long userId, Task task, int points) {
        TouchConfig touchConfig = task.getTouchConfig();
        if (touchConfig == null || !touchConfig.isValid()) {
            return;
        }
        
        // 渲染模板变量
        Map<String, String> variables = new HashMap<>();
        variables.put("taskName", task.getTaskName());
        variables.put("points", String.valueOf(points));
        
        String content = touchConfig.renderContent(variables);
        
        switch (task.getTouchType()) {
            case TOAST:
                System.out.println("[TaskService] [Toast] userId=" + userId + ", message=" + content);
                break;
            case SNACKBAR:
                System.out.println("[TaskService] [Snackbar] userId=" + userId + ", message=" + content);
                break;
            case DIALOG:
                System.out.println("[TaskService] [Dialog] userId=" + userId + 
                                  ", title=" + touchConfig.getTitle() + 
                                  ", message=" + content);
                break;
            case NOTIFICATION:
                System.out.println("[TaskService] [Notification] userId=" + userId + 
                                  ", title=" + touchConfig.getTitle() + 
                                  ", message=" + content);
                break;
            default:
                break;
        }
    }
    
    /**
     * 【内部方法】从事件数据中提取用户ID
     */
    private Long extractUserId(Map<String, Object> eventData) {
        Object userId = eventData.get("userId");
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        return null;
    }
    
    /**
     * 【模拟方法】获取用户标签
     */
    private List<String> getUserTags(Long userId) {
        return List.of("new_user", "active");
    }
    
    /**
     * 【模拟方法】获取用户实验组
     */
    private String getUserExperimentGroup(Long userId) {
        return "exp_001";
    }
}
