package com.fzy.core.entity.system;

import com.fzy.core.base.BaseEntity;
import com.fzy.core.validator.annotation.NotNullValid;
import com.fzy.core.validator.group.Group_1;
import com.fzy.core.validator.group.Group_2;
import com.fzy.core.validator.group.Group_3;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户实体类
 *
 * @author Fucai
 * @date 2018/3/19
 */
@Getter
@Setter
@NoArgsConstructor

@NotNullValid.List({@NotNullValid(fieldName = "username",message = "用户名不能为空",groups = {Group_1.class,
    Group_3.class}),
    @NotNullValid(fieldName = "password",message = "密码不能为空",groups = {Group_3.class}),
    @NotNullValid(fieldName = "mobile",message = "手机号不能为空",groups = {Group_1.class})})
public class User extends BaseEntity<User> {

  /**
   * 用户名
   */
  @Size(max = 32,message = "用户名过长",groups = {Group_1.class})
  @Null(message = "用户名不可更改",groups = {Group_2.class})
  private String username;

  /**
   * 密码
   */
  private String password;


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



  public User(Long id){
    this.id=id;
  }

}
