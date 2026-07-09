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
     * @param request 订单信息
     * @return 创建的订单
     */
    Order publish(OrderPublishRequest request);

    /**
     * 接单
     * @param request 接单信息
     * @return 更新后的订单
     */
    Order accept(OrderAcceptRequest request);

    /**
     * 更新订单状态
     * @param request 状态更新信息
     * @return 更新后的订单
     */
    Order updateStatus(OrderStatusUpdateRequest request);

    /**
     * 查询个人订单列表
     * @param userId 用户ID
     * @param role   角色（publisher/runner）
     * @param status 订单状态筛选（可选）
     * @return 订单列表
     */
    List<Order> listOrders(Long userId, String role, String status);

    /**
     * 确认收货并评价
     * @param request 确认收货信息
     * @return 更新后的订单
     */
    Order confirm(OrderConfirmRequest request);
}
