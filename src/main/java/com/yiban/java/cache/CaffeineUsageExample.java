package com.yiban.java.cache;

import com.github.benmanes.caffeine.cache.*;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author david.duan
 * @packageName com.yiban.java.cache
 * @className CaffeineUsageExample
 * @date 2026/2/11
 * @description
 */
public class CaffeineUsageExample {
    public static void main(String[] args) {
        // 1. 同步加载缓存示例 (LoadingCache)
        LoadingCache<String, String> syncCache = Caffeine.newBuilder()
                // 设置初始容量
                .initialCapacity(100)
                // 设置最大条目数（超过后按 Window TinyLFU 算法淘汰）
                .maximumSize(10_000)
                // 设置写后过期时间（5分钟未写入则失效）
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 设置访问后过期时间（2分钟未访问则失效）
                .expireAfterAccess(2, TimeUnit.MINUTES)
                // 开启统计功能
                .recordStats()
                // 设置移除监听器
                .removalListener((String key, String value, RemovalCause cause) ->
                        System.out.printf("Key %s was removed (%s)%n", key, cause))
                // 构建时指定自动加载逻辑
                .build(key -> "Data_From_DB_" + key);

        // 使用：如果 key 不存在，会自动触发 build 中定义的加载逻辑
        String value = syncCache.get("user_123");
        System.out.println("Sync Get: " + value);


        // 2. 异步加载缓存示例 (AsyncLoadingCache)
        AsyncLoadingCache<String, String> asyncCache = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .buildAsync(key -> {
                    // 模拟耗时操作
                    Thread.sleep(1000);
                    return "Async_Result_" + key;
                });

        // 使用：返回的是 CompletableFuture
        CompletableFuture<String> future = asyncCache.get("order_456");
        future.thenAccept(v -> System.out.println("Async Get: " + v));


        // 3. 手动填充示例 (Manual Cache)
        Cache<String, String> manualCache = Caffeine.newBuilder()
                .maximumSize(100)
                .build();

        // 放入数据
        manualCache.put("key1", "value1");

        // 获取数据，如果不存在则使用 lambda 计算并存入
        String val = manualCache.get("key2", k -> "computed_value");
        System.out.println("Manual Get: " + val);


        // 4. 基于权重的过期策略 (Weigher)
        // 适用于缓存对象大小不一的情况（例如缓存字节数组）
        Cache<String, byte[]> weightCache = Caffeine.newBuilder()
                .maximumWeight(1024 * 1024) // 最大权重 1MB
                .weigher((String key, byte[] bytes) -> bytes.length)
                .build();


        // 5. 打印统计信息 (Stats)
        // syncCache.stats() 提供了命中率、加载时间等详细数据
        System.out.println("Cache Stats: " + syncCache.stats().toString());
    }
}
