package com.example.response;

import com.example.base.BaseResponse;
import com.example.entity.UserInfoDomain;

import java.util.List;

public class QueryUserInfoListResponse {

    private BaseResponse baseResponse = new BaseResponse();
    private List<UserInfoDomain> userInfoList;
    private Integer totalCount;

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
    }

    public List<UserInfoDomain> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfoDomain> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
