package com.example.userinfo;

import com.example.request.UserInfoRequest;
import org.testng.annotations.Test;

public class QueryUserInfoTest{

    UserInfoRequest request = new UserInfoRequest();
    /**
     * 使用HttpUtils
     */
    @Test
    public void testUserInfo(){
        String url = "http://127.0.0.1:8080/demo/pi/user/queryUserInfo/list";
        request.userInfoCommonRequst(url);
    }
    @Test
    public void testUserInfo1(){
        String url = "http://127.0.0.1:8080/demo/pi/user/queryUserInfo/list";
        request.userInfoCommonRequst(url);
    }
}
