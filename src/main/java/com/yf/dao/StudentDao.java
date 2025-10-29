package com.yf.dao;
import com.yf.model.Student;
import java.util.List;

/**
 * Data access object interface for Student operations.
 */
public interface StudentDao {
    /**
     * Retrieves all students from the database.
     * @return A list of all Student objects.
     */
    List<Student> getAllStudent();
}
