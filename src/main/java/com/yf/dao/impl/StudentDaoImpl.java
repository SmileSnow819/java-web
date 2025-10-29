package com.yf.dao.impl;

import com.yf.dao.StudentDao;
import com.yf.model.Student;
import com.yf.until.BaseDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of StudentDao interface.
 */
public class StudentDaoImpl extends BaseDao implements StudentDao {

    @Override
    public List<Student> getAllStudent() {
        List<Student> studentList = new ArrayList<>();
        String sql = "SELECT stuNo, stuName, stuAge FROM student ORDER BY stuNo";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Student student = new Student();
                student.setStuNo(rs.getInt("stuNo"));
                student.setStuName(rs.getString("stuName"));
                student.setStuAge(rs.getInt("stuAge"));
                studentList.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all students at DAO layer: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeAll(conn, ps, rs);
        }
        return studentList;
    }
}

