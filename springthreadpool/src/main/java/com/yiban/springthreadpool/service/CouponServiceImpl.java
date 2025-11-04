package com.yiban.springthreadpool.service;

import com.yiban.springthreadpool.utils.TaskBatchSendUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author david.duan
 * @packageName com.yiban.springthreadpool.service
 * @className CouponServiceImpl
 * @date 2025/11/3
 * @description
 */
@Service
public class CouponServiceImpl implements CouponService {
    //优惠卷数量
    public static final int COUPON_NUMBER = 50;

    @Resource
    private ThreadPoolTaskExecutor threadPool;

    @Override
    public void batchTaskAction() {
        //模拟50张优惠卷
        List<String> couponList = new ArrayList<>(COUPON_NUMBER);
        for (int i = 1; i <= COUPON_NUMBER; i++) {
            couponList.add("优惠券-" + i);
        }
        CountDownLatch countDownLatch = new CountDownLatch(COUPON_NUMBER);
        long startTime = System.currentTimeMillis();
        //开始发放优惠卷
        try {
            for (String coupon : couponList) {
                threadPool.execute(() -> {
                    try {
                        System.out.println(String.format("【%s】发放成功", coupon));
                    } finally {
                        //注意 countDown()只能在线程内部调用
                        countDownLatch.countDown();
                    }
                });
            }
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("任务处理完毕，耗时:" + (endTime - startTime) + " 毫秒");
    }

    @SneakyThrows
    @Override
    public void batchTaskActionV1() {
        List<String> couponList = new ArrayList<>(COUPON_NUMBER);
        for (int i = 1; i <= COUPON_NUMBER; i++) {
            couponList.add("优惠券-" + i);
        }

        long startTime = System.currentTimeMillis();
//        try {
//            TaskBatchSendUtils.send(couponList,threadPool,TaskBatchSendUtils::disposeTask);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        TaskBatchSendUtils.send(couponList,threadPool,TaskBatchSendUtils::disposeTask);
        long endTime = System.currentTimeMillis();
        System.out.println("任务处理完毕，耗时:" + (endTime - startTime) + " 毫秒");
    }
}
