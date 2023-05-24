public class FFTImageFiltering {

    public static int N = 600 ;

    public static void main(String [] args) throws Exception {

        //start benchmarking
        long startTime = System.currentTimeMillis();


        double [] [] X = new double [N] [N] ;
        ReadPGM.read(X, "Week 1/apollonian_gasket.ascii.pgm", N) ;

        new DisplayDensity(X, N, "Original Image") ;


        // create array for in-place FFT, and copy original data to it
        double [] [] CRe = new double [N] [N], CIm = new double [N] [N] ;
        for(int k = 0 ; k < N ; k++) {
            for(int l = 0 ; l < N ; l++) {
                CRe [k] [l] = X [k] [l] ;
            }
        }

        fft2d(CRe, CIm, 1) ;  // Fourier transform
            //display initial FT
        new Display2dFT(CRe, CIm, N, "Init Discrete FT") ;



        // create array for in-place inverse FFT, and copy FT to it
        double [] [] reconRe = new double [N] [N], reconIm = new double [N] [N] ;

        reconRe = cloneArray(CRe, N);
        reconIm = cloneArray(CIm, N);


        fft2d(reconRe, reconIm, -1) ;  // Inverse Fourier transform
        //display initial FT
        new DisplayDensity(reconRe, N, "Initial reconstruction");



        

////////////////       Start Low Pass Filter      ///////////////////////////
        //copy contents of Arrays to new varible 
        double [][] CReLowPassFilter = cloneArray(CRe, N);
        double [][] CImLowPassFilter = cloneArray(CIm, N);
      
        int cutoff = N/8 ;  // for example
        for(int k = 0 ; k < N ; k++) {
            int kSigned = k <= N/2 ? k : k - N ;
            for(int l = 0 ; l < N ; l++) {
                int lSigned = l <= N/2 ? l : l - N ;
                if(Math.abs(kSigned) > cutoff || Math.abs(lSigned) > cutoff) {
                    CReLowPassFilter [k] [l] = 0 ;
                    CImLowPassFilter [k] [l] = 0 ;
                }
            }
        }

        double [][] CReLowPassFilterTruncated = cloneArray(CReLowPassFilter, N);
        double [][] CImLowPassFilterTruncated = cloneArray(CImLowPassFilter, N);
      
        new Display2dFT(CReLowPassFilterTruncated,CImLowPassFilterTruncated,N,"Low Pass Truncated FT");

  
        fft2d(CReLowPassFilter, CImLowPassFilter, -1);
        new DisplayDensity(CReLowPassFilter, N, "Low Pass Recon");

////////////////       End Low Pass Filter      ///////////////////////////
////////////////       Start High Pass Filter      ///////////////////////////
    //copy contents of Arrays to new varible 
    double [][] CReHighPassFilter = cloneArray(CRe, N);
    double [][] CImHighPassFilter = cloneArray(CIm, N);   
    
    cutoff = N/128;  // for example
    for(int k = 0 ; k < N ; k++) {
        int kSigned = k <= N/2 ? k : k - N ;
        for(int l = 0 ; l < N ; l++) {
            int lSigned = l <= N/2 ? l : l - N ;
            if(Math.abs(kSigned) <= cutoff || Math.abs(lSigned) <= cutoff) {
                CReHighPassFilter [k] [l] = 0 ;
                CImHighPassFilter [k] [l] = 0 ;
            }
        }
    }

    // Output Images
    double [][] CReHighPassFilterTruncated = cloneArray(CReHighPassFilter, N);
    double [][] CImHighPassFilterTruncated = cloneArray(CImHighPassFilter, N);   

    new Display2dFT(CReHighPassFilterTruncated,CImHighPassFilterTruncated,N,"High Pass Truncated FT");

    fft2d(CReHighPassFilter, CImHighPassFilter, -1);
    new DisplayDensity(CReHighPassFilter,N,"High Pass Recon");
////////////////       End High Pass Filter      ///////////////////////////

        //end benchmarking
        long endTime = System.currentTimeMillis();
        //output results
        System.out.println("Calculation completed in " +
                           (endTime - startTime) + " milliseconds");                    
    }

    // A function to copy the contents of the Arrays without altering the original reference
    public static double [][] cloneArray(double [][] startArray, int N){
        double [][] copiedArray  = new double[N][N];        
        for (int i = 0; i < N; i++){
            for (int y = 0; y < N; y++){
                copiedArray[i][y] = startArray[i][y];
            }
        }
        return copiedArray;
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