package com.yiban.aop.redis.aspect;

import com.yiban.aop.redis.annotation.MyRedisCache;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author david.duan
 * @packageName com.yiban.aop.redis.aspect
 * @className MyRedisCacheAspect
 * @date 2025/12/3
 * @description
 */
@Component
@Aspect
public class MyRedisCacheAspect {
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    //配置织入点
    @Pointcut("@annotation(com.yiban.aop.redis.annotation.MyRedisCache)")
    public void cachePointCut() {
    }

    @Around("cachePointCut()")
    public Object doCache(ProceedingJoinPoint proceedingJoinPoint) {
        Object returnVal = null;
        try {
            //1.获取带指定注解的方法名
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = methodSignature.getMethod();
            //2.获取方法上的注解标签 MyRedisCache
            MyRedisCache myRedisCacheAnnotation = method.getAnnotation(MyRedisCache.class);
            //3. 拿到注解后，获取注解中的参数进行处理
            String keyPrefix = myRedisCacheAnnotation.keyPrefix();
            String matchValueSpringEL = myRedisCacheAnnotation.matchValue();
            //4. 用SpringEL解析器解析matchValueSpringEL表达式，得到最终的key值
            SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
            Expression expression = spelExpressionParser.parseExpression(matchValueSpringEL); // #id
            StandardEvaluationContext context = new StandardEvaluationContext();
            //5. 获取方法里面的形参个数
            Object[] args = proceedingJoinPoint.getArgs();
            DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
            String[] parameterNames = discoverer.getParameterNames(method);
            for (int i = 0; i < parameterNames.length; i++) {
                System.out.println("形参名:" + parameterNames[i] + "\t" + "形参值:" + args[i].toString());
                context.setVariable(parameterNames[i], args[i].toString());
            }
            //6. 拼接最终的redis key
            String redisKey = keyPrefix + "_" + expression.getValue(context).toString();
            System.out.println("redisKey = " + redisKey);
            //7. 先去redis中查一下有没有缓存，如果有就返回缓存数据
            returnVal = redisTemplate.opsForValue().get(redisKey);
            if (returnVal != null) {
                System.out.println("========= redis中有缓存,从redis中直接获取数据...");
                return returnVal;
            }
            //8. 如果没有缓存，就执行目标方法，获取返回值
            returnVal = proceedingJoinPoint.proceed(); //执行目标方法中的代码逻辑(在这里其实就是去MySQL中查询)
            //9. 将返回值写入到redis中
            if (redisKey != null) {
                System.out.println("========= redis中没有，那就需要把从MySQL中查到的数据再写入到Redis中...... 补偿缓存: " + returnVal);
                redisTemplate.opsForValue().set(redisKey, returnVal);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return returnVal;
    }
}
