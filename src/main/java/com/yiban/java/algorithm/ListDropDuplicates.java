package com.yiban.java.algorithm;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author david.duan
 * @packageName com.yiban.java.algorithm
 * @className ListDropDuplicates
 * @date 2025/10/15
 * @description 假设一个List中有元素(70, 70, - 1, 5, 3, 3, 4, 4, 4, 4, 99) 用5种不同的方法去除重复元素
 */
public class ListDropDuplicates {
    public static void main(String[] args) {
//        m1();
//        m2();
//        m3();
        m4();
    }

    /**
     * 通过挨个比较的方式去重
     */
    private static void m1() {
        List<Integer> initList = Arrays.asList(70, 70, -1, 5, 3, 3, 4, 4, 4, 4, 99);
        List<Integer> srcList = new ArrayList<>(initList);
        List<Integer> newList = new ArrayList<>();

        for (int i = 0; i < srcList.size(); i++) {
            int temp = srcList.get(i);
            if (!newList.contains(temp)) {
                newList.add(temp);
            }
        }

        System.out.println(newList);
    }

    /**
     * 利用HashSet或者linkedHashSet 数据结构天然就能去重
     */
    private static void m2() {
        List<Integer> initList = Arrays.asList(70, 70, -1, 5, 3, 3, 4, 4, 4, 4, 99);
        List<Integer> srcList = new ArrayList<>(initList);
        Set<Integer> hashSet = new HashSet<>(srcList);//第一种方法，用HashSet，但是原始的位置会变
        List<Integer> newList = new ArrayList<>(hashSet);
        newList.forEach(v -> System.out.print(v + " "));

        System.out.println("\n=================");

        Set<Integer> linkedHashSet = new LinkedHashSet<>(srcList);//第一种方法，用HashSet，但是原始的位置会变
        List<Integer> newList1 = new ArrayList<>(linkedHashSet);
        newList1.forEach(v -> System.out.print(v + " "));
    }

    /**
     * 利用java8之后的stream + distinct 流式操作
     */
    private static void m3() {
        List<Integer> initList = Arrays.asList(70, 70, -1, 5, 3, 3, 4, 4, 4, 4, 99);
        List<Integer> srcList = new ArrayList<>(initList);
        List<Integer> newList = srcList.stream().distinct().collect(Collectors.toList());
        System.out.println(newList);
    }

    /**
     * 模拟两个指针，一个从开始扫描，一个从结尾扫描，如果同一个值扫描后返回的下标不一样，就证明有重复的值
     */
    private static void m4() {
        List<Integer> initList = Arrays.asList(70, 70, -1, 5, 3, 3, 4, 4, 4, 4, 99);
        List<Integer> srcList = new ArrayList<>(initList);
        List<Integer> newList = new ArrayList<>(initList);

        for (Integer element : srcList) {
            if (newList.indexOf(element) != newList.lastIndexOf(element)) {
                newList.remove(newList.lastIndexOf(element));
            }
        }

        System.out.println(newList);
    }

}
