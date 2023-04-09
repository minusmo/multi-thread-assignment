import java.util.concurrent.atomic.AtomicInteger;

public class pc_static_block {
    private static int NUM_END = 200000; // default input
    private static int NUM_TREADS = 1;
    // In static load balancing, we use block decomposition.
    // Which divide whole task into subtasks.
    // The size of subtasks are statically defined.
    // Which is calculated by (task size) / (number of workers(threads))
    public static void main(String[] args) {
        if (args.length == 2) {
            NUM_TREADS = Integer.parseInt(args[0]);
            NUM_END = Integer.parseInt(args[1]);
        }
        AtomicInteger counter = new AtomicInteger(0);
        int[] workloads = divideIntoSubtasks(NUM_END, NUM_TREADS);
        Thread[] workers = createWorkers(workloads, counter);
        long startTime = System.currentTimeMillis();
        for (Thread worker : workers) {
            worker.start();
        }
        try {
            for (Thread worker : workers) {
                worker.join();
            }
        } catch (InterruptedException e) {}
        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;
        System.out.println("Program Execution Time: " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END - 1) + " prime# counter=" + counter.get());
    }

    private static int[] divideIntoSubtasks(int num_end, int num_threads) {
        int[] workloads = new int[num_threads];
        for (int i = 0;i < num_threads;i++) {
            int partitionEnd = (num_end/num_threads) * (i+1);
            if (i == num_threads-1 && partitionEnd < num_end) {partitionEnd = num_end;}
            workloads[i] = partitionEnd;
        }
        return workloads;
    }

    private static Thread[] createWorkers(int[] subTasks, AtomicInteger counter) {
        int workStart = 1; int workEnd;
        Thread[] workers = new Thread[subTasks.length];
        for (int i=0;i<subTasks.length;i++) {
            if (i!=0) {workStart = subTasks[i-1]+1;}
            workEnd = subTasks[i];
            workers[i] = new PrimeCheckBlockWorker(i, workStart, workEnd, counter);
        }
        return workers;
    }

}
