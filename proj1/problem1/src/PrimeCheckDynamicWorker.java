import java.util.concurrent.atomic.AtomicInteger;

public class PrimeCheckDynamicWorker extends Thread {
    private final int wid;
    private final PrimeCheckWorkerGroup primeCheckWorkerGroup;
    private final int workDone;
    private final AtomicInteger counter;
    private final int workSize;

    public PrimeCheckDynamicWorker(int wid, PrimeCheckWorkerGroup primeCheckWorkerGroup, int workDone, int workSize, AtomicInteger counter) {
        super("WID "+wid);
        this.wid = wid;
        this.primeCheckWorkerGroup = primeCheckWorkerGroup;
        this.workDone = workDone;
        this.workSize = workSize;
        this.counter = counter;
    }

    public void run() {
        System.out.println(getName()+" is working.");
        int workStart = workDone + 1;
        int workEnd = workStart + workSize;
        long startTime = System.currentTimeMillis();
        for (int i=workStart;i<workEnd;i++) {
            if (isPrime(i)) this.counter.incrementAndGet();
        }
        long endTime = System.currentTimeMillis();
        this.primeCheckWorkerGroup.rest();
        System.out.println(getName()+" is done.");
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
