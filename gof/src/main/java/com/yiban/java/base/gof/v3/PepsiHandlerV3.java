package com.yiban.java.base.gof.v3;

import org.springframework.stereotype.Component;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v3
 * @className PepsiHandlerV3
 * @date 2025/12/9
 * @description
 */
@Component
public class PepsiHandlerV3 extends AbstractColaHandler{
    @Override
    public void getCola(String promotion) {
        System.out.println("实现了 策略+工厂+模板方法 的百事可乐！！！");
    }

    @Override
    public String pepsiMethod() {
        return "在这里实现百事可乐自己独有的逻辑";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FactoryV3.register("Pepsi", this);
    }
}
