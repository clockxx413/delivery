package com.express.delivery.entity;

/**
 * 评价类型常量
 */
public final class ReviewType {

    private ReviewType() {}

    /** 对跑腿员的评价 */
    public static final String RUNNER_REVIEW = "RUNNER_REVIEW";

    /** 对发布者的评价 */
    public static final String PUBLISHER_REVIEW = "PUBLISHER_REVIEW";

    /**
     * 校验评价类型是否合法
     */
    public static boolean isValid(String type) {
        return RUNNER_REVIEW.equals(type) || PUBLISHER_REVIEW.equals(type);
    }
}
