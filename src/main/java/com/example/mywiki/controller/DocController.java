package com.example.mywiki.controller;

import com.example.mywiki.req.DocQueryReq;
import com.example.mywiki.req.DocSaveReq;
import com.example.mywiki.resp.CommonResp;
import com.example.mywiki.resp.DocQueryResp;
import com.example.mywiki.service.DocService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController //用于返回字符串
//@Controller    //用于返回页面,前后端分离中一般用不到
@RequestMapping("/doc")
public class DocController {

    //测试xml配置文件在框架中的使用和值传递。
    @Value("${doc.value:defaultValue}") //：之后为默认值
    private String docValue;

    @Resource
    private DocService docService;

//    @RequestMapping("/doc")
//    public String doc() {
//        return "Hello World!" + docValue;
//    }

    @PostMapping("/post")
    public String PostDoc(String name) {
        return "Hello World!" + name;
    }

    @RequestMapping  ("/all")
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(req);
        resp.setContent(list);
        return resp;
    }

     //sava中导入参数加上@RequestBody注解表示其可以接收来着前端的json数据格式的数据
    @PostMapping("/save")
    public CommonResp sava(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }
     //删除分类的逻辑
    @DeleteMapping ("/delete/{ids}")
    public CommonResp delete(@PathVariable String ids) {
        CommonResp resp = new CommonResp<>();
        List<String> list = Arrays.asList(ids.split(","));
        docService.delete(list);
        return resp;
    }
}
