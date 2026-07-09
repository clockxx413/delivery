package com.express.delivery.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 发布订单请求 DTO
 */
public class OrderCreateRequest {

    @NotNull(message = "发布者用户ID不能为空")
    private Long publisherId;

    @NotBlank(message = "快递公司不能为空")
    private String expressCompany;

    @NotBlank(message = "取件码/快递单号不能为空")
    private String expressCode;

    @NotBlank(message = "取件地址不能为空")
    private String pickupAddress;

    @NotBlank(message = "送达地址不能为空")
    private String deliveryAddress;

    @NotBlank(message = "收件人手机号不能为空")
    private String recipientPhone;

    private String description;

    private BigDecimal tip;

    // ========== Getters & Setters ==========

    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }

    public String getExpressCompany() { return expressCompany; }
    public void setExpressCompany(String expressCompany) { this.expressCompany = expressCompany; }

    public String getExpressCode() { return expressCode; }
    public void setExpressCode(String expressCode) { this.expressCode = expressCode; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getTip() { return tip; }
    public void setTip(BigDecimal tip) { this.tip = tip; }
}
