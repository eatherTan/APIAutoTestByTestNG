package com.example.service;

import com.example.entity.domain.UserInfoDomain;

import java.util.List;


public interface UserInfoService {
    List<UserInfoDomain> queryUserInfo();
}
