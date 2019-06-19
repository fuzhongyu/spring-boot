package com.fzy.core.service.common;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: fucai
 * @Date: 2018/9/5
 */
@Service
public class StringRedisService {

    private static final Logger logger = LoggerFactory.getLogger(StringRedisService.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将值写入redis 如果key存在则覆盖
     *
     * @param key
     * @param value
     */
    public void setString(String key, Object value) {
        setString(key, value, null);
    }

    /**
     * 将值写入redis 如果key存在则覆盖
     *
     * @param key
     * @param value
     * @param expiration 过期时间，单位毫秒
     */
    public void setString(String key, Object value, Long expiration) {
        stringRedisTemplate.opsForValue().set(key,toStr(value));
        setExpire(key,expiration);
    }

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 设置list
     * @param key
     * @param list
     */
    public void addRightList(String key, List<Object> list){
        addRightList(key,list,null);
    }

    /**
     * 设置list
     * @param key
     * @param list
     * @param expiration 过期时间，单位毫秒
     */
    public void addRightList(String key, List<Object> list, Long expiration){
        List<String> val = list.stream()
                .map(this::toStr)
                .collect(Collectors.toList());
        stringRedisTemplate.opsForList().rightPushAll(key,val);
        setExpire(key,expiration);
    }

    /**
     * 设置list
     * @param key
     * @param value
     */
    public void addRightList(String key,Object value){
        stringRedisTemplate.opsForList().rightPush(key,toStr(value));
    }

    /**
     * 获取list中指定索引的元素
     * @param key
     * @param index
     * @return
     */
    public String getIndexList(String key,long index){
        return stringRedisTemplate.opsForList().index(key,index);
    }

    /**
     * 获取list所有数据
     * @param key
     * @return
     */
    public List<String> getListAll(String key){
        return getRangeList(key,0, -1);
    }

    /**
     * 获取list，指定索引区间数据
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> getRangeList(String key, long start, long end){
       return stringRedisTemplate.opsForList().range(key, start, end);
    }



    /**
     * 设置hash
     * @param key  redis中的key
     * @param field map中的key
     * @param value map中key对应的值
     */
    public void addHash(String key,String field,Object value){
        stringRedisTemplate.opsForHash().put(key,field,toStr(value));
    }


    /**
     * 设置hash
     * @param key  redis中的key
     * @param value map
     */
    public void addHash(String key, Map<String,Object> value){
       addHash(key,value,null);
    }

    /**
     * 设置hash
     * @param key  redis中的key
     * @param value map
     * @param expiration 过期时间，单位毫秒
     */
    public void addHash(String key, Map<String,Object> value,Long expiration){
        Map<String,String> params = new HashMap<>();
        for (Map.Entry<String,Object> unit:value.entrySet()){
            params.put(unit.getKey(),toStr(unit.getValue()));
        }
        stringRedisTemplate.opsForHash().putAll(key,params);
        setExpire(key,expiration);
    }

    /**
     * 获取全部map
     * @param key
     * @return
     */
    public Map<String,String> getHash(String key){
        Map<String,String> returnMap = new HashMap<>();
        Map<Object,Object> result = stringRedisTemplate.opsForHash().entries(key);
        if (result!=null && result.size()>0){
            for (Map.Entry<Object,Object> entry:result.entrySet()){
                returnMap.put((String)entry.getKey(),(String)entry.getValue());
            }
        }
        return returnMap;
    }

    /**
     * 获取值
     * @param key
     * @param field
     * @return
     */
    public String getHashValue(String key,String field){
        return (String) stringRedisTemplate.opsForHash().get(key,field);
    }

    /**
     * 删除map中的key
     * @param key
     * @param field
     */
    public void deleteField(String key,String field){
        stringRedisTemplate.opsForHash().delete(key,field);
    }

    public void addSet(String key,Set<Object> set){
        addSet(key,set,null);
    }

    /**
     * 设置集合
     * @param key
     * @param value
     * @param expiration
     */
    public void addSet(String key, Set<Object> value,Long expiration){
        Set<String> set = value.stream()
                .map(this::toStr)
                .collect(Collectors.toSet());
        stringRedisTemplate.opsForSet().add(key,set.toArray(new String[set.size()]));
        setExpire(key,expiration);
    }



    public void addSet(String key,Object value){
        stringRedisTemplate.opsForSet().add(key,toStr(value));
    }

    /**
     * 获取set
     * @param key
     * @return
     */
    public Set<String> getSet(String key){
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * 判断是否在set中
     * @param key
     * @param obj
     * @return
     */
    public Boolean isInSet(String key, Object obj){
        return stringRedisTemplate.opsForSet().isMember(key,toStr(obj));
    }

    /**
     * 设置有序集合
     * @param key
     * @param value
     * @param score
     */
    public void addZSet(String key,Object value, double score){
        stringRedisTemplate.opsForZSet().add(key,toStr(value),score);
    }

    /**
     * 获取有序集合所有数据所有
     * @param key
     * @return
     */
    public Set<String> getZSetAll(String key){
        return getZSet(key,0 , -1);
    }


    /**
     * 获取有序集合指定score数据
     * @param key
     * @return
     */
    public Set<String> getZSet(String key,long start, long end){
        return stringRedisTemplate.opsForZSet().range(key,start,end);
    }

    /**
     * 根据排序获取数据
     * @param key
     * @param min 最小score值
     * @param max 最大score值
     * @return
     */
    public Set<String> getZSetByScores(String key, double min, double max){
        return stringRedisTemplate.opsForZSet().rangeByScore(key,min,max);
    }


    /**
     * 删除
     *
     * @param key
     */
    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 设置过期时间
     * @param key
     * @param expiration 过期时间，单位毫秒
     */
    public void setExpire(String key,Long expiration){
        //设置缓存过期时间
        if (expiration != null && expiration > 0) {
            stringRedisTemplate.expire(key, expiration, TimeUnit.MILLISECONDS);
        }
    }

    private String toStr(Object obj){
        if (obj instanceof String){
            return (String) obj;
        } else {
            return JSON.toJSONString(obj);
        }
    }


}
