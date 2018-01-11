package com.jyss.bacon.service.impl;

import com.jyss.bacon.entity.MobileLogin;
import com.jyss.bacon.mapper.MobileLoginMapper;
import com.jyss.bacon.service.MobileLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class MobileLoginServiceImpl implements MobileLoginService {

    @Autowired
    private MobileLoginMapper mobileLoginMapper;

    @Override
    public int insert(MobileLogin mobileLogin) {
        return mobileLoginMapper.insert(mobileLogin);
    }

    @Override
    public List<MobileLogin> findUserByToken(String token) {
        List<MobileLogin> loginList = mobileLoginMapper.findUserByToken(token);
        if (loginList.size() == 1) {
            if (!loginList.get(0).getToken().equals(token)) {
                loginList = new ArrayList<MobileLogin>();
            }
        } else {
            loginList = new ArrayList<MobileLogin>();
        }
        return loginList;
    }




}
