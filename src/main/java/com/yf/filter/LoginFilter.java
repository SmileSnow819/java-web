package com.yf.filter;

import com.yf.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        // 获取请求的URI
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());
        
        // 获取session中的用户信息
        HttpSession session = req.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // 不需要登录验证的路径
        boolean isLoginPage = path.equals("/login.jsp") || (path.equals("/UserServlet") &&
                              "login".equals(req.getParameter("action")));
        boolean isRegisterPage = path.equals("/register.jsp") ||
                                (path.equals("/UserServlet") && "register".equals(req.getParameter("action")));
        boolean isStaticResource = path.startsWith("/css/") || path.startsWith("/js/") ||
                                  path.startsWith("/images/") || path.endsWith(".css") ||
                                  path.endsWith(".js") || path.endsWith(".png") ||
                                  path.endsWith(".jpg") || path.endsWith(".gif");
        
        // 如果访问的是登录页面、注册页面或静态资源，或者用户已登录，则放行
        if (isLoginPage || isRegisterPage || isStaticResource || currentUser != null) {
            chain.doFilter(request, response);
        } else {
            // 未登录且访问需要登录的页面，重定向到登录页面
            resp.sendRedirect(contextPath + "/login.jsp");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法，可以留空
    }

    @Override
    public void destroy() {
        // 销毁方法，可以留空
    }
}