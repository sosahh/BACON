package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.OrderEvaluate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderEvaluateMapper {

    int insertEvaluate(OrderEvaluate orderEvaluate);

    int updateByPrimaryKeySelective(OrderEvaluate orderEvaluate);

    /**
     * 查询评价数量
     * @param playId
     * @param categoryId
     * @return
     */
    int selectCountEvaluate(@Param("playId")Integer playId, @Param("categoryId")Integer categoryId);

    /**
     * 条件查询评论
     * @param playId
     * @param categoryId
     * @return
     */
    List<OrderEvaluate> selectOrderEvaluateBy(@Param("playId")Integer playId, @Param("categoryId")Integer categoryId);
}