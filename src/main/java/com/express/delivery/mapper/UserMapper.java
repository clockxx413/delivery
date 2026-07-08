package com.express.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.express.delivery.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
