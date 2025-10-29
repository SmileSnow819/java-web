package com.yf.service.impl;

import com.yf.dao.StudentDao;
import com.yf.dao.impl.StudentDaoImpl;
import com.yf.model.Student;
import com.yf.service.StudentService;

import java.util.List;

/**
 * Implementation of StudentService interface.
 */
public class StudentServiceImpl implements StudentService {
    // Dependency on DAO layer
    private StudentDao studentDao = new StudentDaoImpl();

    @Override
    public List<Student> getAllStudent() {
        // Call DAO layer to fetch all students
        return studentDao.getAllStudent();
    }
}
