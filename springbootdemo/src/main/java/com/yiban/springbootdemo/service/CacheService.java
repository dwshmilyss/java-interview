package com.yiban.springbootdemo.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author david.duan
 * @packageName com.yiban.springbootdemo.service
 * @className CacheService
 * @date 2026/2/11
 * @description
 */
@Service
public class CacheService {
    // 使用注解：如果缓存没有，则执行方法体，结果自动存入缓存
    @Cacheable(cacheNames = "user_cache", key = "#id")
    public String getUserById(String id) {
        System.out.println("方法被调用，说明缓存未命中...");
        return "User_" + id;
    }
}
