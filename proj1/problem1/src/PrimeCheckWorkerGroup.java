import java.util.concurrent.atomic.AtomicInteger;

public class PrimeCheckWorkerGroup {
    private final int numEnd;
    private final int taskSize;
    private final int totalWorkers;
    private final AtomicInteger counter;
    private final AtomicInteger availableWorkers;

    public PrimeCheckWorkerGroup(int threads, int numEnd, int taskSize, AtomicInteger counter) {
        this.availableWorkers = new AtomicInteger(threads);
        this.numEnd = numEnd;
        this.taskSize = taskSize;
        this.counter = counter;
        this.totalWorkers = threads;
    }
    public void work() {
        int workSize = 0; int wid = 0;
        AtomicInteger numbersChecked = new AtomicInteger(0);
        AtomicInteger numbersLeft = new AtomicInteger(numEnd);
        Thread[] workers = new Thread[totalWorkers];
        for (int i=0;i<totalWorkers;i++) {
            wid = availableWorkers.getAndDecrement() % totalWorkers;
            workers[i] = new PrimeCheckDynamicWorker(wid, numbersChecked, numbersLeft, taskSize, counter);
            workers[i].start();
        }
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {}
        }
    }
}
