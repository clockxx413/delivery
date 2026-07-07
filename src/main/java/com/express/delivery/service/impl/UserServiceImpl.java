package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.User;
import com.express.delivery.entity.UserRegisterRequest;
import com.express.delivery.mapper.UserMapper;
import com.express.delivery.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String ROLE_USER = "USER";

    @Override
    public User register(UserRegisterRequest request) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User existing = this.getOne(wrapper);
        if (existing != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查学号是否已被注册
        LambdaQueryWrapper<User> studentIdWrapper = new LambdaQueryWrapper<>();
        studentIdWrapper.eq(User::getStudentId, request.getStudentId());
        User existingByStudentId = this.getOne(studentIdWrapper);
        if (existingByStudentId != null) {
            throw new RuntimeException("该学号已被注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRealName(request.getRealName());
        user.setStudentId(request.getStudentId());
        user.setPhone(request.getPhone());
        user.setDormitory(request.getDormitory());
        user.setRole(ROLE_USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        this.save(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        wrapper.eq(User::getPassword, password);
        User user = this.getOne(wrapper);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }
}
