package com.jyss.bacon.service;


import com.jyss.bacon.entity.Page;
import com.jyss.bacon.entity.ResponseResult;
import com.jyss.bacon.entity.UserComment;
import com.jyss.bacon.entity.UserDynamic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDynamicService {

    //点赞
    int insertUserPraise(@Param("uId")Integer uId, @Param("dynamicId")Integer dynamicId);

    //新闻点赞
    int insertUserPraiseNew(@Param("uId")Integer uId, @Param("newId")Integer newId);

    //取消点赞
    int deletePraiseBy(@Param("uId")Integer uId, @Param("dynamicId")Integer dynamicId);

    //取消新闻点赞
    int deleteNewPraiseBy(@Param("uId")Integer uId, @Param("newId")Integer newId);

    //条件查询动态
    Page<UserDynamic> selectUserDynamicBy(@Param("uId")Integer uId, @Param("sex")Integer sex,@Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //查询关注人的动态
    Page<UserDynamic> getDynamicByFellowId(@Param("uId")Integer uId, @Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //查询我的动态
    Page<UserDynamic> selectMyUserDynamic(@Param("uId")Integer uId, @Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //查询陪玩人的动态
    Page<UserDynamic> selectDynamicByPlayId(@Param("uId")Integer uId,@Param("playId")Integer playId,@Param("page") Integer page, @Param("pageSize")Integer pageSize);

    //删除我的动态
    int deleteUserDynamicById(@Param("dynamicId")Integer dynamicId);

    //添加动态
    int insert(UserDynamic userDynamic);

    //评价动态
    ResponseResult insertUserComment(@Param("uId")Integer uId,@Param("dynamicId")Integer dynamicId,@Param("content")String content);

    //评价新闻动态
    ResponseResult insertUserCommentNew(@Param("uId")Integer uId,@Param("newId")Integer newId,@Param("content")String content);

    //动态评价查询
    Page<UserComment> selectCommentBy(@Param("dynamicId")Integer dynamicId,@Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //新闻评价查询
    Page<UserComment> selectNewCommentBy(@Param("newId")Integer newId,@Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //删除评价
    ResponseResult deleteCommentBy(@Param("dynamicId")Integer dynamicId,@Param("uId")Integer uId);

    //删除新闻评价
    ResponseResult deleteNewCommentBy(@Param("newId")Integer newId,@Param("uId")Integer uId);


}
