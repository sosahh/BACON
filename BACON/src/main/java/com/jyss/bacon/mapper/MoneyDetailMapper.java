package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.MoneyDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyDetailMapper {

    int insert(MoneyDetail moneyDetail);




}