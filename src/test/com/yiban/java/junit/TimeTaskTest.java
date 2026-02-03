package com.yiban.java.junit;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @author david.duan
 * @packageName java.com.yiban.java.junit
 * @className TimeTaskTest
 * @date 2026/2/3
 * @description
 */
public class TimeTaskTest {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    @Test
    public void testWithFuture() throws ExecutionException, InterruptedException {
        int delay = 1;

        // 方案 C: 获取 ScheduleFuture 并调用 get()
        // get() 方法会阻塞当前线程，直到定时任务真正被执行
        ScheduledFuture<?> future = scheduler.schedule(() -> {
            System.out.println("Future 模式任务执行: " + (int)(System.currentTimeMillis() / 1000));
        }, delay, TimeUnit.SECONDS);

        System.out.println("等待任务中...");
        future.get(); // 阻塞直到任务完成
        System.out.println("等待结束");
    }

    @Test
    public void testFixedRate() throws InterruptedException {
        int initialDelay = 0; // 首次执行的延迟时间
        int period = 2;       // 连续执行之间的时间间隔

        System.out.println("开始测试循环调度 (FixedRate), 当前时间: " + (int) (System.currentTimeMillis() / 1000));

        // scheduleAtFixedRate: 按固定频率执行
        // 无论上一次任务是否结束，到了间隔时间就会尝试启动下一个任务
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + " - [固定频率] 续期任务执行: " + (int) (System.currentTimeMillis() / 1000));
        }, initialDelay, period, TimeUnit.SECONDS);

        // 为了观察到多次打印，我们需要让主线程阻塞足够久
        // 这里等待 10 秒，理论上会看到 5-6 次打印
        TimeUnit.SECONDS.sleep(10);

        System.out.println("测试结束");
    }

    @Test
    public void testFixedDelay() throws InterruptedException {
        int initialDelay = 0;
        int delay = 2; // 任务结束到下一次任务开始的间隔

        System.out.println("开始测试循环调度 (FixedDelay), 当前时间: " + (int)(System.currentTimeMillis() / 1000));

        // scheduleWithFixedDelay: 按固定延迟执行
        // 会等待上一个任务完成后，再开始计时 delay 秒，然后执行下一个任务
        scheduler.scheduleWithFixedDelay(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " - [固定延迟] 任务开始: " + (int)(System.currentTimeMillis() / 1000));
                // 模拟任务执行耗时
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, initialDelay, delay, TimeUnit.SECONDS);

        TimeUnit.SECONDS.sleep(10);
    }
}
