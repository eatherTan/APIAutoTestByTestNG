package com.example.controller;

import com.example.entity.domain.UserInfoDomain;
import com.example.entity.response.QueryUserInfoListResponse;
import com.example.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 * 这一部门的代码作为项目代码，给编写测试用例时使用
 */
@Controller
@RequestMapping("/pi/user/queryUserInfo")
public class UserInfoController {

    @Autowired(required=true)
    private UserInfoService userInfoService;

    /**
     * 定义一个用户列表的接口
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public QueryUserInfoListResponse queryUserInfo( ){
        QueryUserInfoListResponse response = new QueryUserInfoListResponse();
        List<UserInfoDomain> userInfoList = userInfoService.queryUserInfo();
        response.setUserInfoList(userInfoList);
        return response;
    }
}
