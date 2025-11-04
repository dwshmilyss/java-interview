package com.yiban.java.threadlocal;

import lombok.Getter;

import java.util.Random;
import java.util.concurrent.*;

import static java.lang.Thread.currentThread;

/**
 * @author david.duan
 * @packageName com.yiban.java.threadlocal
 * @className HelloThreadLocalDemo
 * @date 2025/10/27
 * @description
 */
public class HelloThreadLocalDemo {
    public static void main(String[] args) throws InterruptedException {
        SU7 su7 = new SU7();
        CountDownLatch countDownLatch = new CountDownLatch(3);
        //定义三个销售开始卖车
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 3, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        try {
            for (int i = 0; i < 3; i++) {
                new Thread(() ->{
                    try {
                        for (int j = 1; j <= new Random().nextInt(3) + 1; j++) {//保证每个销售至少卖出一辆
                            su7.saleTotal();
                            su7.salePersonal();
                        }
                        System.out.println(currentThread().getName() + "\t" + "个人卖出：" + su7.getSalePersonal().get() + " 辆车");
                    } finally {
                        countDownLatch.countDown();
                    }
                },String.valueOf(i)).start();
            }
            countDownLatch.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "\t" + "一共卖出：" + su7.getSaleTotal() + " 辆车");
    }
}

class SU7 {
    @Getter
    private int saleTotal;

    @Getter
    private ThreadLocal<Integer> salePersonal = ThreadLocal.withInitial(() -> 0);

    public synchronized void saleTotal() {
        saleTotal++;
    }

    public void salePersonal() {
        salePersonal.set(salePersonal.get() + 1);
    }
}
