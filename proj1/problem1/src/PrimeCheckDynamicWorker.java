import java.util.concurrent.atomic.AtomicInteger;

public class PrimeCheckDynamicWorker extends Thread {
    private final int wid;
    private final AtomicInteger workDone;
    private final AtomicInteger workLeft;
    private final AtomicInteger counter;
    private final int taskSize;

    public PrimeCheckDynamicWorker(int wid, AtomicInteger workDone, AtomicInteger workLeft, int taskSize, AtomicInteger counter) {
        super("WID "+wid);
        this.wid = wid;
        this.workDone = workDone;
        this.workLeft = workLeft;
        this.counter = counter;
        this.taskSize = taskSize;
    }

    public void run() {
        int workSize;
        long startTime = System.currentTimeMillis();
        System.out.println(getName()+" started job.");
        while (workLeft.get() > 0) {
            System.out.println(getName()+" is working on a task.");
            workSize = Math.min(workLeft.get(), taskSize);
            workLeft.accumulateAndGet(-workSize, Integer::sum);
            int workStart = workDone.getAndAccumulate(workSize, Integer::sum) + 1;
            int workEnd = workStart + workSize;
            for (int i=workStart;i<workEnd;i++) {
                if (isPrime(i)) this.counter.incrementAndGet();
            }
            System.out.println(getName()+" finished a task.");
        }
        long endTime = System.currentTimeMillis();
        System.out.println(getName()+" finished job.");
        String execTimeMsg = "Execution time of " + getName() + " is " + (endTime - startTime) + "ms";
        System.out.println(execTimeMsg);
    }
    private boolean isPrime(int x) {
        int i;
        if (x<=1) return false;
        for (i=2;i<x;i++) {
            if (x%i == 0) return false;
        }
        return true;
    }
}
