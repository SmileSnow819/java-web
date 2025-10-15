package com.yangfan.dao.impl;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.yangfan.dao.UserDao;
import com.yangfan.pojo.User;
public class UserDaoImpl implements UserDao {

    // 数据库连接常量
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/dbms_demo?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    @Override
    public List<User> getAllUsers() {
        // 1. 定义所需对象，初始为 null
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            // 2. 加载驱动 (JDBC 4.0 之后通常可以省略，但写上更规范)
            Class.forName(DRIVER);

            // 3. 建立连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 4. 定义 SQL 语句并创建 PreparedStatement 对象
            String sql = "SELECT u_id, u_name, u_pwd FROM user";
            ps = conn.prepareStatement(sql);

            // 5. 执行查询，返回结果集
            rs = ps.executeQuery();

            // 6. 遍历结果集，将数据封装到 User 对象中
            while (rs.next()) {
                // a. 创建 User 对象
                User user = new User();

                // b. 从结果集中获取数据并设置到 User 对象
                user.setU_id(rs.getInt("u_id"));
                user.setU_name(rs.getString("u_name"));
                user.setU_pwd(rs.getString("u_pwd"));

                // c. 将 User 对象添加到列表中
                users.add(user);
            }

        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC 驱动未找到！");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ 数据库操作失败！");
            e.printStackTrace();
        } finally {
            // 7. 释放资源，确保关闭顺序：ResultSet -> PreparedStatement -> Connection
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("❌ 关闭资源失败！");
                e.printStackTrace();
            }
        }

        // 8. 返回封装好的用户列表
        return users;
    }
}
