package com.yf.dao.impl;

import com.yf.dao.UserDao;
import com.yf.pojo.User;
import com.yf.until.BaseDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserDao 接口的实现类，继承 BaseDao 工具类。
 * 完成所有数据库操作。
 */
public class UserDaoImpl extends BaseDao implements UserDao {

    private static final Logger LOGGER = Logger.getLogger(UserDaoImpl.class.getName());

    // 辅助方法：从 ResultSet 中提取 User 对象
    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setuId(rs.getInt("u_id"));
        user.setuName(rs.getString("u_name"));
        user.setuPwd(rs.getString("u_pwd"));
        return user;
    }

    @Override
    public User login(String uName, String uPwd) {
        String sql = "SELECT u_id, u_name, u_pwd FROM user WHERE u_name = ? AND u_pwd = ?";

        // 登录/查询方法需要手动管理资源
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null; // 在这里定义ps，便于在finally中关闭

        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);
            ps.setObject(1, uName);
            ps.setObject(2, uPwd);
            rs = ps.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "用户登录查询失败", e);
        } finally {
            // 使用 BaseDao 的 closeAll 关闭资源
            closeAll(rs, ps, conn);
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO user (u_name, u_pwd) VALUES (?, ?)";

        try {
            // 调用父类 (BaseDao) 的增删改方法
            Object[] params = {user.getuName(), user.getuPwd()};
            int rows = executeUpdate(sql, params);
            return rows > 0;
        } catch (RuntimeException e) {
            // 捕获 BaseDao 中抛出的异常，通常是 SQLIntegrityConstraintViolationException (重复键)
            // 这里返回 false 表示注册失败
            LOGGER.log(Level.INFO, "注册失败，可能是用户名已存在: " + user.getuName());
            return false;
        }
    }

    @Override
    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT u_id, u_name, u_pwd FROM user ORDER BY u_id";

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 同样，手动处理 BaseDao 的 executeQuery 返回的资源
            conn = getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                userList.add(extractUser(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "查询所有用户失败", e);
        } finally {
            closeAll(rs, ps, conn);
        }
        return userList;
    }

    @Override
    public boolean delUser(int uId) {
        String sql = "DELETE FROM user WHERE u_id = ?";
        Object[] params = {uId};
        // 调用父类 (BaseDao) 的增删改方法
        int rows = executeUpdate(sql, params);
        return rows > 0;
    }

    @Override
    public boolean updateUser(int uId, String newName, String newPwd) {
        String sql = "UPDATE user SET u_name = ?, u_pwd = ? WHERE u_id = ?";
        Object[] params = {newName, newPwd, uId};
        // 调用父类 (BaseDao) 的增删改方法
        int rows = executeUpdate(sql, params);
        return rows > 0;
    }

    @Override
    public User getUserById(int uId) {
        String sql = "SELECT u_id, u_name, u_pwd FROM user WHERE u_id = ?";

        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 同样，手动处理 BaseDao 的 executeQuery 返回的资源
            conn = getConn();
            ps = conn.prepareStatement(sql);
            ps.setObject(1, uId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "根据ID查询用户失败", e);
        } finally {
            closeAll(rs, ps, conn);
        }
        return null;
    }
}