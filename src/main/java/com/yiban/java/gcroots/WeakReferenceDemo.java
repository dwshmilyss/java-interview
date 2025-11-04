package com.yiban.java.gcroots;

import java.lang.ref.WeakReference;

/**
 * @author david.duan
 * @packageName com.yiban.java.gcroots
 * @className WeakReferenceDemo
 * @date 2025/9/28
 * @description
 */
public class WeakReferenceDemo {
    public static void main(String[] args) {

        //这里直接把new Object()当成参数传入WeakReference的构造函数中，其实就是在构造函数中有个局部变量引用了new Object()
        //当构造函数执行结束，局部变量就消失了，那么new Object()就没有任何引用指向它，那么它就会被GC回收
        //这和Object o1 = new Object(); WeakReference<Object> weakReference = new WeakReference<>(o1); 是有区别的
        WeakReference<Object> weakReference = new WeakReference<>(new Object());
        System.out.println("weakReference = " + weakReference.get());
        System.gc();
        System.out.println("weakReference = " + weakReference.get());

    }
}
