package com.fzy.core.vo.system;

import com.fzy.core.base.BaseEntity;
import com.fzy.core.entity.system.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * 系统登录视图类
 * @author Fucai
 * @date 2018/3/26
 */
@Getter
@Setter
@NoArgsConstructor
public class SystemLoginVo extends BaseEntity<SystemLoginVo>{

  private String token;

  /**
   * 用户名
   */
  private String username;

  /**
   * 手机号
   */
  private String mobile;

  /**
   * 昵称
   */
  private String nickname;


  /**
   * 头像
   */
  private String photo;

  /**
   * 性别，0-男，1-女
   */
  private Integer sex;

  /**
   * 邮件
   */
  private String email;

  /**
   * 用户状态：0-正常，1-冻结
   */
  private Integer status;

  /**
   * 是否是管理员，0-是，1-不是
   */
  private Integer adminFlag;

  public SystemLoginVo(String token){
    this.token=token;
  }

  /**
   * entity转vo
   * @param entity
   * @return
   */
  public static SystemLoginVo convert(User entity){
    SystemLoginVo vo=new SystemLoginVo();
    BeanUtils.copyProperties(entity,vo);
    return vo;
  }

  /**
   * entity转vo
   * @param token
   * @param entity
   * @return
   */
  public static SystemLoginVo convert(String token,User entity){
    SystemLoginVo vo=new SystemLoginVo(token);
    BeanUtils.copyProperties(entity,vo);
    return vo;
  }

}
