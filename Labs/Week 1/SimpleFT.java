public class SimpleFT {
  
    public static int N = 256 ;

    public static void main(String [] args) throws Exception {


        

        double [] [] X = new double [N] [N] ;
        String FileName = "Week 1/wolf.pgm";

        ReadPGM.read(X,FileName,N) ;

        new DisplayDensity(X, N, "Original Image") ;

        double [] [] CRe = new double [N] [N];
        double [] []CIm = new double [N] [N] ;


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

            System.out.println("Completed FT line " + (k + 1) + " out of " + N) ;
        }

        new Display2dFT(CRe, CIm, N, "Discrete FT") ;


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
                sum = Math.round((sumRe-sumIm) / (N*N));// used for pixel normalisation, rounded to eliminate floating point error
                reconstructed[m][n] = sum;
            }
            System.out.println("Completed inverse FT line " + (m+1)+ " out of " + N);
            
       }
        new DisplayDensity(reconstructed, N, "Reconstructed Image") ;


         //Outputs the pixel values of the original image and reconstructed image
        boolean floatingPointErrorPresent = false;
        System.out.println("Original\t|\tReconstructed");
        for (int x = 0; x < N; x++){
            for (int y = 0; y < N; y++)

            if (X[x][y]!=reconstructed[x][y]){ //if the reconstruct and original values dont match
                System.out.println(X[x][y]+"\t\t|\t"+ reconstructed[x][y]); //print those values
                floatingPointErrorPresent = true;  //set errorBool to true
            
            }
        }
        //print the error boolean
        System.out.println("Error in Reconstruction:\t"+floatingPointErrorPresent);



    }
}