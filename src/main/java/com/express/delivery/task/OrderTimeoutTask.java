package com.express.delivery.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.express.delivery.entity.Order;
import com.express.delivery.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单超时自动取消定时任务
 */
@Component
public class OrderTimeoutTask {

    private static final Logger log = LoggerFactory.getLogger(OrderTimeoutTask.class);

    @Autowired
    private OrderService orderService;

    /**
     * 每30秒执行一次：将超时未接单的待接单订单自动取消
     */
    @Scheduled(fixedRate = 30000)
    public void cancelTimeoutOrders() {
        log.info("开始执行订单超时自动取消任务...");

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getStatus, OrderService.STATUS_PENDING)
               .lt(Order::getTimeoutAt, LocalDateTime.now());

        List<Order> timeoutOrders = orderService.list(wrapper);

        for (Order order : timeoutOrders) {
            order.setStatus(OrderService.STATUS_CANCELLED);
            order.setUpdatedAt(LocalDateTime.now());
            orderService.updateById(order);
            log.info("订单 {} 已超时自动取消", order.getId());
        }

        log.info("订单超时自动取消任务完成，本次取消 {} 个订单", timeoutOrders.size());
    }
}
