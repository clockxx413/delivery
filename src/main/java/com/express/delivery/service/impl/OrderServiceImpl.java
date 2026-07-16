package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.Order;
import com.express.delivery.entity.OrderAcceptRequest;
import com.express.delivery.entity.OrderConfirmRequest;
import com.express.delivery.entity.OrderPublishRequest;
import com.express.delivery.entity.OrderStatusUpdateRequest;
import com.express.delivery.mapper.OrderMapper;
import com.express.delivery.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public Order publish(OrderPublishRequest request) {
        Order order = new Order();
        order.setPublisherId(request.getPublisherId());
        order.setStationName(request.getStationName());
        order.setBuilding(request.getBuilding());
        order.setPickupCode(request.getPickupCode());
        order.setItemInfo(request.getItemInfo());
        order.setReward(request.getReward());
        order.setStatus(STATUS_PENDING);

        // 直接用前端传的截止时间
        if (request.getTimeoutAt() == null || request.getTimeoutAt().isEmpty()) {
            throw new RuntimeException("请选择截止时间");
        }

        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime timeout = LocalDateTime.parse(request.getTimeoutAt(), formatter);
            order.setTimeoutAt(timeout);
        } catch (Exception e) {
            throw new RuntimeException("截止时间格式错误，请重新选择");
        }

        this.save(order);
        return order;
    }

    @Override
    public Order accept(OrderAcceptRequest request) {
        Order order = this.getById(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!STATUS_PENDING.equals(order.getStatus())) {
            throw new RuntimeException("该订单当前状态为 " + order.getStatus() + "，无法接单");
        }
        if (order.getPublisherId().equals(request.getRunnerId())) {
            throw new RuntimeException("不能接自己发布的订单");
        }
        order.setRunnerId(request.getRunnerId());
        order.setStatus(STATUS_ACCEPTED);
        this.updateById(order);
        return order;
    }

    @Override
    public Order updateStatus(OrderStatusUpdateRequest request) {
        Order order = this.getById(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        String current = order.getStatus();
        String target = request.getStatus();
        if (!isValidStatusTransition(current, target)) {
            throw new RuntimeException("不允许从 " + current + " 变更为 " + target);
        }
        order.setStatus(target);
        this.updateById(order);
        return order;
    }

    @Override
    public List<Order> listOrders(Long userId, String role, String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        // 如果 role 是 "all"，查询所有订单（跑腿员看大厅）
        if ("all".equals(role)) {
            // 不添加 userId 条件，查询全部
        } else if ("publisher".equals(role)) {
            wrapper.eq(Order::getPublisherId, userId);
        } else if ("runner".equals(role)) {
            wrapper.eq(Order::getRunnerId, userId);
        } else {
            wrapper.and(w -> w.eq(Order::getPublisherId, userId).or().eq(Order::getRunnerId, userId));
        }

        if (status != null && !status.isEmpty()) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreatedAt);
        return this.list(wrapper);
    }

    @Override
    public Order confirm(OrderConfirmRequest request) {
        Order order = this.getById(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!STATUS_DELIVERED.equals(order.getStatus())) {
            throw new RuntimeException("订单当前状态为 " + order.getStatus() + "，无法确认收货");
        }
        if (!order.getPublisherId().equals(request.getUserId())) {
            throw new RuntimeException("只有订单发布者可以确认收货");
        }
        order.setStatus(STATUS_CONFIRMED);
        order.setEvaluation(request.getEvaluation());
        order.setEvaluationContent(request.getEvaluationContent());
        this.updateById(order);
        return order;
    }

    private boolean isValidStatusTransition(String current, String target) {
        switch (current) {
            case STATUS_PENDING:
                return STATUS_ACCEPTED.equals(target) || STATUS_CANCELLED.equals(target);
            case STATUS_ACCEPTED:
                return STATUS_DELIVERING.equals(target) || STATUS_CANCELLED.equals(target);
            case STATUS_DELIVERING:
                return STATUS_DELIVERED.equals(target) || STATUS_CANCELLED.equals(target);
            case STATUS_DELIVERED:
                return STATUS_CONFIRMED.equals(target);
            default:
                return false;
        }
    }
}