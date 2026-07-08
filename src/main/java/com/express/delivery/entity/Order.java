package com.express.delivery.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体
 */
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 发布者用户ID */
    private Long publisherId;

    /** 接单跑腿员用户ID */
    private Long runnerId;

    /** 快递驿站名称 */
    private String stationName;

    /** 配送楼栋 */
    private String building;

    /** 酬劳金额 */
    private BigDecimal reward;

    /** 物品信息描述 */
    private String itemInfo;

    /** 快递单号 */
    private String trackingNumber;

    /**
     * 订单状态:
     * PENDING    - 待接单
     * ACCEPTED   - 已接单
     * DELIVERING - 配送中
     * DELIVERED  - 已送达
     * CONFIRMED  - 已确认收货
     * CANCELLED  - 已取消
     */
    private String status;

    /** 评价星级 (1-5) */
    private Integer evaluation;

    /** 评价内容 */
    private String evaluationContent;

    /** 超时自动取消时间 */
    private LocalDateTime timeoutAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    // ========== Getters & Setters ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
    public Long getRunnerId() { return runnerId; }
    public void setRunnerId(Long runnerId) { this.runnerId = runnerId; }
    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }
    public BigDecimal getReward() { return reward; }
    public void setReward(BigDecimal reward) { this.reward = reward; }
    public String getItemInfo() { return itemInfo; }
    public void setItemInfo(String itemInfo) { this.itemInfo = itemInfo; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getEvaluation() { return evaluation; }
    public void setEvaluation(Integer evaluation) { this.evaluation = evaluation; }
    public String getEvaluationContent() { return evaluationContent; }
    public void setEvaluationContent(String evaluationContent) { this.evaluationContent = evaluationContent; }
    public LocalDateTime getTimeoutAt() { return timeoutAt; }
    public void setTimeoutAt(LocalDateTime timeoutAt) { this.timeoutAt = timeoutAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
