package com.yf.test;

import com.yf.dao.impl.UserDao; // 假设接口在 .dao 包
import com.yf.dao.impl.UserDaoImpl;
import com.yf.pojo.User;
import java.util.Scanner;

/**
 * 用户功能综合测试类 (登录交互，CRUD 自动验证)
 */
public class UserDaoTest {

    // 实例化 DAO 实现类
    private static final UserDao userDao = new UserDaoImpl();

    // 用于 CRUD 验证的硬编码数据
    private static final int TEST_USER_ID = 999; // 预设一个用于测试的ID
    private static final String TEST_USER_NAME = "test_crud_user";
    private static final String TEST_USER_PWD = "test_pwd_123";

    public static void main(String[] args) {

        // =================================================================
        // 1. 登录功能测试 (接受用户键盘输入)
        // =================================================================
        Scanner sc = new Scanner(System.in);

        System.out.println("****************** 欢迎来到登录系统 ******************");

        System.out.println("请输入您的账号:");
        String uName = sc.next();

        System.out.println("请输入您的密码:");
        String uPwd = sc.next();

        // 关闭 Scanner (交互部分结束)
        sc.close();

        // 执行登录验证
        User loggedUser = userDao.login(uName, uPwd);

        if (loggedUser != null) {
            System.out.println("\n[登录成功]!!!!!! 欢迎用户：" + loggedUser.getuName());
        } else {
            System.out.println("\n[登录失败]!!!!!! 请检查您的账号或密码是否正确。");
        }

        // =================================================================
        // 2. 自动验证 CRUD 方法 (不接受键盘输入)
        // =================================================================

        System.out.println("\n================================================");

        System.out.println("\n--- 准备数据: 模拟注册用户 (ID=" + TEST_USER_ID + ") ---");
        userDao.addUser(new User(TEST_USER_NAME, TEST_USER_PWD));
        // 为了后续操作能拿到正确 ID，这里需要手动从数据库获取一次ID，
        // 但为简化，我们假设 TEST_USER_ID 可以在后续步骤中被定位或使用。

        // 步骤 2.1: 根据ID查询 (getUserById)
        System.out.println("\n--- 验证: 根据用户名查询 (Login作为简易GetUser) ---");
        // 我们用 login 验证一下刚注册的用户是否存在（替代getUserById的简单验证）
        User userById = userDao.login(TEST_USER_NAME, TEST_USER_PWD);
        if (userById != null) {
            System.out.println("[查询成功] 用户信息: " + userById);
            // 拿到真实的 ID 用于后续操作
            int realId = userById.getuId();

            // 步骤 2.2: 修改用户信息 (updateUser)
            System.out.println("\n--- 验证: 修改用户信息 (updateUser) ---");
            testUpdateUser(realId, "updated_" + TEST_USER_NAME, "new_pwd_789");

            // 步骤 2.3: 再次查询，验证修改 (getUserById)
            System.out.println("\n--- 验证: 验证修改结果 ---");
            testGetUserById(realId);

            // 步骤 2.4: 注销/删除用户 (delUser)
            System.out.println("\n--- 验证: 注销/删除用户 (delUser) ---");
            testDelUser(realId);

            // 步骤 2.5: 验证删除
            System.out.println("\n--- 验证: 验证删除结果 (默认失败) ---");
            testGetUserById(realId);
        } else {
            System.err.println("错误: 模拟注册失败，后续 CRUD 无法测试。");
        }

        System.out.println("--- 自动验证 CRUD 功能结束 ---");
        System.out.println("================================================");
    }


    private static void testGetUserById(int id) {
        User user = userDao.getUserById(id);
        if (user != null) {
            System.out.println("[查询成功] ID=" + id + " 的用户: " + user);
        } else {
            System.out.println("[查询成功] ID=" + id + " 的用户不存在。");
        }
    }

    private static void testUpdateUser(int id, String newName, String newPwd) {
        User userToUpdate = new User(id, newName, newPwd);
        int rows = userDao.updateUser(userToUpdate);
        if (rows > 0) {
            System.out.println("[修改成功] ID=" + id + " 的用户信息已更新。");
        } else {
            System.out.println("[修改失败] ID=" + id + " 的用户更新失败或不存在。");
        }
    }

    private static void testDelUser(int id) {
        int rows = userDao.delUser(id);
        if (rows > 0) {
            System.out.println("[删除成功] ID=" + id + " 的用户已注销。");
        } else {
            System.out.println("[删除失败] ID=" + id + " 的用户删除失败或不存在。");
        }
    }
}