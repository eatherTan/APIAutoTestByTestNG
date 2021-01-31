package com.example.userinfo;

import com.alibaba.fastjson.JSON;
import com.example.request.UserInfoRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.testng.annotations.Test;
import util.HttpPostUtil;
import util.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QueryUserInfoTest {

    UserInfoRequest request = new UserInfoRequest();
    /**
     * 使用HttpUtils
     */
    @Test
    public void testUserInfo(){
        String url = "http://127.0.0.1:8080/demo/pi/user/queryUserInfo/list";
        request.userInfoCommonRequst(url);
    }
}
