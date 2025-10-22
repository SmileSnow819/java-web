package com.yf.test;

import com.yf.dao.UserDao;
import com.yf.dao.impl.UserDaoImpl;
import com.yf.pojo.User;

import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 用户管理系统控制台界面
 */
public class TestSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDao userDao = new UserDaoImpl();
    private static User currentUser = null; // 当前登录用户

    public static void main(String[] args) {
        System.out.println("****欢迎来到用户管理系统****");
        mainMenu();
    }

    /**
     * 主菜单：登录/注册/退出
     */
    private static void mainMenu() {
        while (true) {
            System.out.println("请选择您的操作:");
            System.out.println("1.登录\t2.注册\t3.退出系统");

            String choice = scanner.next();
            switch (choice) {
                case "1":
                    loginMenu();
                    break;
                case "2":
                    registerMenu();
                    break;
                case "3":
                    System.out.println("系统已退出，感谢使用！");
                    scanner.close();
                    return;
                default:
                    System.out.println("输入有误，请重新选择！");
            }

            // 如果登录成功，进入用户操作菜单
            if (currentUser != null) {
                userMenu();
            }
        }
    }

    /**
     * 登录功能
     */
    private static void loginMenu() {
        System.out.println("请输入您要登录的账号:");
        String uName = scanner.next();
        System.out.println("请输入您要登录的密码:");
        String uPwd = scanner.next();

        currentUser = userDao.login(uName, uPwd);

        if (currentUser != null) {
            System.out.println("登录成功!");
        } else {
            System.out.println("登录失败！账号或密码错误。");
        }
    }

    /**
     * 注册功能
     */
    private static void registerMenu() {
        System.out.println("请输入您要注册的账号:");
        String uName = scanner.next();
        System.out.println("请输入您要注册的密码:");
        String uPwd = scanner.next();

        User newUser = new User();
        newUser.setuName(uName);
        newUser.setuPwd(uPwd);

        if (userDao.addUser(newUser)) {
            System.out.println("注册成功！！！");
        } else {
            System.out.println("注册失败！可能是用户名已存在，请检查。");
        }
    }

    /**
     * 用户操作菜单：增删改查
     */
    private static void userMenu() {
        while (currentUser != null) {
            System.out.println("请选择您的操作:");
            System.out.println("1.展示所有用户\t2.注销用户\t3.修改用户\t4.退出登录");

            String choice = scanner.next();
            switch (choice) {
                case "1":
                    displayAllUsers();
                    break;
                case "2":
                    deleteUser();
                    break;
                case "3":
                    updateUser();
                    break;
                case "4":
                    System.out.println("您已退出登录。");
                    currentUser = null; // 退出登录
                    return; // 返回主菜单
                default:
                    System.out.println("输入有误，请重新选择！");
            }
        }
    }

    /**
     * 1. 展示所有用户
     */
    private static void displayAllUsers() {
        List<User> users = userDao.getAllUser();
        if (users.isEmpty()) {
            System.out.println("系统中暂无用户记录。");
        } else {
            for (User user : users) {
                System.out.println(user);
            }
        }
    }

    /**
     * 2. 注销用户
     */
    private static void deleteUser() {
        System.out.println("请输入您要注销的用户编号:");
        int uId = readIntInput();

        if (uId == -1) return; // 输入错误，返回
        if (uId == currentUser.getuId()) {
            System.out.println("您不能注销当前登录账户！");
            return;
        }

        if (userDao.delUser(uId)) {
            System.out.println("注销成功,继续您的操作！！！");
        } else {
            System.out.println("注销失败，用户编号不存在。");
        }
    }

    /**
     * 3. 修改用户
     */
    private static void updateUser() {
        System.out.println("请输入您要修改的用户的编号:");
        int uId = readIntInput();

        if (uId == -1) return; // 输入错误，返回

        User existingUser = userDao.getUserById(uId);

        if (existingUser == null) {
            System.out.println("该账户不存在，请重新选择！！！");
            return;
        }

        System.out.println(existingUser);
        System.out.println("是否确认编辑(y确认任意键取消)?");
        String confirm = scanner.next();

        if (confirm.equalsIgnoreCase("y")) {
            System.out.println("请输入您要编辑的账号:");
            String newName = scanner.next();
            System.out.println("请输入您要编辑的密码:");
            String newPwd = scanner.next();

            if (userDao.updateUser(uId, newName, newPwd)) {
                System.out.println("修改成功!!!");
                // 如果修改的是当前用户，更新内存中的 currentUser
                if (uId == currentUser.getuId()) {
                    currentUser.setuName(newName);
                    currentUser.setuPwd(newPwd);
                }
            } else {
                System.out.println("修改失败！可能是用户名已存在。");
            }
        } else {
            System.out.println("操作已取消。");
        }
    }

    /**
     * 辅助方法：读取整数输入，处理 InputMismatchException
     */
    private static int readIntInput() {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("输入格式错误，请输入数字。");
            scanner.next(); // 消耗错误输入
            return -1;
        }
    }
}