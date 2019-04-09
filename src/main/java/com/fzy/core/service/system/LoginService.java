package com.fzy.core.service.system;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.entity.system.PayLoad;
import com.fzy.core.entity.system.User;
import com.fzy.core.util.ParamUtil;
import com.fzy.core.util.TokenUtil;
import com.fzy.core.vo.system.SystemLoginVo;

/**
 * 登录service
 *
 * @author Fucai
 * @date 2018/3/24
 */
@Service
public class LoginService {

  @Autowired
  private UserService userService;

  /**
   * 系统后台登录
   *
   * @param username 用户名
   * @param password 密码
   * @return
   */
  public SystemLoginVo systemLogin(String username, String password) {
    User user = userService.getByUserName(username);
    if (user == null){
      throw new ServiceException(ErrorsMsg.ERR_1007);
    }
    if (user.getStatus() == 1){
      throw new ServiceException(ErrorsMsg.ERR_1006);
    }
    UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
    //获取当前subject
    Subject currentUser = SecurityUtils.getSubject();
    try {
      //登录信息校验交给shiro操作
      currentUser.login(usernamePasswordToken);
    } catch (RuntimeException e) {
      throw new ServiceException(ErrorsMsg.ERR_1003);
    }

    //生成token
    String token = TokenUtil.createToken(new PayLoad(ParamUtil.StringParam(user.getId()), username));
    return new SystemLoginVo(token, user.getId());
  }

  /**
   * 系统后台登出
   *
   * @param token
   * @return
   */
  public void systemLogout(String token) {
    //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
    SecurityUtils.getSubject().logout();
    //删除token
    TokenUtil.deleteToken(token);
  }

}
