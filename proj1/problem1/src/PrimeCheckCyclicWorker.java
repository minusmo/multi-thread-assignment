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
        System.out.println(getName()+" is working.");
        int base = 1; int cycleEnd = Math.floorDiv(numEnd,stride);
        int taskStart; int taskEnd;
        for (int i=0;i<cycleEnd;i++) {
            taskStart = base + taskSize * wid;
            taskEnd = taskStart + taskSize;
            if (taskEnd > numEnd) {taskEnd = numEnd;}
            for (int j=taskStart;j<taskEnd;j++) {
                if (isPrime(j)) {counter.incrementAndGet();}
            }
            base += stride;
        }
        System.out.println(getName()+" is done.");
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
