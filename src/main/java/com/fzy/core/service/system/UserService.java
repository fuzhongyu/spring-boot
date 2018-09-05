package com.fzy.core.service.system;

import com.alibaba.fastjson.JSONObject;
import com.fzy.core.base.BaseService;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.dao.system.UserDao;
import com.fzy.core.entity.system.User;
import com.fzy.core.service.common.RedisService;
import com.fzy.core.util.TokenUtil;
import com.fzy.core.util.encryption.Md5Util;
import com.fzy.core.util.RagularUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户service
 *
 * @author Fucai
 * @date 2018/3/20
 */
@Service
public class UserService extends BaseService<UserDao,User> {

  private final static Logger logger= LoggerFactory.getLogger(UserService.class);

  /**
   * user缓存公共头部
   */
  private final static String USER_CACHE="USER_";

  @Autowired
  private RedisService redisService;

  /**
   * 系统密码加密盐值
   */
  public final static String SYSTEM_PASSWORD_SALT="o0pYnwHzpO55Q%IL";


  private final static String SYSTEM_DEFAULT_PASSWORD="666666";

  /**
   * 先从缓存取，如果没有再从数据库读取
   * @param id
   * @return
   */
  @Override
  public User get(Long id) {
    if (id==null){
      return null;
    }
    String userJsonString=redisService.get(USER_CACHE+id);
    if (userJsonString == null){
      User user = super.get(id);
      if (user != null){
        redisService.set(USER_CACHE+id,JSONObject.toJSONString(user));
      }
      return user;
    }
    return JSONObject.parseObject(userJsonString,User.class);
  }

  /**
   * 保存并返回用户
   * @param entity
   * @return
   */
  public User saveAndFlush(User entity) {
    Integer count=super.save(entity);
    entity=get(entity.getId());
    //数据更新到缓存
    if (count==1){
      redisService.set(USER_CACHE+ entity.getId(),JSONObject.toJSONString(entity));
    }
    return entity;
  }

  /**
   * 删除，同时删除缓存数据
   * @param entity
   * @return
   */
  @Override
  public int delete(User entity) {
   Integer count=super.delete(entity);
   if (count==1){
     redisService.del(USER_CACHE+entity.getId());
   }
    return count;
  }

  /**
   * 新增用户
   * @param user
   * @return
   */
  @Transactional(readOnly = false)
  public User saveUser(User user){
    //验证用户名是否存在
    validateUser(user.getUsername());
    //验证手机格式
    RagularUtil.validateMobile(user.getMobile());
    //验证邮箱
    if (user.getEmail()!=null){
      RagularUtil.validateEmail(user.getEmail());
    }
    //密码加密（如果设置了密码则使用该密码，否则使用默认密码）
    if (user.getPassword()!=null){
      user.setPassword(Md5Util.textToMD5L32WithSalt(user.getPassword(),SYSTEM_PASSWORD_SALT));
    }else {
      user.setPassword(Md5Util.textToMD5L32WithSalt(SYSTEM_DEFAULT_PASSWORD,SYSTEM_PASSWORD_SALT));
    }

    return saveAndFlush(user);
  }

  /**
   * 更新用户
   * @param user
   * @return
   */
  @Transactional(readOnly = false)
  public User updateUser(User user){
    //验证手机格式
    if (user.getMobile()!=null){
      RagularUtil.validateMobile(user.getMobile());
    }
    //验证邮箱
    if (user.getEmail()!=null){
      RagularUtil.validateEmail(user.getEmail());
    }
    return saveAndFlush(user);
  }

  /**
   * 更新密码
   * @param userId
   * @param oldPassword
   * @param newPassword
   * @return
   */
  @Transactional(readOnly = false)
  public Integer updatePwd(Long userId,String oldPassword,String newPassword,String token){

    User user=get(userId);
    if (user==null){
      throw new ServiceException(ErrorsMsg.ERR_2);
    }
    user=getPasswordByUserName(user.getUsername());
    //校验旧密码
    if (!user.getPassword().equals(Md5Util.textToMD5L32WithSalt(oldPassword,UserService.SYSTEM_PASSWORD_SALT))){
      throw new ServiceException(ErrorsMsg.ERR_1003);
    }

    User u=new User(userId);
    u.setPassword(Md5Util.textToMD5L32WithSalt(newPassword,SYSTEM_PASSWORD_SALT));
    u.setUpdateTime(System.currentTimeMillis());
    Integer count=dao.updatePassword(u);
    //清除token
    if (count==1){
      TokenUtil.deleteToken(token);
    }
    return count;
  }

  /**
   * 根据名称查询用户
   * @param username
   * @return
   */
  public User getByUserName(String username){
    return dao.getByUserName(username);
  }

  /**
   * 根据用户名获取密码
   * @param username
   * @return
   */
  public User getPasswordByUserName(String username){
    return dao.getPasswordByUserName(username);
  }

  /**
   * 验证用户名是否已存在
   * @param username
   */
  public void validateUser(String username){
    if (StringUtils.isBlank(username)){
      throw new ServiceException(ErrorsMsg.ERR_1002,"用户名不能为空");
    }
    User user=getByUserName(username);
    if (user!=null){
      throw new ServiceException(ErrorsMsg.ERR_1002,"用户名已存在");
    }
  }



}
