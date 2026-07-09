package com.express.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.express.delivery.entity.Order;
import com.express.delivery.entity.OrderCreateRequest;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 发布新订单
     * @param request 订单创建请求
     * @return 创建的订单
     */
    Order createOrder(OrderCreateRequest request);

    /**
     * 跑腿员接单
     * @param orderId 订单ID
     * @param runnerId 跑腿员ID
     * @return 更新后的订单
     */
    Order acceptOrder(Long orderId, Long runnerId);

    /**
     * 完成订单
     * @param orderId 订单ID
     * @param runnerId 跑腿员ID（用于校验）
     * @return 更新后的订单
     */
    Order completeOrder(Long orderId, Long runnerId);

    /**
     * 取消订单
     * @param orderId 订单ID
     * @param userId 操作用户ID
     * @param reason 取消原因
     * @return 更新后的订单
     */
    Order cancelOrder(Long orderId, Long userId, String reason);
}
