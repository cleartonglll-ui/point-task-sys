package com.vivo.pointtask.stage2.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 【演化阶段二】任务实体类 - 引入行为模型
 * 
 * 演化逻辑（对应文章阶段二）：
 * 1. 引入行为模型：任务与行为关联，不再依赖业务方自行实现
 * 2. 支持实验能力：增加实验ID字段，支持灰度测试
 * 3. 支持标签投放：增加目标用户标签配置
 * 
 * 核心改进：
 * - 任务配置中增加了behaviorId，关联到具体的行为定义
 * - 系统可以通过SDK采集埋点，自动判定任务是否达成
 * - 业务方无需再自行实现行为采集和判定逻辑
 * 
 * @author vivo积分任务系统
 * @version 2.0.0
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
     * 【演化新增】关联的行为ID
     * 演化说明：任务与行为解耦，通过behaviorId关联到行为定义
     * 这样系统可以统一处理行为采集和判定
     */
    private Long behaviorId;
    
    /**
     * 奖励积分
     */
    private Integer rewardPoints;
    
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
     * 【演化新增】实验ID，支持灰度测试
     * 演化说明：支持A/B测试，可以控制任务对特定用户群可见
     */
    private String experimentId;
    
    /**
     * 【演化新增】目标用户标签列表
     * 演化说明：支持按标签投放，如只给"新用户"或"VIP用户"展示
     */
    private List<String> targetUserTags;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ==================== 充血模型开始：实体封装业务行为 ====================
    
    /**
     * 【演化改进】判断任务是否有效
     * 演化说明：将判定逻辑封装在实体中，体现业务语义
     * 这是从贫血模型向充血模型演化的第一步
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
     * 【演化新增】检查用户是否符合任务投放条件
     * 演化说明：支持标签投放，只有符合标签条件的用户才能看到任务
     * 
     * @param userTags 用户拥有的标签
     * @return 是否符合条件
     */
    public boolean matchesUserTags(List<String> userTags) {
        // 如果没有配置目标标签，则对所有用户可见
        if (targetUserTags == null || targetUserTags.isEmpty()) {
            return true;
        }
        
        // 用户必须有至少一个目标标签
        return userTags != null && 
               targetUserTags.stream().anyMatch(userTags::contains);
    }
    
    /**
     * 【演化新增】检查用户是否在实验组中
     * 演化说明：支持灰度测试，只有实验组用户才能看到任务
     * 
     * @param userExperimentGroup 用户所属的实验组
     * @return 是否在实验组
     */
    public boolean isInExperimentGroup(String userExperimentGroup) {
        // 如果没有配置实验，则对所有用户可见
        if (experimentId == null || experimentId.isEmpty()) {
            return true;
        }
        
        // 检查用户是否在实验组中（简化实现）
        return experimentId.equals(userExperimentGroup);
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
