package com.yiban.java.algorithm;

/**
 * @author david.duan
 * @packageName com.yiban.java.algorithm
 * @className LeetCode744
 * @date 2025/11/5
 * @description 给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target  ，写一个函数搜索 nums 中的 target，如果 target 存在返回下标，否则返回 -1。
 * 你必须编写一个具有 O(log n) 时间复杂度的算法。
 * <p>
 * 示例 1:
 * 输入: nums = [-1,0,3,5,9,12], target = 9
 * 输出: 4
 * 解释: 9 出现在 nums 中并且下标为 4
 * <p>
 * 示例 2:
 * 输入: nums = [-1,0,3,5,9,12], target = 2
 * 输出: -1
 * 解释: 2 不存在 nums 中因此返回 -1
 *
 * 示例3：
 * nums = [5], target = 5
 * 输出： 0
 */
public class LeetCode704 {
    public static void main(String[] args) {
//        int[] nums = {-1, 0, 3, 5, 9, 12};
//        int target = 9;
//        int[] nums = {-1,0,3,5,9,12};
//        int target = 2;
        int[] nums = {5};
        int target = 5;
//        System.out.println(search(nums, target));
        System.out.println(searchV1(nums, target));
    }

    //我自己写的 有点low
    public static int search(int[] nums, int target) {
        if (nums.length == 1) {
            if (nums[0] == target) {
                return 0;
            }
        }
        int left = 0, right = nums.length - 1;
        while (left < right) {
            if (nums[left] == target) {
                return left;
            }
            if (nums[right] == target) {
                return right;
            }
            if (nums[left] < target) {
                left++;
            } else if (nums[right] > target) {
                right--;
            }
        }
        return -1;
    }

    public static int searchV1(int[] nums, int target) {
        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int middle = (left + right) / 2;//找到数组中间的下标，也就是说中分数组
            if (nums[middle] == target) {//如果中分下标的位置刚好是target，直接返回下标
                return middle;
            } else if (nums[middle] < target) {//这个则代表target在右区间，即[middle+1,right]，
                left = middle + 1;//所以让left = middle+1,这样就直接去右区间继续二分查找
            } else if (nums[middle] > target) {//则表示target在左区间，即[left,middle-1]
                // 直接去左区间继续二分查找
                right = middle - 1;
            }
        }
        return -1;
    }
}
