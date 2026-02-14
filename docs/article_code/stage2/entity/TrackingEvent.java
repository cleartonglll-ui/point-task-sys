package com.vivo.pointtask.stage2.entity;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 【演化阶段二】埋点事件实体 - SDK采集的数据结构
 * 
 * 演化逻辑（对应文章阶段二）：
 * 定义埋点事件的数据结构，用于SDK采集和上报用户行为。
 * 
 * 核心字段：
 * 1. eventName: 事件名称，对应Behavior中定义的eventName
 * 2. userId: 用户ID，用于关联用户
 * 3. parameters: 事件参数，用于行为匹配和过滤
 * 4. timestamp: 事件发生时间
 * 
 * @author vivo积分任务系统
 * @version 2.0.0
 */
public class TrackingEvent {
    
    /**
     * 事件ID
     */
    private Long id;
    
    /**
     * 事件名称
     */
    private String eventName;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 设备ID
     */
    private String deviceId;
    
    /**
     * 事件参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 事件发生时间
     */
    private LocalDateTime timestamp;
    
    /**
     * 接收时间
     */
    private LocalDateTime receiveTime;
    
    public TrackingEvent() {
    }
    
    public TrackingEvent(String eventName, Long userId, Map<String, Object> parameters) {
        this.eventName = eventName;
        this.userId = userId;
        this.parameters = parameters;
        this.timestamp = LocalDateTime.now();
    }
    
    // ==================== Getter/Setter ====================
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    
    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public LocalDateTime getReceiveTime() {
        return receiveTime;
    }
    
    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }
}
