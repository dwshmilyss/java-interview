package com.yiban.java.base.gof.v2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v2
 * @className Factory
 * @date 2025/12/9
 * @description
 */
public class Factory {
    private static Map<String, HandlerStrategyFactory> strategyMap = new ConcurrentHashMap<>();

    public static HandlerStrategyFactory getStrategy(String param) {
        return strategyMap.get(param);
    }

    public static void register(String param, HandlerStrategyFactory handlerStrategyFactory) {
        System.out.println("param = " + param + "\t" + "handlerStrategyFactory = " + handlerStrategyFactory);
        if (param == null || null == handlerStrategyFactory) {
            return;
        }
        strategyMap.put(param, handlerStrategyFactory);
    }
}
