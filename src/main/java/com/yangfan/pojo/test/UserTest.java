package com.yangfan.pojo.test;

import com.yangfan.dao.UserDao;
import com.yangfan.dao.impl.UserDaoImpl;
import com.yangfan.pojo.User;

import java.util.List;

public class UserTest {
    public static void main(String[] args) {
        // 1. 创建 DAO 接口的实现类对象
        UserDao userDao = new UserDaoImpl();

        // 2. 调用功能方法查询全部用户
        List<User> userList = userDao.getAllUsers();

        // 3. 遍历并打印结果到控制台
        if (userList != null && !userList.isEmpty()) {
            System.out.println("✅ 成功查询到用户信息：");
            for (User user : userList) {
                System.out.println(user); // 这里调用了 User 类的 toString() 方法
            }
        } else {
            System.out.println("⚠️ 查询结果为空，数据库中没有用户数据。");
        }
    }
}
