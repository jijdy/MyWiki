package com.example.mywiki.service;

import com.example.mywiki.domain.User;
import com.example.mywiki.domain.UserExample;
import com.example.mywiki.mapper.UserMapper;
import com.example.mywiki.req.UserQueryReq;
import com.example.mywiki.req.UserSaveReq;
import com.example.mywiki.resp.PageResp;
import com.example.mywiki.resp.UserQueryResp;
import com.example.mywiki.util.CopyUtil;
import com.example.mywiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger Log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;


    public PageResp<UserQueryResp> list(UserQueryReq req) {

        UserExample userExample = new UserExample();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            UserExample.Criteria criteria = userExample.createCriteria();
            criteria.andNameLike("%" + req.getLoginName() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        Log.info("总行数：{}", pageInfo.getTotal());
        Log.info("总页数：{}", pageInfo.getPages());


        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            user.setId(snowFlake.nextId());
            userMapper.insert(user);
        } else {
            //否则更新数据即可
            userMapper.updateByPrimaryKey(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }
}