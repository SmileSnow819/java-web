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
        String sql = "INSERT INTO student (stuName, stuAge) VALUES (?, ?)";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, stu.getStuName());
            ps.setInt(2, stu.getStuAge());
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
        String sql = "SELECT stuNo, stuName, stuAge FROM student WHERE stuNo = ?";
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
        String sql = "UPDATE student SET stuName = ?, stuAge = ? WHERE stuNo = ?";
        Connection conn = DBUtil.getConnection();
        PreparedStatement ps = null;
        int result = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, stu.getStuName());
            ps.setInt(2, stu.getStuAge());
            ps.setInt(3, stu.getStuNo());
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
        String sql = "SELECT stuNo, stuName, stuAge FROM student";
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