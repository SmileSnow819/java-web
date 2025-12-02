package com.yf.service.impl;

import com.yf.dao.UserDao;
import com.yf.dao.impl.UserDaoImpl;
import com.yf.model.User;
import com.yf.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public int register(User user) {
        // [业务逻辑]：可以添加密码复杂度校验、用户名是否已存在校验等
        if (user.getU_pwd().length() < 6) {
            System.out.println("业务校验失败：密码长度必须大于6位。");
            return 0;
        }
        return userDao.addUser(user);
    }

    @Override
    public User login(String u_name, String u_pwd) {
        // [业务逻辑]：直接调用 DAO 层查询
        return userDao.getUserByCredentials(u_name, u_pwd);
    }

    @Override
    public User getUserByName(String u_name) {
        // [业务逻辑]：直接调用 DAO 层查询
        return userDao.getUserByName(u_name);
    }
}