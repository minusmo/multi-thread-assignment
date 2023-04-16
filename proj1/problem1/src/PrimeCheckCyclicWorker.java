import java.util.concurrent.atomic.AtomicInteger;

public class PrimeCheckCyclicWorker extends Thread {
    final int wid;
    final int stride; final int taskSize; final int numEnd;
    final AtomicInteger counter;
    public PrimeCheckCyclicWorker(int wid, int stride, int taskSize, int numEnd, AtomicInteger counter) {
        super("WID "+wid);
        this.wid = wid;
        this.stride = stride;
        this.taskSize = taskSize;
        this.numEnd = numEnd;
        this.counter = counter;
    }

    public void run() {
        int base = 1; int cycleEnd = Math.floorDiv(numEnd,stride);
        int taskStart; int taskEnd; int threads = stride / taskSize;
        System.out.println(getName()+" is working.");
        long startTime = System.currentTimeMillis();
        for (int i=0;i<cycleEnd;i++) {
            taskStart = base + taskSize * wid;
            taskEnd = taskStart + taskSize;
            if (i == cycleEnd-1 && wid == threads-1) {taskEnd = numEnd;}
            for (int j=taskStart;j<taskEnd;j++) {
                if (isPrime(j)) {counter.incrementAndGet();}
            }
            base += stride;
        }
        long endTime = System.currentTimeMillis();
        System.out.println(getName()+" is done.");
        String execTimeMsg = "Execution time of " + getName() + " is " + (endTime - startTime) + "ms";
        System.out.println(execTimeMsg);
    }
    private boolean isPrime(int x) {
        int i;
        if (x <= 1) return false;
        for (i = 2; i < x; i++) {
            if (x % i == 0) return false;
        }
        return true;
    }
}
