package com.example.mywiki.controller;

import com.example.mywiki.req.CategoryQueryReq;
import com.example.mywiki.req.CategorySaveReq;
import com.example.mywiki.resp.CategoryQueryResp;
import com.example.mywiki.resp.CommonResp;
import com.example.mywiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController //用于返回字符串
//@Controller    //用于返回页面,前后端分离中一般用不到
@RequestMapping("/category")
public class CategoryController {

    //测试xml配置文件在框架中的使用和值传递。
    @Value("${category.value:defaultValue}") //：之后为默认值
    private String categoryValue;

    @Resource
    private CategoryService categoryService;

    @RequestMapping("/category")
    public String category() {
        return "Hello World!" + categoryValue;
    }

    @PostMapping("/post")
    public String PostCategory(String name) {
        return "Hello World!" + name;
    }

    @RequestMapping  ("/all")
    public CommonResp list(@Valid CategoryQueryReq req) {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all(req);
        resp.setContent(list);
        return resp;
    }

     //sava中导入参数加上@RequestBody注解表示其可以接收来着前端的json数据格式的数据
    @PostMapping("/save")
    public CommonResp sava(@Valid @RequestBody CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }
     //删除分类的逻辑
    @DeleteMapping ("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
