package com.express.delivery.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 快递代取订单实体
 */
@TableName("\"order\"")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 订单编号（唯一） */
    private String orderNo;

    /** 发布者用户ID */
    private Long publisherId;

    /** 接单跑腿员ID（未接单时为空） */
    private Long runnerId;

    /** 快递驿站名称 */
    private String station;

    /** 送达楼栋 */
    private String building;

    /** 酬劳金额 */
    private BigDecimal reward;

    /** 物品描述 */
    private String itemDescription;

    /** 物品分类（如：文件、包裹、食品等） */
    private String itemCategory;

    /**
     * 订单状态:
     * PENDING     - 待接单
     * ACCEPTED    - 已接单
     * IN_PROGRESS - 配送中
     * COMPLETED   - 已完成
     * CANCELLED   - 已取消
     */
    private String status;

    /** 评价内容 */
    private String evaluation;

    /** 评价分数（1-5星） */
    private Integer evaluationScore;

    /** 取消原因 */
    private String cancelReason;

    /** 接单时间 */
    private LocalDateTime acceptedAt;

    /** 完成时间 */
    private LocalDateTime completedAt;

    /** 取消时间 */
    private LocalDateTime cancelledAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // ========== Getters & Setters ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
    public Long getRunnerId() { return runnerId; }
    public void setRunnerId(Long runnerId) { this.runnerId = runnerId; }
    public String getStation() { return station; }
    public void setStation(String station) { this.station = station; }
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    public BigDecimal getReward() { return reward; }
    public void setReward(BigDecimal reward) { this.reward = reward; }
    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }
    public String getItemCategory() { return itemCategory; }
    public void setItemCategory(String itemCategory) { this.itemCategory = itemCategory; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEvaluation() { return evaluation; }
    public void setEvaluation(String evaluation) { this.evaluation = evaluation; }
    public Integer getEvaluationScore() { return evaluationScore; }
    public void setEvaluationScore(Integer evaluationScore) { this.evaluationScore = evaluationScore; }
    public String getCancelReason() { return cancelReason; }
    public void setCancelReason(String cancelReason) { this.cancelReason = cancelReason; }
    public LocalDateTime getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(LocalDateTime acceptedAt) { this.acceptedAt = acceptedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
