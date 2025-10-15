package com.yf.dao.impl;

import com.yf.dao.impl.UserDao;
import com.yf.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    @Override
    public User login(String uName, String uPwd) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        String sql = "SELECT u_id, u_name, u_pwd FROM user WHERE u_name = ? AND u_pwd = ?";

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, uName);
            ps.setString(2, uPwd);
            rs = ps.executeQuery();

            if (rs.next()) {
                // 查询成功，封装 User 对象
                user = new User(
                        rs.getInt("u_id"),
                        rs.getString("u_name"),
                        rs.getString("u_pwd")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn); // 关闭资源
        }
        return user;
    }

    @Override
    public int addUser(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        int rows = 0;
        String sql = "INSERT INTO user (u_name, u_pwd) VALUES (?, ?)";

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getuName());
            ps.setString(2, user.getuPwd());
            rows = ps.executeUpdate(); // 执行更新操作
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return rows;
    }

    @Override
    public int delUser(int uId) {
        Connection conn = null;
        PreparedStatement ps = null;
        int rows = 0;
        String sql = "DELETE FROM user WHERE u_id = ?";

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uId);
            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return rows;
    }

    @Override
    public int updateUser(User user) {
        Connection conn = null;
        PreparedStatement ps = null;
        int rows = 0;
        String sql = "UPDATE user SET u_name = ?, u_pwd = ? WHERE u_id = ?";

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getuName());
            ps.setString(2, user.getuPwd());
            ps.setInt(3, user.getuId());
            rows = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return rows;
    }

    @Override
    public User getUserById(int uId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        String sql = "SELECT u_id, u_name, u_pwd FROM user WHERE u_id = ?";

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, uId);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("u_id"),
                        rs.getString("u_name"),
                        rs.getString("u_pwd")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return user;
    }
}