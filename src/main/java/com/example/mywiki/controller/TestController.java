package com.example.mywiki.controller;

import com.example.mywiki.domain.Test;
import com.example.mywiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController //用于返回字符串
//@Controller    //用于返回页面,前后端分离中一般用不到
public class TestController {

    //测试xml配置文件在框架中的使用和值传递。
    @Value("${test.value:defaultValue}") //：之后为默认值
    private String testValue;

    @Resource
    private TestService testService;

    @RequestMapping("/test")
    public String test() {
        return "Hello World!" + testValue;
    }

    @PostMapping("/test/post")
    public String PostTest(String name) {
        return "Hello World!" + name;
    }

    @RequestMapping  ("test/list")
    public List<Test> list() {
        return testService.list();
    }
}
