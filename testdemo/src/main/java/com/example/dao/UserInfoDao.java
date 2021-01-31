package com.example.dao;

import com.example.entity.UserInfoDomain;

import java.util.List;

public interface UserInfoDao {

    List<UserInfoDomain> queryUserInfoList();
}
