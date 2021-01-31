package com.example.service;

import com.example.mapper.UserInfoDao;
import com.example.entity.domain.UserInfoDomain;
import com.example.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public List<UserInfoDomain> queryUserInfo() {
        List<UserInfoDomain> userInfos = userInfoDao.queryUserInfo();
        return userInfos;
    }
}
