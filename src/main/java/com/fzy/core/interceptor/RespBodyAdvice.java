package com.fzy.core.interceptor;

import com.alibaba.fastjson.JSON;
import com.fzy.core.base.ResponseResult;
import com.fzy.core.filter.BodyReaderFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 参数拦截器，获取返回参数
 * @author Fucai
 * @date 2018/3/20
 */
@ControllerAdvice
public class RespBodyAdvice implements ResponseBodyAdvice<Object> {

  private final static Logger logger = LoggerFactory.getLogger(RespBodyAdvice.class);

  @Override
  public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType,
      Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
      ServerHttpResponse serverHttpResponse) {
    if ( object != null && object instanceof ResponseResult) {
      //将返回参数存到线程变量中
      BodyReaderFilter
          .addValueToMyThreadLocal("responseParamters",  JSON.toJSONString(object));
    }

    return object;
  }

  @Override
  public boolean supports(MethodParameter methodParameter,
      Class<? extends HttpMessageConverter<?>> aClass) {
    //do nothing
    return true;
  }
}
