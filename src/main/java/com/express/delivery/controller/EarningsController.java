package com.express.delivery.controller;

import com.express.delivery.common.Result;
import com.express.delivery.entity.Earnings;
import com.express.delivery.entity.WithdrawRequest;
import com.express.delivery.service.EarningsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 收益模块控制器
 */
@RestController
@RequestMapping("/api/earnings")
public class EarningsController {

    @Autowired
    private EarningsService earningsService;

    /**
     * 查询跑腿员收益列表
     * GET /api/earnings/list?runnerId=xxx
     */
    @GetMapping("/list")
    public Result<List<Earnings>> list(@RequestParam Long runnerId) {
        List<Earnings> list = earningsService.listByRunnerId(runnerId);
        return Result.success(list);
    }

    /**
     * 跑腿员收益统计
     * GET /api/earnings/statistics?runnerId=xxx
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> statistics(@RequestParam Long runnerId) {
        Map<String, Object> stats = earningsService.statistics(runnerId);
        return Result.success(stats);
    }

    /**
     * 提现申请
     * POST /api/earnings/withdraw
     */
    @PostMapping("/withdraw")
    public Result<Earnings> withdraw(@Valid @RequestBody WithdrawRequest request) {
        Earnings result = earningsService.withdraw(request);
        return Result.success(result);
    }
}
