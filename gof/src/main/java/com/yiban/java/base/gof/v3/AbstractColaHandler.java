package com.yiban.java.base.gof.v3;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author david.duan
 * @packageName com.yiban.java.base.gof.v3
 * @className AbstractColaHandler
 * @date 2025/12/9
 * @description
 * 用抽象类代替接口，这样更灵活，因为某些子类不需要实现接口中定义的所有方法
 * 1.把子类都需要实现的方法用抽象方法定义
 * 2.把子类不一定需要实现的方法用普通方法定义，默认抛出异常(当然这里也不一定非要抛出异常，如果子类不覆写那就用父类的逻辑)
 * 3.把子类都需要实现的公共逻辑放在普通方法中，并且提供给子类覆写的机会
 */
public abstract class AbstractColaHandler implements InitializingBean {
    //每个子类都要实现的方法，就用抽象方法表示
    public abstract void getCola(String promotion);

    //模板方法1 按照业务名字 可口可乐自己单独实现
    public String cocaMethod() {
        throw new UnsupportedOperationException();
    }

    //模板方法2 按照业务名字 百世可可自己单独实现
    public String pepsiMethod() {
        throw new UnsupportedOperationException();
    }

    //模板方法3 按照业务名字 娃哈哈自己单独实现
    public String wahahaMethod() {
        throw new UnsupportedOperationException();
    }

    protected void initResource() {
        System.out.println("抽象父类已经帮子类统一实现，当然子类也可以覆写来实现自己的逻辑");
    }

}
