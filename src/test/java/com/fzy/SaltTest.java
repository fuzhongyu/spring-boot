package com.fzy;

import com.fzy.core.util.encryption.Md5Util;
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
        String crdentials = "123456";
        String md5Crdentials = Md5Util.textToMD5L32(crdentials);
        System.out.println(md5Crdentials);
        //以账号作为盐值
        ByteSource salt = ByteSource.Util.bytes("admin");

        //加密迭代10次
        int hashIterations = 10;
        Object result = new SimpleHash(hashAlgorithmName, md5Crdentials, salt, hashIterations);
        System.out.println(((SimpleHash) result).toHex());
        System.out.println(((SimpleHash) result).toBase64());
        System.out.println(result.toString());
    }
}
