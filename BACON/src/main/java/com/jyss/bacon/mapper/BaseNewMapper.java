package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.BaseNew;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseNewMapper {

    int insert(BaseNew baseNew);

    int updateByPrimaryKeySelective(BaseNew baseNew);

    int updateByPrimaryKey(BaseNew baseNew);

    //查询所有
    List<BaseNew> getAllNews();

}