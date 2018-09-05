package com.fzy.core.util.encryption;

import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES对称加密算法编程实现
 * 注：AES加密后的数据不能转成string,否则解密时会出错，所以结合base64编码来实现转换
 *
 * @author Fucai
 * @date 2018/3/24
 */

public class AESUtil {

  private final static String IV="0102030405060708";



  /**
   * AES加密为base 64 code
   * @param data 待加密的内容
   * @param key 密钥
   * @return 加密后的base 64 code
   * @throws Exception
   */
  public static String encryptAESBase64(String data, String key) throws Exception {
    return Base64Util.base64Encrypt(encryptAES(data, key));
  }
  /**
   * 将base 64 code AES解密
   * @param data 待解密的base 64 code
   * @param key 密钥
   * @return 解密后的string
   * @throws Exception
   */
  public static String decryptAESBase64(String data, String key) throws Exception {
    return data.isEmpty() ? null : decryptAES(Base64Util.base64Decrypt(data), key);
  }


  /**
   * 生成密钥
   * @param key 密钥
   * @throws Exception
   */
  public static byte[] initKey(String key) throws Exception{
    //密钥生成器
    KeyGenerator keyGen = KeyGenerator.getInstance("AES");
    //初始化密钥生成器(可选择128位【16字节】，192位【24字节】和256位【32字节】这几种)
    if (key!=null){
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      random.setSeed(key.getBytes());
      keyGen.init(128,random);
    }else {
      keyGen.init(128);
    }

    //生成密钥
    SecretKey secretKey = keyGen.generateKey();
    return secretKey.getEncoded();
  }

  /**
   * 生成key
   * @param key
   * @return
   * @throws Exception
   */
  private static SecretKeySpec getKey(String key) throws Exception {

    byte[] keyBytes = key.getBytes();
    // 创建一个空的16位字节数组（默认值为0）
    byte[] bytes = new byte[16];
    Arrays.fill(bytes, (byte) 0);
    //只取前面16字节
    if (keyBytes.length<bytes.length){
      System.arraycopy(keyBytes,0,bytes,0,keyBytes.length);
    }else {
      System.arraycopy(keyBytes,0,bytes,0,bytes.length);
    }

    System.out.println(Arrays.toString(bytes));


    return new SecretKeySpec(bytes, "AES");
  }

  /**
   * 加密(加密后的数据不可转成string )
   * @param data 要加密数据
   * @param key 密钥 ，可通过initKey方法生成
   * @throws Exception
   */
  public static byte[] encryptAES(byte[] data, byte[] key) throws Exception{
    //密钥
    SecretKey secretKey = new SecretKeySpec(key, "AES");
    //创建密码器
    Cipher cipher = Cipher.getInstance("AES");
    //根据密钥对cipher进行初始化
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    //加密
    byte[] encrypt = cipher.doFinal(data);

    return encrypt;
  }


  /**
   * 解密
   * @param data 要解密数据
   * @param key 密钥 ，可通过initKey方法生成
   * @throws Exception
   */
  public static byte[] decryptAES(byte[] data, byte[] key) throws Exception{
    //恢复密钥生成器
    SecretKey secretKey = new SecretKeySpec(key, "AES");
    //Cipher完成解密
    Cipher cipher = Cipher.getInstance("AES");
    //根据密钥对cipher进行初始化
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] plain = cipher.doFinal(data);
    return plain;
  }


  /**
   * 加密 (加密后的数据不可转成string )
   * @param data 加密数据
   * @param key 密钥
   * @return
   * @throws Exception
   */
  public static byte[] encryptAES(String data,String key) throws Exception {
    SecretKeySpec skeySpec = getKey(key);
    // "算法/模式/补码方式"
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
    IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
    //根据密钥对cipher进行初始化
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
    byte[] encrypted = cipher.doFinal(data.getBytes());
    return  encrypted;
  }

  /**
   * 解密
   * @param data 解密数据
   * @param key  密钥
   * @return
   * @throws Exception
   */
  public static String decryptAES(byte[] data,String key ) throws Exception {
    SecretKeySpec skeySpec = getKey(key);
    // "算法/模式/补码方式"
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
    IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
    byte[] original = cipher.doFinal(data);
    return new String(original);
  }


}
