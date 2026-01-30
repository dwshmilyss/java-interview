package com.yiban.java.base.redisseckill.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.redisseckill.service
 * @className RedisSecKillService
 * @date 2026/1/30
 * @description
 */
@Service
public class RedisSecKillService {
    @Resource
    private RedisTemplate stringRedisTemplate;

    public Boolean doSecKill(String userId, String prodId) {
        String key = "seckill:stock:" + prodId;
        Long stock = stringRedisTemplate.opsForValue().decrement(key);
        if (stock != null && stock >= 0) {
            // 秒杀成功，处理订单逻辑
            System.out.println("User " + userId + " successfully purchased product " + prodId);
            return true;
        } else {
            // 秒杀失败，库存不足，恢复库存
            stringRedisTemplate.opsForValue().increment(key);
            System.out.println("User " + userId + " failed to purchase product " + prodId + " due to insufficient stock");
            return false;
        }
    }
}
