package com.yiban.aop.xianliu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author david.duan
 * @packageName com.yiban.aop.xianliu
 * @className RedisLimitController
 * @date 2025/11/18
 * @description
 */
@Slf4j
@RestController
public class RedisLimitController {
    /**
     * 限制当前接口的访问频率，防止恶意请求，10秒钟内(窗口期)最多访问3次，超过限制返回提示信息
     * @return
     */
    //http://localhost:8081/redis/limit/test
    @RedisLimitAnnotation(key = "redisLimit",expire = 10,permitsPerSecond = 3,msg = "当前访问人数较多，请稍后再试")
    @GetMapping("/redis/limit/test")
    public String redisLimit() {
        return "正常业务返回，订单流水：" + UUID.randomUUID().toString();
    }

    //http://localhost:8081/redis/limit/m1
    @RedisLimitAnnotation(key = "redisLimitM1")
    @GetMapping("redis/limit/m1")
    public void m1() {

    }
}
