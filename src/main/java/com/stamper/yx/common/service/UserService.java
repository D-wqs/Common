package com.stamper.yx.common.service;

import com.stamper.yx.common.entity.User;

public interface UserService {
    User getUser(String name);
    User getAdmin();
    void add(User user);
    void del(Integer userId);
    User getUser(Integer userId);
    void update(User user);
    void save(User user);
}
