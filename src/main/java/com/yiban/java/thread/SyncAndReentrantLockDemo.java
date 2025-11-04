package com.yiban.java.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;

/**
 * @author david.duan
 * @packageName com.yiban.java.thread
 * @className SyncAndReentrantLockDemo
 * @date 2025/9/15
 * @description  题目(主要练习 ReentrantLock 和 Condition)：A B C 三个线程轮流执行，A先执行5次，然后换B执行10次，然后再换C执行15次，然后再到A，这样进行10轮
 *
 * synchronized 和lock 有什么区别？用新的 lock 有什么好处？举例说明
 *
 * 1）synchronized 属于 JVM 层面，属于 java 的关键字
 * •monitorenter（底层是通过 monitor 对象来完成，其实 wait/notify 等方法也依赖于 monitor 对象只能在同步块或者方法中才能调用wait/ notify 等方法）
 * •Lock 是具体类（java.util.concurrent.locks.Lock）是 api 层面的锁
 * 2）使用方法：
 * •synchronized：不需要用户去手动释放锁，当 synchronized 代码执行后，系统会自动让线程释放对锁的占用
 * •ReentrantLock：则需要用户去手动释放锁，若没有主动释放锁，就有可能出现死锁的现象，需要 lock() 和 unlock() 配置 try catch 语句来完成
 * 3）等待是否中断
 * •synchronized：不可中断，除非抛出异常或者正常运行完成
 * •ReentrantLock：可中断，可以设置超时方法
 * –设置超时方法，trylock(long timeout, TimeUnit unit)
 * –lockInterrupible() 放代码块中，调用 interrupt() 方法可以中断
 * 4）加锁是否公平
 * •synchronized：非公平锁
 * •ReentrantLock：默认非公平锁，构造函数可以传递 boolean 值，true 为公平锁，false 为非公平锁
 * 5）锁绑定多个条件 Condition
 * •synchronized：没有，要么随机，要么全部唤醒
 * •ReentrantLock：用来实现分组唤醒需要唤醒的线程，可以精确唤醒，而不是像 synchronized 那样，要么随机，要么全部唤醒
 */
public class SyncAndReentrantLockDemo {
    public static void main(String[] args) {
        ShareSource shareSource = new ShareSource();
        new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    shareSource.print5();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "A").start();

        new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    shareSource.print10();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "B").start();

        new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    shareSource.print15();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "C").start();
    }
}

class ShareSource {
    private int num = 1; //标识位
    private Lock lock = new ReentrantLock();//锁
    private Condition c1 = lock.newCondition(); //钥匙 A线程
    private Condition c2 = lock.newCondition(); //钥匙 B线程
    private Condition c3 = lock.newCondition(); //钥匙 C线程

    //A线程执行流程
    public void print5() {
        try {
            lock.lock();
            //1. 判断
            while (num != 1) { //如果不是1 就等待(也就是让A线程执行)，反之A线程等待
                c1.await(); //
            }
            //2. 执行
            for (int i = 1; i <= 5; i++) {
                System.out.println(currentThread().getName() + "\t" + i);
            }
            //3. 唤醒
            num = 2; //先重置标识位
            c2.signal(); //然后唤醒B线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    //B线程执行流程
    public void print10() {
        try {
            lock.lock();
            //1. 判断
            while (num != 2) { //如果不是2 就等待(也就是让B线程执行)
                c2.await();
            }
            //2. 执行
            for (int i = 1; i <= 10; i++) {
                System.out.println(currentThread().getName() + "\t" + i);
            }
            //3. 唤醒
            num = 3; //先重置标识位
            c3.signal(); //然后唤醒C线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    //C线程执行流程
    public void print15() {
        try {
            lock.lock();
            //1. 判断
            while (num != 3) { //如果不是3 就等待(也就是让C线程执行)
                c3.await();
            }
            //2. 执行
            for (int i = 1; i <= 15; i++) {
                System.out.println(currentThread().getName() + "\t" + i);
            }
            //3. 唤醒
            num = 1; //先重置标识位
            c1.signal(); //然后唤醒A线程
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
