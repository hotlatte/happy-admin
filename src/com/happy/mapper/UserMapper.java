package com.happy.mapper;

import com.happy.model.User;

import java.util.List;

public interface UserMapper {
    List<User> findAll();

    User findById(String id);

    User findByOpenid(String openid);

    int save(User user);

    int update(User user);

}
