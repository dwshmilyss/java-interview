package com.yiban.java.collections.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author david.duan
 * @packageName com.yiban.java.collections.list
 * @className IteratorRemoveBugDemo
 * @date 2025/10/14
 * @description
 */
public class IteratorRemoveBugDemo {
    public static void main(String[] args) {
//        testIterString();
        testIterInteger();
    }

    private static void testIterInteger() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            int item = iterator.next();
            if (item == 3) {
                list.remove(item);//报：java.util.ConcurrentModificationException,这里删除的是下标为3，也就是数组中的第四个元素
//                iterator.remove();  // Correct usage of remove method
                /*
                这里int报但是String就不会报异常的原因是：
                而是因为 list.remove(item) 的 重载方法解析不同：
	            •	在 List<Integer> 中，remove(int) 调用的是按下标删除；
	            •	在 List<String> 中，remove(Object) 调用的是按值删除。
	            当调用remove(int index)也就是说，这行代码在执行的是：删除下标为 3 的元素（即第四个元素）
                    这会导致：
                        •	你在遍历时修改了底层数组结构（modCount++），
                        •	而 迭代器自己也在检测 modCount 是否被修改，
                        •	一旦发现“结构被修改但不是通过 iterator.remove()”，
                    就立刻抛出 ConcurrentModificationException。

                当调用remove(int index)也就是说，这行代码在执行的是：删除值为 “B” 的那个元素
                而且恰好：
                    •	你正在遍历到 “B”；
                    •	iterator.next() 返回的就是 “B”；
                    •	list.remove("B") 实际删除的也是当前元素；
                    •	由于数组结构改变位置和当前游标对齐，可能没有立刻触发 fail-fast 检测（一种“偶然逃过”的情况）。
                但这只是碰巧没抛异常，不是安全的写法。
                在不同 JVM、不同优化层级下，也可能抛异常。

                但是我在jdk8的高版本下，循环100次也没抛出异常，疑惑？
                 */
            }
        }
        list.forEach(v -> System.out.println(v));
    }

    private static void testIterString() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String item = iterator.next();
            if (item.equals("B")) {
                list.remove(item);// 这里直接删除了值为 "B" 的元素，不会报 ConcurrentModificationException。
//                iterator.remove();  // Correct usage of remove method
            }
        }
        list.forEach(v -> System.out.println(v));
//        System.out.println(list);  // Output: [A, C]
    }
}
