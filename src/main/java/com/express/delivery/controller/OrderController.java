package com.express.delivery.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.express.delivery.common.Result;
import com.express.delivery.entity.Order;
import com.express.delivery.entity.OrderCreateRequest;
import com.express.delivery.entity.OrderStatus;
import com.express.delivery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 发布订单
     * POST /api/order
     */
    @PostMapping
    public Result<Order> create(@Valid @RequestBody OrderCreateRequest request) {
        Order order = orderService.createOrder(request);
        return Result.success(order);
    }

    /**
     * 查询订单详情
     * GET /api/order/{id}
     */
    @GetMapping("/{id}")
    public Result<Order> getById(@PathVariable Long id) {
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        return Result.success(order);
    }

    /**
     * 分页查询订单列表（支持按状态筛选）
     * GET /api/order?page=1&size=10&status=PENDING&publisherId=1
     */
    @GetMapping
    public Result<IPage<Order>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long publisherId,
            @RequestParam(required = false) Long runnerId) {

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Order::getStatus, status);
        }
        if (publisherId != null) {
            wrapper.eq(Order::getPublisherId, publisherId);
        }
        if (runnerId != null) {
            wrapper.eq(Order::getRunnerId, runnerId);
        }
        wrapper.orderByDesc(Order::getCreatedAt);

        IPage<Order> result = orderService.page(new Page<>(page, size), wrapper);
        return Result.success(result);
    }

    /**
     * 跑腿员接单
     * PUT /api/order/{id}/accept
     */
    @PutMapping("/{id}/accept")
    public Result<Order> accept(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long runnerId = body.get("runnerId");
        if (runnerId == null) {
            return Result.error("跑腿员ID不能为空");
        }
        Order order = orderService.acceptOrder(id, runnerId);
        return Result.success(order);
    }

    /**
     * 开始配送
     * PUT /api/order/{id}/deliver
     */
    @PutMapping("/{id}/deliver")
    public Result<Order> startDelivery(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long runnerId = body.get("runnerId");
        if (runnerId == null) {
            return Result.error("跑腿员ID不能为空");
        }
        Order order = orderService.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!OrderStatus.ACCEPTED.equals(order.getStatus())) {
            return Result.error("当前订单状态不允许开始配送，当前状态: " + order.getStatus());
        }
        if (!runnerId.equals(order.getRunnerId())) {
            return Result.error("只有接单的跑腿员才能开始配送");
        }
        order.setStatus(OrderStatus.DELIVERING);
        order.setUpdatedAt(java.time.LocalDateTime.now());
        orderService.updateById(order);
        return Result.success(order);
    }

    /**
     * 完成订单
     * PUT /api/order/{id}/complete
     */
    @PutMapping("/{id}/complete")
    public Result<Order> complete(@PathVariable Long id, @RequestBody Map<String, Long> body) {
        Long runnerId = body.get("runnerId");
        if (runnerId == null) {
            return Result.error("跑腿员ID不能为空");
        }
        Order order = orderService.completeOrder(id, runnerId);
        return Result.success(order);
    }

    /**
     * 取消订单
     * PUT /api/order/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public Result<Order> cancel(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long userId = body.get("userId") != null ? ((Number) body.get("userId")).longValue() : null;
        String reason = body.get("reason") != null ? body.get("reason").toString() : "";
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        Order order = orderService.cancelOrder(id, userId, reason);
        return Result.success(order);
    }
}
