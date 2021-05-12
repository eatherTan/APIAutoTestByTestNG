package com.example.util;

import com.example.entity.response.QueryUserInfoListResponse;
import org.testng.annotations.Test;
import util.BuildFrameworkUtil.bd;

import java.lang.reflect.InvocationTargetException;

public class GeneralTestCase {
    @Test
    public void addInterviewPreAllTypeTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String url = "com.example.userinfo";
        String packageName = "userinfo";
        String fileName = "TeacherCertificationProgressList";
        String constantsFileName = "UserInfoConstants";
        Class request = null;
        Class response = QueryUserInfoListResponse.class;
        String type = "pi";
        bd.generateTest(packageName,fileName,constantsFileName,request,response,type,url,"PBFF 老师认证进度");
    }
}
