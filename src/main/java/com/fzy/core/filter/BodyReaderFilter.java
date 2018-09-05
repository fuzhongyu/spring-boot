package com.fzy.core.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;


/**
 * 获取请求流中参数(忽略静态资源获取)
 *
 * 步骤：将取出来的字符串，再次转换成流，然后把它放入到新request 对象中
 *
 * @author Fucai
 * @date 2018/3/20
 */
@WebFilter(filterName = "bodyReaderFilter", urlPatterns = "/*")
public class BodyReaderFilter implements Filter {

  private static Logger logger = LoggerFactory.getLogger(BodyReaderFilter.class);

  private final static List<String> exclusions= Arrays.asList(new String[]{".js",".gif",".jpg",".bmp",".png",".css",".ico",".pdf"});


  private static ThreadLocal<Map<String, Object>> myThreadLocal = new NamedThreadLocal<>(
      "My ThreadLocal");

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;

    String contentType = request.getContentType();
    String method = ((HttpServletRequest) request).getMethod();
    //设置请求时间
    Long requestBeginTime=System.currentTimeMillis();
    BodyReaderFilter.addValueToMyThreadLocal("requestBeginTime",requestBeginTime);

    //如果post请求使用application/json请求方式，则用自定义请求拦截来获取请求参数
    if (contentType != null && "post".equalsIgnoreCase(method) && (contentType.toLowerCase().contains("application/json"))) {

      ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
      chain.doFilter(requestWrapper, response);

    } else {
      if ("get".equalsIgnoreCase(method)){
        String paramters = formateParameter(request);
        //获取请求参数
        BodyReaderFilter.addValueToMyThreadLocal("requestParamters", paramters);
      }
      chain.doFilter(request, response);
    }

  }

  @Override
  public void destroy() {
    //销毁线程变量
    myThreadLocal.remove();

  }

  /**
   * 添加值到线程变量
   */
  public static void addValueToMyThreadLocal(String key, Object value) {
    Map<String, Object> map = myThreadLocal.get();
    if (map == null) {
      map = new HashMap<>();
    }
    map.put(key, value);
    myThreadLocal.set(map);
  }

  /**
   * 获取线程变量数据
   */
  public static Object getValue4MyThreadLocal(String key) {
    return myThreadLocal.get().get(key);
  }


  /**
   * 格式化参数
   */
  private String formateParameter(ServletRequest request) {
    //日志参数格式化
    StringBuilder params = new StringBuilder("");
    Map<String, String[]> paramMap = request.getParameterMap();
    params.append("{");
    if (paramMap != null) {
      for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
        params.append(param.getKey() + "=");
        String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param
            .getValue()[0] : "");
        String paramStr =
            StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue;
        params.append(paramStr);
      }
    }
    params.append("}");

    return params.toString();
  }
}
