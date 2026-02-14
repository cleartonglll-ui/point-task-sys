package com.user.growth.collection.domain.aggregate;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * 行为事件聚合根
 * 领域层：核心业务逻辑，保证数据一致性
 * 
 * 该聚合根负责管理用户行为事件的生命周期，确保事件数据的完整性和有效性。
 * 主要处理行为事件的采集、校验、状态管理等核心业务逻辑。
 */
@Data
@Document(collection = "behavior_event")
public class BehaviorEvent {
    
    @Id
    private String id;           // MongoDB文档ID
    
    private String eventId;      // 事件唯一标识符
    private Long userId;         // 用户ID
    private String eventType;    // 事件类型（如：签到、观看视频、完成作业等）
    private String source;       // 事件来源（APP、WEB、小程序等）
    private LocalDateTime eventTime; // 事件发生时间
    private String eventData;    // 事件具体数据（JSON格式）
    private String deviceId;     // 设备ID
    private String ipAddress;    // IP地址
    private LocalDateTime collectionTime; // 采集时间
    private Integer status;      // 事件处理状态：0-待处理, 1-已处理

    /**
     * 领域方法：校验事件有效性
     * 确保关键字段不为空，保证数据完整性
     * 
     * @return 事件是否有效
     */
    public boolean isValid() {
        return userId != null && eventType != null && !eventType.isEmpty();
    }

    /**
     * 领域方法：标记为已处理
     * 更新事件状态为已处理，并记录处理时间
     */
    public void markAsProcessed() {
        this.status = 1;
        this.collectionTime = LocalDateTime.now();
    }
}