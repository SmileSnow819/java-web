package com.yf.dao.impl;

import com.yf.dao.UserDao;
import com.yf.model.User;
import com.yf.until.BaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of UserDao interface.
 */
public class UserDaoImpl extends BaseDao implements UserDao {

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO user (u_name, u_pwd) VALUES (?, ?)";
        int rows = executeUpdate(sql, user.getUName(), user.getUPwd());
        return rows > 0;
    }

    @Override
    public User login(String uName, String uPwd) {
        String sql = "SELECT u_id, u_name, u_pwd FROM user WHERE u_name = ? AND u_pwd = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, uName);
            ps.setString(2, uPwd);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUId(rs.getInt("u_id"));
                user.setUName(rs.getString("u_name"));
                user.setUPwd(rs.getString("u_pwd"));
            }
        } catch (SQLException e) {
            System.err.println("Login failed at DAO layer: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeAll(conn, ps, rs);
        }
        return user;
    }
}
