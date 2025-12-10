package com.yiban.java.base.gof.v3;

import org.springframework.stereotype.Component;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v3
 * @className CocaHandlerV3
 * @date 2025/12/9
 * @description
 */
@Component
public class CocaHandlerV3 extends AbstractColaHandler{

    @Override
    public void afterPropertiesSet() throws Exception {
        FactoryV3.register("Coca", this);
    }

    @Override
    public void getCola(String promotion) {
        System.out.println("实现了 策略+工厂+模板方法 的可口可乐！！！");
    }

    @Override
    public String cocaMethod() {
        return "在这里实现可口可乐自己独有的逻辑";
    }
}
