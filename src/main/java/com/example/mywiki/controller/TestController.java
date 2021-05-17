package com.example.mywiki.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //用于返回字符串
//@Controller    //用于返回页面,前后端分离中一般用不到
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "Hello World!";
    }
}
