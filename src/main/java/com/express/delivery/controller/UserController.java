package com.express.delivery.controller;

import com.express.delivery.common.Result;
import com.express.delivery.entity.User;
import com.express.delivery.entity.UserRegisterRequest;
import com.express.delivery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * POST /api/user/register
     */
    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody UserRegisterRequest request) {
        User user = userService.register(request);
        return Result.success(user);
    }

    /**
     * 用户登录
     * POST /api/user/login
     */
    @PostMapping("/login")
    public Result<User> login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        return Result.success(user);
    }
}
