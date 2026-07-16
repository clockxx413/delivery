package com.express.delivery.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.express.delivery.entity.RegisterRequest;
import com.express.delivery.entity.User;

public interface UserService extends IService<User> {
    User register(RegisterRequest request);
    User login(String studentId, String password);
}