package com.example.dao.impl;

import com.example.dao.QueryUserInfoDao;
import com.example.entity.UserInfoDomain;
import org.apache.ibatis.session.SqlSession;
import util.DBTools;

import java.util.List;

public class QueryUserInfoDaoImpl implements QueryUserInfoDao {

    SqlSession session = DBTools.getSession();

    @Override
    public List<UserInfoDomain> queryUserInfoList() {
        SqlSession session = DBTools.getSession();
        QueryUserInfoDao mapper = session.getMapper(QueryUserInfoDao.class);
        List<UserInfoDomain> userList = mapper.queryUserInfoList();
        return userList;
    }
}
