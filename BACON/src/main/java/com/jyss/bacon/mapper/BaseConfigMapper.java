package com.jyss.bacon.mapper;


import com.jyss.bacon.entity.BaseConfig;
import com.jyss.bacon.entity.Xtgx;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface BaseConfigMapper {

    //查询协议
    List<BaseConfig> selectBaseConfig(@Param("key")String key);


    //安卓版本更新
    List<Xtgx> selectXtgx(@Param("type")Integer type);

}