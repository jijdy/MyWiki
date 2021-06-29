package com.example.mywiki.util;

import java.io.Serializable;

/*
* 用于获取用户的id地址
* */
public class RequestUtil implements Serializable {

    private static final ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestUtil.remoteAddr.set(remoteAddr);
    }
}
