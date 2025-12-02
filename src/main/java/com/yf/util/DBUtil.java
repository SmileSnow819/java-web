package com.yf.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final Properties props = new Properties();

    // 静态代码块：加载驱动和配置文件
    static {
        try {
            // 使用类加载器获取资源的输入流
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            if (in == null) {
                throw new RuntimeException("找不到db.properties配置文件！");
            }
            props.load(in);
            Class.forName(props.getProperty("jdbc.driver"));
        } catch (IOException e) {
            System.err.println("加载db.properties文件失败！");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC驱动加载失败！");
            throw new RuntimeException(e);
        }
    }

    /** 获取数据库连接 */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                props.getProperty("jdbc.url"),
                props.getProperty("jdbc.user"),
                props.getProperty("jdbc.password")
            );
        } catch (SQLException e) {
            System.err.println("数据库连接失败！");
            e.printStackTrace();
        }
        return conn;
    }

    /** 关闭连接资源 */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /** 完整的关闭资源，实际项目中经常使用 */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}