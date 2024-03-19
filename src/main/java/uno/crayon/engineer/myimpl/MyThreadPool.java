package uno.crayon.engineer.myimpl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 0.确定功能。
 * 程序提交任务给线程池
 * 线程池中是否有空闲线程
 * 没有的话进入队列等待
 * 都满的话，触发拒绝策略
 * 1.代码设计
 * 属性：线程个数，队列，拒绝测试的选择
 */
public class MyThreadPool implements Executor {
    private volatile static int DEFAULT_MAX_POOL_SIZE = 128;
    private volatile static int DEFAULT_CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;


    private volatile int corePoolSize;
    private volatile int maxPoolSize;
    private final BlockingQueue<Runnable> workQueue;

    private final AtomicInteger workThreadCount = new AtomicInteger(0);

    public MyThreadPool(BlockingQueue<Runnable> workQueue) {
        this(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, workQueue);
    }

    public MyThreadPool(int corePoolSize, int maxPoolSize, BlockingQueue<Runnable> workQueue) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.workQueue = workQueue;
    }

    @Override
    public void execute(Runnable command) {
        int count = workThreadCount.get();
        if (count > corePoolSize) {
            // todo addWorker()
        }
    }

    private boolean addWorker(Runnable task) {
        if (workThreadCount.get() >= maxPoolSize) return false;
        // todo 整理好任务提交逻辑，再组织代码
        //if (workQueue.)
        Worker worker = new Worker(task);
        worker.startTask();
        workThreadCount.incrementAndGet();
        return false;
    }

    private final class Worker implements Runnable {
        Runnable firstTask;
        Thread thread;
        public Worker(Runnable firstTask) {
            this.firstTask = firstTask;
            thread = new Thread(this);
        }

        public void startTask() {
            thread.start();
        }

        @Override
        public void run() {
            Runnable task = firstTask;
            try {
                while (task != null || (task = getTask()) != null){
                    task.run();
                    if (workThreadCount.get() > maxPoolSize) {
                        break;
                    }
                    task = null;
                }
            } finally {
                workThreadCount.incrementAndGet();
            }
        }

        private Runnable getTask() {
            try {
                return workQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //todo opt
            return null;
        }
    }
    
    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(new ArrayBlockingQueue<>(300));
        for (int i = 0; i < 31; i++) {
            int finalI = i;
            myThreadPool.execute(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Tread num：" + finalI);
            });
        }
    }
}
