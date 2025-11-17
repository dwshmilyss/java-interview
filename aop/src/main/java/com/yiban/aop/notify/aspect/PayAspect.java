package com.yiban.aop.notify.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author david.duan
 * @packageName com.yiban.aop.proxy
 * @className PayAspect
 * @date 2025/11/12
 * @description
 */
@Aspect
@Component
public class PayAspect {
    @Before("execution(public void com.yiban.aop.notify.service.PayServiceImpl.pay(..))")
    public void beforeNotify() {
        System.out.println("@Before 前置通知-------");
    }

    @After("execution(public void com.yiban.aop.notify.service.PayServiceImpl.pay(..))")
    public void afterNotify() {
        System.out.println("@After 后置通知-------");
    }

    @AfterReturning("execution(public void com.yiban.aop.notify.service.PayServiceImpl.pay(..))")
    public void afterReturnNotify() {
        System.out.println("@AfterReturning 返回通知-------");
    }

    @AfterThrowing("execution(public void com.yiban.aop.notify.service.PayServiceImpl.pay(..))")
    public void afterThrowNotify() {
        System.out.println("@AfterThrowing 异常通知-------");
    }

    @Around("execution(public void com.yiban.aop.notify.service.PayServiceImpl.pay(..))")
    public Object afterAroundNotify(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object returnVal = null;
        System.out.println("@Around 环绕通知AAA-------");
        returnVal = proceedingJoinPoint.proceed();
        System.out.println("@Around 环绕通知BBB-------");

        return returnVal;
    }
}

