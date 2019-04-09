package com.fzy.core.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fzy.core.base.ResponseResult;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.config.util.JsonResponseUtil;
import com.fzy.core.util.TokenUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录验证拦截器
 * <p>
 * * @author Fucai
 *
 * @date 2018/3/20
 */
public class TokenInterceptor implements HandlerInterceptor {


  @Override
  public boolean preHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse, Object o) {
    String token = httpServletRequest.getHeader("token");
    String userId = httpServletRequest.getHeader("user");

    try {
      if (token == null || userId == null) {
        throw new ServiceException(ErrorsMsg.ERR_1001);
      }
      //验证token
      TokenUtil.validateToken(token,userId);
    } catch (ServiceException e) {
      ResponseResult responseResult = new ResponseResult(e.getErrCode());
      JsonResponseUtil.responseJson(httpServletResponse,httpServletRequest, responseResult);
      return false;
    }

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
