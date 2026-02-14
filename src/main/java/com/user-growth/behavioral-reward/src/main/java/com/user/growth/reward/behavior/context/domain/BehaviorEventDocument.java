package com.user.growth.reward.behavior.context.domain;

import com.user.growth.reward.behavior.context.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 行为事件 MongoDB 文档
 *
 * 用于 MongoDB 存储用户行为事件原始数据
 * 支持灵活的扩展属性和高性能查询
 *
 * @author system
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "behavior_events")
public class BehaviorEventDocument {

    /**
     * MongoDB 主键
     */
    @Id
    private String id;

    /**
     * 事件ID（业务唯一标识）
     */
    @Indexed
    private String eventId;

    /**
     * 用户ID
     */
    @Indexed
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 事件类型
     */
    @Indexed
    private EventType eventType;

    /**
     * 事件类型码
     */
    private Integer eventTypeCode;

    /**
     * 课程ID
     */
    @Indexed
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 事件发生时间
     */
    @Indexed
    private LocalDateTime eventTime;

    /**
     * 事件来源
     */
    private String source;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 扩展属性
     */
    private Map<String, Object> extend;

    /**
     * 事件处理状态 0:待处理 1:已处理 2:处理失败
     */
    @Indexed
    private Integer status;

    /**
     * 处理时间
     */
    private LocalDateTime processTime;

    /**
     * 是否已发放积分
     */
    private Boolean pointAwarded;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
