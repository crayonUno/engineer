package uno.crayon.engineer.interview.concurrentprogramming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 子线程循环 10次，接着主线程循环 100次，
 * 接着又回到子线程循环 10次，接着再回到主线程又循环 100次，
 * 如此循环50次，试写出代码。
 */
public class NestedLoop {
    public static void main(String[] args) throws InterruptedException {
        // 假设我们已经知道所有的支付方式类型，保存在一个常量数组中
        final String[] ALL_PAYMENT_TYPES = {"余额", "红包", "余额宝"};
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        // 创建一个线程池，用于执行并发的任务
        ExecutorService executorService = Executors.newFixedThreadPool(ALL_PAYMENT_TYPES.length);

        // 创建一个列表，用于保存所有的支付方式的可用性咨询结果
        List<ConsultResult> allPaymentResults = new ArrayList<>();

        // 创建一个倒计时锁，用于等待所有的任务完成
        CountDownLatch countDownLatch = new CountDownLatch(ALL_PAYMENT_TYPES.length);

        // 遍历所有的支付方式类型，为每个类型创建一个任务，提交到线程池中执行
        for (String paymentType : ALL_PAYMENT_TYPES) {
            // 创建一个任务，调用远程服务的接口，获取可用性咨询结果
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        // 调用远程服务的接口，传入支付方式类型，获取可用性咨询结果
                        ConsultResult result = ConsultResultPaymentRemoteSerivce.isEnabled(paymentType);
                        // 将结果添加到列表中，注意需要同步操作，避免并发问题
                        synchronized (allPaymentResults) {
                            allPaymentResults.add(result);
                        }
                    } catch (Exception e) {
                        // 如果发生异常，打印日志，或者采取其他的处理策略
                        e.printStackTrace();
                    } finally {
                        // 无论成功或失败，都要减少倒计时锁的计数，表示一个任务完成
                        countDownLatch.countDown();
                    }
                }
            };
            // 提交任务到线程池中执行
            executorService.submit(task);
        }

        // 等待所有的任务完成，或者超时，或者被中断
        countDownLatch.await();

        // 关闭线程池，释放资源
        executorService.shutdown();

        // 调用 filterDisablePaynent 方法，过滤不可用的支付方式类型，得到可用支付方式类型列表
        //List<String> availablePaymentList = filterDisablePaynent(allPaymentResults);
        // 返回可用支付方式类型列表，或者进行其他的操作
        //return availablePaymentList;

    }
}
