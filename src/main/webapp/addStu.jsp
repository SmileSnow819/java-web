<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>录入学生信息</title>
    <style>
        .container { width: 400px; margin: 50px auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }
        .error { color: red; }
    </style>
</head>
<body>
<div class="container">
    <h1>录入新学生</h1>

    <%-- 显示 Servlet 转发过来的错误消息（如果录入失败） --%>
    <c:if test="${not empty msg}">
        <p class="error">${msg}</p>
    </c:if>

    <form action="StudentServlet" method="post">
        <input type="hidden" name="action" value="addStu">

        <p>
            <label for="stuName">姓名:</label>
            <input type="text" id="stuName" name="stuName" required>
        </p>

        <p>
            <label for="stuAge">年龄:</label>
            <input type="number" id="stuAge" name="stuAge" required min="15" max="60">
        </p>

        <p>
            <button type="submit">确认录入</button>
            <a href="StudentServlet?action=getAll">
                <button type="button">返回列表</button>
            </a>
        </p>
    </form>
</div>
</body>
</html>