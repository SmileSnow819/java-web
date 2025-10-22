package com.yf.dao.impl;

import com.yf.dao.UserDao;
import com.yf.pojo.User;
import com.yf.until.DBUntil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDao 接口实现类，继承 DBUtil (注意修正包名和类名)
 */
public class UserDaoImpl extends DBUntil implements UserDao {

    // 1. 登录
    @Override
    public User login(String uName, String uPwd) {
        String sql = "SELECT u_id, u_name, u_pwd FROM user WHERE u_name = ? AND u_pwd = ?";
        ResultSet rs = executeQuery(sql, uName, uPwd);
        User user = null;
        Statement stmt = null;
        try {
            if (rs != null && rs.next()) {
                user = new User(rs.getInt("u_id"), rs.getString("u_name"), rs.getString("u_pwd"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    stmt = rs.getStatement();
                }
            } catch (SQLException e) {
                // 忽略或打印异常
            }
            closeAll(rs, stmt, null);
        }
        return user;
    }

    // 2. 注册
    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO user (u_name, u_pwd) VALUES (?, ?)";
        int rows = executeUpdate(sql, user.getuName(), user.getuPwd());
        return rows > 0;
    }

    // 3. 展示所有用户
    @Override
    public List<User> getAllUser() {
        String sql = "SELECT u_id, u_name, u_pwd FROM user";
        ResultSet rs = executeQuery(sql);
        List<User> userList = new ArrayList<>();
        Statement stmt = null;
        try {
            while (rs != null && rs.next()) {
                User user = new User(rs.getInt("u_id"), rs.getString("u_name"), rs.getString("u_pwd"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    stmt = rs.getStatement();
                }
            } catch (SQLException e) {
                // 忽略或打印异常
            }
            closeAll(rs, stmt, null);
        }
        return userList;
    }

    // 4. 注销用户 (delUser)
    @Override
    public boolean delUser(int uId) {
        String sql = "DELETE FROM user WHERE u_id = ?";
        // 调用继承自 DBUtil 的增删改方法
        int rows = executeUpdate(sql, uId);
        return rows > 0;
    }

    // 5. 根据 ID 查询用户 (辅助修改功能)
    @Override
    public User getUserById(int uId) {
        String sql = "SELECT u_id, u_name, u_pwd FROM user WHERE u_id = ?";
        ResultSet rs = executeQuery(sql, uId);
        User user = null;
        Statement stmt = null;
        try {
            if (rs != null && rs.next()) {
                user = new User(rs.getInt("u_id"), rs.getString("u_name"), rs.getString("u_pwd"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    stmt = rs.getStatement();
                }
            } catch (SQLException e) {
                // 忽略或打印异常
            }
            closeAll(rs, stmt, null);
        }
        return user;
    }

    // 6. 编辑用户 (updateUser)
    @Override
    public boolean updateUser(int uId, String newName, String newPwd) {
        String sql = "UPDATE user SET u_name = ?, u_pwd = ? WHERE u_id = ?";
        // 调用继承自 DBUtil 的增删改方法
        int rows = executeUpdate(sql, newName, newPwd, uId);
        return rows > 0;
    }
}
