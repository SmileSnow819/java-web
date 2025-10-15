package com.yf.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // 数据库连接
    private static final String URL = "jdbc:mysql://localhost:3306/dbms_demo?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    static {
        try {
            // 1. 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("无法加载 MySQL 驱动", e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        // 2. 获取连接
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 关闭资源
     */
    public static void close(AutoCloseable... resources) {
        for (AutoCloseable res : resources) {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
