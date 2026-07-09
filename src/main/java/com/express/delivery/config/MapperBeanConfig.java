package com.express.delivery.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.express.delivery.mapper.OrderMapper;
import com.express.delivery.mapper.ReviewMapper;
import com.express.delivery.mapper.RunnerMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 手动配置 MyBatis-Plus（兼容课程定制版 Spring Boot）
 */
@Configuration
public class MapperBeanConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);

        // 手动注册 Mapper 接口
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.addMapper(RunnerMapper.class);
        configuration.addMapper(OrderMapper.class);
        configuration.addMapper(ReviewMapper.class);
        factoryBean.setConfiguration(configuration);

        return factoryBean.getObject();
    }

    @Bean
    public RunnerMapper runnerMapper(SqlSessionFactory sqlSessionFactory) {
        return sqlSessionFactory.openSession(true).getMapper(RunnerMapper.class);
    }

    @Bean
    public OrderMapper orderMapper(SqlSessionFactory sqlSessionFactory) {
        return sqlSessionFactory.openSession(true).getMapper(OrderMapper.class);
    }

    @Bean
    public ReviewMapper reviewMapper(SqlSessionFactory sqlSessionFactory) {
        return sqlSessionFactory.openSession(true).getMapper(ReviewMapper.class);
    }
}
