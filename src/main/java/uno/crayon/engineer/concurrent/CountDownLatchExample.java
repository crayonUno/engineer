package uno.crayon.engineer.concurrent;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {

    public static void main(String[] args) {
        // Creating a CountDownLatch with a count of 3
        CountDownLatch latch = new CountDownLatch(3);

        // Creating and starting three threads
        WorkerThread worker1 = new WorkerThread("Worker 1", latch);
        WorkerThread worker2 = new WorkerThread("Worker 2", latch);
        WorkerThread worker3 = new WorkerThread("Worker 3", latch);

        // Starting the threads
        worker1.start();
        worker2.start();
        worker3.start();

        try {
            // Main thread waits for the latch to count down to zero
            latch.await();

            // All workers have completed their tasks
            System.out.println("All workers have completed their tasks. Main thread continues.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class WorkerThread extends Thread {
        private final CountDownLatch latch;

        public WorkerThread(String name, CountDownLatch latch) {
            super(name);
            this.latch = latch;
        }

        @Override
        public void run() {
            // Simulating some work
            System.out.println(getName() + " is performing its task.");

            // Decrements the count of the latch
            latch.countDown();

            // Additional work...
        }
    }
}
