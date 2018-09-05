package com.fzy.core.entity.system;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * token负载参数
 * @author Fucai
 * @date 2018/3/24
 */
@Getter
@Setter
@NoArgsConstructor
public class PayLoad {

  /**
   * 用户id
   */
  private String userId;

  /**
   * 用户名
   */
  private String username;

  /**
   * token签发时间
   */
  private Long issueTime=System.currentTimeMillis();


  public PayLoad(String userId,String username){
    this.userId=userId;
    this.username=username;
  }

  @Override
  public String toString(){
    StringBuilder builder=new StringBuilder("");
    builder.append("{")
        .append("\"userId\":"+userId)
        .append(",\"username\":\""+username+"\"")
        .append(",\"issueTime\"="+issueTime)
        .append("}");
    return builder.toString();
  }

}
