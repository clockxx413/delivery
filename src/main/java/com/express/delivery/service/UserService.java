package com.express.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.express.delivery.entity.User;
import com.express.delivery.entity.UserRegisterRequest;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param request 注册信息
     * @return 注册成功的用户
     */
    User register(UserRegisterRequest request);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户
     */
    User login(String username, String password);
}
