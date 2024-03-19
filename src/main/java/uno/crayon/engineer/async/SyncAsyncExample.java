package uno.crayon.engineer.async;

import uno.crayon.engineer.algo.SlidingWindowExample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncAsyncExample {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int[] ints = {4, 3, 32, 3, 34};
            return SlidingWindowExample.maxSubArraySum(ints,3);
        }, executor);

        // 异步方法，指定了执行线程池
        future.thenApplyAsync(result -> {
            System.out.println("异步方法执行结果: " + result + "，当前线程：" + Thread.currentThread().getName());
            return result * 2;
        });

        System.out.println("主线程继续执行...");

        executor.shutdown();
    }
}