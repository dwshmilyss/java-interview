package com.yiban.java.base.redisseckill.service;

import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    //小公司版，到这里基本能实现分布式锁解决秒杀超卖问题
    public String doSecKill(String userId, String prodId) {
        String stock = "seckill:stock:" + prodId;
        String lock = "lock:stock:" + prodId;
        String clientId = IdUtil.fastSimpleUUID();
        //在redis中创建一把锁, setnx(k,v) + expire k 10
        Boolean res = stringRedisTemplate.opsForValue().setIfAbsent(lock, clientId, 10, TimeUnit.SECONDS);
        if (!res) {
            return "error_code";
        }

        try {
            int stockCount = Integer.parseInt(String.valueOf(stringRedisTemplate.opsForValue().get(stock)));
            if (stockCount > 0) {
                stockCount = stockCount - 1;
                stringRedisTemplate.opsForValue().set(stock, stockCount);
                System.out.println("扣减成功，剩余库存：" + stockCount);
            } else {
                System.out.println("扣减失败，库存不足！");
            }
        }  finally {
            //防止自己加的锁被别的线程误删
            if (clientId.equalsIgnoreCase(String.valueOf(stringRedisTemplate.opsForValue().get(lock)))) {
                stringRedisTemplate.delete(lock);
            }
        }
        return "success";
    }

    @Resource
    private Redisson redisson;

    /**
     * redisson版
     * @param userId
     * @param prodId
     * @return
     */
    public String doSecKillV1(String userId, String prodId) {
        String stock = "seckill:stock:" + prodId;
        String lock = "lock:stock:" + prodId;

        RLock redissonLock = redisson.getLock(lock);//获取一把分布式锁
        redissonLock.lock();//加锁

        try {
            int stockCount = Integer.parseInt(String.valueOf(stringRedisTemplate.opsForValue().get(stock)));
            if (stockCount > 0) {
                stockCount = stockCount - 1;
                stringRedisTemplate.opsForValue().set(stock, stockCount);
                System.out.println("扣减成功，剩余库存：" + stockCount);
            } else {
                System.out.println("扣减失败，库存不足！");
            }
        }  finally {
          redissonLock.unlock();//释放锁
        }
        return "success";
    }
}
