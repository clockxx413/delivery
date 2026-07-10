package com.express.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.express.delivery.entity.Earnings;
import com.express.delivery.entity.WithdrawRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 收益记录服务接口
 */
public interface EarningsService extends IService<Earnings> {

    /**
     * 订单完成时自动添加收益记录（供B同学的订单模块调用）
     * @param runnerId   跑腿员ID
     * @param orderId    订单ID
     * @param amount     收益金额
     * @param type       收益类型
     * @param description 描述
     * @return 创建的收益记录
     */
    Earnings addEarnings(Long runnerId, Long orderId, BigDecimal amount, String type, String description);

    /**
     * 查询跑腿员的收益列表
     * @param runnerId 跑腿员ID
     * @return 收益记录列表
     */
    List<Earnings> listByRunnerId(Long runnerId);

    /**
     * 收益统计（总收益、待结算、已结算、已提现、订单数）
     * @param runnerId 跑腿员ID
     * @return 统计数据Map
     */
    Map<String, Object> statistics(Long runnerId);

    /**
     * 提现申请
     * @param request 提现请求（runnerId + amount）
     * @return 提现收益记录
     */
    Earnings withdraw(WithdrawRequest request);
}
