package com.user.growth.reward.behavior.context.domain;

import com.user.growth.reward.behavior.context.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 行为事件领域模型
 *
 * 描述用户的学习行为事件，包含事件基本信息和扩展属性
 *
 * @author system
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件ID（唯一标识）
     */
    private String eventId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 事件类型
     */
    private EventType eventType;

    /**
     * 课程ID（如果有）
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;

    /**
     * 事件来源 (web/ios/android)
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
     * 扩展属性（存储其他自定义数据）
     * 例如：视频时长、答题数量、互动类型等
     */
    private Map<String, Object> extend;

    /**
     * 事件处理状态 0:待处理 1:已处理 2:处理失败
     */
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
}
