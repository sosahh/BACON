package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserReport;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportMapper {

    int insert(UserReport userReport);

}