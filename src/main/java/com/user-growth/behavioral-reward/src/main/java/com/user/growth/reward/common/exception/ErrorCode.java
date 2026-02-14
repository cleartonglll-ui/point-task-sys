package com.user.growth.reward.common.exception;

import lombok.Getter;

/**
 * 错误码枚举
 *
 * 统一定义系统中的错误码和错误消息
 *
 * @author system
 * @since 1.0.0
 */
@Getter
public enum ErrorCode {

    // 通用错误码 1000-1999
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(1000, "系统异常"),
    PARAM_ERROR(1001, "参数错误"),
    NOT_FOUND(1002, "资源不存在"),
    RATE_LIMIT_ERROR(1003, "请求过于频繁"),
    LOCK_ERROR(1004, "获取锁失败"),

    // 用户相关 2000-2999
    USER_NOT_FOUND(2000, "用户不存在"),
    USER_ACCOUNT_NOT_FOUND(2001, "用户积分账户不存在"),

    // 积分相关 3000-3999
    POINT_NOT_ENOUGH(3000, "积分余额不足"),
    POINT_DAILY_LIMIT_EXCEEDED(3001, "每日积分上限已达"),
    POINT_ALREADY_AWARDED(3002, "积分已发放，请勿重复领取"),
    POINT_ALREADY_DEDUCTED(3003, "积分已扣减，请勿重复操作"),

    // 任务相关 4000-4999
    TASK_NOT_FOUND(4000, "任务不存在"),
    TASK_DISABLED(4001, "任务已禁用"),
    TASK_EXPIRED(4002, "任务已过期"),
    TASK_LIMIT_EXCEEDED(4003, "任务完成次数已达上限"),
    TASK_NOT_STARTED(4004, "任务未开始"),

    // 奖励相关 5000-5999
    REWARD_NOT_FOUND(5000, "奖励不存在"),
    REWARD_OUT_OF_STOCK(5001, "奖励库存不足"),
    REWARD_OFF_SHELF(5002, "奖励已下架"),
    REWARD_LIMIT_EXCEEDED(5003, "兑换次数已达上限"),
    REWARD_CLAIM_FAILED(5004, "奖励兑换失败"),

    // 事件相关 6000-6999
    EVENT_COLLECT_FAILED(6000, "事件采集失败"),
    EVENT_TYPE_INVALID(6001, "事件类型无效"),
    EVENT_PROCESS_FAILED(6002, "事件处理失败");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
