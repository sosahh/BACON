package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.ScoreBalance;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreBalanceMapper {

    //添加消费记录
    int insert(ScoreBalance scoreBalance);

    int updateByPrimaryKeySelective(ScoreBalance scoreBalance);

}