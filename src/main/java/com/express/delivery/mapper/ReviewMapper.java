package com.express.delivery.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.express.delivery.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 评价 Mapper
 */
@Mapper
public interface ReviewMapper extends BaseMapper<Review> {

    /**
     * 查询某用户的平均评分（作为被评价者）
     */
    @Select("SELECT COALESCE(AVG(score), 0) FROM review WHERE target_user_id = #{userId}")
    Double getAverageScoreByUserId(Long userId);
}
