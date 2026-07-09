package com.express.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.express.delivery.entity.RegisterRequest;
import com.express.delivery.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param request 注册信息
     * @return 注册成功的用户信息（不含密码）
     */
    User register(RegisterRequest request);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户实体，不存在返回 null
     */
    User findByUsername(String username);
}
