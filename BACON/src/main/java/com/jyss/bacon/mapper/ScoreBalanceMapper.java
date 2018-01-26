package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.ScoreBalance;
import com.jyss.bacon.entity.ScoreEarn;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreBalanceMapper {

    //添加消费记录
    int insert(ScoreBalance scoreBalance);

    //添加收入记录
    int insertScoreEarn(ScoreEarn scoreEarn);

    //查询总收入
    String getTotalIncome(@Param("uId")Integer uId);

    //查询今日收入
    String getIncomeToday(@Param("uId")Integer uId);

    //培根币账单
    List<ScoreBalance> selectMyScoreBalance(@Param("uId")Integer uId);

    //收入账单
    List<ScoreEarn> selectScoreEarn(@Param("uId")Integer uId);


}