package com.yf.dao;

import com.yf.model.Student;
import java.util.List;

public interface StudentDao {
    int addStu(Student stu);
    int delStu(int stuNo);
    Student getStuById(int stuNo);
    int updateStu(Student stu);
    List<Student> getAllStu();
}