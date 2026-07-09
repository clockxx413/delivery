package com.express.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.express.delivery.entity.Review;
import com.express.delivery.entity.ReviewCreateRequest;

import java.util.Map;

/**
 * 评价服务接口
 */
public interface ReviewService extends IService<Review> {

    /**
     * 提交评价
     * @param request 评价请求
     * @return 创建的评价
     */
    Review submitReview(ReviewCreateRequest request);

    /**
     * 获取用户评分统计
     * @param userId 用户ID
     * @return 评分统计信息（平均分、评价数量等）
     */
    Map<String, Object> getUserRatingStats(Long userId);
}
