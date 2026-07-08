package com.express.delivery.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 订单状态更新请求 DTO
 */
public class OrderStatusUpdateRequest {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotBlank(message = "目标状态不能为空")
    private String status;

    /** 操作者用户ID（用于校验权限） */
    private Long operatorId;

    // ========== Getters & Setters ==========

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getOperatorId() { return operatorId; }
    public void setOperatorId(Long operatorId) { this.operatorId = operatorId; }
}
