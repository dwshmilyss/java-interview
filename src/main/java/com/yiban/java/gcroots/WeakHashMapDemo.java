package com.yiban.java.gcroots;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author david.duan
 * @packageName com.yiban.java
 * @className WeakHashMapDemo
 * @date 2025/9/28
 * @description 强引用HashMap和弱引用WeakHashMap的区别
 */
public class WeakHashMapDemo {
    public static void main(String[] args) {
//        myHashmap();
        myWeakHashMap();
    }

    private static void myHashmap() {
        Map<Integer, String> map = new HashMap<>();
        Integer key = new Integer(1);
        String value = "hashmap";

        map.put(key, value);
        System.out.println(map);
        //这里把key置为null，但是map中还是有这个key-value的映射关系
        //因为HashMap底层用的是一个叫Node<K,V>的数据结构，Node中有个字段也叫key，但是和上面的key不一样，也就是说Node用了另外一个key来保存new Integer(1)的引用
        //所以这里是两个不同的引用，局部变量key=null是不会影响Node中的key的
        key = null;
        System.gc();
        System.out.println(map);
    }

    private static void myWeakHashMap() {
        Map<Integer, String> map = new WeakHashMap<>();
        Integer key = new Integer(1);
        String value = "weakhashmap";

        map.put(key, value);
        System.out.println(map);
        key = null;
        System.gc();
        //这里打印map就是空了，因为WeakHashMap底层用的是一个叫Entry<K,V>的数据结构，Entry中只有value字段而没有key字段，所以和上面的key是同一个东东
        //如果局部变量key=null，那么Entry中的key也是null了，所以map中就没有这个key-value的映射关系了
        System.out.println(map);
    }
}
