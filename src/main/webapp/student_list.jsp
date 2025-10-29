<%@ page import="com.yf.model.Student" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学生管理系统 - 学生列表</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #e9ecef; color: #333; padding: 20px; }
        .container { max-width: 900px; margin: 0 auto; background: #fff; padding: 30px; border-radius: 10px; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1); }
        h1 { color: #007bff; text-align: center; margin-bottom: 25px; }
        .user-info { text-align: right; margin-bottom: 15px; font-size: 1.1em; color: #6c757d; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #dee2e6; padding: 12px; text-align: left; }
        th { background-color: #007bff; color: white; font-weight: 600; }
        tr:nth-child(even) { background-color: #f8f9fa; }
        tr:hover { background-color: #e2f0ff; }
        .logout-btn { background-color: #dc3545; color: white; padding: 8px 15px; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s; text-decoration: none; display: inline-block; }
        .logout-btn:hover { background-color: #c82333; }
        .header-controls { display: flex; justify-content: space-between; align-items: center; }
    </style>
</head>
<body>
    <%
        // Check if user is logged in (session attribute set by LoginServlet)
        if (session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp?error=Please log in first.");
            return;
        }
    %>
    <div class="container">
        <div class="header-controls">
            <h1>学生信息管理列表</h1>
            <a href="login.jsp" class="logout-btn">退出登录</a>
        </div>

        <%
            // Retrieve the student list from the request scope (set by LoginServlet)
            List<Student> studentList = (List<Student>) request.getAttribute("studentList");

            if (studentList == null || studentList.isEmpty()) {
                out.println("<p style='text-align: center; color: #dc3545;'>暂无学生信息。</p>");
            } else {
        %>
            <table>
                <thead>
                    <tr>
                        <th>学生编号 (stuNo)</th>
                        <th>学生姓名 (stuName)</th>
                        <th>学生年龄 (stuAge)</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Student student : studentList) {
                    %>
                    <tr>
                        <td><%= student.getStuNo() %></td>
                        <td><%= student.getStuName() %></td>
                        <td><%= student.getStuAge() %></td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        <%
            }
        %>
    </div>
</body>
</html>
