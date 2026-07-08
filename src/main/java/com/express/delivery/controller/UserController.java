package com.express.delivery.controller;

import com.express.delivery.common.Result;
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
}
