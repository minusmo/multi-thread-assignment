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
    public synchronized void work() {
        int workSize = 0; int wid = 0;
        int numbersChecked = 0;
        while (numbersChecked < (this.numEnd-1)) {
            workSize = Math.min((this.numEnd - 1 - numbersChecked), this.taskSize);
            if (availableWorkers.get() == 0) {
                try {wait();}
                catch (InterruptedException e) {}
            }
            else {
                wid = availableWorkers.getAndDecrement() % totalWorkers;
                PrimeCheckDynamicWorker worker = new PrimeCheckDynamicWorker(wid,this, numbersChecked, workSize, counter);
                worker.start();
                numbersChecked += workSize;
            }
        }
    }

    public synchronized void rest() {
        this.availableWorkers.incrementAndGet();
        notify();
    }
}
