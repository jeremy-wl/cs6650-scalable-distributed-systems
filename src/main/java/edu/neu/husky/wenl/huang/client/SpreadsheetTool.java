package edu.neu.husky.wenl.huang.client;

import java.util.*;

public class SpreadsheetTool {
    /**
     * @param nums a SORTED list of integers
     * @return the median of all integers
     */
    public static int getMedian(List<Integer> nums) {
        return nums.get(nums.size() >> 1);
    }

    /**
     *
     * @param nums a list of integers
     * @return the mean of all integers
     */
    public static double getMean(List<Integer> nums) {
        long sum = 0;
        for (int num : nums)  sum += num;
        return sum / nums.size();
    }

    public static int nthPercentile(int n, List<Integer> nums) {
        int index = (int) Math.ceil(n / 100.0 * nums.size()) - 1;
        return nums.get(index);
    }
}
