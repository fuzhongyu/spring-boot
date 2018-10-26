package com.fzy.core.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: fucai
 * @Date: 2018/9/5
 */
@Service
public class RedisService {

    private static final Logger logger= LoggerFactory.getLogger(RedisService.class);
    /**
     * 缓存加项目前缀
     */
    private static final String PROJECT_NAME="BOOT_";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将值写入redis 如果key存在则覆盖
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        set(key,value,null);
    }

    /**
     * 将值写入redis 如果key存在则覆盖
     * @param key
     * @param value
     * @param expiration 过期时间，单位毫秒
     */
    public void set(String key,String value,Long expiration){
        String fillKey=PROJECT_NAME+key;
        logger.info("==>  Preparing: redis-set  _key:"+fillKey);
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(fillKey,value);
        //设置缓存过期时间
        if (expiration!=null && expiration>0){
            stringRedisTemplate.expire(fillKey,expiration,TimeUnit.MILLISECONDS);
        }

    }

    /**
     * 获取值
     * @param key
     * @return
     */
    public String get(String key) {
        String fillKey=PROJECT_NAME+key;
        logger.info("==>  Preparing: redis-get  _key:"+fillKey);
        return stringRedisTemplate.opsForValue().get(fillKey);
    }

    /**
     * 删除
     * @param key
     */
    public void del(String key) {
        String fillKey=PROJECT_NAME+key;
        logger.info("==>  Preparing: redis-del  _key:"+fillKey);
        stringRedisTemplate.delete(fillKey);
    }


}
