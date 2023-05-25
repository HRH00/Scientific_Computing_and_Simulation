import java.io.IOException;
import java.util.concurrent.CyclicBarrier;


public class FFTParallel extends Thread{
    
    public static int N = 256 ;
    public static int P = 4;
    int me;
    static CyclicBarrier barrier = new CyclicBarrier(P);  
    final static int B = N/P;
    public static double [] [] X = new double [N] [N] ;
    public static double [] [] CRe = new double [N] [N], CIm = new double [N] [N] ;
    public static double [] [] reconRe = new double [N] [N], reconIm = new double [N] [N] ;
    

    public static void main(String [] args) throws Exception {
        long startTime = System.currentTimeMillis();
  
        try {
            ReadPGM.read(X, "./Labs/Week 1/wolf.pgm", N) ;
        } catch (IOException e) {
            e.printStackTrace();
        }

        FFTParallel [] threads = new FFTParallel [P] ;
        
        for(int me = 0 ; me < P ; me++) {
            threads [me] = new FFTParallel(me) ;
            threads [me].start() ;
        }

        for(int me = 0 ; me < P ; me++) {
            threads [me].join() ;

        }
        //end benchmarking
        long endTime = System.currentTimeMillis();
        //output results
        System.out.println("Calculation completed in "+(endTime - startTime)+" milliseconds");       
        }


        public void run(){
        if(me==0){
            new DisplayDensity(X, N, "Parallel Original Image") ;
        }
        
        int begin = me * B;
        int end = begin + B;



        // create array for in-place FFT, and copy original data to it
        for(int k = begin ; k < end ; k++) {
            for(int l = 0 ; l < N ; l++) {
                CRe [k] [l] = X [k] [l] ;
            }
        }

        sync();   
        fft2d(CRe, CIm, 1,begin,end) ;  // Fourier transform
        sync();
        if(me==0){
        
            new Display2dFT(CRe, CIm, N, "Parallel Discrete FT") ;
        }
        // create array for in-place inverse FFT, and copy FT to it
       
        for(int k = 0 ; k < N ; k++) {
            for(int l = 0 ; l < N ; l++) {
                reconRe [k] [l] = CRe [k] [l] ;
                reconIm [k] [l] = CIm [k] [l] ;
            }
        }


        fft2d(reconRe, reconIm, -1, begin, end) ;  // Inverse Fourier transform

        if(me==0){
            new DisplayDensity(reconRe, N, "Parallel Reconstructed Image") ;                   
            
        }
        }



  
    private static void sync() {
        }
    public static void fft2d(double[][] CRe, double [][] CIm, int number, int begin, int end){
       //For all rows
        for (int i = begin; i < end; i++){
            FFT.fft1d(CRe[i], CIm[i], number);
        }
        
        sync();
        transpose(CRe) ;
        transpose(CIm) ;
        sync();
        //all Columns transposed as rows
        for (int i = begin; i < end; i++){
            FFT.fft1d(CRe[i], CIm[i], number);
        }
        sync();
        transpose(CRe) ;
        transpose(CIm) ;
        sync();
    }

    static void synch() {
    try {
        barrier.await() ;
    }
    catch(Exception e) {
        e.printStackTrace() ;
        System.exit(1) ;
    }
}

    FFTParallel(int me) {
        this.me = me ;
    }

private boolean areMatricesEqual(double[][] matrix1, double[][] matrix2) {
    if (matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length) {
            return false; // Matrices have different dimensions, not equal
        }
        
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                if (matrix1[i][j] != matrix2[i][j]) {
                    return false; // Elements at (i, j) differ, matrices are not equal
                }
            }
        }
        
        return true; // Matrices are equal
    }

    // A function to copy the contents of the Arrays without altering the original reference
    public static double [][] cloneArray(double [][] startArray){
        double [][] copiedArray  = new double[N][N];        
        for (int i = 0; i < N; i++){
            for (int y = 0; y < N; y++){
                copiedArray[i][y] = startArray[i][y];
            }
        }
        return copiedArray;
    }

    static void transpose(double[][] inArray) {
        double[][] temp = new double[N][N]; // Create a new array to store the transpose


        //this loop makes a copy of input array
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                temp[row][column] = inArray[row][column]; 
            }
        }
    
        // perform the transpose 
        for (int row = 0; row < N; row++) {
            for (int column = 0; column < N; column++) {
                inArray[column][row] = temp[row][column];
            }
        }
    }
}