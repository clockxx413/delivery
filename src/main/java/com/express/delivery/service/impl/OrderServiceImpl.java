package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.Order;
import com.express.delivery.entity.OrderCreateRequest;
import com.express.delivery.entity.OrderStatus;
import com.express.delivery.mapper.OrderMapper;
import com.express.delivery.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 订单服务实现
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Override
    public Order createOrder(OrderCreateRequest request) {
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setPublisherId(request.getPublisherId());
        order.setExpressCompany(request.getExpressCompany());
        order.setExpressCode(request.getExpressCode());
        order.setPickupAddress(request.getPickupAddress());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setRecipientPhone(request.getRecipientPhone());
        order.setDescription(request.getDescription());
        order.setTip(request.getTip() != null ? request.getTip() : BigDecimal.ZERO);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        this.save(order);
        return order;
    }

    @Override
    public Order acceptOrder(Long orderId, Long runnerId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new RuntimeException("当前订单状态不允许接单，当前状态: " + order.getStatus());
        }

        order.setRunnerId(runnerId);
        order.setStatus(OrderStatus.ACCEPTED);
        order.setUpdatedAt(LocalDateTime.now());
        this.updateById(order);
        return order;
    }

    @Override
    public Order completeOrder(Long orderId, Long runnerId) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!OrderStatus.ACCEPTED.equals(order.getStatus()) && !OrderStatus.DELIVERING.equals(order.getStatus())) {
            throw new RuntimeException("当前订单状态不允许完成，当前状态: " + order.getStatus());
        }
        if (!runnerId.equals(order.getRunnerId())) {
            throw new RuntimeException("只有接单的跑腿员才能完成此订单");
        }

        order.setStatus(OrderStatus.COMPLETED);
        order.setUpdatedAt(LocalDateTime.now());
        this.updateById(order);
        return order;
    }

    @Override
    public Order cancelOrder(Long orderId, Long userId, String reason) {
        Order order = this.getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (OrderStatus.COMPLETED.equals(order.getStatus())) {
            throw new RuntimeException("已完成的订单无法取消");
        }
        if (OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new RuntimeException("订单已被取消");
        }
        // 发布者或接单跑腿员都可以取消（待接单状态只有发布者可取消）
        if (OrderStatus.PENDING.equals(order.getStatus())) {
            if (!userId.equals(order.getPublisherId())) {
                throw new RuntimeException("待接单状态只有发布者可以取消订单");
            }
        } else {
            if (!userId.equals(order.getPublisherId()) && !userId.equals(order.getRunnerId())) {
                throw new RuntimeException("只有订单发布者或接单跑腿员才能取消订单");
            }
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason(reason);
        order.setUpdatedAt(LocalDateTime.now());
        this.updateById(order);
        return order;
    }

    /**
     * 生成订单编号
     */
    private String generateOrderNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "EXP" + datePart + uuidPart;
    }
}
