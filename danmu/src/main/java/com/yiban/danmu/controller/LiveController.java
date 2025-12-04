package com.yiban.danmu.controller;

import com.yiban.danmu.entities.Content;
import com.yiban.danmu.tools.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author david.duan
 * @packageName com.yiban.danmu.controller
 * @className LiveController
 * @date 2025/12/4
 * @description
 */
@RestController
@Slf4j
public class LiveController {
    @Resource
    RedisTemplate redisTemplate;

    /**
     * 每次进入直播间都获取最新的5条弹幕
     * @param roomId
     * @param userId
     * @return
     * http://localhost:8081/live/goRoom?roomId=100&userId=12
     */
    @GetMapping(value = "/live/goRoom")
    public List<Content> goRoom(Integer roomId,Integer userId) {
        List<Content> contentList = new ArrayList<>(100);
        String key = Constants.ROOM_KEY + roomId;
        //每次进入房间，都获取最新的前5条弹幕
        //对应redis命令： ZREVRANGE room:100 0 4 withscores
        Set<ZSetOperations.TypedTuple<Content>> range = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 4);
        for (ZSetOperations.TypedTuple<Content> tuple : range) {
            Content content = tuple.getValue();
            contentList.add(content);
            log.info("每次进入房间获取最新的前5条弹幕，content={}，score={}", tuple.getValue(), tuple.getScore().longValue());
        }
        //拼接一个user_room_time key写入redis,以便下次根据该时间戳获取最新的5条弹幕
        String userKey = Constants.ROOM_USER_TIME_KEY + userId;
        long now = System.currentTimeMillis() / 1000;
        redisTemplate.opsForValue().set(userKey, now);
        return contentList;
    }

    /**
     * 根据用户最后一次登录的时间戳获取最新的弹幕
     * @param roomId
     * @param userId
     * @return
     * http://localhost:8081/live/contentList?roomId=100&userId=12
     */
    @GetMapping(value = "/live/contentList")
    public List<Content> contentList(Integer roomId, Integer userId) {
        List<Content> contentList = new ArrayList<>(100);
        String key = Constants.ROOM_KEY + roomId;//根据直播间key拉取弹幕
        String userKey = Constants.ROOM_USER_TIME_KEY + userId;//根据用户key获取最后一次登录时间戳
        //获取userKey，拿到最后一次登录直播间的时间戳
        long lastLoginTimeStamp = Long.parseLong(redisTemplate.opsForValue().get(userKey).toString());
        long now = System.currentTimeMillis() / 1000;
        log.info("查找的时间范围:{} - {}", lastLoginTimeStamp, now);
        //根据最后一次登录的时间戳获取最新的弹幕
        // rangeByScoreWithScores() 等价于redis命令： ZRANGEBYSCORE room:100 1764860765 1764862940 withscores
        Set<ZSetOperations.TypedTuple<Content>> set = redisTemplate.opsForZSet().rangeByScoreWithScores(key, lastLoginTimeStamp, now);
        for (ZSetOperations.TypedTuple<Content> contentTypedTuple : set) {
            Content content = contentTypedTuple.getValue();
            contentList.add(content);
            log.info("根据时间范围获取最新的弹幕，content={}，score={}", contentTypedTuple.getValue(), contentTypedTuple.getScore().longValue());
        }
        //最后，把当前时间戳更新到redis
        redisTemplate.opsForValue().set(userKey, now);
        return contentList;
    }
}
