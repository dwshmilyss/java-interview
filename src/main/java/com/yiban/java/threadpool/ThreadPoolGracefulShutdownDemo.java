package com.yiban.java.threadpool;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author david.duan
 * @packageName com.yiban.java
 * @className threadpool
 * @date 2025/10/28
 * @description shutdown方法中有这样的注释：“This method does not wait for previously submitted tasks to complete execution. Use awaitTermination to do that.”
 * 这句话的意思不是说 “shutdown 不会让已提交的任务执行完”，而是说 “shutdown 不会阻塞当前线程去等待执行完”。
 * shutdown的行为是：
 * •	shutdown() 只是发出“关闭请求”；
 * •	线程池中的任务仍会继续执行，直到全部完成；
 * •	但是 调用 shutdown() 的线程不会等待它们执行完；
 * •	如果你想“等待执行完”，就要用 awaitTermination()。
 */
public class ThreadPoolGracefulShutdownDemo {
    public static void main(String[] args) {
        //shutdown就是会拒绝新的任务，但是已经提交的任务会继续执行
//        shutdown_test();
        //shutdownNow就是会拒绝新的任务，同时会尝试停止已经提交的任务
//        shutdownNow_test();
        //awaitTermination
        shutdown_awaitTermination_test();
    }

    /**
     * task interrupted,task-1  因为线程池中只有一个线程，所以第一个提交的任务会被中断(因为提交的1号任务已经开始执行了)
     * waiting task : task-2,任务被拒绝 2~5是等待中的任务(因为在调用shutdownNow之前已经进入到阻塞队列中了)，会被拒绝
     * waiting task : task-3,任务被拒绝
     * waiting task : task-4,任务被拒绝
     * waiting task : task-5,任务被拒绝
     * 第6个任务被拒绝 6~10号任务直接被拒绝，因为是在shutdownNow之后提交的，根本进不到线程池中
     * 第7个任务被拒绝
     * 第8个任务被拒绝
     * 第9个任务被拒绝
     * 第10个任务被拒绝
     */
    private static void shutdownNow_test() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        for (int i = 1; i <= 10; i++) {
            try {
                fixedThreadPool.execute(new Task(i));
            } catch (Exception e) {
                System.out.println("第" + i + "个任务被拒绝");
            }
            if (i == 5) {
                List<Runnable> tasks = fixedThreadPool.shutdownNow();
                for (Runnable task : tasks) {
                    if (task instanceof Task) {
                        System.out.println("waiting task : " + ((Task) task).getName() + ",任务被拒绝");
                    }
                }
            }
        }

    }

    /**
     * 第6个任务及之后的任务都被拒绝了，但是1~5号任务会正常执行完毕
     */
    public static void shutdown_test() {
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        for (int i = 1; i <= 10; i++) {//提交10个任务
            System.out.println("第" + i + "个任务被提交");
            try {
                fixedThreadPool.execute(new Task(i));
                if (i == 5) {//第5个任务提交后，关闭线程池
                    fixedThreadPool.shutdown();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void shutdown_awaitTermination_test() {
        ExecutorService fixedThreadPool = Executors.newSingleThreadExecutor();
        for (int i = 1; i <= 10; i++) {//提交10个任务
            System.out.println("第" + i + "个任务被提交");
            try {
                fixedThreadPool.execute(new Task(i));
                if (i == 5) {//第5个任务提交后，关闭线程池
                    fixedThreadPool.shutdown();
                }
            } catch (Exception e) {
                System.out.println("rejected task : " + i + ",任务被拒绝");
            }
        }

        try {
            boolean isStoped = fixedThreadPool.awaitTermination(5, TimeUnit.SECONDS);
            System.out.println("isStoped = " + isStoped);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--------------------");
        System.out.println(Thread.currentThread().getName() + "\t" + "mission is over");
        System.out.println();
    }


    /**
     * 大型项目里面优雅的关闭线程池
     *
     * @param threadPool
     */
    public static void finalOK_shutdownAndAwaitTermination(ExecutorService threadPool) {
        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(120, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                    if (!threadPool.awaitTermination(120, TimeUnit.SECONDS)) {
                        System.err.println("Pool did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Task implements Runnable {
        @Getter
        String name;

        public Task(int i) {
            name = "task-" + i;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("sleep completed," + getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("task interrupted," + getName());
                return;
            }
            System.out.println(getName() + " finished");
            System.out.println();
        }
    }
}


