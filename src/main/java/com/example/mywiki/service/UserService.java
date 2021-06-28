package com.example.mywiki.service;

import com.example.mywiki.domain.User;
import com.example.mywiki.domain.UserExample;
import com.example.mywiki.exception.BusinessException;
import com.example.mywiki.exception.BusinessExceptionCode;
import com.example.mywiki.mapper.UserMapper;
import com.example.mywiki.req.UserLoginReq;
import com.example.mywiki.req.UserQueryReq;
import com.example.mywiki.req.UserResetPasswordReq;
import com.example.mywiki.req.UserSaveReq;
import com.example.mywiki.resp.PageResp;
import com.example.mywiki.resp.UserLoginResp;
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


    public PageResp<UserQueryResp> list( UserQueryReq req ) {

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

    public void save( UserSaveReq req ) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) { //新增用户
            User user1 = selectByLoginName(req.getLoginName());
            if (ObjectUtils.isEmpty(user1)) {
                //新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {

            user.setLoginName(null);
            user.setPassword(null);

            //有选择性的更新数据，不对用户名进行更新
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    public void delete( Long id ) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName( String loginName ) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList.isEmpty()) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    public void resetPassword( UserResetPasswordReq req ) {
        User user = CopyUtil.copy(req, User.class);
        //有选择性的更新数据，不对用户名进行更新
        userMapper.updateByPrimaryKeySelective(user);
    }

    /*
     * 登录校检操作
     * */
    public UserLoginResp login( UserLoginReq req ) {
        User userDb = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(userDb)) {
            //用户名不存在
            Log.info("用户名不存在：{}", req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_PASSWORD_IS_WRYING);
        } else {
            if (userDb.getPassword().equals(req.getPassword())) {
                //登录成功
                return CopyUtil.copy(userDb, UserLoginResp.class);
            } else {
                //登录失败
                Log.info("密码错误：{}", req.getPassword());
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_PASSWORD_IS_WRYING);

            }
        }
    }
}