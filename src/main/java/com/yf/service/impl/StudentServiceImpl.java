package com.yf.service.impl;

import com.yf.dao.StudentDao;
import com.yf.dao.impl.StudentDaoImpl;
import com.yf.model.Student;
import com.yf.service.StudentService;
import com.yf.util.PageBean;
import java.util.List;

public class StudentServiceImpl implements StudentService {

    // 依赖注入 (Service 依赖 DAO)
    private StudentDao studentDao = new StudentDaoImpl();

    @Override
    public int addStu(Student stu) {
        // [业务逻辑]：检查学生年龄是否合理 (例如，必须在 15 到 60 岁之间)
        if (stu.getStuAge() < 15 || stu.getStuAge() > 60) {
            return 0;
        }
        return studentDao.addStu(stu);
    }

    @Override
    public int delStu(int stuNo) {
        // [业务逻辑]：可以在删除前添加日志记录或权限检查
        return studentDao.delStu(stuNo);
    }

    @Override
    public Student getStuById(int stuNo) {
        // [业务逻辑]：确保查询参数有效
        if (stuNo <= 0) {
            return null;
        }
        return studentDao.getStuById(stuNo);
    }

    @Override
    public int updateStu(Student stu) {
        // [业务逻辑]：检查更新后的数据有效性
        if (stu.getStuName() == null || stu.getStuName().trim().isEmpty()) {
            return 0;
        }
        return studentDao.updateStu(stu);
    }

    @Override
    public List<Student> getAllStu() {
        // [业务逻辑]：可以在这里添加数据预处理或缓存逻辑
        return studentDao.getAllStu();
    }

    @Override
    public PageBean getStuPage(int pageNow) {
        // 创建分页对象
        PageBean page = new PageBean();
        
        // 设置当前页
        page.setPageNow(pageNow);
        
        // 调用功能接口中获取总数量的方法
        int count = studentDao.getStuCount();
        page.setPageCount(count);
        
        // 调用分页数据的方法
        List<Student> stus = studentDao.getStuPage(page.getStartIndex(), page.getPageSize());
        page.getList().addAll(stus);
        
        return page;
    }
    
    @Override
    public List<Student> getAllStu(Integer stuNo, String stuName, Integer startAge, Integer endAge) {
        return studentDao.getAllStu(stuNo, stuName, startAge, endAge);
    }
    
    @Override
    public PageBean getStuPage(int pageNow, Integer stuNo, String stuName, Integer startAge, Integer endAge) {
        // 创建分页对象
        PageBean page = new PageBean();
        
        // 设置当前页
        page.setPageNow(pageNow);
        
        // 调用功能接口中获取符合条件的总数量
        int count = studentDao.getStuCount(stuNo, stuName, startAge, endAge);
        page.setPageCount(count);
        
        // 调用分页数据的方法
        List<Student> stus = studentDao.getStuPage(page.getStartIndex(), page.getPageSize(), stuNo, stuName, startAge, endAge);
        page.getList().addAll(stus);
        
        return page;
    }
    
    @Override
    public int getStuCount(Integer stuNo, String stuName, Integer startAge, Integer endAge) {
        return studentDao.getStuCount(stuNo, stuName, startAge, endAge);
    }
}