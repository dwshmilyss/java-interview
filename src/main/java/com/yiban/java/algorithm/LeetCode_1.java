package com.yiban.java.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author david.duan
 * @packageName com.yiban.java.algorithm
 * @className LeetCode_1
 * @date 2025/11/4
 * @description
 * 输入：nums = [2,7,11,15], target = 9
 * 输出：[0,1]
 * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
 */
public class LeetCode_1 {
    public static void main(String[] args) {
//        int target = 9;
//        int[] nums = {2, 7, 11, 15};
        int target = 6;
        int[] nums = {3, 2, 4};
//        int[] result = twoSum(nums, target);
        int[] result = twoSumV2(nums, target);
        System.out.println("[" + result[0] + ", " + result[1] + "]");
    }

    //暴力解法 O(n^2)
    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i+1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    //优雅解法 时间复杂度：O(n) ，不过要借助HashMap，所以会占用一些空间复杂度，这是必须的，其实说白了无非就是拿空间换时间，或者拿时间换空间
    public static int[] twoSumV2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            int temp = target - nums[i];
            if (map.containsKey(temp)) {
                return new int[]{map.get(temp), i};
            }
            map.put(nums[i], i);//这行代码必须放在判断后面，因为如果target刚好是num[i]的2倍的话，那么直接就会返回2个下标一样的数组，
        }
        return null;
    }
}
