package com.fzy;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author: fucai
 * @Date: 2019-01-28
 */
public class SaltTest {

    public static void main(String[] args) {
        //加密方式
        String hashAlgorithmName = "MD5";
        //密码原值
        Object crdentials = "123456";
        //以账号作为盐值
        ByteSource salt = ByteSource.Util.bytes("chushou");
        //加密2次
        int hashIterations = 10;
        Object result = new SimpleHash(hashAlgorithmName, crdentials, salt, hashIterations);
        System.out.println(((SimpleHash) result).toHex());
        System.out.println(((SimpleHash) result).toBase64());
        System.out.println(result.toString());
    }
}
