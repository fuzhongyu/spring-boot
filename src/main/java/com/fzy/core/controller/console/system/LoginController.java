package com.fzy.core.controller.console.system;

import com.fzy.core.base.BaseController;
import com.fzy.core.base.ResponseResult;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.entity.system.User;
import com.fzy.core.service.system.LoginService;
import com.fzy.core.validator.group.Group_3;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 * @author Fucai
 *
 * @date 2018/3/24
 */
@RestController
@RequestMapping(value = "${custom.console}/system")
public class LoginController extends BaseController{

  public final static Logger logger= LoggerFactory.getLogger(LoginController.class);

  @Autowired
  private LoginService loginService;

  /**
   * 登录
   * @param user
   * @param bindingResult
   * @return
   */
  @PostMapping(value = "/login")
  public ResponseResult login(@RequestBody @Validated(Group_3.class) User user,BindingResult bindingResult){
    if (bindingResult.hasErrors()) {
      doValidateHandler(bindingResult);
    }
    return responseEntity(ErrorsMsg.SUCC_0,loginService.systemLogin(user.getUsername(),user.getPassword()));
  }

  /**
   * 登出
   * @param request
   * @return
   */
  @GetMapping(value = "logout")
  public ResponseResult logout(HttpServletRequest request){
    loginService.systemLogout(request.getHeader("token"));
    return responseEntity(ErrorsMsg.SUCC_0);
  }



}
