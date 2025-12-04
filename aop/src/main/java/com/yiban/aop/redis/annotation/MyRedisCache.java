package com.yiban.aop.redis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.METHOD)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface MyRedisCache {
    //key的前缀
    String keyPrefix();

    //SpringEL表达式，解析占位符对应的value
    String matchValue();
}
