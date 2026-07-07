package com.express.delivery.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 发布订单请求 DTO
 */
public class PublishOrderRequest {

    @NotNull(message = "发布者用户ID不能为空")
    private Long publisherId;

    @NotBlank(message = "驿站名称不能为空")
    private String station;

    @NotBlank(message = "送达楼栋不能为空")
    private String building;

    @NotNull(message = "酬劳不能为空")
    private BigDecimal reward;

    @NotBlank(message = "物品描述不能为空")
    private String itemDescription;

    /** 物品分类 */
    private String itemCategory;

    // ========== Getters & Setters ==========

    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
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
}
