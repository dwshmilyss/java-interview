package com.yiban.springthreadpool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author david.duan
 * @packageName com.yiban.springthreadpool.config
 * @className ThreadPoolProperties
 * @date 2025/11/2
 * @description
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolProperties {
    // 线程池核心线程数
    private int corePoolSize;
    // 线程池最大线程数
    private int maxPoolSize;
    // 线程池中允许的空闲时间（单位：秒）
    private int keepAliveSeconds;
    // 线程池队列容量
    private int queueCapacity;
}
