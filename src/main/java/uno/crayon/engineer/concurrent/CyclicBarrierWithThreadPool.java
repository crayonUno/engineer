package uno.crayon.engineer.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierWithThreadPool {

    public static void main(String[] args) {
        final int numberOfThreads = 3;
        final int numberOfCycles = 2;

        // 创建线程池
        ThreadPoolUtil.getInstance();

        // 创建一个 CyclicBarrier，指定需要等待的线程数量和可重用次数
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfThreads);

        // 创建 CountDownLatch 用于主线程等待所有线程池任务完成
        CountDownLatch allTasksCompletedLatch = new CountDownLatch(numberOfThreads * numberOfCycles);

        // 提交任务给线程池
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadId = i;
            ThreadPoolUtil.submitTask(() -> {
                for (int j = 0; j < numberOfCycles; j++) {
                    try {
                        System.out.println("Thread " + threadId + " is performing its task in cycle " + (j + 1) + ".");

                        // 让线程到达屏障点，等待其他线程
                        cyclicBarrier.await();

                        // 所有线程达到屏障点后，执行下面的代码
                        System.out.println("Thread " + threadId + " continues its execution in cycle " + (j + 1) + ".");

                        // 模拟其他工作...
                        Thread.sleep(100);

                    } catch (InterruptedException | java.util.concurrent.BrokenBarrierException e) {
                        e.printStackTrace();
                    } finally {
                        // 每个任务完成后，递减 CountDownLatch
                        allTasksCompletedLatch.countDown();
                    }
                }
            });
        }

        try {
            // 主线程等待所有线程池任务完成
            allTasksCompletedLatch.await();
            System.out.println("All tasks completed. Main thread continues.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shutdown the thread pool when done
            ThreadPoolUtil.shutdown();
        }
    }
}
