package com.yiban.danmu;

import com.yiban.danmu.entities.Content;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

@SpringBootTest
class DanmuApplicationTests {

    @Resource
    RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        Set<ZSetOperations.TypedTuple<Object>> result =
                redisTemplate.opsForZSet().reverseRangeWithScores("room:100", 0, 4);

        for (ZSetOperations.TypedTuple<Object> tuple : result) {
            Content content = (Content) tuple.getValue();
            Double score = tuple.getScore();

            System.out.println(content.getContent());
            System.out.println(score);
        }
    }

}
