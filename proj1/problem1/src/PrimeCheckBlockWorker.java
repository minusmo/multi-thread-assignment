import java.util.concurrent.atomic.AtomicInteger;
    public class PrimeCheckBlockWorker extends Thread {
        final private AtomicInteger counter;
        final private int workStart;
        final private int workEnd;

        public PrimeCheckBlockWorker(int wid, int workStart, int workEnd, AtomicInteger counter) {
            super("WID "+wid);
            this.workStart = workStart;
            this.workEnd = workEnd;
            this.counter = counter;
        }

        public void run() {
            System.out.println(getName() + " is working.");
            for (int i = workStart; i < workEnd; i++) {
                if (isPrime(i)) counter.incrementAndGet();
            }
            System.out.println(getName() + " is done.");
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
