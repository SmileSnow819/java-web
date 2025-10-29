package com.yf.servlet;

import com.yf.service.UserService;
import com.yf.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles user registration requests.
 * URL: /register
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService = new UserServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Get request parameters
        String uName = request.getParameter("username");
        String uPwd = request.getParameter("password");

        // Set encoding to prevent garbled characters
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // 2. Call service layer for registration
        boolean success = userService.addUser(uName, uPwd);

        // 3. Handle response and redirection
        if (success) {
            // Registration successful, redirect to login page
            System.out.println("Registration successful for user: " + uName);
            // Use response.sendRedirect() to clear the request (POST-Redirect-GET pattern)
            response.sendRedirect("login.jsp?message=Registration successful! Please log in.");
        } else {
            // Registration failed (e.g., username already exists or empty fields), redirect back to registration page
            System.out.println("Registration failed for user: " + uName);
            response.sendRedirect("register.jsp?error=Registration failed. Username might exist or fields are empty.");
        }
    }
}

