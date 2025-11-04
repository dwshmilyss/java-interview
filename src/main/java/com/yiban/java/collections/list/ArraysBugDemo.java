package com.yiban.java.collections.list;

import java.util.Arrays;
import java.util.List;

/**
 * @author david.duan
 * @packageName com.yiban.java.collections.list
 * @className ArraysBugDemo
 * @date 2025/10/14
 * @description
 */
public class ArraysBugDemo {
    public static void main(String[] args) {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5);
        integerList.add(6); // This will throw an UnsupportedOperationException
        /*
        这里会报错是因为asList的返回对象是一个固定大小的List，不支持add方法。asList方法返回的是Arrays内部类中的一个ArrayList实例，这个实例是不可变的。
        该实例并没有实现List的修改方法。Arrays.asList体现的只是适配器模式，它将一个数组适配成一个List。后台的数据仍然是数组，所以调用add方法就会跑到父类AbstractList，在该类中add方法直接就抛出异常了。
        如果需要一个可变的List，可以使用new ArrayList<>(Arrays.asList(...))来创建。

         */
    }
}
