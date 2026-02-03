package com.yiban.utils;

import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

/**
 * @author david.duan
 * @packageName com.yiban.utils
 * @className Test
 * @date 2026/2/2
 * @description
 */
public class Test {
    @org.junit.jupiter.api.Test
    public void test() {
        Map<String,String> map = new HashMap<>();
        String res = map.putIfAbsent("a", "b");
        System.out.println(res);
        res = map.putIfAbsent("a", "c");
        System.out.println(res);
    }
}
