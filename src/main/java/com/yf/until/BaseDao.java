package com.yf.until;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class to encapsulate JDBC connection and resource management.
 */
public class BaseDao {
    // Database connection constants (Replace with your actual credentials)
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/dbms_demo?serverTimezone=UTC";
    private static final String USER = "root"; // Use your MySQL user
    private static final String PASSWORD = "123456"; // 使用您设置的密码

    // Static block to load the JDBC driver
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Error loading MySQL driver: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get a database connection.
     * @return Connection object, or null if connection fails.
     */
    protected Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error getting connection: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Close all JDBC resources.
     * @param conn Connection
     * @param ps PreparedStatement
     * @param rs ResultSet
     */
    protected void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE statement.
     * @param sql The SQL query.
     * @param params Parameters to set in the PreparedStatement.
     * @return The number of rows affected.
     */
    protected int executeUpdate(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement ps = null;
        int rows = 0;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            rows = ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeAll(conn, ps, null);
        }
        return rows;
    }
}
