import java.awt.* ;
import javax.swing.* ;

public class FHP_Color {
    final static int NX = 500, NY =  500 ; // Lattice dimensions, make dimentions less than screen size
    final static int q = 6 ;  // population
    final static int NITER = 2000 ;
    final static int DELAY = 30 ; //ms

    final static double AMBIENT_DENSITY = 0.01;  // initial state, between 0 and 1.0.
    final static double BALLDENSITY = 0.7;  // initial state, between 0 and 1.0.
    static Display display = new Display() ;
    static boolean [] [] [] fin = new boolean [NX] [NY] [q] ;
    static boolean [] [] [] fout = new boolean [NX] [NY] [q] ;

    public static void main(String args []) throws Exception {
      //make some noise!
      for(int i = 0; i < NX ; i++) { 
          for(int j =  0; j < NY ; j++) { 
              boolean [] fin_ij = fin [i] [j] ;
              for(int d = 0 ; d < q ; d++) {
                  if(Math.random() < AMBIENT_DENSITY) {
                      fin_ij [d] = true ;
                  }
              }
          }
       }

        int centerX = NX / 4;  // X-coordinate of the circle center
        int centerY = NY / 2;  // Y-coordinate of the circle center
        int radius = NX / 8;  // Radius of the circle

        for (int i = centerX - radius; i <= centerX + radius; i++) {
            for (int j = centerY - radius; j <= centerY + radius; j++) {
            int distanceX = i - centerX;
            int distanceY = j - centerY;
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
    
            if (distance <= radius) {
                boolean[] fin_ij = fin[i][j];
                for (int d = 0; d < q; d++) {
                    if (Math.random() < BALLDENSITY) {
                        fin_ij[1] = true;
                    }
                }
            }
        }
    }
    

       centerX = (int) ((int) NX/2*1.5);  // X-coordinate of the circle center
    
        for (int i = centerX - radius; i <= centerX + radius; i++) {
        for (int j = centerY - radius; j <= centerY + radius; j++) {
        int distanceX = i - centerX;
        int distanceY = j - centerY;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distance <= radius) {
            boolean[] fin_ij = fin[i][j];
            for (int d = 0; d < q; d++) {
                if (Math.random() < BALLDENSITY) {
                    fin_ij[0] = true;
                }
            }
        }
    }
}


        //fin [NX/2] [NY/2+4] [2] = true ; //vertical collision test
        //fin [NX/2] [NY/2-4] [3] = true ; //vertical collision test

        //fin [NX/2-8] [NY/2+10] [1] = true ; // horizontal collision test
        //fin [NX/2+8] [NY/2+10] [0] = true ; // horizontal collision test

        display.repaint() ;
        Thread.sleep(DELAY) ;

        for(int iter = 0 ; iter < NITER ; iter++) {

            // Collision

            for(int i = 0; i < NX ; i++) { 
                for(int j = 0; j < NY ; j++) { 
                    boolean [] fin_ij = fin [i] [j] ;
                    boolean [] fout_ij = fout [i] [j] ;
                    int pop = 0 ;
                    for(int d = 0 ; d < q ; d++) {
                        if(fin_ij [d]) pop++ ;
                        fout_ij [d] = fin_ij [d] ;
                    }
                    if(pop == 2) {
                        // head on collisions
                        if(fin_ij [0] && fin_ij [1]) {
                            fout_ij [0] = false ;
                            fout_ij [1] = false ;
                            if(Math.random() < 0.5) {
                                fout_ij [2] = true ;
                                fout_ij [3] = true ;
                            }
                            else {
                                fout_ij [4] = true ;
                                fout_ij [5] = true ;
                            }
                        }    
                        if(fin_ij [2] && fin_ij [3]) {
                            fout_ij [2] = false ;
                            fout_ij [3] = false ;
                            if(Math.random() < 0.5) {
                                fout_ij [4] = true ;
                                fout_ij [5] = true ;
                            }
                            else {
                                fout_ij [0] = true ;
                                fout_ij [1] = true ;
                            }
                        }    
                        if(fin_ij [4] && fin_ij [5]) {
                            fout_ij [4] = false ;
                            fout_ij [5] = false ;
                            if(Math.random() < 0.5) {
                                fout_ij [0] = true ;
                                fout_ij [1] = true ;
                            }
                            else {
                                fout_ij [2] = true ;
                                fout_ij [3] = true ;
                            }
                        }    
                    }
                    else if(pop == 4) {
                        // double head on collisions
                        if(!fin_ij [0] && !fin_ij [1]) {
                            fout_ij [0] = true ;
                            fout_ij [1] = true ;
                            if(Math.random() < 0.5) {
                                fout_ij [2] = false ;
                                fout_ij [3] = false ;
                            }
                            else {
                                fout_ij [4] = false ;
                                fout_ij [5] = false ;
                            }
                        }    
                        if(!fin_ij [2] && !fin_ij [3]) {
                            fout_ij [2] = true ;
                            fout_ij [3] = true ;
                            if(Math.random() < 0.5) {
                                fout_ij [4] = false ;
                                fout_ij [5] = false ;
                            }
                            else {
                                fout_ij [0] = false ;
                                fout_ij [1] = false ;
                            }
                        }    
                        if(!fin_ij [4] && !fin_ij [5]) {
                            fout_ij [4] = true ;
                            fout_ij [5] = true ;
                            if(Math.random() < 0.5) {
                                fout_ij [0] = false ;
                                fout_ij [1] = false ;
                            }
                            else {
                                fout_ij [2] = false ;
                                fout_ij [3] = false ;
                            }
                        }    
                    }
                    else if(pop == 3) {
                        // three way collisions
                        if(fin_ij [0] && fin_ij [3] && fin_ij [4]) {
                            fout_ij [0] = false ;
                            fout_ij [1] = true ;
                            fout_ij [2] = true ;
                            fout_ij [3] = false ;
                            fout_ij [4] = false ;
                            fout_ij [5] = true ;
                        }
                        if(fin_ij [1] && fin_ij [2] && fin_ij [5]) {
                            fout_ij [0] = true ;
                            fout_ij [1] = false ;
                            fout_ij [2] = false ;
                            fout_ij [3] = true ;
                            fout_ij [4] = true ;
                            fout_ij [5] = false ;
                        }
                        // head on with spectator
                        if(fin_ij [0] && fin_ij [1]) {
                            fin_ij [0] = false ;
                            fin_ij [1] = false ;
                            if(fin_ij [2] || fin_ij [3]) {
                                fin_ij [4] = true ;
                                fin_ij [5] = true ;
                            }
                            else {
                                fin_ij [2] = true ;
                                fin_ij [3] = true ;
                            }
                        }
                        if(fin_ij [2] && fin_ij [3]) {
                            fin_ij [2] = false ;
                            fin_ij [3] = false ;
                            if(fin_ij [4] || fin_ij [5]) {
                                fin_ij [0] = true ;
                                fin_ij [1] = true ;
                            }
                            else {
                                fin_ij [4] = true ;
                                fin_ij [5] = true ;
                            }
                        }
                        if(fin_ij [4] && fin_ij [5]) {
                            fin_ij [4] = false ;
                            fin_ij [5] = false ;
                            if(fin_ij [0] || fin_ij [1]) {
                                fin_ij [2] = true ;
                                fin_ij [3] = true ;
                            }
                            else {
                                fin_ij [0] = true ;
                                fin_ij [1] = true ;
                            }
                        }
                    }
                }
            }

            // Streaming

            for(int i = 0; i < NX ; i++) { 
                int iP1 = (i + 1) % NX ;
                int iM1 = (i - 1 + NX) % NX ;
                for(int j = 0; j < NY ; j++) { 
                    int jP1 = (j + 1) % NY ;
                    int jM1 = (j - 1 + NY) % NY ;

                    fin [iM1] [j] [0] = fout [i] [j] [0] ;
                    fin [iP1] [j] [1] = fout [i] [j] [1] ;
                    fin [i] [jM1] [2] = fout [i] [j] [2] ;
                    fin [i] [jP1] [3] = fout [i] [j] [3] ;
                    fin [iP1] [jM1] [4] = fout [i] [j] [4] ;
                    fin [iM1] [jP1] [5] = fout [i] [j] [5] ;
                }
            }

            System.out.println("iter = " + iter) ;

            display.repaint() ;
            Thread.sleep(DELAY) ;
        }
    }

    
    static class Display extends JPanel {

        static final double ROW_HEIGHT = Math.sqrt(3) / 2 ;
        final static int CELL_SIZE = 1 ;  
//        (int) 1500/NX ; 

        int displaySizeX = NX ;
        int displaySizeY = (int) (ROW_HEIGHT * CELL_SIZE * NY + 0.5) ;


        public static final int ARROW_START = 2 ;
        public static final int ARROW_END   = 7 ;
        public static final int ARROW_WIDE  = 3 ;

        public static final int DIAG_X_0 = -1 ;
        public static final int DIAG_X_1 = 3 ;
        public static final int DIAG_X_2 = 4 ;
        public static final int DIAG_Y_0 = 4 ;
        public static final int DIAG_Y_1 = 0 ;
        public static final int DIAG_Y_2 = 6 ;

        Display() {

            setPreferredSize(new Dimension(displaySizeX, displaySizeY)) ;

            JFrame frame = new JFrame("HPP-Altered");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(this);
            frame.pack();
            frame.setVisible(true);
        }

        public void paintComponent(Graphics g) {

            g.setColor(Color.yellow) ; //Sets colour of below rect
            g.fillRect(0, 0,  NX /(int) CELL_SIZE , (int) NY / CELL_SIZE ) ; //coordinates and size of background rect



            
            //loop over x and y arrays
            for(int i = 0 ; i < NX ; i++) {
                for(int j = 0 ; j < NY ; j++) {
                    int partical_Density = 0;
                    
                    for (int length = i-3; length <= i+3; length++){
                        for (int height = j-3; height <= j+3; height++){

                            int xVal = (length + NX) % NX ;
                            int yVal = (height + NY) % NY ;

                            for (int particalIndex =0;particalIndex <= q -1 ;particalIndex++){
                                if (fin[xVal][yVal][particalIndex]){
                                    partical_Density++;
                                    break;
                                };
                            }
                        }
                    }
                        
                    //colour selector
                    if (partical_Density<25){
                        int multiplier = 255/25;

                        g.setColor(new Color(0,partical_Density*multiplier,0));
                    }
                    else if(partical_Density<35){
                        
                        int multiplier = 255/35;
                        g.setColor(new Color(partical_Density*multiplier,partical_Density*multiplier,0));
                    }          
                    else{
                        int multiplier = 255/49;
                        g.setColor(new Color(partical_Density*multiplier,0,0));
                    }
                    g.fillRect(i*CELL_SIZE, j*CELL_SIZE, CELL_SIZE, CELL_SIZE) ;


                }
            } 
        }
    }
}

