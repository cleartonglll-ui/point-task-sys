package com.user.growth.reward.reward.context.enums;

import lombok.Getter;

/**
 * 奖励类型枚举
 *
 * @author system
 * @since 1.0.0
 */
@Getter
public enum RewardType {

    /**
     * 学习资料
     */
    MATERIAL(1, "学习资料", "MATERIAL"),

    /**
     * 课程优惠券
     */
    COUPON(2, "课程优惠券", "COUPON"),

    /**
     * 虚拟勋章
     */
    MEDAL(3, "虚拟勋章", "MEDAL"),

    /**
     * 实物奖品
     */
    PRODUCT(4, "实物奖品", "PRODUCT");

    private final Integer code;
    private final String name;
    private final String type;

    RewardType(Integer code, String name, String type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    /**
     * 根据 code 获取枚举
     */
    public static RewardType getByCode(Integer code) {
        for (RewardType rewardType : values()) {
            if (rewardType.getCode().equals(code)) {
                return rewardType;
            }
        }
        return null;
    }
}
