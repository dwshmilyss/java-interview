package com.yiban.java.base.gof.v2;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v2
 * @interfaceName HandlerStrategyFactory
 * @date 2025/12/9
 * @description v2版本 策略 + 工厂 + InitializingBean
 */
public interface HandlerStrategyFactory extends InitializingBean {
    void getCola(String promotion);
}
