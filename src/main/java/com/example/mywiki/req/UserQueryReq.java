package com.example.mywiki.req;

public class UserQueryReq extends PageReq {

    private String loginName;

    @Override
    public String toString() {
        return "UserQueryReq{" +
                "loginName='" + loginName + '\'' +
                '}';
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}