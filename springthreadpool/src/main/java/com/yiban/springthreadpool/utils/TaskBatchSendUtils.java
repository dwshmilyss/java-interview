package com.yiban.springthreadpool.utils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * @author david.duan
 * @packageName com.yiban.springthreadpool.utils
 * @className TaskBatchSendUtils
 * @date 2025/11/3
 * @description
 */
public class TaskBatchSendUtils {
    public static <T> void send(List<T> taskList, Executor threadPool, Consumer<? super T> consumer) throws InterruptedException {
        if (taskList == null || taskList.isEmpty()) {//taskList.isEmpty() 等价于 taskList.size()==0
            return;
        }
        if (Objects.isNull(consumer)) {
            return;
        }
        CountDownLatch countDownLatch = new CountDownLatch(taskList.size());
        for (T t : taskList) {
            threadPool.execute(() -> {
                try {
                    consumer.accept(t);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }

    public static void disposeTask(String task) {
        System.out.println(String.format("【%s】disposeTask下发优惠卷成功",task));
    }

    public static void disposeTaskV1(String task) {
        System.out.println(String.format("【%s】disposeTask下发短信成功",task));
    }

    public static void disposeTaskV2(String task) {
        System.out.println(String.format("【%s】disposeTask下发邮件成功",task));
    }

    public static void disposeTaskV3(String task) {
        System.out.println(String.format("【%s】disposeTask下发二维码成功",task));
    }
}
