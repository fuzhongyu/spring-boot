package com.fzy.core.entity.system;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fzy.core.base.BaseEntity;
import java.util.List;

/**
 * 用户实体类
 *
 * @author Fucai
 * @date 2018/3/19
 */
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity<User> {

  /**
   * 用户名
   */
  private String username;

  /**
   * 密码
   */
  private String password;


  /**
   * 昵称
   */
  private String nickname;

  /**
   * 用户类型，0-普通用户，1-管理员
   */
  private Integer type;

  /**
   * 用户状态：0-正常，1-冻结
   */
  private Integer status;

  /**
   * 角色列表
   */
  private List<Role> roleList;


  public User(Long id) {
    this.id = id;
  }

}
