package com.example.asserts;

import com.example.dao.UserInfoDao;
import com.example.entity.UserInfoDomain;
import com.example.response.QueryUserInfoListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import java.util.List;

public class QueryUserInfoAssert {

    @Autowired
    private UserInfoDao userInfoDao;

    public void userInfoListAssert(QueryUserInfoListResponse response){
        List<UserInfoDomain> userList = userInfoDao.queryUserInfoList();

        Assert.assertEquals(response.getTotalCount().intValue(),userList.size()); //数量校验
        for (int i = 0; i < userList.size() ; i++) {
            UserInfoDomain userInfoDomain = userList.get(i);
            //每个信息都和数据库信息对比
            UserInfoDomain userInfoResp = response.getUserInfoList().get(i);
            Assert.assertEquals(userInfoResp.getId(),userInfoDomain.getId());
            Assert.assertEquals(userInfoResp.getName(),userInfoDomain.getName());
            Assert.assertEquals(userInfoResp.getAge(),userInfoDomain.getAge());
            Assert.assertEquals(userInfoResp.getGender(),userInfoDomain.getGender());

        }
    }
}
