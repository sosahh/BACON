package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.UserMessage;
import com.jyss.bacon.entity.UserReport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReportMapper {

    int insert(UserReport userReport);

    //处理查询
    List<UserReport> getUserReport(@Param("uId")Integer uId);

    //查询已读消息记录
    List<UserMessage> selectUserMessageBy(@Param("uId")Integer uId,@Param("newId")Integer newId,@Param("type")Integer type);

    //添加已读消息
    int insertUserMessage(UserMessage userMessage);

}