package com.user.growth.reward.behavior.context.enums;

import lombok.Getter;

/**
 * 行为事件类型枚举
 *
 * 定义所有支持的用户行为类型
 *
 * @author system
 * @since 1.0.0
 */
@Getter
public enum EventType {

    /**
     * 上课签到
     */
    SIGN_IN(1, "上课签到", "SIGN_IN"),

    /**
     * 完成作业
     */
    COMPLETE_HOMEWORK(2, "完成作业", "COMPLETE_HOMEWORK"),

    /**
     * 观看视频
     */
    WATCH_VIDEO(3, "观看视频", "WATCH_VIDEO"),

    /**
     * 课堂互动
     */
    CLASS_INTERACTION(4, "课堂互动", "CLASS_INTERACTION"),

    /**
     * 课后答题
     */
    QUIZ(5, "课后答题", "QUIZ"),

    /**
     * 邀请好友
     */
    INVITE_FRIEND(6, "邀请好友", "INVITE_FRIEND");

    /**
     * 类型码
     */
    private final Integer code;

    /**
     * 类型名称
     */
    private final String name;

    /**
     * 任务编码（与任务表关联）
     */
    private final String taskCode;

    EventType(Integer code, String name, String taskCode) {
        this.code = code;
        this.name = name;
        this.taskCode = taskCode;
    }

    /**
     * 根据 code 获取枚举
     */
    public static EventType getByCode(Integer code) {
        for (EventType eventType : values()) {
            if (eventType.getCode().equals(code)) {
                return eventType;
            }
        }
        return null;
    }

    /**
     * 根据 taskCode 获取枚举
     */
    public static EventType getByTaskCode(String taskCode) {
        for (EventType eventType : values()) {
            if (eventType.getTaskCode().equals(taskCode)) {
                return eventType;
            }
        }
        return null;
    }
}
