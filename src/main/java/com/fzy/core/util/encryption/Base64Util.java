package com.fzy.core.util.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Base64加解密
 *
 * @author Fucai
 * @date 2018/3/24
 */

public class Base64Util {
  /**
   * base64算法加密
   * @param data 待编码的数据
   * @return
   */
  public static String base64Encrypt(byte[] data){
    return data==null?null:new BASE64Encoder().encode(data).replaceAll("[\\s*\t\n\r]", "");
  }


  /**
   * base64算法解密
   * @param data 待解码的数据
   * @return
   * @throws Exception
   */
  public static byte[] base64Decrypt(String data) throws Exception{
    return data.isEmpty()?null:new BASE64Decoder().decodeBuffer(data);
  }


}
