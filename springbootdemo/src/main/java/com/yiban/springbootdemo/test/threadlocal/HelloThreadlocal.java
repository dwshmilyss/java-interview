package com.yiban.springbootdemo.test.threadlocal;

import lombok.Getter;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HelloThreadlocal {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        SU7 su7 = new SU7();
        CountDownLatch countDownLatch = new CountDownLatch(3);//3个销售来卖车，卖完之后分别统计各个销售的销售量和总的销售量
        for (int i = 1; i <= 3; i++) {
            new Thread(() -> {
                try {
                    for (int j = 1; j <= new Random().nextInt(3) + 1; j++) {// +1是保证每个销售至少卖出一辆
                        su7.saleTotal();//所有销售员销售的车数累加
                        su7.salePersonal();//每个销售员单独统计其销售数量
                    }
                    System.out.println(Thread.currentThread().getName() + "\t" + "销售数为：" + su7.salePersonalThreadLocal.get());
                } finally {
                    countDownLatch.countDown();
                    su7.salePersonalThreadLocal.remove();//按照阿里的java规范，用完threadlocal必须remove，否则容易造成业务混乱和内存泄漏，尤其是在线程池的场景下。
                }
            }, String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t" + "销售总数为：" + su7.getSaleTotal());
    }
}

class SU7 {
    @Getter
    private int saleTotal;//总的销售数量

    public synchronized void saleTotal() {
        saleTotal = saleTotal + 1;
    }

    public ThreadLocal<Integer> salePersonalThreadLocal = ThreadLocal.withInitial(() -> 0);

    public void salePersonal() {
        salePersonalThreadLocal.set(salePersonalThreadLocal.get() + 1);
    }
}
