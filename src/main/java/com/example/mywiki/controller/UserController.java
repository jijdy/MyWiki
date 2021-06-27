package com.example.mywiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.mywiki.req.UserLoginReq;
import com.example.mywiki.req.UserQueryReq;
import com.example.mywiki.req.UserResetPasswordReq;
import com.example.mywiki.req.UserSaveReq;
import com.example.mywiki.resp.CommonResp;
import com.example.mywiki.resp.PageResp;
import com.example.mywiki.resp.UserLoginResp;
import com.example.mywiki.resp.UserQueryResp;
import com.example.mywiki.service.UserService;
import com.example.mywiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController //用于返回字符串
@RequestMapping("/user")
public class UserController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    //sava中导入参数加上@RequestBody注解表示其可以接收来着前端的json数据格式的数据
    //对密码进行MD5加密
    @PostMapping("/save")
    public CommonResp sava(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
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

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);

        Long token = snowFlake.nextId();
        LOGGER.info("生成单点登录token：{}，并放入redis中", token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS );

        LOGGER.info("redis中的数据：{}", redisTemplate.opsForValue().get(token));

        resp.setContent(userLoginResp);
        return resp;
    }
}