package com.fzy.core.config.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 密码+盐值 加密
 *
 * @author: fucai
 * @Date: 2019-01-28
 */
public class PasswordSaltUtil {

    /**
     * 密码hash迭代次数
     */
    public final static int HASH_ITERATIONS = 10;

    /**
     * 加密类型
     */
    private final static String MD5_HASH_ALGORITHM_NAME = "MD5";


    public static String md5Hex(String password, String salt) {
        if (StringUtils.isEmpty(salt)) {
            salt = "";
        }
        ByteSource saltByte = ByteSource.Util.bytes(salt);
        Object result = new SimpleHash(MD5_HASH_ALGORITHM_NAME, password, saltByte, HASH_ITERATIONS);
        return result.toString();
    }

}
