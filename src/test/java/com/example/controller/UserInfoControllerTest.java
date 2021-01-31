package com.example.controller;

import com.example.DemoApplication;
import com.example.entity.domain.UserInfoDomain;
import com.example.entity.response.QueryUserInfoListResponse;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

/**
 * 单元测试
 */
class UserInfoControllerTest{
    @Autowired
    private UserInfoController userInfoController;
    @Test
    void queryUserInfo() {
        QueryUserInfoListResponse list = userInfoController.queryUserInfo();
    }
}