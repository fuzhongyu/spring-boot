package com.fzy.core.config.util;

import com.alibaba.fastjson.JSON;
import com.fzy.core.base.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 返回json数据工具类
 *
 * @author: fucai
 * @Date: 2019-01-20
 */
public class JsonResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonResponseUtil.class);

    public static void responseJson(HttpServletResponse response, HttpServletRequest request, ResponseResult result) {
        responseHeaderConfig(request,response);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JSON.toJSONString(result));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }

    }

    /**
     * 返回数据请求头设置
     * @param request
     * @param response
     */
    public static void  responseHeaderConfig(HttpServletRequest request,HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        //跨域
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        //跨域 Header
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,PATCH,OPTIONS,HEAD,DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Authentication,Origin,X-Requested-With,Content-Type,token,user");
        response.setHeader("Access-Control-Max-Age", "3600");
    }
}
