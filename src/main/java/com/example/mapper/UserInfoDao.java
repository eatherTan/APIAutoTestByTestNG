package com.example.mapper;

import com.example.entity.domain.UserInfoDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserInfoDao {
    List<UserInfoDomain> queryUserInfo();
}
