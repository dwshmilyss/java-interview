package com.yiban.aop.redis.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author david.duan
 * @packageName com.yiban.aop.redis.config
 * @className JacksonConfig
 * @date 2025/12/3
 * @description 配置 Jackson2ObjectMapperBuilderCustomizer（全局生效）
 * 主要是为了处理 LocalDateTime 等 Java 8 时间类型，避免在 Redis 中存储时出现乱码问题(在redis中存储的时间是字符串，但是取出后变成数组了)
 */
@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.modules(new JavaTimeModule());
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }
}
