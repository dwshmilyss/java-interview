package com.yiban.java.threadlocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.currentThread;

/**
 * @author david.duan
 * @packageName com.yiban.java.threadlocal
 * @className ThreadLocalDemoV2
 * @date 2025/10/28
 * @description
 */
public class ThreadLocalDemoV2 {
    public static void main(String[] args) {
        MyData myData = new MyData();
        //模拟一个银行有3个受理窗口
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

        try {
            //10个顾客(请求线程)，池子中有3个线程来处理(相当于3个受理窗口)
            for (int i = 1; i <= 10; i++) {
                int finalI = i;
                fixedThreadPool.submit(() -> {
                    try {
                        int beforeInt = myData.threadLocalField.get();//开始办理业务之前啥都没有
                        myData.add();
                        int afterInt = myData.threadLocalField.get();//相当于办完业务有个回执单
                        System.out.println(currentThread().getName() + "\t" + "工作窗口\t" + "受理第" + finalI +"个顾客业务\t" + "Before: " + beforeInt + "\t" + "After: " + afterInt);
                    } finally {
                        myData.threadLocalField.remove();//如果不加remove，线程复用时，ThreadLocal的值会一直存在。就会导致同一个窗口的值被上一个顾客影响，比如第二个顾客会拿到第一个顾客的回执单
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fixedThreadPool.shutdown();
        }
    }
}

class MyData {
    ThreadLocal<Integer> threadLocalField = ThreadLocal.withInitial(() -> 0);

    public void add() {//模拟银行办理业务
        threadLocalField.set(threadLocalField.get() + 1);
    }
}
