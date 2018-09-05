package com.fzy.core.service.system;

import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.entity.system.PayLoad;
import com.fzy.core.entity.system.User;
import com.fzy.core.util.ParamUtil;
import com.fzy.core.util.TokenUtil;
import com.fzy.core.util.encryption.Md5Util;
import com.fzy.core.vo.system.SystemLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录service
 * @author Fucai
 * @date 2018/3/24
 */
@Service
public class LoginService {

  @Autowired
  private UserService userService;


  /**
   * 系统后台登录
   * @param username 用户名
   * @param password 密码
   * @return
   */
  public SystemLoginVo systemLogin(String username,String password){
    User user=userService.getPasswordByUserName(username);
    if (user==null){
      throw new ServiceException(ErrorsMsg.ERR_1002,"用户不存在");
    }
    if (!user.getPassword().equals(Md5Util.textToMD5L32WithSalt(password,UserService.SYSTEM_PASSWORD_SALT))){
      throw new ServiceException(ErrorsMsg.ERR_1003);
    }
    String token=TokenUtil.createToken(new PayLoad(ParamUtil.StringParam(user.getId()),username));
    User uInfo=userService.get(user.getId());
    return SystemLoginVo.convert(token,uInfo);
  }

  /**
   * 系统后台登出
   * @param token
   * @return
   */
  public void systemLogout(String token){
     TokenUtil.deleteToken(token);
  }



  /**
   * 商户后台登出
   * @param token
   * @return
   */
  public void businessPlatformLogout(String token){
    TokenUtil.deleteToken(token);
  }

}
