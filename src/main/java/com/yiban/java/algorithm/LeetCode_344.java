package com.yiban.java.algorithm;

/**
 * @author david.duan
 * @packageName com.yiban.java.algorithm
 * @className LeetCode_2
 * @date 2025/11/5
 * @description
 * 编写一个函数，其作用是将输入的字符串反转过来。输入字符串以字符数组 s 的形式给出。
 * 不要给另外的数组分配额外的空间，你必须原地修改输入数组、使用 O(1) 的额外空间解决这一问题。
 * 示例 1：
 * 输入：s = ["h","e","l","l","o"]
 * 输出：["o","l","l","e","h"]
 * 示例 2：
 * 输入：s = ["H","a","n","n","a","h"]
 * 输出：["h","a","n","n","a","H"]
 */
public class LeetCode_344 {
    public static void main(String[] args) {
        char[] chars = {'h', 'e', 'l', 'l', 'o'};
        reverseString(chars);
        System.out.println(chars);
    }

    public static void reverseString(char[] chars) {
        /*
        一左一右两个指针相向而行
        结果比目标，小了要变大，左指针右移+
        结果比目标，大了要变小，右指针左移-
         */

        //先定义两个指针
        int left = 0;
        int right = chars.length - 1;
        while (left < right) {//当左右两个指针重合时，循环结束
            //交换两个指针的数据
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;

            //然后移动指针
            left++; //左指针右移
            right--;//右指针左移
        }
    }
}
