package com.yiban.java.base.gof.v1;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v1
 * @className WahahaHandler
 * @date 2025/12/9
 * @description
 */
public class WahahaHandlerV1 implements HandlerStrategy{
    @Override
    public void getCola(String promotion) {
        System.out.println("实现了策略模式的娃哈哈可乐");
    }
}
