package com.yiban.java.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author david.duan
 * @packageName com.yiban.java.thread
 * @className TimerTaskDemo
 * @date 2026/2/3
 * @description
 *
 * 在 Java 中实现类似 Redisson 这种“定时、异步、且能自动续期（递归调度）”的逻辑，通常有三种主流方案：
 * 1. ScheduledExecutorService：Java 标准库自带，最简单、最常用。
 * 2. Netty HashedWheelTimer：Redisson 源码真正使用的方案，适合管理成千上万个超大规模的任务。
 * 3. CompletableFuture + 递归：强调异步回调链。
 * 我为你准备了一份包含这几种实现方式的代码示例，演示如何模拟“看门狗”续期的逻辑。
 */
public class TimerTaskDemo {
    // 1. 标准 JDK 调度器
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    // 模拟锁的状态：是否还在续期名单中
    private static final AtomicBoolean isLockHeld = new AtomicBoolean(true);
    // 模拟锁的剩余生存时间 (TTL)
    private static final AtomicInteger lockTTL = new AtomicInteger(30);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- 开始执行看门狗续期模拟 ---");

        // 启动模拟业务逻辑：25秒后释放锁
        new Thread(() -> {
            try {
                Thread.sleep(25000);
                isLockHeld.set(false);
                System.out.println("[业务] 业务执行完毕，准备释放锁...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 触发第一次续期调度
        renewExpiration();

        // 阻塞主线程观察日志
        Thread.sleep(40000);
        scheduler.shutdown();
    }

    /**
     * 核心续期逻辑：对应 Redisson 的 renewExpiration()
     */
    private static void renewExpiration() {
        // 如果锁已经不被持有，直接退出，停止“递归”
        if (!isLockHeld.get()) {
            System.out.println("[看门狗] 锁已释放，停止续期。");
            return;
        }

        // 对应 Redisson 的 internalLockLeaseTime / 3 (假设总时长30s，每10s续期一次)
        long delay = 10;

        System.out.println("[看门狗] 计划在 " + delay + " 秒后执行续期任务...");

        scheduler.schedule(() -> {
            // 1. 模拟异步执行 Redis 续期指令 (对应 renewExpirationAsync)
            CompletableFuture<Boolean> future = simulateRedisUpdateAsync();

            // 2. 异步回调处理 (对应 future.onComplete)
            future.thenAccept(success -> {
                if (success) {
                    System.out.println("[看门狗] 续期成功，当前 TTL 重置为 30s");
                    // 3. 递归调用自身，实现下一次调度
                    renewExpiration();
                } else {
                    System.out.println("[看门狗] 续期失败或不再需要续期。");
                }
            });

        }, delay, TimeUnit.SECONDS);
    }

    /**
     * 模拟异步更新 Redis 的操作
     */
    private static CompletableFuture<Boolean> simulateRedisUpdateAsync() {
        return CompletableFuture.supplyAsync(() -> {
            // 模拟网络开销
            try { Thread.sleep(500); } catch (InterruptedException e) {}

            if (isLockHeld.get()) {
                lockTTL.set(30); // 重置 TTL
                return true;
            }
            return false;
        });
    }
}
