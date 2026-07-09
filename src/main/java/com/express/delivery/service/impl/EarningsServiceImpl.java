package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.Earnings;
import com.express.delivery.entity.WithdrawRequest;
import com.express.delivery.mapper.EarningsMapper;
import com.express.delivery.service.EarningsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 收益记录服务实现
 */
@Service
public class EarningsServiceImpl extends ServiceImpl<EarningsMapper, Earnings> implements EarningsService {

    private static final String TYPE_DELIVERY_FEE = "DELIVERY_FEE";
    private static final String TYPE_WITHDRAW = "WITHDRAW";
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_SETTLED = "SETTLED";
    private static final String STATUS_WITHDRAWN = "WITHDRAWN";

    @Override
    @Transactional
    public Earnings addEarnings(Long runnerId, Long orderId, BigDecimal amount, String type, String description) {
        // 防重复：同一订单不能重复添加收益
        LambdaQueryWrapper<Earnings> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Earnings::getOrderId, orderId)
               .ne(Earnings::getType, TYPE_WITHDRAW); // 排除提现记录
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("该订单已生成收益记录，请勿重复操作");
        }

        Earnings earnings = new Earnings();
        earnings.setRunnerId(runnerId);
        earnings.setOrderId(orderId);
        earnings.setAmount(amount != null ? amount : new BigDecimal("5.00"));
        earnings.setType(type != null ? type : TYPE_DELIVERY_FEE);
        earnings.setStatus(STATUS_SETTLED); // 订单完成后直接设为已结算
        earnings.setDescription(description != null ? description : ("订单 #" + orderId + " 配送收益"));
        earnings.setCreatedAt(LocalDateTime.now());
        earnings.setUpdatedAt(LocalDateTime.now());

        this.save(earnings);
        return earnings;
    }

    @Override
    public List<Earnings> listByRunnerId(Long runnerId) {
        LambdaQueryWrapper<Earnings> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Earnings::getRunnerId, runnerId)
               .orderByDesc(Earnings::getCreatedAt);
        return this.list(wrapper);
    }

    @Override
    public Map<String, Object> statistics(Long runnerId) {
        List<Earnings> all = listByRunnerId(runnerId);

        BigDecimal totalEarnings = BigDecimal.ZERO;
        BigDecimal pendingAmount = BigDecimal.ZERO;
        BigDecimal settledAmount = BigDecimal.ZERO;
        BigDecimal withdrawnAmount = BigDecimal.ZERO;
        int totalOrders = 0;
        int pendingOrders = 0;
        int settledOrders = 0;
        int withdrawnOrders = 0;

        for (Earnings e : all) {
            // 提现类型不计入收益统计
            if (TYPE_WITHDRAW.equals(e.getType())) {
                withdrawnAmount = withdrawnAmount.add(e.getAmount().abs());
                continue;
            }

            totalEarnings = totalEarnings.add(e.getAmount());
            totalOrders++;

            switch (e.getStatus()) {
                case STATUS_PENDING:
                    pendingAmount = pendingAmount.add(e.getAmount());
                    pendingOrders++;
                    break;
                case STATUS_SETTLED:
                    settledAmount = settledAmount.add(e.getAmount());
                    settledOrders++;
                    break;
                case STATUS_WITHDRAWN:
                    withdrawnAmount = withdrawnAmount.add(e.getAmount());
                    withdrawnOrders++;
                    break;
            }
        }

        // 可提现余额 = 已结算金额
        BigDecimal availableBalance = settledAmount;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("runnerId", runnerId);
        result.put("totalEarnings", totalEarnings);
        result.put("pendingAmount", pendingAmount);
        result.put("settledAmount", settledAmount);
        result.put("withdrawnAmount", withdrawnAmount);
        result.put("availableBalance", availableBalance);
        result.put("totalOrders", totalOrders);
        result.put("pendingOrders", pendingOrders);
        result.put("settledOrders", settledOrders);
        result.put("withdrawnOrders", withdrawnOrders);
        return result;
    }

    @Override
    @Transactional
    public Earnings withdraw(WithdrawRequest request) {
        // 1. 计算可提现余额（已结算的收益总额）
        BigDecimal available = getAvailableBalance(request.getRunnerId());

        if (available.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("当前无可提现余额");
        }
        if (request.getAmount().compareTo(available) > 0) {
            throw new RuntimeException("提现金额超出可提现余额。可提现: " + available + "元");
        }

        // 2. 把已结算的收益标记为已提现（先进先出，直到满足提现金额）
        LambdaQueryWrapper<Earnings> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Earnings::getRunnerId, request.getRunnerId())
               .eq(Earnings::getStatus, STATUS_SETTLED)
               .ne(Earnings::getType, TYPE_WITHDRAW)
               .orderByAsc(Earnings::getCreatedAt);
        List<Earnings> settledList = this.list(wrapper);

        BigDecimal remaining = request.getAmount();
        for (Earnings e : settledList) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
            e.setStatus(STATUS_WITHDRAWN);
            e.setUpdatedAt(LocalDateTime.now());
            this.updateById(e);
            remaining = remaining.subtract(e.getAmount());
        }

        // 3. 创建提现记录
        Earnings withdrawRecord = new Earnings();
        withdrawRecord.setRunnerId(request.getRunnerId());
        withdrawRecord.setOrderId(0L); // 提现不关联具体订单
        withdrawRecord.setAmount(request.getAmount().negate()); // 负数表示支出
        withdrawRecord.setType(TYPE_WITHDRAW);
        withdrawRecord.setStatus(STATUS_SETTLED); // 提现已完成
        withdrawRecord.setDescription("提现 " + request.getAmount() + " 元");
        withdrawRecord.setCreatedAt(LocalDateTime.now());
        withdrawRecord.setUpdatedAt(LocalDateTime.now());

        this.save(withdrawRecord);
        return withdrawRecord;
    }

    /**
     * 计算可提现余额
     */
    private BigDecimal getAvailableBalance(Long runnerId) {
        LambdaQueryWrapper<Earnings> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Earnings::getRunnerId, runnerId)
               .eq(Earnings::getStatus, STATUS_SETTLED)
               .ne(Earnings::getType, TYPE_WITHDRAW);
        List<Earnings> settledList = this.list(wrapper);
        return settledList.stream()
                .map(Earnings::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
