package com.yf.dao;

import com.yf.model.User;

public interface UserDao {
    /**
     * 注册功能：添加新用户
     * @param user 用户对象
     * @return 影响行数
     */
    int addUser(User user);

    /**
     * 登录功能：根据用户名和密码查询用户
     * @param u_name 用户名
     * @param u_pwd 密码
     * @return 找到的用户对象，如果未找到则返回 null
     */
    User getUserByCredentials(String u_name, String u_pwd);
}