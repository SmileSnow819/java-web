<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page isELIgnored="false" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>编辑学生信息</title>
    <style>
      .container {
        width: 400px;
        margin: 50px auto;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 5px;
      }
    </style>
  </head>
  <body>
    <%-- 显示当前登录用户信息和退出按钮 --%>
    <div style="text-align: right; margin-bottom: 20px">
      <c:if test="${not empty sessionScope.currentUser}">
        欢迎，${sessionScope.currentUser.u_name}！
        <a href="UserServlet?action=logout" style="margin-left: 10px">
          <button>退出登录</button>
        </a>
      </c:if>
    </div>

    <div class="container">
      <h1>编辑学生信息</h1>

      <%-- 错误信息显示（如果编辑失败，Servlet会转发回来） --%>
      <c:if test="${not empty msg}">
        <p style="color: red">${msg}</p>
      </c:if>

      <form action="StudentServlet" method="post">
        <input type="hidden" name="action" value="updateStu" />

        <p>
          <label>学号 (不可修改):</label>
          <%-- 传递不可修改的学号作为WHERE条件 --%>
          <input type="text" name="stuNo" value="${student.stuNo}" readonly />
        </p>

        <p>
          <label for="stuName">姓名:</label>
          <%-- 显示当前姓名，允许修改 --%>
          <input
            type="text"
            id="stuName"
            name="stuName"
            value="${student.stuName}"
            required
          />
        </p>

        <p>
          <label for="stuAge">年龄:</label>
          <%-- 显示当前年龄，允许修改 --%>
          <input
            type="number"
            id="stuAge"
            name="stuAge"
            value="${student.stuAge}"
            required
            min="15"
            max="60"
          />
        </p>

        <p>
          <button type="submit">确认修改</button>
          <a href="StudentServlet?action=getAll">
            <button type="button">取消并返回</button>
          </a>
        </p>
      </form>
    </div>
  </body>
</html>
