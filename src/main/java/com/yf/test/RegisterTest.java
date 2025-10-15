package com.yf.test;

import com.yf.dao.impl.UserDao;
import com.yf.dao.impl.UserDaoImpl;
import com.yf.pojo.User;
import java.util.Scanner;

/**
 * 用户注册测试类
 */
public class RegisterTest {

    public static void main(String[] args) {
        // 创建 Scanner 对象用于获取键盘输入
        Scanner sc = new Scanner(System.in);

        System.out.println("****************** 欢迎使用注册系统 ******************");

        // 1. 获取用户想要注册的账号
        System.out.println("请输入您想要注册的账号:");
        String newName = sc.next();

        // 2. 获取用户想要注册的密码
        System.out.println("请输入您的密码:");
        String newPwd = sc.next();

        // 3. 将输入数据封装成 User 对象
        User newUser = new User(newName, newPwd);

        // 4. 创建功能实现对象 (UserDaoImpl)
        UserDao userDao = new UserDaoImpl();

        // 5. 调用注册功能
        System.out.println("正在尝试注册用户: " + newName + "...");
        int rows = userDao.addUser(newUser);

        // 6. 判断注册是否成功
        if (rows > 0) {
            System.out.println("\n恭喜您，注册成功!!!!!!");
            System.out.println("用户 [" + newName + "] 已添加到数据库。");
        } else {
            System.out.println("\n注册失败!!!!!!");
            // 失败原因通常是数据库连接问题或 SQL 异常，需要检查控制台的错误输出
            System.out.println("未能添加用户到数据库，请检查日志。");
        }

        // 关闭 Scanner
        sc.close();
    }
}