package com.yiban.aop.notify.controller;

import com.yiban.aop.notify.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author david.duan
 * @packageName com.yiban.aop
 * @className PayController
 * @date 2025/11/13
 * @description
 */
@RestController
public class PayController {
    @Autowired
    private PayService payService;

    @GetMapping("/pay/aop")
    public String pay() {
        System.out.println("Spring version:" + SpringVersion.getVersion() + "\t" + "Springboot version:" + SpringBootVersion.getVersion());
        payService.pay();
        return UUID.randomUUID().toString();
    }
}
