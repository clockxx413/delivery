package com.express.delivery.entity;

import java.math.BigDecimal;

public class OrderPublishRequest {

    private Long publisherId;
    private String stationName;
    private String building;
    private String pickupCode;
    private String itemInfo;
    private BigDecimal reward;
    private String timeoutAt;

    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }

    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public String getPickupCode() { return pickupCode; }
    public void setPickupCode(String pickupCode) { this.pickupCode = pickupCode; }

    public String getItemInfo() { return itemInfo; }
    public void setItemInfo(String itemInfo) { this.itemInfo = itemInfo; }

    public BigDecimal getReward() { return reward; }
    public void setReward(BigDecimal reward) { this.reward = reward; }

    public String getTimeoutAt() { return timeoutAt; }
    public void setTimeoutAt(String timeoutAt) { this.timeoutAt = timeoutAt; }
}