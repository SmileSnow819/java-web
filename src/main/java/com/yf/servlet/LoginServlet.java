package com.yf.servlet;

import com.yf.model.Student;
import com.yf.model.User;
import com.yf.service.StudentService;
import com.yf.service.UserService;
import com.yf.service.impl.StudentServiceImpl;
import com.yf.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Handles user login requests and, upon success, fetches student list.
 * URL: /login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserServiceImpl();
    private StudentService studentService = new StudentServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get request parameters
        String uName = request.getParameter("username");
        String uPwd = request.getParameter("password");

        request.setCharacterEncoding("UTF-8");

        // 2. Call service layer for login
        User user = userService.login(uName, uPwd);

        // 3. Handle login result
        if (user != null) {
            // Login successful
            System.out.println("User logged in: " + uName);

            // Store user info in session
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);

            // Fetch all student data (business requirement)
            List<Student> studentList = studentService.getAllStudent();

            // Put student list into request scope
            request.setAttribute("studentList", studentList);

            // Request Forward to student information page
            // Request forward preserves the request object, allowing data (studentList) to be passed
            request.getRequestDispatcher("student_list.jsp").forward(request, response);

        } else {
            // Login failed, redirect back to login page
            System.out.println("Login failed for user: " + uName);
            // Use sendRedirect() and attach an error message
            response.sendRedirect("login.jsp?error=Invalid username or password.");
        }
    }
}

