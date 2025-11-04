package com.yiban.springthreadpool.service;

import com.yiban.springthreadpool.entites.CustomerMixInfo;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author david.duan
 * @packageName com.yiban.springthreadpool.service
 * @className CostomerServiceImpl
 * @date 2025/11/4
 * @description
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private ThreadPoolTaskExecutor threadPool;

    @SneakyThrows
    @Override
    public CustomerMixInfo findCustomerByCompletableFuture() {
        long beginTime = System.currentTimeMillis();
        //串行调用三个微服务耗时=100+200+300=600ms
        CustomerMixInfo customerMixInfo = new CustomerMixInfo();
        customerMixInfo.setCid(100);
//        customerMixInfo.setCname(getCustomerName());
//        customerMixInfo.setScore(getScore());
//        customerMixInfo.setOrderInfo(getOrderInfo());
        // 使用CompletableFuture并行获取数据
        CompletableFuture<Void> futureCustomerName = CompletableFuture.runAsync(() ->{
            customerMixInfo.setCname(this.getCustomerName());
        },threadPool);

        CompletableFuture<Void> futureScore = CompletableFuture.runAsync(() -> {
            customerMixInfo.setScore(this.getScore());
        },threadPool);

        CompletableFuture<Void> futureOrderInfo = CompletableFuture.runAsync(() -> {
            customerMixInfo.setOrderInfo(this.getOrderInfo());
        },threadPool);
        //最后把三个future都执行完成，再返回结果。其耗时为三个微服务耗时的最大值，即300ms
        CompletableFuture.allOf(futureCustomerName, futureScore, futureOrderInfo).join();
        long endTime = System.currentTimeMillis();
        System.out.println("获取数据耗时:" + (endTime - beginTime));

        return customerMixInfo;
    }

    //模拟从三个不同的微服务中获取数据
    private String getCustomerName() {
        // 模拟从微服务1获取数据
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "张三";
    }

    private int getScore() {
        // 模拟从微服务1获取数据
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 100;
    }


    private String getOrderInfo() {
        // 模拟从微服务1获取数据
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "iphone17 pro max";
    }
}
