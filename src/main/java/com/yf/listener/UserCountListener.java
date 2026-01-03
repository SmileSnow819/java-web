package com.yf.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * 在线用户统计监听器
 * 监听Session的创建、销毁和属性变化，实时更新在线人数
 * 使用Set来跟踪所有已登录的Session ID，确保同一Session只计算一次
 */
public class UserCountListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    private static final String ONLINE_SESSION_IDS = "onlineSessionIds";

    /**
     * 应用启动时初始化在线人数为0
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext application = sce.getServletContext();
        // 使用Set来存储已登录的Session ID
        Set<String> sessionIds = new HashSet<>();
        application.setAttribute(ONLINE_SESSION_IDS, sessionIds);
        application.setAttribute("onlineCount", 0);
        System.out.println("[UserCountListener] 应用启动，初始化在线人数为0");
    }

    /**
     * 应用关闭时的清理工作
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[UserCountListener] 应用关闭");
    }

    /**
     * Session创建时调用
     * 注意：此时用户可能还没有登录，所以不增加在线人数
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.println("[UserCountListener] Session创建: " + se.getSession().getId());
    }

    /**
     * Session销毁时调用
     * 从Set中移除该Session ID，并更新在线人数
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ServletContext application = session.getServletContext();
        String sessionId = session.getId();
        
        @SuppressWarnings("unchecked")
        Set<String> sessionIds = (Set<String>) application.getAttribute(ONLINE_SESSION_IDS);
        if (sessionIds != null && sessionIds.remove(sessionId)) {
            // 如果成功移除了Session ID，说明该Session是已登录的，需要更新在线人数
            int count = sessionIds.size();
            application.setAttribute("onlineCount", count);
            System.out.println("[UserCountListener] Session销毁: " + sessionId + "，当前在线人数: " + count);
        }
    }

    /**
     * 向Session中添加属性时调用
     * 当添加currentUser属性时，说明用户登录成功，将该Session ID添加到Set中
     */
    @Override
    public void attributeAdded(HttpSessionBindingEvent se) {
        // 只处理currentUser属性的添加
        if ("currentUser".equals(se.getName())) {
            HttpSession session = se.getSession();
            ServletContext application = session.getServletContext();
            String sessionId = session.getId();
            
            @SuppressWarnings("unchecked")
            Set<String> sessionIds = (Set<String>) application.getAttribute(ONLINE_SESSION_IDS);
            if (sessionIds == null) {
                sessionIds = new HashSet<>();
                application.setAttribute(ONLINE_SESSION_IDS, sessionIds);
            }
            
            // 将Session ID添加到Set中（Set会自动去重）
            if (sessionIds.add(sessionId)) {
                // 只有成功添加时才更新计数（避免重复添加）
                int count = sessionIds.size();
                application.setAttribute("onlineCount", count);
                System.out.println("[UserCountListener] 用户登录，Session ID: " + sessionId + "，当前在线人数: " + count);
            }
        }
    }

    /**
     * 从Session中移除属性时调用
     * 当移除currentUser属性时，说明用户退出登录，从Set中移除该Session ID
     */
    @Override
    public void attributeRemoved(HttpSessionBindingEvent se) {
        // 只处理currentUser属性的移除
        if ("currentUser".equals(se.getName())) {
            HttpSession session = se.getSession();
            ServletContext application = session.getServletContext();
            String sessionId = session.getId();
            
            @SuppressWarnings("unchecked")
            Set<String> sessionIds = (Set<String>) application.getAttribute(ONLINE_SESSION_IDS);
            if (sessionIds != null && sessionIds.remove(sessionId)) {
                // 如果成功移除了Session ID，更新在线人数
                int count = sessionIds.size();
                application.setAttribute("onlineCount", count);
                System.out.println("[UserCountListener] 用户退出登录，Session ID: " + sessionId + "，当前在线人数: " + count);
            }
        }
    }

    /**
     * Session属性替换时调用
     * 当在同一个Session中替换currentUser时（比如同一浏览器标签页切换账号）
     * 这种情况不需要改变在线人数，因为Session ID没有变化
     */
    @Override
    public void attributeReplaced(HttpSessionBindingEvent se) {
        if ("currentUser".equals(se.getName())) {
            HttpSession session = se.getSession();
            String sessionId = session.getId();
            System.out.println("[UserCountListener] Session中currentUser被替换，Session ID: " + sessionId + "，在线人数不变");
            // 不需要更新在线人数，因为Session ID没有变化，只是在同一个Session中切换了用户
        }
    }
}

