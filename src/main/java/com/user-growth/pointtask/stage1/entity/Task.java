package com.vivo.pointtask.stage1.entity;

import java.time.LocalDateTime;

/**
 * 【演化阶段一】任务实体类 - 贫血模型
 * 
 * 演化说明：
 * 这是积分任务系统的初始版本，采用典型的"贫血模型"设计。
 * 实体类只包含数据字段和getter/setter，没有任何业务行为。
 * 所有的业务逻辑都散落在Service层，导致业务逻辑与数据分离。
 * 
 * 架构痛点：
 * 1. 实体类只是数据容器，无法体现业务语义
 * 2. 业务逻辑散落在各处，难以维护
 * 3. 跨项目协作需要业务方自行实现任务判定逻辑
 * 
 * @author vivo积分任务系统
 * @version 1.0.0
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
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // ==================== 贫血模型的典型特征：只有getter/setter ====================
    
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
