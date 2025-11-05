package com.yf.controller;

import com.yf.model.User;
import com.yf.service.UserService;
import com.yf.service.impl.UserServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");

        if ("login".equals(action)) {
            login(req, resp);
        } else if ("register".equals(action)) {
            register(req, resp);
        } else {
            // 默认跳转到登录页面
            resp.sendRedirect("login.jsp");
        }
    }

    // --- 注册功能 ---
    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String user = req.getParameter("user");
        String pwd = req.getParameter("pwd");

        User u = new User();
        u.setU_name(user);
        u.setU_pwd(pwd);

        int num = userService.register(u);

        if(num > 0) { // 注册成功

            String contextPath = req.getContextPath(); // This returns "/test"

            // 构造完整的URL： /test/login.jsp?msg=...
            resp.sendRedirect(contextPath + "/login.jsp?msg=注册成功，请登录！");
        } else {
            // 注册失败的转发逻辑
            req.setAttribute("msg", "注册失败，用户名可能已存在或信息不符合要求。");
            req.getRequestDispatcher("zhuce.jsp").forward(req, resp);
        }
    }

    // --- 登录功能 ---
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String user = req.getParameter("user");
        String pwd = req.getParameter("pwd");

        User u = userService.login(user, pwd);

        if (u != null) { // 登录成功
            // 存: 将用户信息存入 Session
            req.getSession().setAttribute("currentUser", u);
            // 转: 重定向到学生列表页面 (系统的主要功能页面)
            resp.sendRedirect("StudentServlet?action=getAll");
        } else { // 登录失败
            // 存: 存储错误信息
            req.setAttribute("msg", "用户名或密码错误！");
            // 转: 请求转发回登录页面
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}