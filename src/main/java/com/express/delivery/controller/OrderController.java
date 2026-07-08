package com.express.delivery.controller;

import com.express.delivery.common.Result;
import com.express.delivery.entity.*;
import com.express.delivery.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单发布接口
     * POST /api/order/publish
     */
    @PostMapping("/publish")
    public Result<Order> publish(@Valid @RequestBody OrderPublishRequest request) {
        Order order = orderService.publish(request);
        return Result.success(order);
    }

    /**
     * 接单接口
     * POST /api/order/accept
     */
    @PostMapping("/accept")
    public Result<Order> accept(@Valid @RequestBody OrderAcceptRequest request) {
        Order order = orderService.accept(request);
        return Result.success(order);
    }

    /**
     * 订单状态修改接口
     * POST /api/order/updateStatus
     */
    @PostMapping("/updateStatus")
    public Result<Order> updateStatus(@Valid @RequestBody OrderStatusUpdateRequest request) {
        Order order = orderService.updateStatus(request);
        return Result.success(order);
    }

    /**
     * 个人订单列表查询接口
     * GET /api/order/list?userId=1&role=publisher&status=PENDING
     */
    @GetMapping("/list")
    public Result<List<Order>> list(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "all") String role,
            @RequestParam(required = false) String status) {
        List<Order> orders = orderService.listOrders(userId, role, status);
        return Result.success(orders);
    }

    /**
     * 确认收货接口（包含评价信息）
     * POST /api/order/confirm
     */
    @PostMapping("/confirm")
    public Result<Order> confirm(@Valid @RequestBody OrderConfirmRequest request) {
        Order order = orderService.confirm(request);
        return Result.success(order);
    }
}
