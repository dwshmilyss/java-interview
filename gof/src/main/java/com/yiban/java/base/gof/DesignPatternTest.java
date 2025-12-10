package com.yiban.java.base.gof;

import com.yiban.java.base.gof.v2.Factory;
import com.yiban.java.base.gof.v2.HandlerStrategyFactory;
import com.yiban.java.base.gof.v3.AbstractColaHandler;
import com.yiban.java.base.gof.v3.FactoryV3;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v2
 * @className StrategyAndFactoryTest
 * @date 2025/12/9
 * @description
 */
@SpringBootTest
public class DesignPatternTest {
    @Test
    public void testV2() {
        String param = "Pepsi";
        HandlerStrategyFactory invokeStrategy = Factory.getStrategy(param);
        invokeStrategy.getCola("1111");
    }

    @Test
    public void testV3() {
        String param = "Wahaha";
        AbstractColaHandler abstractColaHandler = FactoryV3.getStrategy(param);
        abstractColaHandler.getCola(null);
    }
}
