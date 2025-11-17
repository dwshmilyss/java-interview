package com.yiban.aop.interface_analyze;

/**
 * @author david.duan
 * @packageName com.yiban.aop.interface_analyze
 * @className ClientTest
 * @date 2025/11/17
 * @description
 */
public class ClientTest {
    public int mul(int x,int y) {
        return x * y;
    }

    public int add(int x, int y) {
        return x + y;
    }

    public void thank(int x, int y) {
        System.out.println("thx help me test bug");
    }
}
