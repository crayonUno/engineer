package uno.crayon.engineer.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalExample {

    // 定义一个 ThreadLocal 对象
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // 在第一个线程中设置 ThreadLocal 值
        executorService.submit(() -> {
            threadLocal.set("Value set in Thread 1");

            // 模拟执行一些任务
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 在任务完成后，调用 remove 方法清理 ThreadLocal
            // debug: 清除整个 Entry~
            threadLocal.remove();
        });

        // 在第二个线程中获取 ThreadLocal 值
        executorService.submit(() -> {
            // 模拟执行一些任务
            for (int i = 0; i < 3; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 注意：在第二个线程中并没有调用 remove 方法清理 ThreadLocal
        });

        // 关闭线程池
        executorService.shutdown();
    }
}
