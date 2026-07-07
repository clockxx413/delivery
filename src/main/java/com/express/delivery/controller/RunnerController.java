package com.express.delivery.controller;

import com.express.delivery.common.Result;
import com.express.delivery.entity.Runner;
import com.express.delivery.entity.RunnerApplyRequest;
import com.express.delivery.service.RunnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 跑腿员控制器
 */
@RestController
@RequestMapping("/api/runner")
public class RunnerController {

    @Autowired
    private RunnerService runnerService;

    /**
     * 跑腿员申请接口
     * POST /api/runner/apply
     */
    @PostMapping("/apply")
    public Result<Runner> apply(@Valid @RequestBody RunnerApplyRequest request) {
        Runner runner = runnerService.apply(request);
        return Result.success(runner);
    }
}
