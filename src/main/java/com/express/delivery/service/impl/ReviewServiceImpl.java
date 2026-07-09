package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.Review;
import com.express.delivery.entity.ReviewCreateRequest;
import com.express.delivery.entity.ReviewType;
import com.express.delivery.mapper.ReviewMapper;
import com.express.delivery.service.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评价服务实现
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Override
    public Review submitReview(ReviewCreateRequest request) {
        // 校验评价类型
        if (!ReviewType.isValid(request.getType())) {
            throw new RuntimeException("无效的评价类型: " + request.getType());
        }

        // 校验评分范围
        if (request.getScore() < 1 || request.getScore() > 5) {
            throw new RuntimeException("评分必须在1-5之间");
        }

        // 检查该用户是否已对此订单评价
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getOrderId, request.getOrderId())
               .eq(Review::getReviewerId, request.getReviewerId())
               .eq(Review::getType, request.getType());
        Review existing = this.getOne(wrapper);
        if (existing != null) {
            throw new RuntimeException("您已对此订单进行过评价，请勿重复评价");
        }

        // 创建评价
        Review review = new Review();
        review.setOrderId(request.getOrderId());
        review.setReviewerId(request.getReviewerId());
        review.setTargetUserId(request.getTargetUserId());
        review.setScore(request.getScore());
        review.setContent(request.getContent());
        review.setType(request.getType());
        review.setCreatedAt(LocalDateTime.now());

        this.save(review);
        return review;
    }

    @Override
    public Map<String, Object> getUserRatingStats(Long userId) {
        // 查询对该用户的所有评价
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getTargetUserId, userId);
        List<Review> reviews = this.list(wrapper);

        int count = reviews.size();
        double avgScore = 0.0;
        if (count > 0) {
            avgScore = reviews.stream()
                    .mapToInt(Review::getScore)
                    .average()
                    .orElse(0.0);
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("userId", userId);
        stats.put("avgScore", Math.round(avgScore * 10.0) / 10.0); // 保留一位小数
        stats.put("reviewCount", count);

        // 各分数段统计
        long score5 = reviews.stream().filter(r -> r.getScore() == 5).count();
        long score4 = reviews.stream().filter(r -> r.getScore() == 4).count();
        long score3 = reviews.stream().filter(r -> r.getScore() == 3).count();
        long score2 = reviews.stream().filter(r -> r.getScore() == 2).count();
        long score1 = reviews.stream().filter(r -> r.getScore() == 1).count();

        stats.put("score5", score5);
        stats.put("score4", score4);
        stats.put("score3", score3);
        stats.put("score2", score2);
        stats.put("score1", score1);

        return stats;
    }
}
