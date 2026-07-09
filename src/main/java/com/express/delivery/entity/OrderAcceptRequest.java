package com.express.delivery.entity;

import jakarta.validation.constraints.NotNull;

/**
 * 接单请求 DTO
 */
public class OrderAcceptRequest {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "跑腿员用户ID不能为空")
    private Long runnerId;

    // ========== Getters & Setters ==========

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getRunnerId() { return runnerId; }
    public void setRunnerId(Long runnerId) { this.runnerId = runnerId; }
}
