package com.express.delivery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.express.delivery.entity.RegisterRequest;
import com.express.delivery.entity.User;
import com.express.delivery.mapper.UserMapper;
import com.express.delivery.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User register(RegisterRequest request) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStudentId, request.getStudentId());
        User existing = baseMapper.selectOne(wrapper);
        if (existing != null) {
            throw new RuntimeException("该学号已注册");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setStudentId(request.getStudentId());
        user.setRole("USER");
        baseMapper.insert(user);
        return user;
    }

    @Override
    public User login(String studentId, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getStudentId, studentId)
                .eq(User::getPassword, password);
        return baseMapper.selectOne(wrapper);
    }
}