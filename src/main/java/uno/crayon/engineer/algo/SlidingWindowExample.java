package uno.crayon.engineer.algo;

public class SlidingWindowExample {

    public static int maxSubArraySum(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return 0;
        }

        int n = nums.length;
        int maxSum = 0;
        int currentSum = 0;

        // 计算初始窗口的和
        for (int i = 0; i < k; i++) {
            currentSum += nums[i];
        }

        // 遍历数组并维护滑动窗口的和
        for (int i = k; i < n; i++) {
            // 窗口右移，添加新元素，移除旧元素
            currentSum = currentSum + nums[i] - nums[i - k];
            
            // 更新最大和
            maxSum = Math.max(maxSum, currentSum);
        }

        return maxSum;
    }

    public static void main(String[] args) {
        int[] nums = {1, -2, 3, 4, -1, 2, 1, -5, 4};
        int k = 3;

        int result = maxSubArraySum(nums, k);
        System.out.println("Maximum Subarray Sum: " + result);
    }
}
