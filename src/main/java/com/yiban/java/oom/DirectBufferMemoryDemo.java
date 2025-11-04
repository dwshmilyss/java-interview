package com.yiban.java.oom;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author david.duan
 * @packageName com.yiban.java.oom
 * @className DirectBufferMemoryDemo
 * @date 2025/10/10
 * @description
 * ByteBuffer.allocateDirect 分配的是堆外内存
 * ByteBuffer.allocate 分配的是堆内存
 */
public class DirectBufferMemoryDemo {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //-XX:MaxDirectMemorySize=5m
//        System.out.println(sun.misc.VM.maxDirectMemory() / 1024 / 1024 / 1024 + " GB");//jdk1.8
//        ByteBuffer bb = ByteBuffer.allocateDirect(6 * 1024 * 1024);

        //jdk17
//        System.out.println("MaxDirectMemory = " + getMaxDirectMemory() / (1024 * 1024) + " MB");
        //万能运行，适配包括jdk8在内的之后所有版本
        System.out.println("MaxDirectMemory = " + getMaxDirectMemory() / (1024 * 1024) + " MB");
    }

    /**
     * jdk17的实现方式
     * 从JDK 9+ 开始 sun.misc.VM 被迁移为 jdk.internal.misc.VM
     * 其 maxDirectMemory() 方法仍存在，所以用反射依然可取到真实值
     * 但注意：运行时需要添加模块开放参数(--add-opens java.base/jdk.internal.misc=ALL-UNNAMED)，否则反射可能失败。
     *
     *
     * @return
     */
    public static long getMaxDirectMemory() {
        try {
            Class<?> vmClass = Class.forName("jdk.internal.misc.VM");
            Method m = vmClass.getDeclaredMethod("maxDirectMemory");
            m.setAccessible(true);
            return (long) m.invoke(null);
        } catch (Throwable t) {
            return Runtime.getRuntime().maxMemory(); // fallback
        }
    }

    public static long getMaxDirectMemoryRunAnyWhere() {
        // 优先尝试反射调用 VM.maxDirectMemory()
        try {
            Class<?> vmClass;
            try {
                // JDK 9+
                vmClass = Class.forName("jdk.internal.misc.VM");
            } catch (ClassNotFoundException e) {
                // JDK 8 and earlier
                vmClass = Class.forName("sun.misc.VM");
            }

            Method m = vmClass.getDeclaredMethod("maxDirectMemory");
            m.setAccessible(true);
            Object value = m.invoke(null);
            if (value instanceof Long) {
                return (Long) value;
            }
        } catch (Throwable ignore) {
        }

        // 退回方案：解析 -XX:MaxDirectMemorySize
        List<String> args = ManagementFactory.getRuntimeMXBean().getInputArguments();
        for (String arg : args) {
            if (arg.startsWith("-XX:MaxDirectMemorySize=")) {
                String value = arg.substring("-XX:MaxDirectMemorySize=".length());
                return parseMemoryValue(value);
            }
        }

        // 再退：使用堆大小近似
        return Runtime.getRuntime().maxMemory();
    }

    private static long parseMemoryValue(String value) {
        value = value.trim().toUpperCase();
        long multiplier = 1;
        if (value.endsWith("K")) {
            multiplier = 1024L;
            value = value.substring(0, value.length() - 1);
        } else if (value.endsWith("M")) {
            multiplier = 1024L * 1024L;
            value = value.substring(0, value.length() - 1);
        } else if (value.endsWith("G")) {
            multiplier = 1024L * 1024L * 1024L;
            value = value.substring(0, value.length() - 1);
        }
        return Long.parseLong(value) * multiplier;
    }

}
