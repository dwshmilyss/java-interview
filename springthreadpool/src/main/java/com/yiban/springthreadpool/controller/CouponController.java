package com.yiban.springthreadpool.controller;

import com.yiban.springthreadpool.service.CouponService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author david.duan
 * @packageName com.yiban.springthreadpool.controller
 * @className CouponController
 * @date 2025/11/3
 * @description
 */
@Slf4j
@RestController
public class CouponController {
    @Resource
    private CouponService couponService;

    @GetMapping(value = "/coupon/sendv1")
    public void sendV1() {
        couponService.batchTaskAction();
    }

    @GetMapping(value = "/coupon/sendv2")
    public void sendV2() {
        couponService.batchTaskActionV1();
    }
}
