package com.happy.service.impl;

import com.happy.mapper.UserMapper;
import com.happy.model.User;
import com.happy.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService implements IUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public User findById(String id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByOpenid(String openid) {
        return userMapper.findByOpenid(openid);
    }

    @Override
    public int save(User user) {
        return userMapper.save(user);
    }

    @Override
    public int update(User user) {
        return userMapper.update(user);
    }
}
