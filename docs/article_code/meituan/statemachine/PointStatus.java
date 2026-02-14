package com.vivo.pointtask.meituan.statemachine;

/**
 * 【状态机】积分状态枚举
 * 
 * 参考：适合几十万用户量的积分系统设计
 * 
 * 状态流转设计：
 * CREATED(创建) → AVAILABLE(可用) → FROZEN(冻结) → CONSUMED(已消费)
 *                                    ↓
 *                                  EXPIRED(过期)
 * 
 * 或者：
 * CREATED(创建) → AVAILABLE(可用) → EXPIRED(过期)
 * 
 * 状态说明：
 * - CREATED: 积分刚创建，还未生效（如异步发放时的中间状态）
 * - AVAILABLE: 积分可用，可以消费
 * - FROZEN: 积分冻结（如订单未确认收货时，积分暂时不可用）
 * - CONSUMED: 积分已被消费
 * - EXPIRED: 积分已过期
 * 
 * @version 5.0.0
 */
public enum PointStatus {
    
    /**
     * 创建：积分记录刚创建，还未生效
     * 场景：异步发放积分时的中间状态
     */
    CREATED(0, "创建"),
    
    /**
     * 可用：积分可用，可以消费
     */
    AVAILABLE(1, "可用"),
    
    /**
     * 冻结：积分暂时不可用
     * 场景：订单未确认收货、退款审核中等
     */
    FROZEN(2, "冻结"),
    
    /**
     * 已消费：积分已被使用
     */
    CONSUMED(3, "已消费"),
    
    /**
     * 过期：积分已过期
     */
    EXPIRED(4, "过期");
    
    private final int code;
    private final String description;
    
    PointStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据code获取状态
     */
    public static PointStatus fromCode(int code) {
        for (PointStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
    
    /**
     * 检查状态是否可以转换到目标状态
     */
    public boolean canTransitionTo(PointStatus targetStatus) {
        return switch (this) {
            case CREATED -> targetStatus == AVAILABLE || targetStatus == EXPIRED;
            case AVAILABLE -> targetStatus == FROZEN || targetStatus == CONSUMED || targetStatus == EXPIRED;
            case FROZEN -> targetStatus == AVAILABLE || targetStatus == CONSUMED || targetStatus == EXPIRED;
            case CONSUMED, EXPIRED -> false; // 终态，不可转换
        };
    }
    
    /**
     * 是否为终态
     */
    public boolean isFinal() {
        return this == CONSUMED || this == EXPIRED;
    }
    
    /**
     * 是否可用（可以消费）
     */
    public boolean isAvailable() {
        return this == AVAILABLE;
    }
}
