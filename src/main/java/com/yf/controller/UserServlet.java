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
        String contextPath = req.getContextPath(); // 在这里获取一次 contextPath

        if ("login".equals(action)) {
            login(req, resp);
        } else if ("register".equals(action)) {
            register(req, resp);
        } else {
            // 默认跳转到登录页面 (当 action 为空或不匹配时)
            // 确保使用绝对路径重定向，避免相对路径错误
            resp.sendRedirect(contextPath + "/login.jsp");
        }
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String user = req.getParameter("user");
        String pwd = req.getParameter("pwd");

        User u = new User();
        u.setU_name(user);
        u.setU_pwd(pwd);

        int num = userService.register(u);
        // --- 诊断日志：查看 num 的真实值 ---
        System.out.println("用户 " + user + " 注册操作返回结果 num = " + num);
        if(num > 0) { // 注册成功
            // 1. 获取应用程序的根路径，即： /test
            String contextPath = req.getContextPath();

            // 2. 拼接完整的重定向 URL： /test + /login.jsp = /test/login.jsp
            String redirectUrl = contextPath + "/login.jsp";



            resp.sendRedirect(redirectUrl);

        } else {

            req.setAttribute("msg", "注册失败，用户名可能已存在或信息不符合要求。");
            req.getRequestDispatcher("zhuce.jsp").forward(req, resp);
            // 此时浏览器URL保持不变，仍是 http://localhost:8080/test/UserServlet
        }
    }

    // --- 登录功能 ---
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // ... (登录逻辑不变)
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