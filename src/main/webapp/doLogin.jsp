<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("UTF-8"); // 解决 POST 请求中文乱码
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if ("张三".equals(username) && "123456".equals(password)) {
        request.getRequestDispatcher("success.jsp").forward(request, response);
    } else {
        out.println("<h3>登录失败！用户名或密码错误。</h3>");
        out.println("<a href='login.jsp'>返回登录</a>");
    }
%>