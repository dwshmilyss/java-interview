package com.yiban.java.base.gof.v3;

import com.yiban.java.base.gof.v2.HandlerStrategyFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v3
 * @className FactoryV3
 * @date 2025/12/9
 * @description
 */
@Component
public class FactoryV3 {
    //缓存注册进来的实例工厂
    private static Map<String, AbstractColaHandler> strategyMap = new ConcurrentHashMap<>();

    public static AbstractColaHandler getStrategy(String param) {
        return strategyMap.get(param);
    }

    public static void register(String param, AbstractColaHandler abstractColaHandler) {
        System.out.println("param = " + param + "\t" + "abstractColaHandler = " + abstractColaHandler);
        if (param == null || null == abstractColaHandler) {
            return;
        }
        strategyMap.put(param, abstractColaHandler);
    }}
