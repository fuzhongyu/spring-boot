package com.fzy.core.controller.console.system;

import com.fzy.core.base.BaseController;
import com.fzy.core.base.PageEntity;
import com.fzy.core.base.ResponseResult;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.entity.system.User;
import com.fzy.core.service.system.UserService;
import com.fzy.core.util.ParamUtil;
import com.fzy.core.validator.group.GroupIdNull;
import com.fzy.core.validator.group.Group_1;
import com.fzy.core.validator.group.Group_2;
import com.fzy.core.vo.system.PasswordVo;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户controller
 *
 * @author Fucai
 * @date 2018/3/20
 */
@RestController
@RequestMapping(value = "${custom.console}/system/user")
public class UserController extends BaseController {

  public final static Logger logger= LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;


  /**
   * 查询用户列表
   * @param user
   * @return
   */
  @GetMapping(value = "")
  public ResponseResult findPage(User user,Integer pageNo,Integer pageSize){
    if (pageNo==null||pageSize==null){
      pageNo=defaultPageNo;
      pageSize=defaultPageSize;
    }

    PageEntity<User> pageEntity=userService.findPage(user ,pageNo,pageSize);
    return responseEntity(ErrorsMsg.SUCC_0,pageEntity);
  }

  /**
   * 根据id查询用户
   * @param userId
   * @return
   */
  @GetMapping(value = "/{userId}")
  public ResponseResult get(@PathVariable("userId") String userId,HttpServletRequest request){
    //验证用户仿造
    super.validateUser(request,userId);

    if (ParamUtil.LongParam(userId)==null){
      throw new ServiceException(ErrorsMsg.ERR_2);
    }
    return responseEntity(ErrorsMsg.SUCC_0,userService.get(ParamUtil.LongParam(userId)));
  }

  /**
   * 验证用户名
   * @param username
   * @return
   */
  @GetMapping(value = "/action/validate_username")
  public ResponseResult validateUserName(String username){
    userService.validateUser(username);
    return responseEntity(ErrorsMsg.SUCC_0);
  }

  /**
   * 新增用户
   * @param user
   * @param bindingResult
   * @return
   */
  @PostMapping(value = "")
  public ResponseResult add(@RequestBody @Validated({Group_1.class, GroupIdNull.class}) User user,BindingResult bindingResult){
    if (bindingResult.hasErrors()) {
      doValidateHandler(bindingResult);
    }
    userService.saveUser(user);
    return responseEntity(ErrorsMsg.SUCC_0,user);
  }

  /**
   * 修改用户信息
   * @param user
   * @param bindingResult
   * @return
   */
  @PutMapping(value = "{userId}")
  public ResponseResult update(@PathVariable("userId") String userId,@RequestBody @Validated({Group_2.class}) User user,BindingResult bindingResult,HttpServletRequest request){
    //验证用户仿造
    super.validateUser(request,userId);

    if (bindingResult.hasErrors()) {
      super.doValidateHandler(bindingResult);
    }
    user.setId(ParamUtil.LongParam(userId));
    userService.updateUser(user);
    return responseEntity(ErrorsMsg.SUCC_0,user);
  }

  /**
   * 修改密码
   * @param userId
   * @param vo
   * @param bindingResult
   * @param request
   * @return
   */
  @PutMapping(value = "/{userId}/pwd")
  public ResponseResult updatePassword(@PathVariable("userId") String userId,@RequestBody @Validated({Group_1.class})
      PasswordVo vo,BindingResult bindingResult,HttpServletRequest request){
    //验证用户仿造
    super.validateUser(request,userId);

    if (bindingResult.hasErrors()) {
      super.doValidateHandler(bindingResult);
    }

    userService.updatePwd(ParamUtil.LongParam(userId),vo.getOldPassword(),vo.getNewPassword(),request.getHeader("token"));
    //修改密码后要重新登录
    return responseEntity(ErrorsMsg.ERR_1004);
  }

  /**
   * 删除用户
   * @return
   */
  @DeleteMapping(value = "/{userId}")
  public ResponseResult delete(@PathVariable("userId") String userId,HttpServletRequest request){
    //验证用户仿造
    super.validateUser(request,userId);

    if (ParamUtil.LongParam(userId)==null){
      throw new ServiceException(ErrorsMsg.ERR_2);
    }
    userService.delete(new User(ParamUtil.LongParam(userId)));
    return responseEntity(ErrorsMsg.SUCC_0);
  }


}
