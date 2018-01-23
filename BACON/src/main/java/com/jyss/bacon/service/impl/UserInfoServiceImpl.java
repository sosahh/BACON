package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.*;
import com.jyss.bacon.mapper.*;
import com.jyss.bacon.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
     * 根据条件查询     type: 10 = 全部，11 = 22岁以下，12 = 22-25岁，13 = 25岁以上
     */
    @Override
    public Page<UserInfo> getUserInfoBy(Integer categoryId, Integer sex, String titlePwName, String type, Integer page, Integer pageSize) {
        List<UserInfo> list = new ArrayList<UserInfo>();

        //查询年龄
        Xtcl xtcl1 = xtclMapper.getClsValue("age_type", "1");
        int age = Integer.parseInt(xtcl1.getBz_value());                              //22
        Xtcl xtcl2 = xtclMapper.getClsValue("age_type", "2");
        int age1 = Integer.parseInt(xtcl2.getBz_value());                             //25

        PageHelper.startPage(page,pageSize);
        if(type.equals("10")){
            //全部年龄
            list = userInfoMapper.getUserInfoBy(categoryId,sex,titlePwName,null,null);
        }else if (type.equals("11")){
            //22岁以下
            list = userInfoMapper.getUserInfoBy(categoryId,sex,titlePwName,age,null);
        }else if (type.equals("12")){
            //22-25岁
            list = userInfoMapper.getUserInfoBy(categoryId,sex,titlePwName,age1,age);
        }else if (type.equals("13")){
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
     * 查询新人用户
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Page<UserInfo> getNewUserInfo(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserInfo> list = userInfoMapper.getNewUserInfo();
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
        //认证游戏
        List<UserAuth> userAuthList = userAuthMapper.getUserAuthByIsShelve(playId, null, 2);
        //动态
        List<UserDynamic> userDynamicList = userDynamicMapper.getPicture(playId);
        if(uId == null){
            result.setType(false);
            result.setuId(0);
        }else{
            //是否已关注
            List<UserFollow> fellowList = userFollowMapper.getUserFellowBy(uId, playId, 1);
            if(fellowList != null && fellowList.size()>0){
                result.setType(true);
            }else {
                result.setType(false);
            }
            result.setuId(uId);
        }
        result.setUser(user);
        result.setCount(count);
        result.setPictures(userDynamicList);
        result.setGames(userAuthList);
        return result;
    }


}
