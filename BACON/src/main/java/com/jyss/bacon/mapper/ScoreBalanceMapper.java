package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.ScoreBalance;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreBalanceMapper {

    //添加消费记录
    int insert(ScoreBalance scoreBalance);

    int updateByPrimaryKeySelective(ScoreBalance scoreBalance);

    //查询总收入
    String getTotalIncome(@Param("uId")Integer uId);

    //查询今日收入
    String getIncomeToday(@Param("uId")Integer uId);

    //查询我的账单
    List<ScoreBalance> selectMyScoreBalance(@Param("uId")Integer uId);

}