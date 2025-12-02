package com.yf.service;

import com.yf.model.User;

public interface UserService {
    int register(User user);
    User login(String u_name, String u_pwd);
    /**
     * 根据账号查询用户（用于注册时验证账号是否已存在）
     * @param u_name 用户名
     * @return 找到的用户对象，如果未找到则返回 null
     */
    User getUserByName(String u_name);
}