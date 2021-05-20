package com.example.mywiki.controller;

import com.example.mywiki.domain.Ebook;
import com.example.mywiki.resp.CommonResp;
import com.example.mywiki.service.EbookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController //用于返回字符串
//@Controller    //用于返回页面,前后端分离中一般用不到
@RequestMapping("/ebook")
public class EbookController {

    //测试xml配置文件在框架中的使用和值传递。
    @Value("${ebook.value:defaultValue}") //：之后为默认值
    private String ebookValue;

    @Resource
    private EbookService ebookService;

    @RequestMapping("/ebook")
    public String ebook() {
        return "Hello World!" + ebookValue;
    }

    @PostMapping("/post")
    public String PostEbook(String name) {
        return "Hello World!" + name;
    }

    @RequestMapping  ("/list")
    public CommonResp list(String name) {
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        List<Ebook> list = ebookService.list(name); //调用服务层，通过服务层调用Mapper层数据库数据
        resp.setContent(list);
        return resp;
    }
}
