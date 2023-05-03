public class FFTImage {

    public static int N = 256 ;

    public static void main(String [] args) throws Exception {

        //start benchmarking
        long startTime = System.currentTimeMillis();


        double [] [] X = new double [N] [N] ;
        ReadPGM.read(X, "Week 1/wolf.pgm", N) ;

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

        // Function to compare 2D double arrays
        public static boolean compare2DArrays(double[][] array1, double[][] array2) {
            // Check if the arrays have the same number of rows and columns
            if (array1.length != array2.length || array1[0].length != array2[0].length) {
                return false;
            }
    
            // Iterate through each element and compare their values
            for (int i = 0; i < array1.length; i++) {
                for (int j = 0; j < array1[0].length; j++) {
                    if (array1[i][j] != array2[i][j]) {
                        return false;
                    }
                }
            }
    
            return true; // Arrays are equal
        }

}