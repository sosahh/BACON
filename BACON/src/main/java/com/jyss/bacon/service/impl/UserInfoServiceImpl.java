package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.*;
import com.jyss.bacon.mapper.*;
import com.jyss.bacon.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService{

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserDynamicMapper userDynamicMapper;
    @Autowired
    private XtclMapper xtclMapper;

    /**
     * 用户名或id搜索
     */
    @Override
    public Page<UserInfo> getUserByNickOrAccount(String account, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserInfo> list = userInfoMapper.getUserByNickOrAccount(account);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);

        Page<UserInfo> result = new Page<>();
        result.setTotal(pageInfo.getTotal());
        result.setNext(pageInfo.isHasNextPage());
        result.setRows(list);
        return result;
    }

    /**
     * 根据类目查询
     */
    @Override
    public Page<UserInfo> getUserByCategoryId(Integer categoryId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserInfo> list = userInfoMapper.getUserInfoBy(categoryId,null,"",null,null);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);

        Page<UserInfo> result = new Page<>();
        result.setTotal(pageInfo.getTotal());
        result.setNext(pageInfo.isHasNextPage());
        result.setRows(list);
        return result;
    }

    /**
     * 根据条件查询     type: 1 = 22岁以下，2 = 22-25岁，3 = 25岁以上
     */
    @Override
    public Page<UserInfo> getUserInfoBy(Integer categoryId, Integer sex, String titlePwName, Integer type, Integer page, Integer pageSize) {
        List<UserInfo> list = new ArrayList<UserInfo>();

        //查询年龄
        Xtcl xtcl1 = xtclMapper.getClsValue("age_type", "1");
        int age = Integer.parseInt(xtcl1.getBz_value());                              //22
        Xtcl xtcl2 = xtclMapper.getClsValue("age_type", "2");
        int age1 = Integer.parseInt(xtcl2.getBz_value());                             //25

        PageHelper.startPage(page,pageSize);
        if(type == null){
            //全部年龄
            list = userInfoMapper.getUserInfoBy(categoryId,sex,titlePwName,null,null);
        }else if (type == 1){
            //22岁以下
            list = userInfoMapper.getUserInfoBy(categoryId,sex,titlePwName,age,null);
        }else if (type == 2){
            //22-25岁
            list = userInfoMapper.getUserInfoBy(categoryId,sex,titlePwName,age1,age);
        }else if (type == 3){
            //25岁以上
            list = userInfoMapper.getUserInfoBy(categoryId,sex,titlePwName,null,age1);
        }
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);

        Page<UserInfo> result = new Page<>();
        result.setTotal(pageInfo.getTotal());
        result.setNext(pageInfo.isHasNextPage());
        result.setRows(list);
        return result;
    }


    /**
     * 查询明星用户
     */
    @Override
    public Page<UserInfo> getStarUserInfo(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserInfo> list = userInfoMapper.getStarUserInfo();
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);

        return new Page<>(pageInfo);
    }


    /**
     * 查询热门用户
     */
    @Override
    public Page<UserInfo> getHotUserInfo(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserInfo> list = userInfoMapper.getHotUserInfo();
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);

        return new Page<>(pageInfo);
    }


    /**
     * 查询详细信息
     */
    @Override
    public UserDetailResult findUserDetailInfo(Integer uId, Integer playId) {
        UserDetailResult result = new UserDetailResult();
        List<User> userList = userMapper.selectUserBy(playId + "", null, null);

        User user = userList.get(0);
        //关注数
        int count = userFollowMapper.getUserFellowCount(playId);
        //是否已关注
        List<UserFollow> fellowList = userFollowMapper.getUserFellowBy(uId, playId, 1);
        //认证游戏
        List<UserAuth> userAuthList = userAuthMapper.getUserAuthBy(playId, null, 2);

        if(uId == playId){
            result.setuId(uId);
            result.setUser(user);
            result.setCount(count);
            result.setPictures(new ArrayList<>());
            result.setType(false);
            result.setGames(userAuthList);
            return result;
        }
        //动态
        List<UserDynamic> userDynamicList = userDynamicMapper.getPicture(playId);
        List<String> list = new ArrayList<>();
        for (UserDynamic userDynamic : userDynamicList) {
            list.add(userDynamic.getPicture1());
        }

        if(fellowList != null && fellowList.size()>0){
            result.setType(true);
        }else {
            result.setType(false);
        }
        result.setuId(uId);
        result.setUser(user);
        result.setCount(count);
        result.setPictures(list);
        result.setGames(userAuthList);
        return result;
    }


}
