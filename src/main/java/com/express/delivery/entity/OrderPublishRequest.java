package com.express.delivery.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单发布请求 DTO
 */
public class OrderPublishRequest {

    @NotNull(message = "发布者ID不能为空")
    private Long publisherId;

    @NotBlank(message = "驿站名称不能为空")
    private String stationName;

    @NotBlank(message = "楼栋不能为空")
    private String building;

    @NotNull(message = "酬劳不能为空")
    private BigDecimal reward;

    private String itemInfo;

    private String trackingNumber;

    // ========== Getters & Setters ==========

    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
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
}
