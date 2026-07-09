package com.express.delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.express.delivery.common.Result;
import com.express.delivery.entity.Review;
import com.express.delivery.entity.ReviewCreateRequest;
import com.express.delivery.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 评价控制器
 */
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * 提交评价
     * POST /api/review
     */
    @PostMapping
    public Result<Review> submit(@Valid @RequestBody ReviewCreateRequest request) {
        Review review = reviewService.submitReview(request);
        return Result.success(review);
    }

    /**
     * 查询评价详情
     * GET /api/review/{id}
     */
    @GetMapping("/{id}")
    public Result<Review> getById(@PathVariable Long id) {
        Review review = reviewService.getById(id);
        if (review == null) {
            return Result.error("评价不存在");
        }
        return Result.success(review);
    }

    /**
     * 查询某订单的评价列表
     * GET /api/review/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    public Result<java.util.List<Review>> listByOrder(@PathVariable Long orderId) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getOrderId, orderId)
               .orderByDesc(Review::getCreatedAt);
        java.util.List<Review> reviews = reviewService.list(wrapper);
        return Result.success(reviews);
    }

    /**
     * 分页查询某用户的评价列表
     * GET /api/review/user/{userId}?page=1&size=10&type=RUNNER_REVIEW
     */
    @GetMapping("/user/{userId}")
    public Result<IPage<Review>> listByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type) {

        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getTargetUserId, userId);
        if (type != null && !type.isEmpty()) {
            wrapper.eq(Review::getType, type);
        }
        wrapper.orderByDesc(Review::getCreatedAt);

        IPage<Review> result = reviewService.page(new Page<>(page, size), wrapper);
        return Result.success(result);
    }

    /**
     * 查询用户评分统计
     * GET /api/review/user/{userId}/rating-stats
     */
    @GetMapping("/user/{userId}/rating-stats")
    public Result<Map<String, Object>> getRatingStats(@PathVariable Long userId) {
        Map<String, Object> stats = reviewService.getUserRatingStats(userId);
        return Result.success(stats);
    }
}
