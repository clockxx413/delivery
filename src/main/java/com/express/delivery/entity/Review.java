package com.express.delivery.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 评价实体
 */
@TableName("review")
public class Review {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联的订单ID */
    private Long orderId;

    /** 评价者用户ID */
    private Long reviewerId;

    /** 被评价用户ID */
    private Long targetUserId;

    /** 评分（1-5星） */
    private Integer score;

    /** 评价内容 */
    private String content;

    /** 评价类型: RUNNER_REVIEW-对跑腿员评价, PUBLISHER_REVIEW-对发布者评价 */
    private String type;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // ========== Getters & Setters ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }

    public Long getTargetUserId() { return targetUserId; }
    public void setTargetUserId(Long targetUserId) { this.targetUserId = targetUserId; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
