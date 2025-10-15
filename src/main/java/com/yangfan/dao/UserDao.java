package com.yangfan.dao;

import com.yangfan.pojo.User;
import com.yangfan.pojo.User;
import java.util.List;

public interface UserDao {
    /**
     * 查询数据库中所有的用户信息
     * @return 包含所有用户对象的 List 集合
     */
    List<User> getAllUsers();
}
