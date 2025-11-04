package com.yiban.java.base;

/**
 * @author david.duan
 * @packageName com.yiban.java.base
 * @className StringDemo
 * @date 2025/10/10
 * @description
 */
public class StringDemo {
    public static void main(String[] args) {
//        testIntern();
        /*
        证明无论是成员变量还是类变量，只要是字符串字面量，都会进入字符串常量池
         */
        System.out.println(ClassA.s2 == ClassB.s2);
        System.out.println(new ClassA().s1 == new ClassB().s1);
    }

    public static void testIntern() {
        String s1 = new String("abc");
        String s2 = new String("abc").intern();
        String s3 = "abc";
        System.out.println(s1 == s2);
        System.out.println(s2 == s3);
    }

    static class ClassA{
        String s1 = "abc";
        public static String s2 = "hello";
    }

    static class ClassB {
        public static String s2 = "hello";
        String s1 = "abc";
    }
}
