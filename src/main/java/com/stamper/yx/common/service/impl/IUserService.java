package com.stamper.yx.common.service.impl;

import com.stamper.yx.common.entity.User;
import com.stamper.yx.common.mapper.sqlite.UserMapper;
import com.stamper.yx.common.service.UserService;
import com.stamper.yx.common.sys.AppConstant;
import com.stamper.yx.common.sys.error.PrintException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class IUserService implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(String name) {
        if (StringUtils.isNotBlank(name)) {
            User user = userMapper.getUserByName(name);
            if (user != null) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User getAdmin() {
        User userByName = userMapper.getUserByName(AppConstant.USER);
        if(userByName!=null){
            return userByName;
        }
        return null;
    }

    @Override
    @Transactional
    public void add(User user) {
        if (user != null) {
            Integer add = userMapper.add(user);
            if (add != 1) {
                log.error("创建用户失败");
                throw new PrintException("创建用户失败");
            }
        }
    }

    @Override
    @Transactional
    public void del(Integer userId) {
        if (userId != null) {
            User userById = userMapper.getUserById(userId);
            if(userById==null){
                throw new PrintException("当前用户不存在");
            }
            try {
                userMapper.del(userById);
            }catch (Exception e){
                log.error("删除用户异常e-->{{}}",e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public User getUser(Integer userId) {
        if (userId != null && userId != 0) {
            User userById = userMapper.getUserById(userId);
            if(userById!=null){
                return userById;
            }
        }
        return null;
    }

    @Transactional
    @Override
    public void update(User user) {
        if(user!=null&&user.getId()!=null){
            Integer update = userMapper.update(user);
            if(update!=1){
                log.error("更新用户数据失败");
                throw new PrintException("更新用户数据失败");
            }
        }
    }

    @Transactional
    @Override
    public void save(User user) {
        if(user!=null){
            User userByName = userMapper.getUserByName(AppConstant.USER);
            if(userByName==null){
                userMapper.add(user);
            }else{
                user.setId(userByName.getId());
                String strDateFormat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strDateFormat);
                String format = simpleDateFormat.format(new Date());
                user.setUpdateDate(format);
                userMapper.update(user);
            }
        }
    }
}
