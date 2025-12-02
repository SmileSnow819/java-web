package com.yf.service;

import com.yf.model.Student;
import com.yf.util.PageBean;
import java.util.List;

public interface StudentService {
    int addStu(Student stu);
    int delStu(int stuNo);
    Student getStuById(int stuNo);
    int updateStu(Student stu);
    List<Student> getAllStu();
    
    // 分页业务
    PageBean getStuPage(int pageNow);
    
    // 带搜索条件的方法
    List<Student> getAllStu(Integer stuNo, String stuName, Integer startAge, Integer endAge);
    PageBean getStuPage(int pageNow, Integer stuNo, String stuName, Integer startAge, Integer endAge);
    
    // 获取符合条件的总记录数（用于删除后判断是否需要跳转页面）
    int getStuCount(Integer stuNo, String stuName, Integer startAge, Integer endAge);
}