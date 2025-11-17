package com.yiban.aop.notify.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author david.duan
 * @packageName com.yiban.aop.service
 * @className PayServiceImpl
 * @date 2025/11/12
 * @description
 */
@Service
public class PayServiceImpl implements PayService {
    @Override
    public void pay() {

        System.out.println("PayServiceImpl 微信支付业务逻辑：" + UUID.randomUUID().toString());
//        int a = 10/0;
    }
}
