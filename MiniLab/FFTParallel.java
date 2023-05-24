import java.io.IOException;
import java.util.concurrent.CyclicBarrier;


public class FFTParallel extends Thread{
    
    public static int N = 256 ;
    public static int P = 4;
    int me;

    FFTParallel(int me) {
        this.me = me ;
    }
    public static void main(String [] args) throws Exception {

        LBMThreaded [] threads = new LBMThreaded [P] ;
        for(int me = 0 ; me < P ; me++) {
            threads [me] = new LBMThreaded(me) ;
            threads [me].start() ;
        }

        for(int me = 0 ; me < P ; me++) {
            threads [me].join() ;

        }

        }


        public void run(){
        System.out.println("Thread "+" Started");

        //start benchmarking
        long startTime = System.currentTimeMillis();


        double [] [] X = new double [N] [N] ;
        
        try {
            ReadPGM.read(X, "Week 1/wolf.pgm", N) ;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        new DisplayDensity(X, N, "Original Image") ;


        // create array for in-place FFT, and copy original data to it
        double [] [] CRe = new double [N] [N], CIm = new double [N] [N] ;
        for(int k = 0 ; k < N ; k++) {
            for(int l = 0 ; l < N ; l++) {
                CRe [k] [l] = X [k] [l] ;
            }
        }

        fft2d(CRe, CIm, 1) ;  // Fourier transform
        
        new Display2dFT(CRe, CIm, N, "Discrete FT") ;

        // create array for in-place inverse FFT, and copy FT to it
        double [] [] reconRe = new double [N] [N],
                     reconIm = new double [N] [N] ;
        for(int k = 0 ; k < N ; k++) {
            for(int l = 0 ; l < N ; l++) {
                reconRe [k] [l] = CRe [k] [l] ;
                reconIm [k] [l] = CIm [k] [l] ;
            }
        }

        fft2d(reconRe, reconIm, -1) ;  // Inverse Fourier transform

        new DisplayDensity(reconRe, N, "Reconstructed Image") ;
        

        //end benchmarking
        long endTime = System.currentTimeMillis();
        //output results
        System.out.println("Calculation completed in " +
                           (endTime - startTime) + " milliseconds");                    
                           
    }


    public static void fft2d(double[][] CRe, double [][] CIm, int number){
       
        //For all rows
        for (int i = 0; i < N; i++){
            FFT.fft1d(CRe[i], CIm[i], number);
        }
        
        transpose(CRe) ;
        transpose(CIm) ;

        //all Columns transposed as rows
        for (int i = 0; i < N; i++){
            FFT.fft1d(CRe[i], CIm[i], number);
        }

        transpose(CRe) ;
        transpose(CIm) ;

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