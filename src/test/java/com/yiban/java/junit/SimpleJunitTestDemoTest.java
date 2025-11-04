package com.yiban.java.junit;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 注意：被测试类的方法除了private，其他的修饰符都能被测试
 * BeforeEach 和 AfterEach 的意思是每次调用方法的时候都会先执行BeforeEach，调用结束后再执行AfterEach
 * BeforeAll 和 AfterAll 只能修饰static方法，且在整个类中只会执行一次
 */
class SimpleJunitTestDemoTest {

    private SimpleJunitTestDemo simpleJunitTestDemo;

    private static StringBuilder sb;

    @BeforeEach
    void init() {
        System.out.println("init ....");
        simpleJunitTestDemo = new SimpleJunitTestDemo();
    }

    @AfterEach
    void destroy() {
        System.out.println("destroy ...");
        simpleJunitTestDemo = null;
    }

    @Test
    void add() {
        System.out.println("测试add方法...");
        int reVal = simpleJunitTestDemo.add(2, 2);
        assertEquals(4, reVal);
    }

    @Test
    void sub() {
        System.out.println("测试sub方法...");
        int reVal = simpleJunitTestDemo.sub(2, 2);
        assertEquals(0, reVal);
    }

    @Test
    void mut() {
        System.out.println("测试mut方法...");
        int reVal = simpleJunitTestDemo.mut(2, 3);
        assertEquals(6, reVal);
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll ....");
        sb = new StringBuilder("begin...");
        System.out.println(sb.toString());
    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll ...");
        sb = sb.append("end");
        System.out.println(sb.toString());
    }

}