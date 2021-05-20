package com.example.mywiki.controller;

import com.example.mywiki.domain.Demo;
import com.example.mywiki.service.DemoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController //用于返回字符串
//@Controller    //用于返回页面,前后端分离中一般用不到
public class DemoController {

    //测试xml配置文件在框架中的使用和值传递。
    @Value("${demo.value:defaultValue}") //：之后为默认值
    private String demoValue;

    @Resource
    private DemoService demoService;

    @RequestMapping("/demo")
    public String demo() {
        return "Hello World!" + demoValue;
    }

    @PostMapping("/demo/post")
    public String PostDemo(String name) {
        return "Hello World!" + name;
    }

    @RequestMapping  ("demo/list")
    public List<Demo> list() {
        return demoService.list();
    }
}
