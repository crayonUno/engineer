package uno.crayon.engineer.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author
 */
public class FutureAsyncCombineDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // 异步执行任务1
        Future<String> future1 = executorService.submit(() -> fetchDataFromSource1());

        // 异步执行任务2
        Future<String> future2 = executorService.submit(() -> fetchDataFromSource2());

        // 注册回调，处理异步任务完成后的逻辑
        registerCallback(future1, future2);

        // 关闭线程池
        executorService.shutdown();
    }

    private static void registerCallback(Future<String> future1, Future<String> future2) {
        new Thread(() -> {
            try {
                // 阻塞等待任务1完成
                String result1 = future1.get();

                // 阻塞等待任务2完成
                String result2 = future2.get();

                // 处理任务完成后的逻辑，例如组装订单卡片
                assembleOrderCard(result1, result2);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static String fetchDataFromSource1() {
        // 模拟耗时任务1
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Data from Source 1";
    }

    private static String fetchDataFromSource2() {
        // 模拟耗时任务2
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Data from Source 2";
    }

    private static void assembleOrderCard(String result1, String result2) {
        // 组装订单卡片的逻辑
        System.out.println("Assembling order card with data: " + result1 + " and " + result2);
    }
}
