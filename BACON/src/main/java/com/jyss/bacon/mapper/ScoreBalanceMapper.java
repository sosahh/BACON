package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.ScoreBalance;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreBalanceMapper {

    //添加消费记录
    int insert(ScoreBalance scoreBalance);

    int updateByPrimaryKeySelective(ScoreBalance scoreBalance);

    //查询总收入
    double getTotalIncome(@Param("uId")Integer uId);

    //查询今日收入
    double getIncomeToday(@Param("uId")Integer uId);

}