package com.example.request;

import com.alibaba.fastjson.JSON;
import com.example.asserts.QueryUserInfoAssert;
import com.example.response.QueryUserInfoListResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.testng.Assert;
import util.HttpUtils;

import java.util.HashMap;
import java.util.Map;

public class UserInfoRequest {

    QueryUserInfoAssert userInfoAssert = new QueryUserInfoAssert();

    public void userInfoCommonRequst(String url){
        Map<String,String> map = new HashMap<>();
        map.put("param","123");
        map.put("param2","123");
        String response = HttpUtils.postHttp(url,map);
        QueryUserInfoListResponse userInfoListResponse = JSON.parseObject(response,QueryUserInfoListResponse.class);
        if (userInfoListResponse.getBaseResponse().getErrorCode().equals("0000")){
            userInfoAssert.userInfoListAssert(userInfoListResponse);
        }else {
            Assert.fail("接口错误，校验不通过");
        }
    }
}
