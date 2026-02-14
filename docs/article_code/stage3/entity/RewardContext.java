package com.vivo.pointtask.stage3.entity;

import java.util.Map;

/**
 * 【演化阶段三】奖励计算上下文
 * 
 * 演化逻辑（对应文章阶段三）：
 * 支持动态奖励计算，通过表达式引擎根据事件数据计算奖励。
 * 
 * 使用场景：
 * - 消费返积分：根据消费金额计算奖励
 * - 阅读返积分：根据阅读时长计算奖励
 * - 邀请返积分：根据邀请人数计算奖励
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public class RewardContext {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 事件金额（用于消费类任务）
     */
    private Double eventAmount;
    
    /**
     * 事件数量（用于计数类任务）
     */
    private Integer eventCount;
    
    /**
     * 事件时长，单位秒（用于时长类任务）
     */
    private Long eventDuration;
    
    /**
     * 原始事件数据
     */
    private Map<String, Object> rawEventData;
    
    /**
     * 扩展参数
     */
    private Map<String, Object> extraParams;
    
    // ==================== 建造者模式 ====================
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private RewardContext context = new RewardContext();
        
        public Builder userId(Long userId) {
            context.userId = userId;
            return this;
        }
        
        public Builder taskId(Long taskId) {
            context.taskId = taskId;
            return this;
        }
        
        public Builder eventAmount(Double amount) {
            context.eventAmount = amount;
            return this;
        }
        
        public Builder eventCount(Integer count) {
            context.eventCount = count;
            return this;
        }
        
        public Builder eventDuration(Long duration) {
            context.eventDuration = duration;
            return this;
        }
        
        public Builder rawEventData(Map<String, Object> data) {
            context.rawEventData = data;
            return this;
        }
        
        public Builder extraParams(Map<String, Object> params) {
            context.extraParams = params;
            return this;
        }
        
        public RewardContext build() {
            return context;
        }
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public Double getEventAmount() {
        return eventAmount;
    }
    
    public void setEventAmount(Double eventAmount) {
        this.eventAmount = eventAmount;
    }
    
    public Integer getEventCount() {
        return eventCount;
    }
    
    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }
    
    public Long getEventDuration() {
        return eventDuration;
    }
    
    public void setEventDuration(Long eventDuration) {
        this.eventDuration = eventDuration;
    }
    
    public Map<String, Object> getRawEventData() {
        return rawEventData;
    }
    
    public void setRawEventData(Map<String, Object> rawEventData) {
        this.rawEventData = rawEventData;
    }
    
    public Map<String, Object> getExtraParams() {
        return extraParams;
    }
    
    public void setExtraParams(Map<String, Object> extraParams) {
        this.extraParams = extraParams;
    }
}
