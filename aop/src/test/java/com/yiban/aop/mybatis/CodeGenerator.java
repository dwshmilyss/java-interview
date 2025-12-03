package com.yiban.aop.mybatis;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author david.duan
 * @packageName com.yiban.aop.mybatis
 * @className CodeGenerator
 * @date 2025/12/2
 * @description
 */
public class CodeGenerator {
    public static void main(String[] args) {

        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/test?serverTimezone=Asia/Shanghai",
                        "root", "120653")
                .globalConfig(builder -> {
                    builder.author("edz")
                            .outputDir("/Users/edz/sourceCode/java-interview/aop/src/main/java"); // 生成路径
                })
                .packageConfig(builder -> {
                    builder.parent("com.yiban.aop.redis_cache_component")
                            .moduleName("aop");
                })
                .strategyConfig(builder -> {
                    builder.addInclude("t_user");  // 想生成的表
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用 FreeMarker 模板引擎
                .execute();
    }
}
