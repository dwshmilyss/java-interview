package com.yiban.redisseckill;

import com.yiban.redisseckill.service.RedisSecKillService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class RedisSeckillApplicationTests {

    @Resource
    private RedisSecKillService redisSecKillService;

    @Test
    void contextLoads() {
//        redisSecKillService.doSecKill("dww", "100");
    }

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Test
    void testRedis() {
        System.out.println(stringRedisTemplate.opsForValue().get("seckill:stock:100"));
        System.out.println("-----------------");
        System.out.println(stringRedisTemplate.opsForValue().get("mykey"));
        System.out.println("-----------------");
        System.out.println(stringRedisTemplate.opsForValue().getBit("bit1", 7));
        System.out.println(stringRedisTemplate.opsForValue().getBit("bit1", 8));
    }

}
