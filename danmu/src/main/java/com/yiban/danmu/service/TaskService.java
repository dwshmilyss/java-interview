package com.yiban.danmu.service;

import com.yiban.danmu.entities.Content;
import com.yiban.danmu.tools.Constants;
import com.yiban.utils.IdUtils;
import com.yiban.utils.RandomStringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author david.duan
 * @packageName com.yiban.danmu.service
 * @className TaskService
 * @date 2025/12/4
 * @description
 */
@Service
@Slf4j
public class TaskService {
    @Resource
    private RedisTemplate redisTemplate;

    //模拟生成直播弹幕数据
    @PostConstruct
    public void init() {
        log.info("======== 开始直播.....");
        System.out.println();
        //1. 启动一个线程，模拟用户发弹幕
        new Thread(() -> {
            try {
                AtomicInteger atomicInteger = new AtomicInteger();
                while (true) {
                    if (atomicInteger.get() == 100) {
                        break;
                    }
                    String key = Constants.ROOM_KEY + 100;
                    Random random = new Random();
                    for (int i = 1; i <= 5; i++) {
                        //1.1 构建弹幕对象
                        Content content = new Content();
                        content.setId(IdUtils.createSnowflake().nextId());//这里用雪花算法生成分布式唯一ID
                        content.setUserId(random.nextInt(1000) + 1);
                        int temp = random.nextInt(100) + 1;
                        content.setContent("发表言论:" + temp + "\t" + RandomStringUtils.generateRandomString(temp));
                        //1.2 拿到当前时间戳
                        long time = System.currentTimeMillis() / 1000;
                        //1.3 模拟redis zadd命令插入数据
                        /*
                         zadd score1 member1 [score2 member2] ...
                         zadd 向有序集合zset中插入数据，或者更新已有的成员的分数(zset是按score排序的)
                         这里的time就是score 也就是时间戳，member就是弹幕内容对象
                         */
                        redisTemplate.opsForZSet().add(key, content, time);
                        log.info("模拟直播间房间号100发送弹幕数据:{}",content);
                    }
                    //TODO 在分布式系统中，建议用xxl-job来实现定时
                    try {
                        TimeUnit.SECONDS.sleep(5);//暂停5s发送下一批的5条弹幕
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //每5s生成5条数据，然后atomicInteger+1，直到100退出循环
                    atomicInteger.getAndIncrement();
                    System.out.println("============= 间隔5s生成一批弹幕数据...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "init_live_data").start();
    }
}
