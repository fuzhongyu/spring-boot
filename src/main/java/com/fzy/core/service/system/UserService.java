package com.fzy.core.service.system;

import com.alibaba.fastjson.JSONObject;
import com.fzy.core.service.common.RedisService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fzy.core.base.BaseService;
import com.fzy.core.base.ServiceException;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.config.util.PasswordSaltUtil;
import com.fzy.core.dao.system.UserDao;
import com.fzy.core.dao.system.UserRoleDao;
import com.fzy.core.entity.system.Role;
import com.fzy.core.entity.system.User;
import com.fzy.core.entity.system.UserRole;
import com.fzy.core.service.common.RedisService;
import com.fzy.core.util.TokenUtil;
import java.util.*;

/**
 * 用户service
 *
 * @author Fucai
 * @date 2018/3/20
 */
@Service
public class UserService extends BaseService<UserDao, User> {

  private final static Logger logger = LoggerFactory.getLogger(UserService.class);

  /**
   * user缓存公共头部
   */
  private final static String USER_CACHE = "USER_";

  @Autowired
  private RedisService redisService;

  @Autowired
  private UserRoleDao userRoleDao;

  /**
   * 先从缓存取，如果没有再从数据库读取
   *
   * @param id
   * @return
   */
  @Override
  public User get(Long id) {
    if (id == null) {
      return null;
    }
    String userJsonString = redisService.get(USER_CACHE + id);
    if (userJsonString == null) {
      User user = super.get(id);
      if (user != null) {
        redisService.set(USER_CACHE + id, JSONObject.toJSONString(user));
      }
      return user;
    }
    return JSONObject.parseObject(userJsonString, User.class);
  }

  /**
   * 保存并返回用户
   *
   * @param entity
   * @return
   */
  @Transactional(readOnly = false)
  public User saveAndFlush(User entity) {
    //更新数据先从redis删除数据
    redisService.del(USER_CACHE + entity.getId());
    super.save(entity);
    //更新用户角色关系
    userRoleDao.deleteRoleByUser(entity.getId());
    if (CollectionUtils.isNotEmpty(entity.getRoleList())){
      List<UserRole> userRoleList = new ArrayList<>();
      for (Role unit: entity.getRoleList()){
        UserRole userRole = new UserRole(entity.getId(), unit.getId());
        userRoleList.add(userRole);
      }
      userRoleDao.batchInsert(userRoleList);
    }
    return get(entity.getId());
  }

  /**
   * 删除，同时删除缓存数据
   *
   * @param id
   * @return
   */
  @Transactional(readOnly = false)
  public int deleteUser(Long id) {
    redisService.del(USER_CACHE + id);
    return super.delete(id);
  }

  /**
   * 新增用户
   *
   * @param user
   * @return
   */
  @Transactional(readOnly = false)
  public User saveUser(User user) {
    //验证用户名是否存在
    validateUser(user.getUsername());
    //密码加密（如果设置了密码则使用该密码，否则使用默认密码）
    if (user.getPassword() != null) {
      user.setPassword(PasswordSaltUtil.md5Hex(user.getPassword(), user.getUsername()));
    }
    return saveAndFlush(user);
  }

  /**
   * 更新用户
   *
   * @param user
   * @return
   */
  @Transactional(readOnly = false)
  public User updateUser(User user) {
    User u = get(user.getId());
    if (u == null){
      throw new ServiceException(ErrorsMsg.ERR_2);
    }
    redisService.del(USER_CACHE + u.getId());
    if (StringUtils.isNotBlank(user.getPassword())){
      user.setPassword(PasswordSaltUtil.md5Hex(user.getPassword(), u.getUsername()));
    }
    return saveAndFlush(user);
  }

  /**
   * 更新密码
   *
   * @param userId
   * @param oldPassword
   * @param newPassword
   * @return
   */
  @Transactional(readOnly = false)
  public Integer updatePwd(Long userId, String oldPassword, String newPassword, String token) {

    User user = get(userId);
    if (user == null) {
      throw new ServiceException(ErrorsMsg.ERR_2);
    }
    redisService.del(USER_CACHE + userId);
    //校验旧密码
    if (!user.getPassword().equals(PasswordSaltUtil.md5Hex(oldPassword, user.getUsername()))) {
      throw new ServiceException(ErrorsMsg.ERR_1003);
    }

    User u = new User(userId);
    u.setPassword(PasswordSaltUtil.md5Hex(newPassword, user.getUsername()));
    Integer count = save(u);
    //清除token
    if (count == 1) {
      //登出
      SecurityUtils.getSubject().logout();
      //删除token
      TokenUtil.deleteToken(token);
    }
    return count;
  }

  /**
   * 根据名称查询用户
   *
   * @param username
   * @return
   */
  public User getByUserName(String username) {
    if (StringUtils.isBlank(username)) {
      return null;
    }
    return dao.getByUserName(username);
  }


  /**
   * 验证用户名是否已存在
   *
   * @param username
   */
  public void validateUser(String username) {
    if (StringUtils.isBlank(username)) {
      throw new ServiceException(ErrorsMsg.ERR_1002, "用户名不能为空");
    }
    User user = getByUserName(username);
    if (user != null) {
      throw new ServiceException(ErrorsMsg.ERR_1002, "用户名已存在");
    }
  }

}
