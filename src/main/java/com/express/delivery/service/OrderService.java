package com.express.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.express.delivery.entity.Order;
import com.express.delivery.entity.OrderAcceptRequest;
import com.express.delivery.entity.OrderConfirmRequest;
import com.express.delivery.entity.OrderPublishRequest;
import com.express.delivery.entity.OrderStatusUpdateRequest;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    /** 订单状态常量 */
    String STATUS_PENDING    = "PENDING";
    String STATUS_ACCEPTED   = "ACCEPTED";
    String STATUS_DELIVERING = "DELIVERING";
    String STATUS_DELIVERED  = "DELIVERED";
    String STATUS_CONFIRMED  = "CONFIRMED";
    String STATUS_CANCELLED  = "CANCELLED";

    /**
     * 发布订单
     */
    Order publish(OrderPublishRequest request);

    /**
     * 接单
     */
    Order accept(OrderAcceptRequest request);

    /**
     * 更新订单状态
     */
    Order updateStatus(OrderStatusUpdateRequest request);

    /**
     * 查询个人订单列表
     */
    List<Order> listOrders(Long userId, String role, String status);

    /**
     * 确认收货并评价
     */
    Order confirm(OrderConfirmRequest request);
}