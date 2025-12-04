package com.yiban.aop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class AopApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testRedisTemplateClass() {
        System.out.println(redisTemplate.getClass());
        System.out.println(redisTemplate.getValueSerializer().getClass());
    }

}
