<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户注册</title>
</head>
<body>
<h1>用户注册</h1>

<p style="color: red;">${msg}</p>

<form action="UserServlet" method="post">
    <input type="hidden" name="action" value="register">
    <p>用户名: <input type="text" name="user" required></p>
    <p>密 码: <input type="password" name="pwd" required minlength="6"></p>
    <p>
        <button type="submit">注册</button>
        <a href="login.jsp">返回登录</a>
    </p>
</form>
</body>
</html>