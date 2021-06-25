package com.example.mywiki.controller;

import com.example.mywiki.req.UserQueryReq;
import com.example.mywiki.req.UserSaveReq;
import com.example.mywiki.resp.CommonResp;
import com.example.mywiki.resp.PageResp;
import com.example.mywiki.resp.UserQueryResp;
import com.example.mywiki.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController //用于返回字符串
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    //sava中导入参数加上@RequestBody注解表示其可以接收来着前端的json数据格式的数据
    @PostMapping("/save")
    public CommonResp sava(@Valid @RequestBody UserSaveReq req) {
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    //删除分类的逻辑
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable long id) {
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }
}