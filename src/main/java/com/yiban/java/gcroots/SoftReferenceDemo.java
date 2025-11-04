package com.yiban.java.gcroots;

import java.lang.ref.SoftReference;

/**
 * @author david.duan
 * @packageName com.yiban.java.gcroots
 * @className SoftReferenceDemo
 * @date 2025/9/28
 * @description
 * 强引用: * 强引用是使用最普遍的引用。如果一个对象具有强引用，那垃圾回收器绝不会回收它。
 * 强引用是使用最普遍的引用。如果一个对象具有强引用，那垃圾回收器绝不会回收它。
 * 当内存空间不足，Java虚拟机宁愿抛出OutOfMemoryError错误，使程序异常终止，也不会靠随意回收具有强引用的对象来解决内存不足的问题。
 *
 * 软引用: 一个对象如果有软引用，内存空间足够，垃圾回收器就不会回收它；如果内存空间不足了，就会回收这些对象的内存。
 * 只要垃圾回收器没有回收它，该对象就可以被程序使用。软引用可用来实现内存敏感的高速缓存。
 *
 * 弱引用: 弱引用与软引用的区别在于：只具有弱引用的对象拥有更短暂的生命周期。在垃圾回收器线程扫描它所管辖的内存区域的过程中，
 * 如果发现有对象只有弱引用的存在，那么就会被回收。不过由于垃圾回收器是一个优先级很低的线程
 */
public class SoftReferenceDemo {
    public static void main(String[] args) {
//        softRef_Memory_Enough();
        System.out.println("=================");
        softRef_Memory_NotEnough();
    }

    //Xmx默认是物理内存的1/4  Xms默认是物理内存的1/64
    private static void softRef_Memory_Enough() {
        Object o1 = new Object();
        SoftReference<Object> softReference = new SoftReference<>(o1);
        System.out.println("o1 = " + o1);
        System.out.println("softReference = " + softReference);

        o1 = null;
        System.gc();
        System.out.println("o1 = " + o1);
        System.out.println("softReference = " + softReference.get());
    }

    /**
     * JVM设置，故意产生大对象，让它发生OOM，看软引用的回收情况
     * -Xmx5m -Xms5m -XX:+PrintGCDetails
     */
    private static void softRef_Memory_NotEnough() {
        Object o1 = new Object();
        SoftReference<Object> softReference = new SoftReference<>(o1);
        System.out.println("o1 = " + o1);
        System.out.println("softReference = " + softReference.get());

        o1 = null;
//        System.gc();

        try {
            byte[] bytes = new byte[30 * 1024 * 1024];
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("o1 = " + o1);
            System.out.println("softReference = " + softReference.get());
        }
    }
}
