package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.Item;
import com.jyss.bacon.entity.Page;
import com.jyss.bacon.entity.UserDynamic;
import com.jyss.bacon.entity.UserPraise;
import com.jyss.bacon.mapper.UserDynamicMapper;
import com.jyss.bacon.mapper.UserPraiseMapper;
import com.jyss.bacon.service.UserDynamicService;
import com.jyss.bacon.utils.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserDynamicServiceImpl implements UserDynamicService {
    @Autowired
    private UserDynamicMapper userDynamicMapper;
    @Autowired
    private UserPraiseMapper userPraiseMapper;

    /**
     * 点赞
     */
    @Override
    public int insertUserPraise(Integer uId, Integer dynamicId) {
        UserPraise userPraise = new UserPraise();
        userPraise.setuId(uId);
        userPraise.setDynamicId(dynamicId);
        userPraise.setStatus(1);
        userPraise.setCreated(new Date());
        int count = userPraiseMapper.insert(userPraise);
        return count;
    }

    /**
     * 取消点赞
     */
    @Override
    public int deletePraiseBy(Integer uId, Integer dynamicId) {
        int count = userPraiseMapper.deletePraiseBy(dynamicId, uId);
        return count;
    }


    /**
     * 条件查询动态      status: 0=未点赞，1=已点赞
     */
    @Override
    public Page<UserDynamic> selectUserDynamicBy(Integer uId, Integer sex, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserDynamic> dynamicList = userDynamicMapper.selectUserDynamicBy(null, sex);
        for (UserDynamic userDynamic : dynamicList) {
            List<UserPraise> praiseList = userPraiseMapper.getUserPraiseBy(userDynamic.getId(), uId, 1);
            if(praiseList != null && praiseList.size()>0){
                userDynamic.setStatus(1);
            }else{
                userDynamic.setStatus(0);
            }
            long count = userPraiseMapper.getCountPraise(userDynamic.getId());
            userDynamic.setCount(count);
            userDynamic.setShowTime(DateFormatUtils.showTimeText(userDynamic.getCreated()));
        }
        PageInfo<UserDynamic> pageInfo = new PageInfo<>(dynamicList);

        return new Page<UserDynamic>(pageInfo);
    }


    /**
     * 查询关注人的动态
     */
    @Override
    public Page<UserDynamic> getDynamicByFellowId(Integer uId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserDynamic> list = userDynamicMapper.getDynamicByFellowId(uId);
        for (UserDynamic userDynamic : list) {
            userDynamic.setShowTime(DateFormatUtils.showTimeText(userDynamic.getCreated()));
            List<UserPraise> praiseList = userPraiseMapper.getUserPraiseBy(userDynamic.getId(), uId, 1);
            if(praiseList != null && praiseList.size()>0){
                userDynamic.setStatus(1);
            }else{
                userDynamic.setStatus(0);
            }
            long count = userPraiseMapper.getCountPraise(userDynamic.getId());
            userDynamic.setCount(count);
        }
        PageInfo<UserDynamic> pageInfo = new PageInfo<>(list);
        return new Page<UserDynamic>(pageInfo);
    }

    /**
     * 查询我的动态      status: 0=未点赞，1=已点赞
     */
    @Override
    public Page<UserDynamic> selectMyUserDynamic(Integer uId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserDynamic> dynamicList = userDynamicMapper.selectUserDynamicBy(uId, null);
        for (UserDynamic userDynamic : dynamicList) {
            List<UserPraise> praiseList = userPraiseMapper.getUserPraiseBy(userDynamic.getId(), uId, 1);
            if(praiseList != null && praiseList.size()>0){
                userDynamic.setStatus(1);
            }else{
                userDynamic.setStatus(0);
            }
            long count = userPraiseMapper.getCountPraise(userDynamic.getId());
            userDynamic.setCount(count);
            userDynamic.setShowTime(DateFormatUtils.showTimeText(userDynamic.getCreated()));
        }
        PageInfo<UserDynamic> pageInfo = new PageInfo<>(dynamicList);

        return new Page<UserDynamic>(pageInfo);
    }

    /**
     * 删除我的动态
     */
    @Override
    public int deleteUserDynamicById(Integer dynamicId) {
        int count = userDynamicMapper.deleteUserDynamicById(dynamicId);
        return count;
    }

    /**
     * 发布动态
     */


}
