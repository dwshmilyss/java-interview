package com.yiban.java.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * @author david.duan
 * @packageName com.yiban.java.threadlocal
 * @className ThreadLocalDemoV3
 * @date 2025/10/28
 * @description
 */
@Slf4j
public class ThreadLocalDemoV3 {
    public static void main(String[] args) {
//        m1();
//        m2();
//        m3();
        m4();
    }

    /**
     * 如果main线程中修改了threadlocal的值，子线程要想再获取的话，就要祭出中级杀手锏：InheritableThreadLocal
     */
    private static void m4() {
        TransmittableThreadLocal<String> transmittableThreadLocal = new TransmittableThreadLocal<>();
        //这里创建线程数为1的线程池才能看到效果
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        //需要用TtlExecutors.getTtlExecutorService()把原来的线程池包装下
        singleThreadPool = TtlExecutors.getTtlExecutorService(singleThreadPool);

        //主线程写入值
        transmittableThreadLocal.set(currentThread().getName() + "-Java");
        log.info("major : {}", transmittableThreadLocal.get());
        System.out.println();
        //子线程第一次获取主线程的值
        singleThreadPool.submit(() -> {
            log.info("ThreadPool第一次获取major : {}", transmittableThreadLocal.get());
        });
        //休眠1秒
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //主线程修改值
        transmittableThreadLocal.set(currentThread().getName() + "-Vue");
        log.info("major : {}", transmittableThreadLocal.get());
        System.out.println();
        //子线程第二次获取主线程的值
        singleThreadPool.submit(() -> {
            log.info("ThreadPool第二次获取major : {}", transmittableThreadLocal.get());
        });
        singleThreadPool.shutdown();
    }

    /**
     * 但有些情况下是InheritableThreadLocal搞不定的
     * 例如后面main线程修该了ThreadLocal中的数据，这时候如果用的是线程池，由于线程池会复用线程，尤其是只有一个线程的池子，
     * 这时即使用InheritableThreadLocal，子线程也是获取不到的
     * m2()方法中的子线程之所以能获取到，是因为每次我们都是new了一个Thread，每个子线程都是新的
     */
    private static void m3() {
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        inheritableThreadLocal.set(currentThread().getName() + "-Java");
        log.info("major : {}", inheritableThreadLocal.get());
        System.out.println();

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        fixedThreadPool.submit(() -> {
            log.info("ThreadPool第一次获取major : {}", inheritableThreadLocal.get());
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println();
        System.out.println();

        //这里修改了main线程中的ThreadLocal数据
        inheritableThreadLocal.set(currentThread().getName() + "-Vue");
        log.info("major : {}", inheritableThreadLocal.get());
        System.out.println();
        System.out.println();

        fixedThreadPool.submit(() -> {
            log.info("ThreadPool第二次获取major : {}", inheritableThreadLocal.get());
        });

        fixedThreadPool.shutdown();
    }

    /**
     * 子线程想要获取主线程中ThreadLocal存储的数据，就要用InheritableThreadLocal。
     */
    private static void m2() {
        InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        inheritableThreadLocal.set(currentThread().getName() + "-Java");
        log.info("major : {}", inheritableThreadLocal.get());
        System.out.println();

        new Thread(() -> {
            log.info("thread1 : {}", inheritableThreadLocal.get());
        }, "thread1").start();

        new Thread(() -> {
            log.info("thread2 : {}", inheritableThreadLocal.get());
        }, "thread2").start();

        new Thread(() -> {
            log.info("thread3 : {}", inheritableThreadLocal.get());
        }, "thread3").start();
    }

    /**
     * main线程用ThreadLocal存储数据，然后创建多个子线程去获取ThreadLocal中的值，子线程是获取不到的。
     */
    private static void m1() {
        ThreadLocal<String> threadLocal = ThreadLocal.withInitial(() -> null);
        threadLocal.set(currentThread().getName() + "-Java");
        log.info("major : {}", threadLocal.get());
        System.out.println();

        new Thread(() -> {
            log.info("major : {}", threadLocal.get());//thread1线程是否能拿到主线程中ThreadLocal写入的值呢？
            threadLocal.set(currentThread().getName() + "-Vue");
            log.info("thread1 : {}", threadLocal.get());
        }, "thread1").start();

        System.out.println();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //又启动一个线程
        new Thread(() -> {
            try {
                log.info("major : {}", threadLocal.get());
                threadLocal.set(currentThread().getName() + "-Flink");
                log.info("thread2 : {}", threadLocal.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "thread2").start();

        System.out.println();

        //搞一个异步线程看看是否能拿到main中的ThreadLocal值
        CompletableFuture.supplyAsync(() -> {
            try {
                log.info("major : {}", threadLocal.get());
                threadLocal.set(currentThread().getName() + "-Spark");
                log.info("thread3 : {}", threadLocal.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
        //这里一定要暂停一下  不然异步线程可能还没有执行完成
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
