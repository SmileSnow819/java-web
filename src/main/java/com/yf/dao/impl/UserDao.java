package com.yf.dao.impl;

import com.yf.pojo.User;
public interface UserDao {

    /**
     * 登录功能：根据用户名和密码查询用户
     * @param uName 用户名
     * @param uPwd 密码
     * @return 查到的用户对象，如果未找到则返回 null
     */
    User login(String uName, String uPwd);

    /**
     * 注册功能：向数据库添加新用户
     * @param user 待添加的用户对象
     * @return 影响的行数 (1 表示成功，0 表示失败)
     */
    int addUser(User user);

    /**
     * 注销/删除功能：根据用户ID删除用户
     * @param uId 用户ID
     * @return 影响的行数 (1 表示成功，0 表示失败)
     */
    int delUser(int uId);

    /**
     * 修改功能：根据用户ID更新用户名和密码
     * @param user 包含新信息和ID的用户对象
     * @return 影响的行数 (1 表示成功，0 表示失败)
     */
    int updateUser(User user);

    /**
     * 根据ID查询用户
     * @param uId 用户ID
     * @return 查到的用户对象，如果未找到则返回 null
     */
    User getUserById(int uId);
}
