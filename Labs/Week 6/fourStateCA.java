import java.awt.*;
import javax.swing.*;


// 0 - Resting
// 2 - Excited plateau
// 1 - Recovering
// 3 - Excited Wave front

public class fourStateCA {

    final static int N = 50;
    final static int CELL_SIZE = 5;
    final static int DELAY = 100;

    static int[][] state = new int[N][N];
    static int[][] timeToStateChange = new int[N][N];//create a new array for time variable
    static boolean[][] excitedNeighbour = new boolean[N][N];

    static Display display = new Display();

    public static void main(String args[]) throws Exception {

        // Define initial state - excited bottom row / resting elsewhere.
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                state[i][j] = j == N - 1 ? 3 : 0;
                timeToStateChange[i][j] =  j == N - 1 ? 2 : 0; //set time to 0 where state is excited
            }
        }

        display.repaint();
        pause();

        // Main update loop.
        int iter = 0;
        while (true) {

            System.out.println("iter = " + iter++);

            // Chop wave when half-way up.
            if (iter == N / 2) {
                for (int i = 0; i < N / 2; i++) {
                    for (int j = 0; j < N; j++) {
                        state[i][j] = 0;
                        timeToStateChange[i][j]=0;//update state
                    }
                }

            }

            // Calculate which cells have excited neighbnours.
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {

                    // find neighbours...
                    int ip = Math.min(i + 1, N - 1);
                    int im = Math.max(i - 1, 0);

                    int jp = Math.min(j + 1, N - 1);
                    int jm = Math.max(j - 1, 0);

                    excitedNeighbour[i][j]
                             =
                             //search for excited value 2
                               state[im][jm] == 2 
                            || state[im][j] == 2
                            || state[im][jp] == 2
                            || state[i][jm] == 2
                            || state[i][jp] == 2
                            || state[ip][jm] == 2
                            || state[ip][j] == 2
                            || state[ip][jp] == 2
                            //search for excited value 3
                            || state[im][jm] == 3
                            || state[im][j] == 3
                            || state[im][jp] == 3
                            || state[i][jm] == 3
                            || state[i][jp] == 3
                            || state[ip][jm] == 3
                            || state[ip][j] == 3
                            || state[ip][jp] == 3;


                           // 2 and 3 represent excited
                }
            }

            // Update state.
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {

                    //decrement state towards 0 with each iteration
                    if (state[i][j] != 0){
                       timeToStateChange[i][j] -= 1;
                    }

                    switch (state[i][j]) {
                        case 0:
                            //update if there is excited neighbour AND Time to Change is 0 
                            if (excitedNeighbour[i][j] && (timeToStateChange[i][j]==0)) {
                                state[i][j] = 3;//wave front
                                timeToStateChange[i][j] = 2;//update time
                            }
                            break;
                        case 2:
                            if (timeToStateChange[i][j]==0){
                                state[i][j] = 1;// recovering
                                timeToStateChange[i][j] = 3;//update time
                            }
                            break;
                        case 3:
                            if (excitedNeighbour[i][j] && (timeToStateChange[i][j]==0)) {
                                state[i][j] = 2;// excited wave centre
                                timeToStateChange[i][j] = 3;//update time                        
                            }                                                 
                            break;
                        default: // 1
                            if (timeToStateChange[i][j]==0){
                                state[i][j] = 0; //resting
                            }
                            break;
                    }
                }
            }

            display.repaint();
            pause();
        }
    }

    static class Display extends JPanel {

        final static int WINDOW_SIZE = N * CELL_SIZE;

        Display() {

            setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));

            JFrame frame = new JFrame("Minimal excitable media model");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(this);
            frame.pack();
            frame.setVisible(true);
        }

        public void paintComponent(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (state[i][j] > 0) {
                        if (state[i][j] == 2) {//WAVE CENTRE
                            g.setColor(new Color(126,5,5));
                        }
                        else if (state[i][j] == 3) {//WAVE FRONT
                            g.setColor(new Color(255,10,10));
                        }
                        else {
                            g.setColor(Color.blue);//recovering
                        }
                        g.fillRect(CELL_SIZE * i, CELL_SIZE * j,
                                CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    static void pause() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
