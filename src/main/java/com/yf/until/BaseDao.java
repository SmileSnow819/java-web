package com.yf.until;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 封装 JDBC 链接数据库的增删改方法和查询方法，作为 DAO 实现类的父类。
 * 结构参考提供的 BaseDao.png
 */
public class BaseDao {
    private static final Logger LOGGER = Logger.getLogger(BaseDao.class.getName());

    // JDBC 驱动名和数据库 URL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/dbms_user?useSSL=false&serverTimezone=UTC";

    // 数据库的用户名与密码
    private static final String USER = "root"; // 替换为您的数据库用户名
    private static final String PWD = "123456"; // 替换为您的数据库密码

    // 静态代码块加载驱动
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "无法加载 MySQL 驱动", e);
            throw new RuntimeException("数据库驱动加载失败", e);
        }
    }

    // 声明一个方法:用来获取数据库连接
    public Connection getConn() throws SQLException {
        // 使用 DriverManager.getConnection() 方法获取连接
        return DriverManager.getConnection(URL, USER, PWD);
    }

    // 声明一个方法:用来关闭所有资源
    public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "关闭数据库资源失败", e);
            // 抛出运行时异常，确保调用者知道出现了错误
            throw new RuntimeException("关闭数据库资源失败", e);
        }
    }

    /**
     * 封装增删改方法 (INSERT, UPDATE, DELETE)
     * @param sql SQL 语句
     * @param objs SQL 参数数组
     * @return 影响的行数
     */
    public int executeUpdate(String sql, Object[] objs) {
        int num = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConn();
            ps = conn.prepareStatement(sql);

            // 设置参数
            if (objs != null) {
                for (int i = 0; i < objs.length; i++) {
                    ps.setObject(i + 1, objs[i]);
                }
            }
            num = ps.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "执行更新操作失败: " + sql, e);
            // 抛出运行时异常，便于上层捕获处理
            throw new RuntimeException("数据库更新操作失败", e);
        } finally {
            closeAll(null, ps, conn);
        }
        return num;
    }

    /**
     * 封装查询方法 (SELECT)，返回结果集
     * @param sql SQL 语句
     * @param objs SQL 参数数组
     * @return ResultSet 结果集
     */
    public ResultSet executeQuery(String sql, Object[] objs) throws SQLException {
        // 注意：这里的 conn 和 ps 必须保持打开状态直到 DAOImpl 处理完 ResultSet
        Connection conn = getConn();
        PreparedStatement ps = conn.prepareStatement(sql);

        // 设置参数
        if (objs != null) {
            for (int i = 0; i < objs.length; i++) {
                ps.setObject(i + 1, objs[i]);
            }
        }

        // 这里的资源关闭需要在调用方 (UserDaoImpl) 手动处理
        return ps.executeQuery();
    }
}