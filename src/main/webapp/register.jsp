<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f4f4f4; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .register-container { background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); width: 300px; }
        h2 { text-align: center; color: #333; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input[type="text"], .form-group input[type="password"] { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .btn-group { display: flex; justify-content: center; margin-top: 20px; }
        .btn { padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; transition: background-color 0.3s; }
        .btn-success { background-color: #28a745; color: white; width: 100%; }
        .btn:hover { opacity: 0.9; }
        .error { text-align: center; padding: 10px; margin-bottom: 15px; border-radius: 4px; background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .back-link { display: block; text-align: center; margin-top: 15px; color: #007bff; text-decoration: none; }
        .back-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="register-container">
        <h2>用户注册</h2>
        <%
            // Get error message from request URL parameters (e.g., after failed registration)
            String error = request.getParameter("error");
            if (error != null && !error.isEmpty()) {
                out.println("<div class='error'>" + error + "</div>");
            }
        %>
        
        <form action="register" method="post">
            <div class="form-group">
                <label for="username">用户名:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">密 码:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="btn-group">
                <button type="submit" class="btn btn-success">注 册</button>
            </div>
        </form>
        <a href="login.jsp" class="back-link">返回登录</a>
    </div>
</body>
</html>
