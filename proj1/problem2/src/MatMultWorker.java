public class MatMultWorker extends Thread {
    final int wid;
    final int[][] a; final int[][] b; final int[][] resultMatrix;
    final int rowStart; final int rowEnd;
    final int colStart; final int colEnd;
    public MatMultWorker(int wid, int[][] a, int[][] b, int[][] resultMatrix, int rowStart, int rowEnd, int colStart, int colEnd) {
        super("wid "+wid);
        this.wid = wid;
        this.a = a;
        this.b = b;
        this.resultMatrix = resultMatrix;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
        this.colStart = colStart;
        this.colEnd = colEnd;
    }

    public void run() {
        System.out.println(getName()+" is working.");
        for (int i=rowStart;i<rowEnd;i++) {
            for (int j=colStart;j<colEnd;j++) {
                resultMatrix[i][j] = a[i][j] * b[i][j];
            }
        }
        System.out.println(getName()+" is done.");
    }
}
