package com.yiban.springthreadpool.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author david.duan
 * @packageName com.yiban.springthreadpool.config
 * @className ThreadPoolConfig
 * @date 2025/11/2
 * @description
 */
@Configuration
public class ThreadPoolConfig {
    //线程池配置
    @Resource
    private ThreadPoolProperties threadPoolProperties;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        threadPool.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        threadPool.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        threadPool.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());

        threadPool.setThreadNamePrefix("spring默认线程池-");
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池关闭时等待所有任务完成
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        threadPool.initialize();
        return threadPool;
    }
}
