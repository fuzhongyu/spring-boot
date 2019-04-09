package com.fzy.core.vo.system;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 系统登录视图类
 *
 * @author Fucai
 * @date 2018/3/26
 */
@Getter
@Setter
@NoArgsConstructor
public class SystemLoginVo {

  private Long id;

  private String token;

  public SystemLoginVo(String token, Long id){
    this.token = token;
    this.id = id;
  }


}
