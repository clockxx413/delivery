package com.express.delivery.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.express.delivery.entity.*;

/**
 * 订单服务接口
 */
public interface OrderService extends IService<Order> {

    /**
     * 发布快递代取订单
     * @param request 订单信息
     * @return 创建的订单
     */
    Order publish(PublishOrderRequest request);

    /**
     * 跑腿员接单
     * @param request 接单信息
     * @return 更新后的订单
     */
    Order accept(AcceptOrderRequest request);

    /**
     * 更新订单状态
     * @param request 状态更新信息
     * @return 更新后的订单
     */
    Order updateStatus(UpdateStatusRequest request);

    /**
     * 获取订单列表（分页）
     * @param page 页码
     * @param size 每页条数
     * @param userId 用户ID（可选，用于筛选）
     * @param status 订单状态（可选，用于筛选）
     * @param keyword 关键词搜索（可选）
     * @return 分页结果
     */
    Page<Order> listOrders(int page, int size, Long userId, String status, String keyword);

    /**
     * 确认收货（含评价）
     * @param request 确认收货信息
     * @return 更新后的订单
     */
    Order confirm(ConfirmOrderRequest request);
}
