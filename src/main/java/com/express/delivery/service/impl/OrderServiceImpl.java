package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.*;
import com.express.delivery.mapper.OrderMapper;
import com.express.delivery.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 订单服务实现
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    // 订单状态常量
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";

    @Override
    public Order publish(PublishOrderRequest request) {
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setPublisherId(request.getPublisherId());
        order.setStation(request.getStation());
        order.setBuilding(request.getBuilding());
        order.setReward(request.getReward());
        order.setItemDescription(request.getItemDescription());
        order.setItemCategory(request.getItemCategory());
        order.setStatus(STATUS_PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        this.save(order);
        return order;
    }

    @Override
    public Order accept(AcceptOrderRequest request) {
        Order order = this.getById(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!STATUS_PENDING.equals(order.getStatus())) {
            throw new RuntimeException("该订单已被接单或已取消");
        }

        order.setRunnerId(request.getRunnerId());
        order.setStatus(STATUS_ACCEPTED);
        order.setAcceptedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        this.updateById(order);

        return order;
    }

    @Override
    public Order updateStatus(UpdateStatusRequest request) {
        Order order = this.getById(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        String oldStatus = order.getStatus();
        String newStatus = request.getNewStatus();

        // 校验状态流转合法性
        if (!isValidStatusTransition(oldStatus, newStatus)) {
            throw new RuntimeException("不允许从 " + oldStatus + " 切换到 " + newStatus);
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        // 特定状态的时间记录
        if (STATUS_IN_PROGRESS.equals(newStatus)) {
            // 配送中
        } else if (STATUS_CANCELLED.equals(newStatus)) {
            order.setCancelledAt(LocalDateTime.now());
        }

        this.updateById(order);
        return order;
    }

    @Override
    public Page<Order> listOrders(int page, int size, Long userId, String status, String keyword) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();

        // 按用户ID筛选（作为发布者或跑腿员）
        if (userId != null) {
            wrapper.and(w -> w.eq(Order::getPublisherId, userId).or().eq(Order::getRunnerId, userId));
        }

        // 按状态筛选
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Order::getStatus, status);
        }

        // 关键词搜索（搜索物品描述和驿站名称）
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Order::getItemDescription, keyword)
                              .or()
                              .like(Order::getStation, keyword)
                              .or()
                              .like(Order::getBuilding, keyword));
        }

        // 按创建时间倒序排列
        wrapper.orderByDesc(Order::getCreatedAt);

        Page<Order> pageParam = new Page<>(page, size);
        return this.page(pageParam, wrapper);
    }

    @Override
    public Order confirm(ConfirmOrderRequest request) {
        Order order = this.getById(request.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!STATUS_IN_PROGRESS.equals(order.getStatus()) && !STATUS_ACCEPTED.equals(order.getStatus())) {
            throw new RuntimeException("当前订单状态不允许确认收货");
        }
        // 只有发布者本人才能确认收货
        if (!order.getPublisherId().equals(request.getUserId())) {
            throw new RuntimeException("只有发布者本人才能确认收货");
        }

        order.setStatus(STATUS_COMPLETED);
        order.setEvaluation(request.getEvaluation());
        order.setEvaluationScore(request.getEvaluationScore());
        order.setCompletedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        this.updateById(order);

        return order;
    }

    /**
     * 校验状态流转是否合法
     */
    private boolean isValidStatusTransition(String oldStatus, String newStatus) {
        if (oldStatus.equals(newStatus)) return true;

        return switch (oldStatus) {
            case STATUS_PENDING ->
                STATUS_ACCEPTED.equals(newStatus) || STATUS_CANCELLED.equals(newStatus);
            case STATUS_ACCEPTED ->
                STATUS_IN_PROGRESS.equals(newStatus) || STATUS_CANCELLED.equals(newStatus);
            case STATUS_IN_PROGRESS ->
                STATUS_COMPLETED.equals(newStatus) || STATUS_CANCELLED.equals(newStatus);
            case STATUS_COMPLETED, STATUS_CANCELLED -> false; // 终态不可变更
            default -> false;
        };
    }

    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return "EXP" + datePart + uuidPart;
    }
}
