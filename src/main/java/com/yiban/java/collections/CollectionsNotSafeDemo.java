package com.yiban.java.collections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author david.duan
 * @packageName com.yiban.java.collections
 * @className ArrayListNotSafeDemo
 * @date 2025/7/23
 * @description ArrayList 不是线程安全的，因为其add()没有加锁，所以会有并发问题
 */
public class CollectionsNotSafeDemo {
    public static void main(String[] args) {
//        testArrayListNotSafe();
//        testHashSetNotSafe();
        testHashMapNotSafe();
    }

    public static void testArrayListNotSafe() {
        /*
        使用线程不安全的ArrayList 在add remove的时候如果同时有线程在遍历的话，就会报java.util.ConcurrentModificationException。这种异常并不是因为线程安全问题本身，而是因为 ArrayList 的快速失败机制（fail-fast）
        这里会报错是因为下面的for循环里面开启了30个线程，在执行list.add的时候也执行了sout(list)，其中sout(list)其实是调用了list.toString()，而在toString中就是迭代每个元素
         */
//        List<String> list = new ArrayList<>();
        // 解决方案1 使用vector
//        List<String> list = new Vector<>();
        // 解决方案2 Collections.synchronizedList
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
        // 解决方案3 CopyOnWriteArrayList
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 30; i++) { //循环次数越多出错概率越高
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println("list = " + list);
            },String.valueOf(i)).start();
        }
    }

    public static void testHashSetNotSafe() {
        /*
        HashSet 同样也是线程不安全的，和ArrayList的原理一样
         */
//        Set<String> set = new HashSet<>();

        //解决方法1
//        Set<String> set = Collections.synchronizedSet(new HashSet<>());

        //解决方法2
        Set<String> set = new CopyOnWriteArraySet<>();

        for (int i = 0; i < 30; i++) { //循环次数越多出错概率越高
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println("set = " + set);
            },String.valueOf(i)).start();
        }
    }
    
    public static void testHashMapNotSafe() {
        /*
        HashSet 同样也是线程不安全的，和ArrayList的原理一样
         */
//        Map<String, String> map = new HashMap<>();

        //解决方式1 Hashtable
//        Map<String, String> map = new Hashtable<>();

        //解决方式2 Collections.synchronizedMap
//        Map<String, String> map = Collections.synchronizedMap(new HashMap<>());

        //解决方式3 ConcurrentHashMap
        Map<String, String> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 30; i++) { //循环次数越多出错概率越高
            new Thread(() -> {
                map.put(UUID.randomUUID().toString().substring(0, 8),UUID.randomUUID().toString().substring(0, 8));
                System.out.println("map = " + map);
            },String.valueOf(i)).start();
        }
    }
}
