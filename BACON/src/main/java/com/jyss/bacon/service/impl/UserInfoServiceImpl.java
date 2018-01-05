package com.jyss.bacon.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyss.bacon.entity.Page;
import com.jyss.bacon.entity.UserInfo;
import com.jyss.bacon.mapper.UserInfoMapper;
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
        result.setRows(list);
        return result;
    }

    /**
     * 根据类目查询
     */
    @Override
    public Page<UserInfo> getUserByCategoryId(Integer categoryId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<UserInfo> list = userInfoMapper.getUserByCategoryId(categoryId,null,"",null,null);
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);

        Page<UserInfo> result = new Page<>();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }

    /**
     * 根据条件查询     type: 0 = 全部年龄，1 = 22岁以下，2 = 22-25岁，3 = 25岁以上
     */
    @Override
    public Page<UserInfo> getUserInfoBy(Integer categoryId, Integer sex, String titlePwName, Integer type, Integer page, Integer pageSize) {
        List<UserInfo> list = new ArrayList<UserInfo>();
        PageHelper.startPage(page,pageSize);
        if(type == 0){
            //全部年龄
            list = userInfoMapper.getUserByCategoryId(categoryId,sex,titlePwName,null,null);
        }else if (type == 1){
            //22岁以下
            list = userInfoMapper.getUserByCategoryId(categoryId,sex,titlePwName,22,null);
        }else if (type == 2){
            //22-25岁
            list = userInfoMapper.getUserByCategoryId(categoryId,sex,titlePwName,25,22);
        }else if (type == 3){
            //25岁以上
            list = userInfoMapper.getUserByCategoryId(categoryId,sex,titlePwName,null,25);
        }
        PageInfo<UserInfo> pageInfo = new PageInfo<>(list);

        Page<UserInfo> result = new Page<>();
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);
        return result;
    }




}
