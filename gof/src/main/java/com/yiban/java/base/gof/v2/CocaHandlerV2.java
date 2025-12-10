package com.yiban.java.base.gof.v2;

import org.springframework.stereotype.Component;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v2
 * @className CocaHandler
 * @date 2025/12/9
 * @description
 */
@Component
public class CocaHandlerV2 implements HandlerStrategyFactory{
    @Override
    public void getCola(String promotion) {
        System.out.println("实现了策略+工厂的可口可乐！！！");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Factory.register("Coca", this);
    }
}
