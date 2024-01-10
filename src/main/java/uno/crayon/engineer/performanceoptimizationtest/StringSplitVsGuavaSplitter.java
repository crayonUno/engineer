package uno.crayon.engineer.performanceoptimizationtest;

import com.google.common.base.Splitter;

public class StringSplitVsGuavaSplitter {
    public static void main(String[] args) throws Exception{
        Thread.sleep(20000);
        String input = "apple/orange/banana/grape".repeat(100000);  // 重复字符串以增加测试量
        String delimiter = "/";
        // Performance test using Guava Splitter
        long startTimeGuava = System.nanoTime();
        Splitter splitter = Splitter.on(delimiter).trimResults();
        splitter.splitToList(input);
        long endTimeGuava = System.nanoTime();
        long guavaElapsedTime = endTimeGuava - startTimeGuava;

        System.out.println("Guava Elapsed Time: " + guavaElapsedTime + " nanoseconds");

        // Performance test using String#split()
        long startTimeStringSplit = System.nanoTime();
        input.split(delimiter);
        long endTimeStringSplit = System.nanoTime();
        long stringSplitElapsedTime = endTimeStringSplit - startTimeStringSplit;

        System.out.println("String#split() Elapsed Time: " + stringSplitElapsedTime + " nanoseconds");
    }
}
