package com.express.delivery.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long publisherId;

    @TableField("runner_id")
    private Long runnerId;

    @TableField("station")
    private String stationName;

    private String building;

    @TableField("pickup_code")
    private String pickupCode;

    @TableField("item_info")
    private String itemInfo;

    private String status;

    @TableField("reward")
    private BigDecimal reward;

    @TableField("tracking_number")
    private String trackingNumber;

    @TableField("rating")
    private Integer evaluation;

    @TableField("comment")
    private String evaluationContent;

    @TableField("timeout_at")
    private LocalDateTime timeoutAt;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @TableField("completed_at")
    private LocalDateTime completedAt;

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

    public String getPickupCode() { return pickupCode; }
    public void setPickupCode(String pickupCode) { this.pickupCode = pickupCode; }

    public String getItemInfo() { return itemInfo; }
    public void setItemInfo(String itemInfo) { this.itemInfo = itemInfo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getReward() { return reward; }
    public void setReward(BigDecimal reward) { this.reward = reward; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

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

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}