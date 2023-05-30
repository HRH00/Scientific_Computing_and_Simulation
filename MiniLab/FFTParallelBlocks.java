import java.util.concurrent.CyclicBarrier ;


public class FFTParallelBlocks extends Thread {
    int me;
    FFTParallelBlocks(int me) {
        this.me = me ;
    }
    public static int N = 256 ;
    //public static int N = 512 ;

    public static int P = 2 ;
    public static int blockSize=N/P;
    static CyclicBarrier barrier = new CyclicBarrier(P) ;

    static double [] [] X = new double [N] [N] ;
    static double [] [] CRe = new double [N] [N], CIm = new double [N] [N] ;
    static double [] [] reconRe = new double [N] [N], reconIm = new double [N] [N] ;

    public static void main(String [] args) throws Exception {



        //start benchmarking
        long startTime = System.currentTimeMillis();


        //ReadPGM.read(X, "MiniLab/wolf.pgm", N) ;
        ReadPGM.read(X, "MiniLab/wolf.pgm", N) ;
        FFTParallelBlocks [] threads = new FFTParallelBlocks [P] ;
        for(int me = 0 ; me < P ; me++) {
            threads [me] = new FFTParallelBlocks(me) ;
            threads [me].start() ;
        }

        for(int me = 0 ; me < P ; me++) {
            threads [me].join() ;
        }

        //end benchmarking
        long endTime = System.currentTimeMillis();
        //output results
        System.out.println("Calculation completed in " +
                           (endTime - startTime) + " milliseconds");                    
                           
    }
    public void run() {
        int startBlock = blockSize*me;
        int endBlock = blockSize+startBlock;

        if (me==0){new DisplayDensity(X, N, "Original Image") ;}


        // create local array for in-place FFT, and copy original data to it
        for(int k = startBlock ; k < endBlock ; k++) {
            for(int l = 0 ; l < N ; l++) {
                CRe [k] [l] = X [k] [l] ;
            }
        }

        sync();

        fft2d(CRe, CIm, 1,me) ;  // Fourier transform
        if (me==0){
            new Display2dFT(CRe, CIm, N, "Discrete FT") ;
            
        }

        // create array for in-place inverse FFT, and copy FT to it

        for(int k = startBlock ; k < endBlock ; k++) {
            for(int l = 0 ; l < N ; l++) {
                reconRe [k] [l] = CRe [k] [l] ;
                reconIm [k] [l] = CIm [k] [l] ;
            }

        
        }

//        output
        fft2d(reconRe, reconIm, -1, me) ;  // Inverse Fourier transform
        if (me==0){new DisplayDensity(reconRe, N, "Reconstructed Image") ;}

 
    }

 public static void fft2d(double[][] CRe, double [][] CIm, int number, int me){
    sync();
    double[][] tempCRe = new double[N][N], tempCIm = new double[N][N]; // Create a new array to store the transpose
    
    for(int k =0 ; k < N ; k++) {
        for(int l = 0 ; l < N ; l++) {
            tempCRe [k] [l] = CRe [k] [l] ;
        }
    }

    for(int k =0 ; k < N ; k++) {
        for(int l = 0 ; l < N ; l++) {
            tempCIm [k] [l] = CIm [k] [l] ;
        }
    }

        //For all rows
        for (int i = 0; i < N; i++){
            FFT.fft1d(tempCRe[i], tempCIm[i], number);
        }
        
        transpose(tempCRe,me) ;
        transpose(tempCIm,me) ;

        //all Columns transposed as rows
        for (int i = 0; i < N; i++){
            FFT.fft1d(tempCRe[i], tempCIm[i], number);
        }

        transpose(tempCRe,me) ;
        transpose(tempCIm,me) ;
        sync();



        for(int k = me*blockSize ; k < (me*blockSize)+blockSize ; k++) {
            for(int l = 0 ; l < N ; l++) {
                CRe [k] [l] = tempCRe [k] [l] ;
                CIm [k] [l] = tempCIm [k] [l] ;
            }
        }

        for(int k =0 ; k < N ; k++) {
            for(int l = 0 ; l < N ; l++) {
                tempCIm [k] [l] = CIm [k] [l] ;
            }
        }
        sync();

    }

  
    static void transpose(double[][] inArray,int me) {
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

    static void sync() {
        try {
            barrier.await() ;
        }
        catch(Exception e) {
            e.printStackTrace() ;
            System.exit(1) ;
        }
    }
}