package com.express.delivery.entity;

/**
 * 订单状态常量
 */
public final class OrderStatus {

    private OrderStatus() {}

    /** 待接单 */
    public static final String PENDING = "PENDING";

    /** 已接单 */
    public static final String ACCEPTED = "ACCEPTED";

    /** 配送中 */
    public static final String DELIVERING = "DELIVERING";

    /** 已完成 */
    public static final String COMPLETED = "COMPLETED";

    /** 已取消 */
    public static final String CANCELLED = "CANCELLED";

    /**
     * 校验状态是否合法
     */
    public static boolean isValid(String status) {
        return PENDING.equals(status)
                || ACCEPTED.equals(status)
                || DELIVERING.equals(status)
                || COMPLETED.equals(status)
                || CANCELLED.equals(status);
    }
}
