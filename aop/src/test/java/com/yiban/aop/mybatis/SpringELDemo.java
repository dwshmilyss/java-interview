package com.yiban.aop.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author david.duan
 * @packageName com.yiban.aop.mybatis
 * @className SpringELDemo
 * @date 2025/12/3
 * @description
 */
@Slf4j
public class SpringELDemo {
    public static void main(String[] args) {
        //1.log占位符
        log.info("hello {}","dww");

        //2. string.format
        String str = String.format("hello %S", "dww");
        System.out.println("str = " + str);
        System.out.println();

        //3. String.format with multiple placeholders
        String str1 = String.format("hello %s, your age is %d", "dww", 25);
        System.out.println("str1 = " + str1);
        System.out.println();

        //4. String.format with different types of placeholders
        String str2 = String.format("hello %s, your height is %.2f meters", "dww", 1.75);
        System.out.println("str2 = " + str2);
        System.out.println();

        //5. String.format with date
        String str3 = String.format("hello %s, today's date is %tF", "dww", java.util.Calendar.getInstance());
        System.out.println("str3 = " + str3);
        System.out.println();

        //6. String.format with time
        String str4 = String.format("hello %s, current time is %tT", "dww", java.util.Calendar.getInstance());
        System.out.println("str4 = " + str4);
        System.out.println();

        //7. spring el expression in log statement
        String name = "#userid";
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(name);
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("userid", "dww");
        String res1 = expression.getValue(context, String.class);
        System.out.println("res1 = " + res1);
    }
}
