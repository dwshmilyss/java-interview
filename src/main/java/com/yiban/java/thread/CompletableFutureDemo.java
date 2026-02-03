package com.yiban.java.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author david.duan
 * @packageName com.yiban.java.thread
 * @className CompletableFutureDemo
 * @date 2026/2/2
 * @description
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 创建异步任务
        // supplyAsync: 有返回值；runAsync: 无返回值
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            simulateDelay(1);
            return "Task Result";
        });

        // 2. 结果处理（转换）
        // thenApply: 转换结果，接收上一个结果，返回新结果
        CompletableFuture<Integer> lengthFuture = future.thenApply(s -> {
            System.out.println("Processing result: " + s);
            return s.length();
        });

        // 3. 结果消费
        // thenAccept: 消费结果，无返回值
        lengthFuture.thenAccept(len -> System.out.println("Result length is: " + len));

        // 4. 任务编排（组合）
        // thenCompose: 组合两个有依赖关系的异步任务（类似于 flatMap）
        CompletableFuture<String> chainedFuture = future.thenCompose(s ->
                CompletableFuture.supplyAsync(() -> s + " + Chained Task")
        );

        // thenCombine: 合并两个互不依赖的任务，并在两个都完成后处理结果
        CompletableFuture<String> combinedFuture = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCombine(CompletableFuture.supplyAsync(() -> "World"), (s1, s2) -> s1 + " " + s2);

        // 5. 任务并行（等待）
        // allOf: 等待所有任务完成
        // anyOf: 任何一个任务完成即可
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(future, combinedFuture);

        // 6. 异常处理
        // exceptionally: 处理异常并返回默认值
        // handle: 无论成功还是异常都会执行，可处理结果或异常
        CompletableFuture<String> exceptionFuture = CompletableFuture.supplyAsync(() -> {
            if (true) throw new RuntimeException("Error occurred!");
            return "Success";
        }).exceptionally(ex -> {
            System.err.println("Exception handled: " + ex.getMessage());
            return "Default Value";
        });

        // 阻塞获取结果用于演示（生产环境下建议使用回调）
        System.out.println("Combined: " + combinedFuture.get());
        System.out.println("Exception result: " + exceptionFuture.get());

        simulateDelay(2); // 保持主线程存活
    }

    private static void simulateDelay(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
