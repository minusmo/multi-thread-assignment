import java.util.concurrent.atomic.AtomicInteger;

public class pc_dynamic {
    private static int NUM_END = 200000; // default input
    private static int NUM_THREADS = 1;
    private static final int TASK_SIZE = 10;
    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_THREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        AtomicInteger counter = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();
        checkPrimeNumberDynamically(NUM_THREADS,NUM_END,TASK_SIZE,counter);
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("Program Execution Time: " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END - 1) + " prime# counter=" + counter);
    }

    private static void checkPrimeNumberDynamically(int num_threads, int num_end, int taskSize, AtomicInteger counter) {
        PrimeCheckWorkerGroup workerGroup = new PrimeCheckWorkerGroup(num_threads, num_end, taskSize, counter);
        workerGroup.work();
    }
}
