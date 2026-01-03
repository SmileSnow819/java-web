package com.yf.dao.impl;

import com.yf.dao.StudentDao;
import com.yf.model.Student;
import com.yf.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao { // 确保不是 abstract

    // --- 辅助方法：关闭资源 (避免代码冗余) ---
    private void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        DBUtil.close(rs, ps, conn); // 使用DBUtil的完整关闭方法
    }

    @Override
    public int addStu(Student stu) {
        String sql = "INSERT INTO student (stuName, stuAge, stuImg) VALUES (?, ?, ?)";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, stu.getStuName());
            ps.setInt(2, stu.getStuAge());
            ps.setString(3, stu.getStuImg());
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, conn);
        }
        return result;
    }

    @Override
    public int delStu(int stuNo) {
        String sql = "DELETE FROM student WHERE stuNo = ?";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, stuNo);
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, conn);
        }
        return result;
    }

    @Override
    public Student getStuById(int stuNo) {
        String sql = "SELECT stuNo, stuName, stuAge, stuImg FROM student WHERE stuNo = ?";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Student stu = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, stuNo);
            rs = ps.executeQuery();
            if (rs.next()) {
                stu = new Student();
                stu.setStuNo(rs.getInt("stuNo"));
                stu.setStuName(rs.getString("stuName"));
                stu.setStuAge(rs.getInt("stuAge"));
                stu.setStuImg(rs.getString("stuImg"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return stu;
    }

    @Override
    public int updateStu(Student stu) {
        // 如果stuImg不为空，则更新头像；否则不更新头像字段
        String sql;
        if (stu.getStuImg() != null && !stu.getStuImg().trim().isEmpty()) {
            sql = "UPDATE student SET stuName = ?, stuAge = ?, stuImg = ? WHERE stuNo = ?";
        } else {
            sql = "UPDATE student SET stuName = ?, stuAge = ? WHERE stuNo = ?";
        }
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, stu.getStuName());
            ps.setInt(2, stu.getStuAge());
            if (stu.getStuImg() != null && !stu.getStuImg().trim().isEmpty()) {
                ps.setString(3, stu.getStuImg());
                ps.setInt(4, stu.getStuNo());
            } else {
                ps.setInt(3, stu.getStuNo());
            }
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, conn);
        }
        return result;
    }

    @Override
    public List<Student> getAllStu() {
        String sql = "SELECT stuNo, stuName, stuAge, stuImg FROM student";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Student> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Student stu = new Student();
                stu.setStuNo(rs.getInt("stuNo"));
                stu.setStuName(rs.getString("stuName"));
                stu.setStuAge(rs.getInt("stuAge"));
                stu.setStuImg(rs.getString("stuImg"));
                list.add(stu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }
    
    @Override
    public int getStuCount() {
        String sql = "SELECT COUNT(*) FROM student";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return count;
    }
    
    @Override
    public List<Student> getStuPage(int index, int pageSize) {
        String sql = "SELECT stuNo, stuName, stuAge FROM student LIMIT ?, ?";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Student> list = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, index);
            ps.setInt(2, pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                Student stu = new Student();
                stu.setStuNo(rs.getInt("stuNo"));
                stu.setStuName(rs.getString("stuName"));
                stu.setStuAge(rs.getInt("stuAge"));
                stu.setStuImg(rs.getString("stuImg"));
                list.add(stu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }
    
    @Override
    public int getStuCount(Integer stuNo, String stuName, Integer startAge, Integer endAge) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM student WHERE 1=1");
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            // 动态构建SQL条件
            if (stuNo != null) {
                sql.append(" AND stuNo = ?");
            }
            if (stuName != null && !stuName.trim().isEmpty()) {
                sql.append(" AND stuName LIKE ?");
            }
            if (startAge != null) {
                sql.append(" AND stuAge >= ?");
            }
            if (endAge != null) {
                sql.append(" AND stuAge <= ?");
            }
            
            ps = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            if (stuNo != null) {
                ps.setInt(paramIndex++, stuNo);
            }
            if (stuName != null && !stuName.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + stuName + "%");
            }
            if (startAge != null) {
                ps.setInt(paramIndex++, startAge);
            }
            if (endAge != null) {
                ps.setInt(paramIndex++, endAge);
            }
            
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return count;
    }
    
    @Override
    public List<Student> getStuPage(int index, int pageSize, Integer stuNo, String stuName, Integer startAge, Integer endAge) {
        StringBuilder sql = new StringBuilder("SELECT stuNo, stuName, stuAge, stuImg FROM student WHERE 1=1");
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Student> list = new ArrayList<>();
        try {
            // 动态构建SQL条件
            if (stuNo != null) {
                sql.append(" AND stuNo = ?");
            }
            if (stuName != null && !stuName.trim().isEmpty()) {
                sql.append(" AND stuName LIKE ?");
            }
            if (startAge != null) {
                sql.append(" AND stuAge >= ?");
            }
            if (endAge != null) {
                sql.append(" AND stuAge <= ?");
            }
            sql.append(" LIMIT ?, ?");
            
            ps = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            if (stuNo != null) {
                ps.setInt(paramIndex++, stuNo);
            }
            if (stuName != null && !stuName.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + stuName + "%");
            }
            if (startAge != null) {
                ps.setInt(paramIndex++, startAge);
            }
            if (endAge != null) {
                ps.setInt(paramIndex++, endAge);
            }
            ps.setInt(paramIndex++, index);
            ps.setInt(paramIndex++, pageSize);
            
            rs = ps.executeQuery();
            while (rs.next()) {
                Student stu = new Student();
                stu.setStuNo(rs.getInt("stuNo"));
                stu.setStuName(rs.getString("stuName"));
                stu.setStuAge(rs.getInt("stuAge"));
                stu.setStuImg(rs.getString("stuImg"));
                list.add(stu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }
    
    @Override
    public List<Student> getAllStu(Integer stuNo, String stuName, Integer startAge, Integer endAge) {
        StringBuilder sql = new StringBuilder("SELECT stuNo, stuName, stuAge, stuImg FROM student WHERE 1=1");
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Student> list = new ArrayList<>();
        try {
            // 动态构建SQL条件
            if (stuNo != null) {
                sql.append(" AND stuNo = ?");
            }
            if (stuName != null && !stuName.trim().isEmpty()) {
                sql.append(" AND stuName LIKE ?");
            }
            if (startAge != null) {
                sql.append(" AND stuAge >= ?");
            }
            if (endAge != null) {
                sql.append(" AND stuAge <= ?");
            }
            
            ps = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            if (stuNo != null) {
                ps.setInt(paramIndex++, stuNo);
            }
            if (stuName != null && !stuName.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + stuName + "%");
            }
            if (startAge != null) {
                ps.setInt(paramIndex++, startAge);
            }
            if (endAge != null) {
                ps.setInt(paramIndex++, endAge);
            }
            
            rs = ps.executeQuery();
            while (rs.next()) {
                Student stu = new Student();
                stu.setStuNo(rs.getInt("stuNo"));
                stu.setStuName(rs.getString("stuName"));
                stu.setStuAge(rs.getInt("stuAge"));
                stu.setStuImg(rs.getString("stuImg"));
                list.add(stu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, ps, conn);
        }
        return list;
    }
}