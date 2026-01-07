package com.yf.dao.impl;

import com.yf.dao.UserDao;
import com.yf.model.User;
import com.yf.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        DBUtil.close(rs, ps, conn);
    }

    @Override
    public int addUser(User user) {
        // 假设 u_id 是自增主键，我们只插入用户名和密码
        String sql = "INSERT INTO user (u_name, u_pwd) VALUES (?, ?)";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getU_name());
            ps.setString(2, user.getU_pwd());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, conn);
        }
        return result;
    }

    @Override
    public User getUserByCredentials(String u_name, String u_pwd) {
        String sql = "SELECT u_id, u_name, u_pwd FROM user WHERE u_name = ? AND u_pwd = ?";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, u_name);
            ps.setString(2, u_pwd);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setU_id(rs.getInt("u_id"));
                user.setU_name(rs.getString("u_name"));
                user.setU_pwd(rs.getString("u_pwd"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return user;
    }

    @Override
    public User getUserByName(String u_name) {
        // 创建一个对象作为最终结果
        User user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 编写sql语句
            String sql = "SELECT * FROM user WHERE u_name=?";
            
            // 获取数据库连接
            conn = DBUtil.getConnection();
            if (conn == null) {
                return null;
            }
            
            // 创建PreparedStatement对象
            ps = conn.prepareStatement(sql);
            // 设置参数
            ps.setString(1, u_name);
            // 执行查询
            rs = ps.executeQuery();
            // 处理结果集
            if (rs.next()) {
                // 创建用户对象
                user = new User();
                user.setU_id(rs.getInt("u_id"));
                user.setU_name(rs.getString("u_name"));
                user.setU_pwd(rs.getString("u_pwd"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 不抛出RuntimeException，而是返回null
            return null;
        } finally {
            // 关闭资源
            closeResources(rs, ps, conn);
        }
        return user;
    }
}