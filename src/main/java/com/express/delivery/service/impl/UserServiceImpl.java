package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.RegisterRequest;
import com.express.delivery.entity.User;
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
    public User register(RegisterRequest request) {
        // 检查用户名是否已存在
        User existing = findByUsername(request.getUsername());
        if (existing != null) {
            throw new RuntimeException("用户名已存在: " + request.getUsername());
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setStudentId(request.getStudentId());
        user.setRole(ROLE_USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        this.save(user);

        // 返回时清除密码
        user.setPassword(null);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.getOne(wrapper);
    }
}
