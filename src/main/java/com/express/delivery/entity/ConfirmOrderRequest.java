package com.express.delivery.entity;

import jakarta.validation.constraints.NotNull;

/**
 * 确认收货请求 DTO（含评价）
 */
public class ConfirmOrderRequest {

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /** 评价内容 */
    private String evaluation;

    /** 评价分数（1-5星） */
    private Integer evaluationScore;

    // ========== Getters & Setters ==========

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEvaluation() { return evaluation; }
    public void setEvaluation(String evaluation) { this.evaluation = evaluation; }
    public Integer getEvaluationScore() { return evaluationScore; }
    public void setEvaluationScore(Integer evaluationScore) { this.evaluationScore = evaluationScore; }
}
