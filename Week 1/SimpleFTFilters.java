public class SimpleFTFilters {
  
    public static int N = 256 ;

    public static void main(String [] args) throws Exception {    

        //start benchmarking 
        long startTime = System.currentTimeMillis();


        double [] [] X = new double [N] [N] ;
        String FileName = "Week 1/wolf.pgm";

        ReadPGM.read(X,FileName,N) ;

        new DisplayDensity(X, N, "Original Image") ;



        double [] [] CRe = new double [N] [N];
        double [] []CIm = new double [N] [N] ;

//////////////   initial FT //////////////////////////

        for(int k = 0 ; k < N ; k++) {
            for(int l = 0 ; l < N ; l++) {
                double sumRe = 0, sumIm = 0 ;
                // Nested for loops performing sum over X elements
                for(int m=0; m < N; m++) {
                    for(int n = 0; n < N; n++) {
                        double arg = -2*Math.PI*(m * k + n * l)/N ;
                        double cos = Math.cos(arg);
                        double sin = Math.sin(arg);
                        sumRe += cos * X [m] [n];
                        sumIm += sin * X [m] [n];
                    }
                }

                CRe [k] [l] = sumRe ;
                CIm [k] [l] = sumIm ;
   
            }
         //   System.out.println("Initial FT |\t" + "Completed FT line " + (k + 1) + " out of " + N) ;
        }

        new Display2dFT(CRe, CIm, N, "Discrete FT") ;
        reconstructFT(CRe, CIm, N, "Initial FT");

//////////////   initial End //////////////////////////




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
        new Display2dFT(
            CReLowPassFilter,
            CImLowPassFilter,
            N,
            "Low Pass Truncated FT");

        reconstructFT(CReLowPassFilter,CImLowPassFilter,N,"Low Pass");
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
        new Display2dFT(
            CReHighPassFilter,
            CImHighPassFilter,
            N,
            "High Pass Truncated FT");

        reconstructFT(CReHighPassFilter, CImHighPassFilter, N, "High Pass");

////////////////       End High Pass Filter      ///////////////////////////
////////////////           End Main             ///////////////////////////

     
        long endTime = System.currentTimeMillis();

        System.out.println("Calculation completed in " +
                        (endTime - startTime) + " milliseconds");  

}
    // Helper Functions - Performs inverse FT to reconsruct images
    public static void reconstructFT(double [][] CRe, double [][] CIm, int N, String title){
        double [] [] reconstructed = new double [N] [N] ;
        for (int m = 0; m <  N; m++) {
            for (int n = 0; n < N; n++) {
                double sumRe=0;
                double sumIm=0;
                double sum=0;
                for (int k = 0; k < N; k++) {
                    for (int l = 0; l < N; l++) {
                        double arg = 2 * Math.PI * (m * k + n * l) / N;
                        double cos = Math.cos(arg);
                        double sin = Math.sin(arg);
                        sumRe += cos * CRe[k][l] - sin * CIm[k][l];
                        sumIm += sin * CRe[k][l] + cos * CIm[k][l];
                    }
                }
                sum =(sumRe-sumIm) / (N*N);
                reconstructed[m][n] = sum;
            }
           // System.out.println(title + " Reconstruct |\t" + "Completed inverse FT line " + (m+1)+ " out of " + N);
            
        }
        new DisplayDensity(reconstructed, N, (title+" Reconstruct")) ;

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
}