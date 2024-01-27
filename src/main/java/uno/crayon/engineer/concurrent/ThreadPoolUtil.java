package uno.crayon.engineer.concurrent;

import java.util.concurrent.*;

public class ThreadPoolUtil {
    private static volatile ThreadPoolExecutor threadPoolExecutor;

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int MAX_WORK_QUEUE_SIZE = 100;
    private static final long KEEP_ALIVE_TIME = 60L;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final ArrayBlockingQueue<Runnable> WORK_QUEUE = new ArrayBlockingQueue<>(MAX_WORK_QUEUE_SIZE);

    private ThreadPoolUtil() {
        // private constructor to prevent instantiation
    }

    public static ThreadPoolExecutor getInstance() {
        if (threadPoolExecutor == null) {
            synchronized (ThreadPoolUtil.class) {
                if (threadPoolExecutor == null) {
                    // 使用自定义的拒绝策略
                    RejectedExecutionHandler rejectedExecutionHandler = new CustomRejectedExecutionHandler();
                    threadPoolExecutor = new ThreadPoolExecutor(
                            CORE_POOL_SIZE,
                            MAX_POOL_SIZE,
                            KEEP_ALIVE_TIME,
                            TIME_UNIT,
                            WORK_QUEUE,
                            new CustomThreadFactory("MyThreadPool-Worker"),
                            rejectedExecutionHandler
                    );
                }
            }
        }
        return threadPoolExecutor;
    }

    public static void submitTask(Runnable task) {
        try {
            getInstance().submit(task);
        } catch (RejectedExecutionException e) {
            // 处理任务被拒绝的情况
            System.err.println("Task submission rejected: " + e.getMessage());
        }
    }

    public static void shutdown() {
        getInstance().shutdown();
    }

    private static class CustomThreadFactory implements ThreadFactory {
        private final String threadNamePrefix;

        public CustomThreadFactory(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName(threadNamePrefix + "-" + thread.getId());
            return thread;
        }
    }

    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // 自定义拒绝策略，这里简单地打印错误消息
            System.err.println("Task rejected. ThreadPool is full.");
        }
    }

    public static void main(String[] args) {
        // Example usage
        for (int i = 0; i < 15; i++) {
            final int taskId = i;
            ThreadPoolUtil.submitTask(() -> {
                System.out.println("Task " + taskId + " is running in thread " + Thread.currentThread().getName());
                // Perform some task...
            });
        }

        // Shutdown the thread pool when done
        ThreadPoolUtil.shutdown();
    }
}
