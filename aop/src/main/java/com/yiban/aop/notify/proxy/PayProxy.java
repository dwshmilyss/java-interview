package com.yiban.aop.notify.proxy;

import com.yiban.aop.notify.service.PayService;

/**
 * @author david.duan
 * @packageName com.yiban.aop.proxy
 * @className PayProxy
 * @date 2025/11/12
 * @description
 */
public class PayProxy implements PayService {
    private PayService payService;

    public PayProxy(PayService payService) {
        this.payService = payService;
    }

    private void before() {
        System.out.println("打开微信。。。");
    }

    private void after() {
        System.out.println("关闭微信。。。");
    }


    @Override
    public void pay() {
        before();
        payService.pay();
        after();
    }
}
