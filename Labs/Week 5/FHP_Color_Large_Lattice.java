
import java.awt.* ;


import javax.swing.* ;

public class FHP_Color_Large_Lattice {

    //set these 
    static int Lattice_Size = 3000; // make multiple ofdisplaySizeX, such that NX / displaySizeX is odd
    final static int Display_Size = 1000; //Window size Pixels

    //Delay between each iteration
    final static int DELAY = 0 ; //ms

    //set partical densitys
    final static double AMBIENT_Partical_DENSITY = 0;  // initial state, between 0 and 1.0.
    final static double BALLDENSITY = .7;  // initial state, between 0 and 1.0. 

    //Makes one of the circles a vaccum
    final static boolean MAKE_A_VACUUM_CIRCLE = false;

    final static double Color_Low_Cutoff = 0.3; //must be between 0 and 1, MUST be lower than Color_Med_Cutoff
    final static double Color_Med_Cutoff  = 0.9; //must be between 0 and 1

    final static int displaySizeX = Display_Size;
    final static int displaySizeY = Display_Size;
    final static int NITER = 1000 ;

    static int NX =  Lattice_Size ; // Lattice dimensions 
    static int NY =  Lattice_Size ; // Lattice dimensions 
    static boolean error = false;
    

    final static int q = 6 ;  // population

    static Display display = new Display() ;

    static boolean [] [] [] fin = new boolean [NX] [NY] [q] ;
    static boolean [] [] [] fout = new boolean [NX] [NY] [q] ;

    public static void main(String args []) throws Exception {

    //normalise the lattice size
    if (((NX / displaySizeX) % 2 == 0) || (NX % displaySizeX !=0)){
        int multipleLatticeBy = (int) (Math.round((double) NX / displaySizeX));
        if (multipleLatticeBy % 2 == 0 ){
            multipleLatticeBy++;
        }
        error = true;
        System.out.println("Lattice Dimentions bad, Set NX =\t"+displaySizeX*multipleLatticeBy);
    }
    else if (NX<Display_Size) {
        error = true;
        System.out.println("Lattice Size to small, set to:\t"+Display_Size*3);
    }
    else{System.out.println("Lattice size is good!");}

      //make some noise!
      for(int i = 0; i < NX ; i++) { 
          for(int j =  0; j < NY ; j++) { 
              boolean [] fin_ij = fin [i] [j] ;
              for(int d = 0 ; d < q ; d++) {
                    if(Math.random() < AMBIENT_Partical_DENSITY){
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

    //Draw Circle on right Side
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
    
    if(MAKE_A_VACUUM_CIRCLE){
        radius = (int) (NX/5);

        for (int i = centerX - radius; i <= centerX + radius; i++) {
            for (int j = centerY - radius; j <= centerY + radius; j++) {
                int distanceX = i - centerX;
                int distanceY = j - centerY;
                double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                if (distance <= radius) {
                    boolean[] fin_ij = fin[i][j];
                    for (int d = 0; d < q; d++) {
                            fin_ij[d] = false;
                    }
                }
            }
        }   
    }


        display.repaint() ;
        Thread.sleep(DELAY) ;
        if (!error){
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
        }}
        else{
            System.exit(0); // error case
        }
    }

    
    static class Display extends JPanel {


        final static int CELL_SIZE = NX / displaySizeX ; 
        final static int MAX_PARTICAL_DENSITY = CELL_SIZE*CELL_SIZE;
        
        final static int Cutoff_Low = (int) (MAX_PARTICAL_DENSITY * Color_Low_Cutoff);
        final static double Multiplier_Low =  (255.0/Cutoff_Low);
        
        final static int Cutoff_Medium = (int) (MAX_PARTICAL_DENSITY * Color_Med_Cutoff);
        final static double Multiplier_Medium = 255.0/Cutoff_Medium;
        
        final static double Multiplier_High = 255.0/MAX_PARTICAL_DENSITY;

        Display() {

            setPreferredSize(new Dimension(displaySizeX, displaySizeY)) ;

            JFrame frame = new JFrame("HPP-Altered-Large-Lattice");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(this);
            frame.pack();
            frame.setVisible(true);
        }



        public void paintComponent(Graphics g) {



            g.setColor(Color.yellow) ; //Sets colour of below rect
            g.fillRect(0, 0,  displaySizeX/1, displaySizeY/1) ; //coordinates and size of background rect

          

            g.setColor(Color.black) ; //Sets colour of below rect
            int start_Cell_X;
            int start_Cell_Y;
            int end_Cell_X;
            int end_Cell_Y  ;

            //for each pixel
            for(int i = 0 ; i < displaySizeX ; i++) {
                for(int j = 0 ; j < displaySizeY; j++) {
                    int partical_Density = 0;

                    
                    //iterate over the fin Array
                    start_Cell_X = i * CELL_SIZE;
                    start_Cell_Y = j * CELL_SIZE;
                    end_Cell_X = start_Cell_X + CELL_SIZE ;
                    end_Cell_Y = start_Cell_Y + CELL_SIZE ;
                    //count density
                    for (int x = start_Cell_X; x < end_Cell_X; x++){
                        for (int y = start_Cell_Y; y < end_Cell_Y; y++){                         

                            for (int state = 0; state <= q -1 ;state++){
                                if (fin[x][y][state]){
                                    
                                    partical_Density++;
                                    break;
                                };
                            }
                        }
                    }


                    
                    if(partical_Density <=Cutoff_Low){
                        g.setColor(new Color(0,(int) (partical_Density*Multiplier_Low),0));}
                    else if(partical_Density <=Cutoff_Medium){
                        g.setColor(new Color((int)(partical_Density*Multiplier_Medium),(int)(partical_Density*Multiplier_Medium),0));}
                    else{
                        int redValue=((int)(partical_Density*Multiplier_High));
                        g.setColor(new Color((int) (redValue),0,0));
                 
                    }


                    g.fillRect(i, j, 10,10);



                }
            }

        }
    }
} 