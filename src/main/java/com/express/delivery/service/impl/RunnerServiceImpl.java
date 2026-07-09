package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.Runner;
import com.express.delivery.entity.RunnerApplyRequest;
import com.express.delivery.mapper.RunnerMapper;
import com.express.delivery.service.RunnerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 跑腿员服务实现
 */
@Service
public class RunnerServiceImpl extends ServiceImpl<RunnerMapper, Runner> implements RunnerService {

    /** 申请状态常量 */
    private static final String STATUS_PENDING = "PENDING";

    @Override
    public Runner apply(RunnerApplyRequest request) {
        // 检查用户是否已提交过申请
        LambdaQueryWrapper<Runner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Runner::getUserId, request.getUserId());
        Runner existing = this.getOne(wrapper);
        if (existing != null) {
            throw new RuntimeException("您已提交过跑腿员申请，当前状态: " + existing.getStatus());
        }

        // 创建新的跑腿员申请
        Runner runner = new Runner();
        runner.setUserId(request.getUserId());
        runner.setRealName(request.getRealName());
        runner.setPhone(request.getPhone());
        runner.setStudentId(request.getStudentId());
        runner.setStatus(STATUS_PENDING);
        runner.setCreatedAt(LocalDateTime.now());
        runner.setUpdatedAt(LocalDateTime.now());

        this.save(runner);
        return runner;
    }
}
