package com.yiban.aop.xianliu;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author david.duan
 * @packageName com.yiban.aop.xianliu
 * @className RedisLimitAspect
 * @date 2025/11/18
 * @description
 */
@Aspect
@Component
@Slf4j
public class RedisLimitAspect {
    Object returnVal = null;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> redisLuaScript;

    @PostConstruct
    public void init() {
        // Initialize the Lua script for Redis
        redisLuaScript = new DefaultRedisScript<>();
        redisLuaScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("rateLimit.lua")));
        redisLuaScript.setResultType(Long.class);
    }

    @Around("@annotation(com.yiban.aop.xianliu.RedisLimitAnnotation)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("---------环绕通知 111111");
        Object res = proceedingJoinPoint.proceed();
        MethodSignature signature = (MethodSignature)proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        //获取指定注解，如果拿到，则说明该方法需要限流
        RedisLimitAnnotation redisLimitAnnotation = method.getAnnotation(RedisLimitAnnotation.class);
        if (Objects.nonNull(redisLimitAnnotation)) { //等价于 redisLimitAnnotation != null
            //获取redis的key
            String key = redisLimitAnnotation.key();
            String className = method.getDeclaringClass().getName();
            String methodName = method.getName();

            String limitKey = key + "\t" + className + "\t" + methodName;
            log.info("limitKey = {}",limitKey);

            if (key == null) {
                throw new Exception("it is danger,limitKey can not be null");
            }
            long permisLimit = redisLimitAnnotation.permitsPerSecond();
            long expire = redisLimitAnnotation.expire();

            List<String> keyList = new ArrayList<>();
            keyList.add(key);

            long count = stringRedisTemplate.execute(
                    redisLuaScript,
                    keyList,
                    String.valueOf(permisLimit),
                    String.valueOf(expire));

            System.out.println("Access count: " + count + " for key: " + key + " with limit: " + permisLimit + " and expire: " + expire + " at time: " + System.currentTimeMillis() + "ms");
            if (count == 0) {//当次数用尽时，启动限流功能，并返回提示语
                log.info("限流成功,key = " + key);
                return redisLimitAnnotation.msg();
            }
        }
        System.out.println("---------环绕通知 22222");

        return res;
    }
}
