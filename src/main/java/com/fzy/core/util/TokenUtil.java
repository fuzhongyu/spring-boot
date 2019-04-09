package com.fzy.core.util;

import com.fzy.core.base.ServiceException;
import com.fzy.core.config.CustomConfigProperties;
import com.fzy.core.config.ErrorsMsg;
import com.fzy.core.entity.system.PayLoad;
import com.fzy.core.service.common.RedisService;
import com.fzy.core.util.encryption.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * token工具类
 *   token生成规则：负载参数（PayLoad）进行AES对称加密（防止仿造）生成,服务端也可对token解密获得用户参数
 * @author Fucai
 * @date 2018/3/24
 */

public class TokenUtil {

  private final static Logger logger= LoggerFactory.getLogger(TokenUtil.class);


  /**
   * 用于控制token在redis中过期时间更新
   */
  private final static Map<String, Long> TOKEN_MANAGE = new ConcurrentHashMap<>();


  /**
   * token缓存公共头部
   */
  private static final String Token_Cache = "Token_";


  /**
   * token加密密钥
   */
  public final static String TOKEN_KEY="6F^b2B^E1GaeUc#r";


  private static RedisService redisService=SpringContextHolder.getBean(RedisService.class);

  /**
   * 创建token
   *
   * @param data token中负载的参数
   * @return
   */
  public static String createToken(PayLoad data) {
    String token = null;
    try {
      token = AESUtil.encryptAESBase64(data.toString(), TOKEN_KEY);
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(ErrorsMsg.ERR_1, "token生成失败");
    }
    redisService.set(Token_Cache + token, ParamUtil.StringParam(data.getUserId()), CustomConfigProperties.TOKEN_EXPIRATION);
    return token;
  }

  /**
   * 验证token
   *
   * @param token 请求头中的token
   * @param clientUserId 请求头中的userId
   */
  public static void validateToken(String token,String clientUserId) {
    String cacheUserId = redisService.get(Token_Cache + token);
    if (cacheUserId == null) {
      throw new ServiceException(ErrorsMsg.ERR_1001);
    }else if (!cacheUserId.equals(clientUserId)){
      throw new ServiceException(ErrorsMsg.ERR_2);
    }
    //当token距离 上次在redis中更新的时间 大于指定值(注：该值小于过期时间)时，则去更新token在redis中的过期时间
    if (TOKEN_MANAGE.get(token) == null || System.currentTimeMillis() - TOKEN_MANAGE.get(token) > CustomConfigProperties.TOKEN_EXPIRATION_UPDATE) {
      redisService.set(Token_Cache + token, cacheUserId, CustomConfigProperties.TOKEN_EXPIRATION);
      TOKEN_MANAGE.put(token, System.currentTimeMillis());
    }

  }

  /**
   * 删除token
   *
   * @param token
   */
  public static void deleteToken(String token) {
    redisService.del(Token_Cache + token);
  }

  /**
   * 获取userId
   *
   * @param token
   * @return
   */
  public static Long getUserId(String token) {
    Long userId = ParamUtil.LongParam(redisService.get(Token_Cache + token));
    if (userId == null) {
      throw new ServiceException(ErrorsMsg.ERR_1001);
    }
    return userId;

  }



}
