package com.express.delivery.entity;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 提现申请请求 DTO
 */
public class WithdrawRequest {

    @NotNull(message = "跑腿员ID不能为空")
    private Long runnerId;

    @NotNull(message = "提现金额不能为空")
    private BigDecimal amount;

    // ========== Getters & Setters ==========

    public Long getRunnerId() { return runnerId; }
    public void setRunnerId(Long runnerId) { this.runnerId = runnerId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
