<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page isELIgnored="false" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>用户登录</title>
    <link rel="stylesheet" href="css/style.css" />
  </head>
  <body>
    <div class="container">
      <div class="header">
        <h1>欢迎回来</h1>
        <p>请登录您的账户</p>
      </div>

      <c:if test="${not empty msg}">
        <div class="alert alert-danger">${msg}</div>
      </c:if>

      <form action="UserServlet" method="post">
        <input type="hidden" name="action" value="login" />

        <div class="form-group">
          <label for="user">用户名</label>
          <div class="input-with-icon username-icon">
            <input
              type="text"
              id="user"
              name="user"
              class="form-control"
              placeholder="请输入用户名"
              required
            />
          </div>
        </div>

        <div class="form-group">
          <label for="pwd">密码</label>
          <div class="input-with-icon password-icon">
            <input
              type="password"
              id="pwd"
              name="pwd"
              class="form-control"
              placeholder="请输入密码"
              required
            />
          </div>
        </div>

        <button type="submit" class="btn btn-primary">登录</button>

        <div class="link">
          <a href="register.jsp">还没有账户？立即注册</a>
        </div>
      </form>
    </div>
  </body>
</html>
