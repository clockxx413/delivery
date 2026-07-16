package com.express.delivery.controller;

import com.express.delivery.common.Result;
import com.express.delivery.entity.LoginRequest;
import com.express.delivery.entity.RegisterRequest;
import com.express.delivery.entity.User;
import com.express.delivery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * POST /api/user/register
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return Result.success(user);
    }

    /**
     * 用户登录接口
     * POST /api/user/login
     */
    @PostMapping("/login")
    public Result<User> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getStudentId(), request.getPassword());
        if (user == null) {
            return Result.error("学号或密码错误");
        }
        return Result.success(user);
    }
}