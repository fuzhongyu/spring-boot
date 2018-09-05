package com.fzy.core.interceptor;

import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.util.TokenUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录验证拦截器
 *
 *  * @author Fucai
 * @date 2018/3/20
 */
public class TokenInterceptor implements HandlerInterceptor {


  @Override
  public boolean preHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o) throws Exception {
    String token=httpServletRequest.getHeader("token");
    if (token==null){
      throw new ServiceException(ErrorsMsg.ERR_1001);
    }
    //更新token过期时间
    TokenUtil.updateToken(token);

    return true;
  }

  @Override
  public void postHandle(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
      throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

  }
}
