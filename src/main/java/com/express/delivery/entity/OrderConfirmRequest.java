package com.express.delivery.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 确认收货请求 DTO
 */
public class OrderConfirmRequest {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Min(value = 1, message = "评价星级最小为1")
    @Max(value = 5, message = "评价星级最大为5")
    private Integer evaluation;

    private String evaluationContent;

    // ========== Getters & Setters ==========

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getEvaluation() { return evaluation; }
    public void setEvaluation(Integer evaluation) { this.evaluation = evaluation; }
    public String getEvaluationContent() { return evaluationContent; }
    public void setEvaluationContent(String evaluationContent) { this.evaluationContent = evaluationContent; }
}
