package com.express.delivery.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus 配置
 * 注意：MyBatis-Plus 3.5.9 已移除 PaginationInnerInterceptor，
 * 分页功能由 MybatisPlusInterceptor 内置支持，无需额外配置方言。
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // MyBatis-Plus 3.5.9+ 分页插件已内置，自动识别数据库方言
        return interceptor;
    }
}
