package com.example.mywiki.exception;

/*
* 自定义异常类的枚举对象
* */
public enum BusinessExceptionCode {
    USER_LOGIN_NAME_EXIST("登录名已存在"),
    USER_LOGIN_PASSWORD_IS_WRYING("用户名或密码或错误"),
    USER_VOTE_REPEAT("您已经点赞了")
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
