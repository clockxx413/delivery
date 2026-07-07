package com.express.delivery.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.express.delivery.common.Result;
import com.express.delivery.entity.*;
import com.express.delivery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 发布快递代取订单
     * POST /api/order/publish
     */
    @PostMapping("/publish")
    public Result<Order> publish(@Valid @RequestBody PublishOrderRequest request) {
        Order order = orderService.publish(request);
        return Result.success(order);
    }

    /**
     * 跑腿员接单
     * POST /api/order/accept
     */
    @PostMapping("/accept")
    public Result<Order> accept(@Valid @RequestBody AcceptOrderRequest request) {
        Order order = orderService.accept(request);
        return Result.success(order);
    }

    /**
     * 更新订单状态
     * POST /api/order/updateStatus
     */
    @PostMapping("/updateStatus")
    public Result<Order> updateStatus(@Valid @RequestBody UpdateStatusRequest request) {
        Order order = orderService.updateStatus(request);
        return Result.success(order);
    }

    /**
     * 获取订单列表（支持分页、状态筛选、关键词搜索）
     * GET /api/order/list?page=1&size=10&userId=1&status=PENDING&keyword=快递
     */
    @GetMapping("/list")
    public Result<Page<Order>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        Page<Order> orderPage = orderService.listOrders(page, size, userId, status, keyword);
        return Result.success(orderPage);
    }

    /**
     * 确认收货（含评价）
     * POST /api/order/confirm
     */
    @PostMapping("/confirm")
    public Result<Order> confirm(@Valid @RequestBody ConfirmOrderRequest request) {
        Order order = orderService.confirm(request);
        return Result.success(order);
    }
}
