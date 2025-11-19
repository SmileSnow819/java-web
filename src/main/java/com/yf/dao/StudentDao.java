package com.yf.dao;

import com.yf.model.Student;
import java.util.List;

public interface StudentDao {
    int addStu(Student stu);
    int delStu(int stuNo);
    Student getStuById(int stuNo);
    int updateStu(Student stu);
    List<Student> getAllStu();
    
    // 分页功能:
    // 获取总数量的功能:
    int getStuCount();
    // 获取分页信息的功能:
    List<Student> getStuPage(int index, int pageSize);
}