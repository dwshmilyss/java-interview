package com.yiban.springbootdemo.test.threadlocal;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalRemoveDemo {
    public static void main(String[] args) {
        MyData myData = new MyData();
        //模拟3个银行窗口
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 3, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        try {
            for (int i = 0; i < 10; i++) {//模拟10个用户过来办理业务
                int finalI = i;
                threadPoolExecutor.execute(() -> {
                    try {
                        int before = myData.integerThreadLocal.get();//先获取一下用户办理之前的状态
                        myData.add();
                        int after = myData.integerThreadLocal.get();//办理完业务之后再获取一下用户的状态
                        System.out.println(Thread.currentThread().getName() +"\t" + "受理业务，" +
                                "用户：" + finalI + "\t before ：" +before + "\t after" + after);
                    } finally {
                        myData.integerThreadLocal.remove();//在线程池中用完ThreadLocal一定要remove
                    }
                });
            }
        }finally {
            threadPoolExecutor.shutdown();
        }
    }
}

class MyData {
    //正常情况下，应该是一个用户来办理一次业务，办理之前的次数是0，办理之后是1，每个用户都应该是这样
    public ThreadLocal<Integer> integerThreadLocal = ThreadLocal.withInitial(() -> 0);//办理业务的次数

    public void add() {
        integerThreadLocal.set(integerThreadLocal.get() + 1);//模拟办理一次银行业务
    }
}
