package com.example.dao;

import com.example.entity.UserInfoDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 把数据库的查询独立出来是为了能复用该数据库查询
 */
@Mapper
public interface QueryUserInfoDao {

    List<UserInfoDomain> queryUserInfoList();
}
