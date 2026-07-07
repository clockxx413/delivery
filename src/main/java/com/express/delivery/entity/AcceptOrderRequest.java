package com.express.delivery.entity;

import jakarta.validation.constraints.NotNull;

/**
 * 接单请求 DTO
 */
public class AcceptOrderRequest {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "跑腿员ID不能为空")
    private Long runnerId;

    // ========== Getters & Setters ==========

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getRunnerId() { return runnerId; }
    public void setRunnerId(Long runnerId) { this.runnerId = runnerId; }
}
