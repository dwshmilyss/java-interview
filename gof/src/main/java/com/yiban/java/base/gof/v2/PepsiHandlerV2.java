package com.yiban.java.base.gof.v2;

import org.springframework.stereotype.Component;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v2
 * @className PepsiHandlerV2
 * @date 2025/12/9
 * @description
 */
@Component
public class PepsiHandlerV2 implements HandlerStrategyFactory{
    @Override
    public void getCola(String promotion) {
        System.out.println("实现了策略+工厂的百世可乐");
        if ("618".equalsIgnoreCase(promotion)) {
            //618促销活动
        }
        if ("1111".equalsIgnoreCase(promotion)) {
            //双11促销活动
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Factory.register("Pepsi",this);
    }
}
