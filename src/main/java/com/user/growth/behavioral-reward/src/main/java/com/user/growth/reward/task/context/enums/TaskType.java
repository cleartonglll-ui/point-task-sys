package com.user.growth.reward.task.context.enums;

import lombok.Getter;

/**
 * 任务类型枚举
 *
 * @author system
 * @since 1.0.0
 */
@Getter
public enum TaskType {

    /**
     * 签到
     */
    SIGN_IN(1, "签到"),

    /**
     * 作业
     */
    HOMEWORK(2, "作业"),

    /**
     * 视频
     */
    VIDEO(3, "视频"),

    /**
     * 互动
     */
    INTERACTION(4, "互动"),

    /**
     * 答题
     */
    QUIZ(5, "答题"),

    /**
     * 邀请
     */
    INVITE(6, "邀请");

    private final Integer code;
    private final String name;

    TaskType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据 code 获取枚举
     */
    public static TaskType getByCode(Integer code) {
        for (TaskType taskType : values()) {
            if (taskType.getCode().equals(code)) {
                return taskType;
            }
        }
        return null;
    }
}
