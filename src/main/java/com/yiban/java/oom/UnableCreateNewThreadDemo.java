package com.yiban.java.oom;

/**
 * @author david.duan
 * @packageName com.yiban.java.oom
 * @className UnableCreateNewThreadDemo
 * @date 2025/10/10
 * @description 该类需要放到Linux中运行
 * 在Linux中，针对单个进程，普通用户默认可以创建的线程数是1024个线程，但是由于还有一些系统级别的线程，所以一般上限就900多
 * 如果是root用户，则没有限制
 * 如果要修改限制，需要编辑Linux中的 /etc/security/
 */
public class UnableCreateNewThreadDemo {
    public static void main(String[] args) {
        for (int i = 0; ; i++) {
            System.out.println("i = " + i);
            new Thread(() -> {
                try {
                    Thread.sleep(Integer.MAX_VALUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, ""+i).start();
        }
    }
}
