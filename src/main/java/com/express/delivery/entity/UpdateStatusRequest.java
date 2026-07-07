package com.express.delivery.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 更新订单状态请求 DTO
 */
public class UpdateStatusRequest {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "操作者用户ID不能为空")
    private Long userId;

    @NotBlank(message = "新状态不能为空")
    private String newStatus;

    // ========== Getters & Setters ==========

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
}
