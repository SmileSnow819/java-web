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
        } else if ("logout".equals(action)) {
            logout(req, resp);
        } else if ("checkUsername".equals(action)) {
            checkUsername(req, resp);
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
        
        // 检查是否为AJAX请求
        String contentType = req.getHeader("Content-Type");
        boolean isAjax = contentType != null && contentType.contains("multipart/form-data");
        
        if(num > 0) { // 注册成功
            if (isAjax) {
                // AJAX请求返回JSON响应
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().println("{\"success\": true, \"message\": \"注册成功\"}");
            } else {
                // 普通请求重定向
                String contextPath = req.getContextPath();
                String redirectUrl = contextPath + "/login.jsp";
                resp.sendRedirect(redirectUrl);
            }
        } else {
            if (isAjax) {
                // AJAX请求返回JSON响应
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().println("{\"success\": false, \"message\": \"注册失败，用户名可能已存在或信息不符合要求\"}");
            } else {
                // 普通请求转发
                req.setAttribute("msg", "注册失败，用户名可能已存在或信息不符合要求。");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
            }
        }
    }

    // --- 退出登录功能 ---
    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 清除session中的用户信息
        req.getSession().removeAttribute("currentUser");
        // 可选：使session失效
        req.getSession().invalidate();
        // 重定向到登录页面
        resp.sendRedirect(req.getContextPath() + "/login.jsp?logout=success");
    }

    // --- 登录功能 ---
    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String user = req.getParameter("user");
        String pwd = req.getParameter("pwd");

        User u = userService.login(user, pwd);
        
        // 检查是否为AJAX请求
        String contentType = req.getHeader("Content-Type");
        boolean isAjax = contentType != null && contentType.contains("multipart/form-data");

        if (u != null) { // 登录成功
            // 存: 将用户信息存入 Session
            req.getSession().setAttribute("currentUser", u);
            
            if (isAjax) {
                // AJAX请求返回JSON响应
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().println("{\"success\": true, \"message\": \"登录成功\"}");
            } else {
                // 转: 重定向到学生列表页面 (系统的主要功能页面)
                resp.sendRedirect("StudentServlet?action=getAll");
            }
        } else { // 登录失败
            if (isAjax) {
                // AJAX请求返回JSON响应
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().println("{\"success\": false, \"message\": \"用户名或密码错误\"}");
            } else {
                // 存: 存储错误信息
                req.setAttribute("msg", "用户名或密码错误！");
                // 转: 请求转发回登录页面
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            }
        }
    }

    // --- 验证账号是否存在（用于注册时异步验证）---
    private void checkUsername(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 设置响应内容类型和字符编码
        resp.setContentType("text/plain;charset=UTF-8");
        
        // 取: 获取请求当中的数据
        String u_name = req.getParameter("u_name");
        
        // 添加调试日志
        System.out.println("--- checkUsername 调试信息 ---");
        System.out.println("接收到的用户名参数: " + u_name);
        
        if (u_name == null || u_name.trim().isEmpty()) {
            System.out.println("用户名为空，返回false");
            resp.getWriter().println(false);
            return;
        }
        
        try {
            // 验证功能: 调用根据账号查询用户的业务
            User user = userService.getUserByName(u_name.trim());
            
            if (user != null) {
                // 该用户已经注册
                System.out.println("用户 " + u_name + " 已存在，返回false (不可注册)");
                resp.getWriter().println(false);
            } else {
                // 该用户可以注册
                System.out.println("用户 " + u_name + " 不存在，返回true (可以注册)");
                resp.getWriter().println(true);
            }
        } catch (Exception e) {
            System.err.println("checkUsername 发生异常: " + e.getMessage());
            e.printStackTrace();
            // 发生异常时返回false，阻止注册
            resp.getWriter().println(false);
        }
    }
}