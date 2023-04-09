import java.util.concurrent.atomic.AtomicInteger;

public class pc_static_cyclic {
    private static int NUM_END = 200000; // default input
    private static int NUM_TREADS = 1;
    private static final int TASK_SIZE = 10;
    // In static load balancing, we use cyclic decomposition.
    // Which divide whole task into subtasks in cyclic way.
    // The size of subtasks are statically defined.
    // And the subtasks are assigned to each thread in cyclic way.
    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_TREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        AtomicInteger counter = new AtomicInteger(0);
        Thread[] workers = assignSubTasks(NUM_END, NUM_TREADS, counter);
        long startTime = System.currentTimeMillis();
        for (Thread worker : workers) {worker.start();}
        try {
            for (Thread worker : workers) {worker.join();}
        } catch (InterruptedException e) {}
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("Program Execution Time: " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END - 1) + " prime# counter=" + counter.get());
    }

    private static Thread[] assignSubTasks(int num_end, int num_threads, AtomicInteger counter) {
        Thread[] workers = new Thread[num_threads];
        int stride = TASK_SIZE * num_threads;
        for (int i=0;i<num_threads;i++) {
            workers[i] = new PrimeCheckCyclicWorker(i,stride,TASK_SIZE,num_end,counter);
        }
        return workers;
    }
}
