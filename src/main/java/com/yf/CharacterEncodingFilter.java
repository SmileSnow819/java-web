package com.yf;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 统一设置请求和响应编码为 UTF-8 的过滤器
 */
@WebFilter("/*") // 匹配所有请求
public class CharacterEncodingFilter implements Filter {

    private static final String ENCODING = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // 1. 修复请求乱码：设置请求编码
        request.setCharacterEncoding(ENCODING);

        // 2. 修复响应乱码：设置响应编码和内容类型
        response.setContentType("text/html;charset=" + ENCODING);
        response.setCharacterEncoding(ENCODING);

        // 放行请求
        chain.doFilter(request, response);
    }

    // 省略 init 和 destroy 方法...
}

