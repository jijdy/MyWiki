package com.example.mywiki.exception;

/*
* 自定义异常类的枚举对象
* */
public enum BusinessExceptionCode {
    USER_LOGIN_NAME_EXIST("登录名已存在")
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
