package com.user.growth.reward.point.context.enums;

import lombok.Getter;

/**
 * 积分类型枚举
 *
 * 定义积分变动类型
 *
 * @author system
 * @since 1.0.0
 */
@Getter
public enum PointType {

    /**
     * 获得 - 完成任务获得积分
     */
    EARN(1, "获得", "EARN"),

    /**
     * 消费 - 兑换奖励消耗积分
     */
    SPEND(2, "消费", "SPEND"),

    /**
     * 冻结 - 冻结积分（未实现）
     */
    FREEZE(3, "冻结", "FREEZE"),

    /**
     * 解冻 - 解冻积分（未实现）
     */
    UNFREEZE(4, "解冻", "UNFREEZE");

    private final Integer code;
    private final String name;
    private final String bizType;

    PointType(Integer code, String name, String bizType) {
        this.code = code;
        this.name = name;
        this.bizType = bizType;
    }

    /**
     * 根据 code 获取枚举
     */
    public static PointType getByCode(Integer code) {
        for (PointType pointType : values()) {
            if (pointType.getCode().equals(code)) {
                return pointType;
            }
        }
        return null;
    }
}
