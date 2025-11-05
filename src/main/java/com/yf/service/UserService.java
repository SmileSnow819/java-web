package com.yf.service;

import com.yf.model.User;

public interface UserService {
    int register(User user);
    User login(String u_name, String u_pwd);
}