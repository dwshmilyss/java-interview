package com.yiban.java.base.gof.v0;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v0
 * @className NoPatternDemo
 * @date 2025/12/9
 * @description 无任何设计模式
 * 有三种可乐,每个可乐都有自己的销售策略(不同的业务逻辑)
 */
public class NoPatternDemo {
    public static void main(String[] args) {
        getCola("coca",null);
    }

    public static void getCola(String name,String promotion) {
        if ("Coca".equalsIgnoreCase(name)) {
            System.out.println("这是可口可乐！");
            System.out.println("这是可口可乐！");
            System.out.println("这是可口可乐！");
            System.out.println("这是可口可乐！");
            System.out.println("这是可口可乐！");
        } else if ("Pepsi".equalsIgnoreCase(name)) {
            System.out.println("这是百世可乐");
            if ("618".equalsIgnoreCase(promotion)) {
                //618促销活动
            }
            if ("1111".equalsIgnoreCase(promotion)) {
                //双11促销活动
            }
        } else if ("Wahaha".equalsIgnoreCase(name)) {
            System.out.println("这是娃哈哈可乐");
        }
    }
}
