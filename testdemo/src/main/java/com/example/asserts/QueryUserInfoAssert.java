package com.example.asserts;

import com.example.dao.QueryUserInfoDao;
import com.example.dao.impl.QueryUserInfoDaoImpl;
import com.example.entity.UserInfoDomain;
import com.example.response.QueryUserInfoListResponse;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;
import util.DBTools;

import java.util.List;


public class QueryUserInfoAssert {

    QueryUserInfoDao queryUserInfoDao = new QueryUserInfoDaoImpl();

    public void userInfoListAssert(QueryUserInfoListResponse response) {
        List<UserInfoDomain> userList = queryUserInfoDao.queryUserInfoList();
//        Assert.assertEquals(response.getTotalCount().intValue(),userList.size()); //数量校验
        for (int i = 0; i < userList.size(); i++) {
            UserInfoDomain userInfoDomain = userList.get(i);
            //每个信息都和数据库信息对比
            UserInfoDomain userInfoResp = response.getUserInfoList().get(i);
            Assert.assertEquals(userInfoResp.getId(), userInfoDomain.getId());
            Assert.assertEquals(userInfoResp.getName(), userInfoDomain.getName());
            Assert.assertEquals(userInfoResp.getAge(), userInfoDomain.getAge());
            Assert.assertEquals(userInfoResp.getGender(), userInfoDomain.getGender());
        }
    }
}

