import java.util.*;
import java.lang.*;

// command-line execution example) java MatmultD 6 < mat500.txt
// 6 means the number of threads to use
// < mat500.txt means the file that contains two matrices is given as standard input
//
// In eclipse, set the argument value and file input by using the menu [Run]->[Run Configurations]->{[Arguments], [Common->Input File]}.

// Original JAVA source code: http://stackoverflow.com/questions/21547462/how-to-multiply-2-dimensional-arrays-matrix-multiplication
public class MatmultD
{
    private static Scanner sc = new Scanner(System.in);
    public static void main(String [] args)
    {
        int thread_no=0;
        if (args.length==1) thread_no = Integer.valueOf(args[0]);
        else thread_no = 1;

        int a[][]=readMatrix();
        int b[][]=readMatrix();
        // We use static load balancing approach with multi-threads 2,4,6...32
        int[][] c;
        long startTime = System.currentTimeMillis();
        if (thread_no == 1) {
            c = multMatrix(a,b);
        }
        else {
            c = multiplyMatrixInParallel(a,b, thread_no);
        }
        long endTime = System.currentTimeMillis();

        //printMatrix(a);
        //printMatrix(b);
        printMatrix(c);

        //System.out.printf("thread_no: %d\n" , thread_no);
        //System.out.printf("Calculation Time: %d ms\n" , endTime-startTime);

        System.out.printf("[thread_no]:%2d , [Time]:%4d ms\n", thread_no, endTime-startTime);
    }

    public static int[][] readMatrix() {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = sc.nextInt();
            }
        }
        return result;
    }

    public static void printMatrix(int[][] mat) {
        System.out.println("Matrix["+mat.length+"]["+mat[0].length+"]");
        int rows = mat.length;
        int columns = mat[0].length;
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
//                System.out.printf("%4d " , mat[i][j]);
                sum+=mat[i][j];
            }
//            System.out.println();
        }
        System.out.println();
        System.out.println("Matrix Sum = " + sum + "\n");
    }

    private static int[][] multiplyMatrixInParallel(int a[][], int b[][], int threads) {
        // We use divide and conquer approach for this multiplication.
        // Decompose the matrix into number of threads.
        // For example, 4x4 -> 1x1 with 4 threads.
        // Each section's width and height is calculated in this way.
        final int aRows = a.length;
        final int aCols = a[0].length;
        final int[][] resultMatrix = new int[aRows][aCols];
        Thread[] workers = new Thread[threads];
        int rowStart = 0; int rowEnd;
        final int rowStride = Math.floorDiv(aRows,threads); // 500 / 6 -> 83

        // We divide the whole matrix in 2 parts. left and right.
        // Then divide each part into threads/2 parts again.
        // For example, matrix is 50x50 and 6 threads,
        // Divide 50 columns into 25 and 25 maybe not symmetric when the columns are odd.
        // The divide 50 rows into 16, 16 and 18.
        int wid = 0;
        for (int i=0;i<threads;i++) {
            rowStart = i * rowStride;
            rowEnd = (i != threads-1) ? rowStart + rowStride : aRows;
            workers[wid] = new MatMultWorker(wid, a, b, resultMatrix, aCols, rowStart, rowEnd, 0, aCols);
            workers[wid].start();
            wid++;
        }
        try {
            for (Thread worker : workers) {
                worker.join();
            }
        } catch (InterruptedException e) {}
        return resultMatrix;
    }

    public static int[][] multMatrix(int a[][], int b[][]){//a[m][n], b[n][p]
        if(a.length == 0) return new int[0][0];
        if(a[0].length != b.length) return null; //invalid dims

        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;
        int ans[][] = new int[m][p];

        for(int i = 0;i < m;i++){
            for(int j = 0;j < p;j++){
                for(int k = 0;k < n;k++){
                    ans[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return ans;
    }
}