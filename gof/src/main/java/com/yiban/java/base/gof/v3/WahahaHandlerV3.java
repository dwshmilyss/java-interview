package com.yiban.java.base.gof.v3;

import org.springframework.stereotype.Component;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v3
 * @className WahahaHandlerV3
 * @date 2025/12/9
 * @description
 */
@Component
public class WahahaHandlerV3 extends AbstractColaHandler{
    @Override
    public void getCola(String promotion) {
        System.out.println("实现了 策略+工厂+模板方法 的娃哈哈");
    }

    @Override
    public String wahahaMethod() {
        return "在这里实现娃哈哈自己独有的逻辑";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FactoryV3.register("Wahaha", this);
    }
}
