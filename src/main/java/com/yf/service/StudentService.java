package com.yf.service;

import com.yf.model.Student;
import java.util.List;

/**
 * Business logic service interface for Student operations.
 */
public interface StudentService {
    /**
     * Handles business logic for fetching all students.
     * @return List of all students.
     */
    List<Student> getAllStudent();
}