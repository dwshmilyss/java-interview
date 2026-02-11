package com.yiban.springbootdemo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author david.duan
 * @packageName com.yiban.springbootdemo.config
 * @className CacheConfig
 * @date 2026/2/11
 * @description
 */
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // 1. 定义基础缓存规格
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES));

        // 2. 关键：设置 CacheLoader (实现自动加载逻辑)
        // 当使用 cache.get(key) 且不存在时，会自动触发此逻辑
        cacheManager.setCacheLoader(key -> {
            return "Data_From_DB_For_" + key;
        });

        return cacheManager;
    }
}
