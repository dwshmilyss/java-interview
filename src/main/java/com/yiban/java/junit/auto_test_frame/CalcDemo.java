package com.yiban.java.junit.auto_test_frame;

/**
 * @author david.duan
 * @packageName com.yiban.java.junit.auto_test_frame
 * @className CalcDemo
 * @date 2025/10/22
 * @description
 */
public class CalcDemo {
    @MyAutoFrameTest
    public int mul(int a, int b) {
        return a * b;
    }

    @MyAutoFrameTest
    public int div(int a, int b) {
        return a / b;
    }

}
