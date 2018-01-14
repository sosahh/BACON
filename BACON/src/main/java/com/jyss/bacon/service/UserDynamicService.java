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

    //取消点赞
    int deletePraiseBy(@Param("uId")Integer uId, @Param("dynamicId")Integer dynamicId);

    //条件查询动态
    Page<UserDynamic> selectUserDynamicBy(@Param("uId")Integer uId, @Param("sex")Integer sex,@Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //查询关注人的动态
    Page<UserDynamic> getDynamicByFellowId(@Param("uId")Integer uId, @Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //查询我的动态
    Page<UserDynamic> selectMyUserDynamic(@Param("uId")Integer uId, @Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //删除我的动态
    int deleteUserDynamicById(@Param("dynamicId")Integer dynamicId);

    //添加动态


    //评价动态
    ResponseResult insertUserComment(@Param("uId")Integer uId,@Param("dynamicId")Integer dynamicId,@Param("content")String content);

    //动态评价查询
    Page<UserComment>  selectCommentBy(@Param("dynamicId")Integer dynamicId,@Param("page")Integer page, @Param("pageSize")Integer pageSize);

    //删除评价
    ResponseResult deleteCommentBy(@Param("dynamicId")Integer dynamicId,@Param("uId")Integer uId);

}
