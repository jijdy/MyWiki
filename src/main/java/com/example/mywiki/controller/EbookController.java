package com.example.mywiki.controller;

import com.example.mywiki.req.EbookQueryReq;
import com.example.mywiki.req.EbookSaveReq;
import com.example.mywiki.resp.CommonResp;
import com.example.mywiki.resp.EbookQueryResp;
import com.example.mywiki.resp.PageResp;
import com.example.mywiki.service.EbookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    public CommonResp list(@Valid EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req); //调用服务层，通过服务层调用Mapper层数据库数据
        resp.setContent(list);
        return resp;
    }

     //sava中导入参数加上@RequestBody注解表示其可以接收来着前端的json数据格式的数据
    @PostMapping("/save")
    public CommonResp sava(@Valid @RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
     //删除电子书的逻辑
    @DeleteMapping ("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
