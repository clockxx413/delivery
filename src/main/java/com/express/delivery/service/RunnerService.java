package com.express.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.express.delivery.entity.Runner;
import com.express.delivery.entity.RunnerApplyRequest;

/**
 * 跑腿员服务接口
 */
public interface RunnerService extends IService<Runner> {

    /**
     * 申请成为跑腿员
     * @param request 申请信息
     * @return 创建的跑腿员记录
     */
    Runner apply(RunnerApplyRequest request);
}
