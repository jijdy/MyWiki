package com.example.mywiki.controller;

import com.example.mywiki.req.DocSaveReq;
import com.example.mywiki.resp.CommonResp;
import com.example.mywiki.resp.DocQueryResp;
import com.example.mywiki.service.DocService;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController //用于返回字符串
//@Controller    //用于返回页面,前后端分离中一般用不到
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @PostMapping("/post")
    public String PostDoc(String name) {
        return "Hello World!" + name;
    }

    @RequestMapping  ("/all/{ebookId}")
    public CommonResp list(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all(ebookId);
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
     //拿出文档的数据
    @GetMapping  ("/find-content/{id}")
    public CommonResp get(@PathVariable long id) {
        CommonResp<String> resp = new CommonResp<>();
        if (ObjectUtils.isEmpty(id)) {
            resp.setSuccess(false);
        }else {
            resp.setContent(docService.getContent(id));
        }
        return resp;
    }
}
