package com.yiban.aop.notify;

import com.yiban.aop.notify.proxy.PayProxy;
import com.yiban.aop.notify.service.PayService;
import com.yiban.aop.notify.service.PayServiceImpl;

/**
 * @author david.duan
 * @packageName com.yiban.aop
 * @className Client
 * @date 2025/11/12
 * @description
 */
public class ClientTest {
    public static void main(String[] args) {
//        testPayServiceV1();
//        testPayServiceV2();
        testPayServiceV3();
    }

    private static void testPayServiceV1() {
        PayService payService = new PayServiceImpl();
        payService.pay();
    }

    private static void testPayServiceV2() {
        PayProxy payProxy = new PayProxy(new PayServiceImpl());
        payProxy.pay();
    }

    private static void testPayServiceV3() {
        //跟V1比就是加入了aop的各种通知，一开始的V1是没有加aop通知的
        PayService payService = new PayServiceImpl();
        payService.pay();
    }
}
