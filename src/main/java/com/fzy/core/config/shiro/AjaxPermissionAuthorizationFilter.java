package com.fzy.core.config.shiro;

import com.fzy.core.base.ResponseResult;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.config.util.JsonResponseUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 对没有登录的请求进行拦截，全部返回json格式，不使用shiro原本跳转链接的拦截方式
 *
 * @author: fucai
 * @Date: 2019-01-20
 */
public class AjaxPermissionAuthorizationFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        ResponseResult result = new ResponseResult(ErrorsMsg.ERR_1001);
        JsonResponseUtil.responseJson((HttpServletResponse) response,(HttpServletRequest)request, result);
        return false;
    }


    @Bean
    public FilterRegistrationBean registration(AjaxPermissionAuthorizationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }
}
