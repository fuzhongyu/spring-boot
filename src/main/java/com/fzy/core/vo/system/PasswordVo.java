package com.fzy.core.vo.system;

import com.fzy.core.validator.annotation.NotNullValid;
import com.fzy.core.validator.group.Group_1;
import lombok.Getter;
import lombok.Setter;

/**
 * 密码vo类
 *
 * @author Fucai
 * @date 2018/3/25
 */
@Getter
@Setter

@NotNullValid.List({@NotNullValid(fieldName = "oldPassword",message = "旧密码不能为空",groups = {Group_1.class}),
    @NotNullValid(fieldName = "newPassword",message = "新密码不能为空",groups = {Group_1.class})})
public class PasswordVo {

  private String oldPassword;

  private String newPassword;

}
