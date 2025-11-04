package com.yiban.springthreadpool;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootTest
class SpringthreadpoolApplicationTests {

    @Resource
    private ThreadPoolTaskExecutor threaPool;

    @Test
    void contextLoads() {
        System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.println("测试threadPool getCorePoolSize = " + threaPool.getCorePoolSize());
        System.out.println("测试threadPool getMaxPoolSize = " + threaPool.getMaxPoolSize());
        System.out.println("测试threadPool getQueueCapacity = " + threaPool.getQueueCapacity());
        System.out.println("测试threadPool getKeepAliveSeconds = " + threaPool.getKeepAliveSeconds());
    }

}
