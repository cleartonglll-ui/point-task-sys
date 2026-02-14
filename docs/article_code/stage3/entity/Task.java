package com.vivo.pointtask.stage3.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 【演化阶段三】任务实体类 - 完整充血模型
 * 
 * 演化逻辑（对应文章阶段三）：
 * 1. 支持多源数据采集：不仅支持埋点，还支持数据库、消息队列、API等
 * 2. 引入表达式引擎：支持动态规则配置，无需修改代码
 * 3. 丰富触达形式：支持自定义弹窗、消息透传等
 * 
 * 核心改进：
 * - 数据源配置化，支持多种数据源类型
 * - 规则表达式化，支持复杂业务逻辑
 * - 触达方式配置化，支持多种触达形式
 * 
 * 这是从贫血模型到充血模型演化的最终形态。
 * 实体不仅包含数据，还封装了完整的业务行为和验证逻辑。
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public class Task {
    
    /**
     * 任务ID
     */
    private Long id;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 任务描述
     */
    private String description;
    
    /**
     * 任务类型：1-每日任务 2-一次性任务 3-周期性任务
     */
    private Integer taskType;
    
    /**
     * 关联的行为ID
     */
    private Long behaviorId;
    
    /**
     * 奖励积分
     */
    private Integer rewardPoints;
    
    /**
     * 【演化新增】动态奖励表达式
     * 演化说明：支持根据行为数据动态计算奖励，如"消费金额的1%"
     * 示例表达式："event.amount * 0.01"
     */
    private String rewardExpression;
    
    /**
     * 任务开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 任务结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 任务状态：0-禁用 1-启用
     */
    private Integer status;
    
    /**
     * 实验ID，支持灰度测试
     */
    private String experimentId;
    
    /**
     * 目标用户标签列表
     */
    private List<String> targetUserTags;
    
    /**
     * 【演化新增】触达配置
     * 演化说明：配置任务完成后的触达方式（弹窗、消息等）
     */
    private TouchConfig touchConfig;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ==================== 充血模型：完整的业务行为封装 ====================
    
    /**
     * 【演化改进】判断任务是否有效
     * 演化说明：完整的业务验证，包含状态、时间窗口检查
     * 
     * @return 是否有效
     */
    public boolean isValid() {
        if (status == null || status != 1) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        if (startTime != null && now.isBefore(startTime)) {
            return false;
        }
        
        if (endTime != null && now.isAfter(endTime)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 【演化改进】检查用户是否符合任务投放条件
     * 
     * @param userTags 用户拥有的标签
     * @return 是否符合条件
     */
    public boolean matchesUserTags(List<String> userTags) {
        if (targetUserTags == null || targetUserTags.isEmpty()) {
            return true;
        }
        
        return userTags != null && 
               targetUserTags.stream().anyMatch(userTags::contains);
    }
    
    /**
     * 【演化改进】检查用户是否在实验组中
     * 
     * @param userExperimentGroup 用户所属的实验组
     * @return 是否在实验组
     */
    public boolean isInExperimentGroup(String userExperimentGroup) {
        if (experimentId == null || experimentId.isEmpty()) {
            return true;
        }
        
        return experimentId.equals(userExperimentGroup);
    }
    
    /**
     * 【演化新增】判断是否需要触达用户
     * 
     * @return 是否需要触达
     */
    public boolean shouldNotifyUser() {
        return touchConfig != null && touchConfig.isEnabled();
    }
    
    /**
     * 【演化新增】获取触达方式
     * 
     * @return 触达方式类型
     */
    public TouchType getTouchType() {
        if (touchConfig == null) {
            return TouchType.NONE;
        }
        return touchConfig.getTouchType();
    }
    
    /**
     * 【演化新增】计算奖励积分
     * 演化说明：支持固定奖励和动态奖励（通过表达式计算）
     * 
     * @param context 奖励计算上下文，包含事件数据等
     * @return 奖励积分
     */
    public int calculateReward(RewardContext context) {
        // 如果有动态奖励表达式，使用表达式计算
        if (rewardExpression != null && !rewardExpression.isEmpty()) {
            // 实际应调用表达式引擎计算
            // 这里简化处理
            return evaluateRewardExpression(rewardExpression, context);
        }
        
        // 否则返回固定奖励
        return rewardPoints != null ? rewardPoints : 0;
    }
    
    /**
     * 【内部方法】评估奖励表达式
     * 
     * @param expression 表达式
     * @param context 上下文
     * @return 计算结果
     */
    private int evaluateRewardExpression(String expression, RewardContext context) {
        // 简化实现：实际应使用Aviator等表达式引擎
        // 示例：如果表达式是 "event.amount / 100"，则返回 amount/100
        if (expression.contains("event.amount")) {
            Double amount = context.getEventAmount();
            if (amount != null) {
                // 简化解析，实际应使用表达式引擎
                if (expression.contains("/ 100")) {
                    return (int) (amount / 100);
                } else if (expression.contains("* 0.01")) {
                    return (int) (amount * 0.01);
                }
            }
        }
        return rewardPoints != null ? rewardPoints : 0;
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getTaskType() {
        return taskType;
    }
    
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }
    
    public Long getBehaviorId() {
        return behaviorId;
    }
    
    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }
    
    public Integer getRewardPoints() {
        return rewardPoints;
    }
    
    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
    
    public String getRewardExpression() {
        return rewardExpression;
    }
    
    public void setRewardExpression(String rewardExpression) {
        this.rewardExpression = rewardExpression;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getExperimentId() {
        return experimentId;
    }
    
    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }
    
    public List<String> getTargetUserTags() {
        return targetUserTags;
    }
    
    public void setTargetUserTags(List<String> targetUserTags) {
        this.targetUserTags = targetUserTags;
    }
    
    public TouchConfig getTouchConfig() {
        return touchConfig;
    }
    
    public void setTouchConfig(TouchConfig touchConfig) {
        this.touchConfig = touchConfig;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
