package com.yiban.java.gcroots;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @author david.duan
 * @packageName com.yiban.java.gcroots
 * @className ReferenceQueueDemo
 * @date 2025/9/28
 * @description
 * ReferenceQueue 弱、软、虚三种引用在GC回收的时候会将引用对象加入到ReferenceQueue中
 * 通过ReferenceQueue可以获取到被GC回收的对象，从而进行一些清理工作
 * 例如：ThreadLocal中的Entry就是通过ReferenceQueue来实现的
 * 1. 创建一个ReferenceQueue对象
 * 2. 创建一个WeakReference对象，并将ReferenceQueue对象传入
 * 3. 将WeakReference对象放入到HashMap中
 * 4. GC回收时，会将WeakReference对象加入到ReferenceQueue中
 * 5. 通过ReferenceQueue.poll()方法获取到被GC回收的对象
 * 6. 通过WeakReference.get()方法获取到被GC回收的对象
 * 7. 通过WeakReference.clear()方法清除WeakReference对象
 * 8. 通过WeakReference.isEnqueued()方法判断WeakReference对象是否被GC回收
 * 9. 通过WeakReference.enqueue()方法手动将WeakReference对象加入到ReferenceQueue中
 */
public class ReferenceQueueDemo {
    public static void main(String[] args) throws InterruptedException {
//        testReferenceQueue();

        testRefQueueWithName();
    }

    /**
     *  弱引用 / 软引用：ref.get() 在 GC 之后通常会返回 null，所以不能直接用 get() 确认对象身份。
     * 	虚引用 (PhantomReference)：ref.get() 永远就是 null。
     *  所以必须通过 额外保存标识 来知道是谁被回收了。
     *
     */
    private static void testRefQueueWithName() throws InterruptedException {
        ReferenceQueue<Object> queue = new ReferenceQueue<>();

        Object obj1 = new Object();
        Object obj2 = new Object();

        MyWeakRef ref1 = new ReferenceQueueDemo().new MyWeakRef(obj1, queue, "对象1");
        MyWeakRef ref2 = new ReferenceQueueDemo().new MyWeakRef(obj2, queue, "对象2");

        obj1 = null;
        obj2 = null;

        System.gc();
        Thread.sleep(500);

        // 阻塞等待被回收的对象
        MyWeakRef ref;
//        while ((ref = (MyWeakRef) queue.remove()) != null) {//remove会阻塞线程
//            System.out.println(ref.getName() + " 已被 GC 回收");
//        }
        while ((ref = (MyWeakRef) queue.poll()) != null) {//poll不会阻塞，直接返回
            System.out.println(ref.getName() + " 已被 GC 回收");
        }
    }

    private static void testReferenceQueue() throws InterruptedException {
        Object o1 = new Object();
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        WeakReference<Object> weakReference = new WeakReference<>(o1,referenceQueue);

        System.out.println("o1 = " + o1);
        System.out.println("weakReference = " + weakReference.get());
        System.out.println("referenceQueue = " + referenceQueue.poll());//GC之前引用队列中什么都没有


        o1 = null;//这里一定要把o1置为null，否则weakReference.get还是能拿到对象的，因为o1是个强引用，不设置为null就不会被回收
        System.gc();
        Thread.sleep(500);//让GC执行完
        System.out.println("o1 = " + o1);
        System.out.println("weakReference = " + weakReference.get());
        System.out.println("referenceQueue = " + referenceQueue.poll());//GC之后被回收的弱引用对象会被加入到引用队列中
    }

    /**
     * 搞一个自己的MyWeakRef类，继承自WeakReference
     * 然后再搞一个name用来区别被回收的是哪个类
     */
    class MyWeakRef extends WeakReference<Object> {
        private final String name; // 保存额外信息
        public MyWeakRef(Object referent, ReferenceQueue<Object> queue, String name) {
            super(referent, queue);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


}
