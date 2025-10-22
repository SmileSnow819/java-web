package com.yf.until;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * JDBC 数据库连接工具类
 */
public class DBUntil {
    private static String DRIVER;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    // 静态代码块：加载数据库配置并注册驱动
    static {
        Properties prop = new Properties();
        try (InputStream is = DBUntil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) {
                System.err.println("错误：未找到 db.properties 配置文件！请检查路径。");
            }
            prop.load(is);

            DRIVER = prop.getProperty("jdbc.driver");
            URL = prop.getProperty("jdbc.url");
            USER = prop.getProperty("jdbc.user");
            PASSWORD = prop.getProperty("jdbc.password");

            Class.forName(DRIVER);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("数据库驱动或配置加载失败！");
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 获取数据库连接
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("获取数据库连接失败：" + e.getMessage());
        }
        return conn;
    }

    /**
     * 封装 JDBC 的增、删、改通用方法
     * @param sql SQL语句
     * @param params 替换占位符（?）的参数
     * @return 影响的行数
     */
    public int executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        int rows = 0;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            rows = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("执行增删改操作失败：" + e.getMessage());
            // 常见的如用户名重复 (UNIQUE constraint) 也会在这里报错
        } finally {
            closeAll(null, ps, conn);
        }
        return rows;
    }

    /**
     * 封装 JDBC 的查询方法（返回 ResultSet，但需在调用者处关闭资源）
     * @param sql SQL查询语句
     * @param params 替换占位符（?）的参数
     * @return 结果集 ResultSet
     */
    public ResultSet executeQuery(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            rs = ps.executeQuery();
            // 注意：不在这里关闭 conn 和 ps，它们需要在 rs 被处理后关闭。
            // 将 PreparedStatement 存储到 Connection 对象中，方便后续关闭
            if(conn != null) {
                // 这是一个临时的解决方案，目的是让 closeAll 能够拿到 PreparedStatement
                // 更好的设计是让 DBUtil 封装整个查询过程，返回 List<T> 而非 ResultSet
            }

        } catch (SQLException e) {
            System.err.println("执行查询操作失败：" + e.getMessage());
            closeAll(rs, ps, conn); // 失败时关闭
        }
        return rs;
    }

    /**
     * 封装 JDBC 资源的关闭方法
     */
    public void closeAll(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("关闭资源失败：" + e.getMessage());
        }
    }
}