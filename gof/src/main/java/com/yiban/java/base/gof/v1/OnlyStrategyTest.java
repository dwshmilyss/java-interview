package com.yiban.java.base.gof.v1;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v1
 * @className OnlyStrategyTest
 * @date 2025/12/9
 * @description 只用了策略模式来优化
 */
public class OnlyStrategyTest {
    public static void main(String[] args) {
        getCola("coca","1111");
    }

    /**
     * 每个分支中的业务逻辑都被分工拿走了，放在各自的处理类中
     * @param name
     * @param promotion
     */
    public static void getCola(String name,String promotion) {
        if ("Coca".equalsIgnoreCase(name)) {
            new CocaHandlerV1().getCola(promotion);
        } else if ("Pepsi".equalsIgnoreCase(name)) {
            new PepsiHandlerV1().getCola(promotion);
        } else if ("Wahaha".equalsIgnoreCase(name)) {
            new WahahaHandlerV1().getCola(promotion);
        }
    }
}
