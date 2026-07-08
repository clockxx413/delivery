package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.*;
import com.express.delivery.mapper.OrderMapper;
import com.express.delivery.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单服务实现
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    /** 默认超时时间（分钟） */
    private static final int DEFAULT_TIMEOUT_MINUTES = 30;

    @Override
    public Order publish(OrderPublishRequest request) {
        // 校验酬劳金额
        if (request.getReward().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("酬劳金额必须大于0");
        }

        Order order = new Order();
        order.setPublisherId(request.getPublisherId());
        order.setStationName(request.getStationName());
        order.setBuilding(request.getBuilding());
        order.setReward(request.getReward());
        order.setItemInfo(request.getItemInfo());
        order.setTrackingNumber(request.getTrackingNumber());
        order.setStatus(STATUS_PENDING);
        order.setTimeoutAt(LocalDateTime.now().plusMinutes(DEFAULT_TIMEOUT_MINUTES));
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        this.save(order);
        return order;
    }

    @Override
    public Order accept(OrderAcceptRequest request) {
        // 查询订单
        Order order = this.getById(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 校验订单状态是否为待接单
        if (!STATUS_PENDING.equals(order.getStatus())) {
            throw new RuntimeException("该订单当前状态为 " + order.getStatus() + "，无法接单");
        }

        // 不允许发布者接自己的单
        if (order.getPublisherId().equals(request.getRunnerId())) {
            throw new RuntimeException("不能接自己发布的订单");
        }

        // 更新订单
        order.setRunnerId(request.getRunnerId());
        order.setStatus(STATUS_ACCEPTED);
        order.setUpdatedAt(LocalDateTime.now());

        this.updateById(order);
        return order;
    }

    @Override
    public Order updateStatus(OrderStatusUpdateRequest request) {
        Order order = this.getById(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        String targetStatus = request.getStatus();
        String currentStatus = order.getStatus();

        // 校验状态流转合法性
        if (!isValidStatusTransition(currentStatus, targetStatus)) {
            throw new RuntimeException("不允许从 " + currentStatus + " 变更为 " + targetStatus);
        }

        order.setStatus(targetStatus);
        order.setUpdatedAt(LocalDateTime.now());

        this.updateById(order);
        return order;
    }

    @Override
    public List<Order> listOrders(Long userId, String role, String status) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        // 根据角色筛选
        if ("publisher".equals(role)) {
            wrapper.eq(Order::getPublisherId, userId);
        } else if ("runner".equals(role)) {
            wrapper.eq(Order::getRunnerId, userId);
        } else {
            // 查询全部相关订单（发布或接单）
            wrapper.and(w -> w.eq(Order::getPublisherId, userId).or().eq(Order::getRunnerId, userId));
        }

        // 按状态筛选
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Order::getStatus, status);
        }

        // 按创建时间倒序
        wrapper.orderByDesc(Order::getCreatedAt);

        return this.list(wrapper);
    }

    @Override
    public Order confirm(OrderConfirmRequest request) {
        Order order = this.getById(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 校验订单状态
        if (!STATUS_DELIVERED.equals(order.getStatus())) {
            throw new RuntimeException("订单当前状态为 " + order.getStatus() + "，无法确认收货");
        }

        // 校验操作者身份（只有发布者可以确认收货）
        if (!order.getPublisherId().equals(request.getUserId())) {
            throw new RuntimeException("只有订单发布者可以确认收货");
        }

        // 更新订单状态和评价
        order.setStatus(STATUS_CONFIRMED);
        order.setEvaluation(request.getEvaluation());
        order.setEvaluationContent(request.getEvaluationContent());
        order.setUpdatedAt(LocalDateTime.now());

        this.updateById(order);
        return order;
    }

    /**
     * 校验订单状态流转是否合法
     *
     * PENDING  → ACCEPTED  (接单)
     * PENDING  → CANCELLED (超时取消)
     * ACCEPTED → DELIVERING (开始配送)
     * ACCEPTED → CANCELLED (取消)
     * DELIVERING → DELIVERED (送达)
     * DELIVERED → CONFIRMED (确认收货)
     */
    private boolean isValidStatusTransition(String current, String target) {
        if (current.equals(target)) return false;

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
