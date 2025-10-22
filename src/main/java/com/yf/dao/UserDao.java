package com.yf.dao;

import com.yf.pojo.User;
import java.util.List;

/**
 * 用户数据访问对象接口 (DAO)
 */
public interface UserDao {

    User login(String uName, String uPwd);

    boolean addUser(User user);

    List<User> getAllUser();

    boolean delUser(int uId);

    User getUserById(int uId); // 辅助方法：按ID查询用户

    boolean updateUser(int uId, String newName, String newPwd);
}