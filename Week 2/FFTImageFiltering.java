public class FFTImageFiltering {

    public static int N = 256 ;

    public static void main(String [] args) throws Exception {

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
    }


    public static void fft2d(double[][] CRe, double [][] CIm, int number){
    }

}