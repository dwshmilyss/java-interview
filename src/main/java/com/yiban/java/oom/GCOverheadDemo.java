package com.yiban.java.oom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author david.duan
 * @packageName com.yiban.java.oom
 * @className GCOverheadDemo
 * @date 2025/10/10
 * @description OOM:GC overhead limit exceeded 意思是JVM 98%的时间都在做GC，只要2%的时间在正常干活，这肯定是不行的，因为我们的程序是用来运行的，又不是专门做GC的
 * 这个demo可能需要在早期的jdk中试验
 * 网上很多教程/例子跑出 GC overhead limit exceeded 的都是 JDK8 早期版本（比如 1.8.0_20 ~ 1.8.0_202）。
 * 反正我用1.8.0_451是没跑成功
 * 	•	在 老版本 JDK8 上，ParallelGC 更可能报 GC overhead limit exceeded。
 * 	•	在 新版本 JDK8u452 上，两者大概率都会直接报 heap space（因为 GC 逻辑调整过，JVM 认为直接 OOM 更“干脆”）。
 */
public class GCOverheadDemo {
    //-Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+UseParallelGC -XX:MaxDirectMemorySize=5m
    public static void main(String[] args) {
//        testUserList();

        testUseMap();
    }

    private static void testUserList() {
        int i = 0;
        List<String> list = new ArrayList<>();

        try {
            while (true) {
                list.add(String.valueOf(++i).intern());
            }
        } catch (Exception e) {
            System.out.println("i = " + i);
            e.printStackTrace();
        }
    }

    public static void testUseMap() {
        Map<Integer, String> map = new HashMap<>();
        int i = 0;
        try {
            while (true) {
                // 不断往 map 塞数据，key 保证唯一
                map.put(i, String.valueOf(i++));
            }
        } catch (Throwable e) {
            System.out.println("最后 i = " + i);
            e.printStackTrace();
        }
    }
}
