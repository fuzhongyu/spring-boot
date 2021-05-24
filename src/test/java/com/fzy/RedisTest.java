package com.fzy;

import com.fzy.core.service.common.StringRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author: fucai
 * @Date: 2019-01-29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisService redisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testIncreace() {
        redisTemplate.opsForValue().increment("a", 1);
        redisTemplate.opsForValue().increment("b", 1);
        redisTemplate.opsForValue().increment("a", 1);
    }

    @Test
    public void setHash() {
        Map<String,String> map = new HashMap<>();
        map.put("test","test");
        redisService.addHash("aaa", "bb",map);
    }

    @Test
    public void setList(){
        List<Object> list = new ArrayList<>();
        list.add("ite1");
        list.add("ite2");
        redisService.addRightList("test_list",list,10000L);
        redisService.addRightList("test_list","item1");
        redisService.addRightList("test_list","item2");
        List<String> result=redisService.getListAll("test_list");
        System.out.println(Arrays.toString(result.toArray()));
        System.out.println(redisService.getIndexList("test_list",1));
    }

    @Test
    public void setSet(){
        Set<Object> set = new HashSet<>();
        set.add("ite1");
        set.add("ite2");
        redisService.addSet("test_set",set,10000L);
        redisService.addSet("test_set","item1");
        redisService.addSet("test_set","item2");
        Set<String> result = redisService.getSet("test_set");
        System.out.println(Arrays.toString(result.toArray()));
        System.out.println(redisService.isInSet("test_set","item1"));
    }

    @Test
    public void setZSet(){
        redisService.addZSet("test_zset","item1",1);
        redisService.addZSet("test_zset","item2",5);
        redisService.addZSet("test_zset","item3",3);
        Set<String> result = redisService.getZSet("test_zset",0,-1);
        Set<String> result1 = redisService.getZSetByScores("test_zset",0,5);
        System.out.println(Arrays.toString(result.toArray()));
        System.out.println(Arrays.toString(result1.toArray()));
    }

}
