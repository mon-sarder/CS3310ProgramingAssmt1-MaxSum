import java.util.Random;

public class MaxSubsequenceSum {

    /* ---------------- Result Class ---------------- */
    // Holds the maximum subsequence sum and its start/end indices
    static class Result {
        int maxSum;  // value of the maximum subsequence sum
        int start_index;   // starting index of the subsequence
        int end_index;     // ending index of the subsequence

        Result(int maxSum, int start_index, int end_index) {
            this.maxSum = maxSum;
            this.start_index = start_index;
            this.end_index = end_index;
        }
    }

    /* ---------------- Algorithm 1: O(n^3) Brute Force ---------------- */
    // Checks every possible subsequence by summing elements repeatedly
    public static Result maxSubsequenceSumCubic(int[] array) {
        int n = array.length;
        int maxSum = Integer.MIN_VALUE; // smallest possible start value
        int start_index = 0, end_index = 0;

        // Try all pairs (i, j) representing subsequence boundaries
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int sum = 0;

                // Compute subsequence sum from i to j
                for (int k = i; k <= j; k++) {
                    sum += array[k];
                }

                // Update if a new maximum is found
                if (sum > maxSum) {
                    maxSum = sum;
                    start_index = i;
                    end_index = j;
                }
            }
        }
        return new Result(maxSum, start_index, end_index);
    }

    /* ---------------- Algorithm 2: O(n^2) Improved Brute Force ---------------- */
    // Avoids recomputing sums by accumulating incrementally
    public static Result maxSubsequenceSumQuadratic(int[] array) {
        int n = array.length;
        int maxSum = Integer.MIN_VALUE;
        int start_index = 0, end_index = 0;

        // For each starting index i, expand the subsequence forward
        for (int i = 0; i < n; i++) {
            int sum = 0; // reset sum for new starting index
            for (int j = i; j < n; j++) {
                sum += array[j]; // extend subsequence to index j
                
                //Update if the sum is higher than the current max. 
                if (sum > maxSum) {
                    maxSum = sum;
                    start_index = i;
                    end_index = j;
                }
            }
        }
        return new Result(maxSum, start_index, end_index);
    }

    // ---------------- Algorithm 3: O(n log n) Divide and Conquer ----------------
    // Splits array into halves, combines results across the midpoint
    public static Result maxSubsequenceSumDivideConquer(int[] array) {
        return divideAndConquer(array, 0, array.length - 1);
    }

    // Recursive helper function
    private static Result divideAndConquer(int[] array, int left, int right) {
        // Base case: single element subsequence
        if (left == right) {
            return new Result(array[left], left, right);
        }

        int middle = (left + right) / 2;

        // Solve left half
        Result leftResult = divideAndConquer(array, left, middle);

        // Solve right half
        Result rightResult = divideAndConquer(array, middle + 1, right);

        // Solve subsequence crossing the midpoint
        Result crossResult = maxCrossingSum(array, left, middle, right);

        // Return the best of the three
        if (leftResult.maxSum >= rightResult.maxSum && leftResult.maxSum >= crossResult.maxSum)
            return leftResult;
        else if (rightResult.maxSum >= leftResult.maxSum && rightResult.maxSum >= crossResult.maxSum)
            return rightResult;
        else
            return crossResult;
    }

    // Finds maximum subsequence crossing the midpoint
    private static Result maxCrossingSum(int[] array, int left, int middle, int right) {
        int sum = 0;

        // Find best sum in left half
        int leftSum = Integer.MIN_VALUE;
        int maxLeft = middle;
        for (int i = middle; i >= left; i--) {
            sum += array[i];
            if (sum > leftSum) {
                leftSum = sum;
                maxLeft = i;
            }
        }

        // Find best sum in right half
        sum = 0;
        int rightSum = Integer.MIN_VALUE;
        int maxRight = middle + 1;
        for (int j = middle + 1; j <= right; j++) {
            sum += array[j];
            if (sum > rightSum) {
                rightSum = sum;
                maxRight = j;
            }
        }

        // Combine both halves
        return new Result(leftSum + rightSum, maxLeft, maxRight);
    }

    // ---------------- Algorithm 4: O(n) Kadane’s Algorithm ----------------
    // Dynamic programming idea: extend current sum or restart
    public static Result maxSubsequenceSumKadane(int[] array) {
        int maxSoFar = array[0];          // global maximum
        int maxEndingHere = array[0];     // maximum subsequence ending at current index
        int start_index = 0, end_index = 0, s = 0;  // indices to track subsequence

        for (int i = 1; i < array.length; i++) {
            // Either extend current subsequence or start new one at i
            if (array[i] > maxEndingHere + array[i]) {
                maxEndingHere = array[i];
                s = i; // new start candidate
            } else {
                maxEndingHere += array[i];
            }

            // Update global maximum if needed
            if (maxEndingHere > maxSoFar) {
                maxSoFar = maxEndingHere;
                start_index = s;
                end_index = i;
            }
        }
        return new Result(maxSoFar, start_index, end_index);
    }

    // ---------------- Main Program ----------------
    public static void main(String[] args) {
        Random rand = new Random();

        // Generate 2 test arrays, each with at least 100 integers (between -50 and 50, etc.)
        int[] test1 = rand.ints(100, -50, 50).toArray();
        int[] test2 = rand.ints(100, -100, 100).toArray();

        // Run all algorithms on both test datasets
        runAlgorithms(test1, "Test Data 1");
        runAlgorithms(test2, "Test Data 2");
    }

    // Runs the 4 algorithms on one array and prints results with timing
    private static void runAlgorithms(int[] array, String label) {
        System.out.println("\n==== " + label + " ====");

        long start, end, cpuTime;

        // Algorithm 1: O(n^3)
        start = System.nanoTime();
        Result r1 = maxSubsequenceSumCubic(array);
        end = System.nanoTime();
        cpuTime = end - start;
        System.out.println("O(n^3) Brute Force -> Sum: " + r1.maxSum +
                " | Start: " + r1.start_index + " | End: " + r1.end_index + " | Time: " + cpuTime + " ns");

        // Algorithm 2: O(n^2)
        start = System.nanoTime();
        Result r2 = maxSubsequenceSumQuadratic(array);
        end = System.nanoTime();
        cpuTime = end - start;
        System.out.println("O(n^2) Improved -> Sum: " + r2.maxSum +
                " | Start: " + r2.start_index + " | End: " + r2.end_index + " | Time: " + cpuTime + " ns");

        // Algorithm 3: O(n log n)
        start = System.nanoTime();
        Result r3 = maxSubsequenceSumDivideConquer(array);
        end = System.nanoTime();
        cpuTime = end - start;
        System.out.println("O(n log n) Divide & Conquer -> Sum: " + r3.maxSum +
                " | Start: " + r3.start_index + " | End: " + r3.end_index + " | Time: " + cpuTime + " ns");

        // Algorithm 4: O(n)
        start = System.nanoTime();
        Result r4 = maxSubsequenceSumKadane(array);
        end = System.nanoTime();
        cpuTime = end - start;
        System.out.println("O(n) Kadane -> Sum: " + r4.maxSum +
                " | Start: " + r4.start_index + " | End: " + r4.end_index + " | Time: " + cpuTime + " ns");
    }
}
