package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.ScoreBalance;
import com.jyss.bacon.mapper.ScoreBalanceMapper;
import com.jyss.bacon.service.ScoreBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ScoreBalanceServiceImpl implements ScoreBalanceService{

    @Autowired
    private ScoreBalanceMapper scoreBalanceMapper;


    @Override
    public int insert(ScoreBalance scoreBalance) {
        return scoreBalanceMapper.insert(scoreBalance);
    }
}
