package com.fzy.core.filter;

import com.fzy.core.config.util.JsonResponseUtil;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 跨域处理拦截器
 *
 * @author Fucai
 * @date 2019/1/29
 */
@WebFilter(filterName = "corsFilter", urlPatterns = "/*")
public class CorsFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(CorsFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        JsonResponseUtil.responseHeaderConfig(httpServletRequest,httpServletResponse);

        // 浏览器是会先发一次options请求，如果请求通过，则继续发送正式的post请求
        // 配置options的请求返回
        if (httpServletRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            httpServletResponse.setStatus(HttpStatus.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
