package com.yf.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 姓名敏感词过滤器
 * 过滤学生姓名中的敏感词，替换为相应数量的星号
 */
@WebFilter(urlPatterns = {"/StudentServlet", "/addStu.jsp", "/updateStu.jsp"})
public class NameFilter implements Filter {

    // 敏感词列表
    private static final List<String> SENSITIVE_WORDS = Arrays.asList(
        "admin", "user"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法，可以留空
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        
        // 创建请求包装器，用于修改请求参数
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(req) {
            @Override
            public String getParameter(String name) {
                String value = super.getParameter(name);
                // 只过滤 stuName 参数
                if ("stuName".equals(name) && value != null) {
                    return filterSensitiveWords(value);
                }
                return value;
            }

            @Override
            public String[] getParameterValues(String name) {
                String[] values = super.getParameterValues(name);
                if ("stuName".equals(name) && values != null) {
                    String[] filteredValues = new String[values.length];
                    for (int i = 0; i < values.length; i++) {
                        filteredValues[i] = filterSensitiveWords(values[i]);
                    }
                    return filteredValues;
                }
                return values;
            }
        };

        // 如果是 StudentServlet 的请求，检查是否有敏感词被过滤
        if (req.getRequestURI().contains("StudentServlet")) {
            String originalName = req.getParameter("stuName");
            if (originalName != null) {
                String filteredName = filterSensitiveWords(originalName);
                // 如果过滤后的名称与原始名称不同，说明有敏感词被过滤
                if (!originalName.equals(filteredName)) {
                    // 将原始名称和过滤后的名称存储到 request 属性中，供 JSP 页面显示
                    wrappedRequest.setAttribute("originalName", originalName);
                    wrappedRequest.setAttribute("safeName", filteredName);
                }
            }
        }

        chain.doFilter(wrappedRequest, response);
    }

    /**
     * 过滤敏感词，将敏感词替换为相应数量的星号
     * @param text 原始文本
     * @return 过滤后的文本
     */
    private String filterSensitiveWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return text;
        }

        String result = text;
        // 遍历所有敏感词
        for (String word : SENSITIVE_WORDS) {
            // 不区分大小写匹配
            String regex = "(?i)" + word;
            // 替换为相应数量的星号（Java 8 兼容方式）
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                stars.append("*");
            }
            String replacement = stars.toString();
            result = result.replaceAll(regex, replacement);
        }
        return result;
    }

    @Override
    public void destroy() {
        // 销毁方法，可以留空
    }
}

