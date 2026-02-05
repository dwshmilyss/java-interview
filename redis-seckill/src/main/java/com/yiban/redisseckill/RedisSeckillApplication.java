package com.yiban.redisseckill;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisSeckillApplication {

    @Bean
    public Redisson redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.0.1:6379").setDatabase(0);
        config.setLockWatchdogTimeout(10000);//设置watch dog的超时时间，单位是毫秒，默认30s
        return (Redisson) Redisson.create(config);
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisSeckillApplication.class, args);
    }

}
