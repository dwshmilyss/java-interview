package com.yiban.java.base.gof.v1;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v1
 * @className PepsiHandler
 * @date 2025/12/9
 * @description
 */
public class PepsiHandlerV1 implements HandlerStrategy{
    @Override
    public void getCola(String promotion) {
        System.out.println("实现了策略模式的百世可乐");
        if ("618".equalsIgnoreCase(promotion)) {
            //618促销活动
        }
        if ("1111".equalsIgnoreCase(promotion)) {
            //双11促销活动
        }
    }
}
