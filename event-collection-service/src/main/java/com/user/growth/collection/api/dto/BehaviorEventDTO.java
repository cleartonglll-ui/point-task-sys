package com.user.growth.collection.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 行为事件DTO
 * 接口层：数据传输对象
 */
@Data
public class BehaviorEventDTO {
    private String eventId;
    private Long userId;
    private String eventType;
    private String source;
    private LocalDateTime eventTime;
    private String eventData;
    private String deviceId;
    private String ipAddress;
}
