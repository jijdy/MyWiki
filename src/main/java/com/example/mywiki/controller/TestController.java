package com.example.mywiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //用于返回字符串
//@Controller    //用于返回页面,前后端分离中一般用不到
public class TestController {

    //测试xml配置文件在框架中的使用和值传递。
    @Value("${test.value:defaultValue}") //：之后为默认值
    private String testValue;

    @RequestMapping("/test")
    public String test() {
        return "Hello World!" + testValue;
    }

    @PostMapping("/test/post")
    public String PostTest(String name) {
        return "Hello World!" + name;
    }
}
