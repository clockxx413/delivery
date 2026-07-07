package com.express.delivery.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.express.delivery.entity.Order;
import com.express.delivery.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单超时自动取消定时任务
 * 每60秒扫描一次，将超过30分钟未被接单的订单自动取消
 */
@Component
@EnableScheduling
public class OrderTimeoutTask {

    /** 超时阈值（分钟） */
    private static final int TIMEOUT_MINUTES = 30;

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 每60秒执行一次
     */
    @Scheduled(fixedRate = 60000)
    public void cancelTimeoutOrders() {
        // 计算超时时间点
        LocalDateTime timeoutPoint = LocalDateTime.now().minusMinutes(TIMEOUT_MINUTES);

        // 查找待接单且超过30分钟的订单
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getStatus, "PENDING")
                    .lt(Order::getCreatedAt, timeoutPoint);

        List<Order> timeoutOrders = orderMapper.selectList(queryWrapper);

        if (!timeoutOrders.isEmpty()) {
            // 批量更新为已取消
            LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Order::getStatus, "PENDING")
                         .lt(Order::getCreatedAt, timeoutPoint)
                         .set(Order::getStatus, "CANCELLED")
                         .set(Order::getCancelReason, "订单超时未接单，系统自动取消")
                         .set(Order::getCancelledAt, LocalDateTime.now())
                         .set(Order::getUpdatedAt, LocalDateTime.now());

            orderMapper.update(null, updateWrapper);
            System.out.println("[定时任务] 已自动取消 " + timeoutOrders.size() + " 个超时订单");
        }
    }
}
