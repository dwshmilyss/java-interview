package com.yiban.aop.interface_analyze;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author david.duan
 * @packageName com.yiban.aop.interface_analyze
 * @className MethodExporterAspect
 * @date 2025/11/17
 * @description
 */
@Aspect
@Slf4j
@Component
public class MethodExporterAspect {
    @Around("@annotation(com.yiban.aop.interface_analyze.MethodExporter)")
    public Object methodExporter(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object returnVal = null;
        long startTime = System.currentTimeMillis();
        System.out.println("------@Around AAA 环绕通知");
        returnVal = proceedingJoinPoint.proceed();//执行目标方法
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        //1 获取目标的方法名
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        //2 获取目标方法名上的注解标签
        MethodExporter annotation = method.getAnnotation(MethodExporter.class);
        if (annotation != null) {
            //3 获取方法上的形参信息
            StringBuffer jsonInputParams = new StringBuffer();
            Object[] args = proceedingJoinPoint.getArgs();
            DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(method);
            for (int i = 0; i < parameterNames.length; i++) {
                jsonInputParams.append(parameterNames[i] + "\t" + args[i] + ";").toString();
            }
            //4 将拼接后的字符串序列化
            String jsonRes = null;
            if (returnVal != null) {
                jsonRes = new ObjectMapper().writeValueAsString(returnVal);
            } else {
                jsonRes = null;
            }
            log.info("\n方法分析上报：" +
                    "\n类名方法名：" + proceedingJoinPoint.getTarget().getClass().getName() + "." + proceedingJoinPoint.getSignature().getName() + "()" +
                    "\n执行耗时：" + costTime + " 毫秒" +
                    "\n输入参数：" + jsonInputParams +
                    "\n返回结果：" + jsonRes +
                    "\n over");
        }
        System.out.println("------@Around BBB 环绕通知");
        return returnVal;
    }
}
