package com.yiban.aop.xianliu;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedisLimitAnnotation {
    /*
    redis 中的key(存的是对应的接口，)
     */
    String key() default "";

    /*
    每个接口最多的访问次数
     */
    long permitsPerSecond() default 3;

    /*
    过期时间
     */
    long expire() default 30;

    /*
    温馨提示语
     */
    String msg() default "系统繁忙or你点击太快，请稍后再试";
}
