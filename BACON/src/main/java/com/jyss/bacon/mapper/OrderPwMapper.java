package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.OrderPw;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPwMapper {

    //添加陪玩订单
    int insert(OrderPw orderPw);

    int updateByPrimaryKeySelective(OrderPw orderPw);

    //条件查询
    List<OrderPw> selectOrderPwBy(@Param("playId")Integer playId,@Param("id")Integer id,
                                  @Param("uId")Integer uId,@Param("status")Integer status);

    //查询个人订单
    List<OrderPw> selectOrderPwByUid(@Param("uId")Integer uId);

    //查询陪玩订单
    List<OrderPw> selectOrderPwByPlayId(@Param("playId")Integer playId);

    //删除未支付订单
    int deleteOrderPwBy(@Param("id")Integer id,@Param("uId")Integer uId);

}