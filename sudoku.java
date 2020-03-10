//Solves 9x9 Sudoku (usually takes ~ 1-5 min)
//randomizes starting condition (places numbers 1-9)
//finds random 0 position, tries all possibilities
//successful guesses are saved in gamestates[][][]
//if no possible move (dead-end), load previous save
//goes back n states if n dead-ends accrue ("oops" variable)

import java.util.*;
import java.security.SecureRandom;

public class sudoku {

    //toggle "effects" for visual output, otherwise state progression is written
    static boolean effects = true;
    //prints extra information, yet boards are not printed - unless print() is toggled in code
    static boolean verbose = false;

    //number of saved states and max
    static int s = 0, maxstates = 100;

    //counts accruing failed guesses & resets upon correct guess & determines how far back to load
    static int oops = 0;

    //makes sure guess has passed through all 3 checks (row, col, box)
    static int pass = 0;

    //saves <states> successful-move states
    static int[][][] gamestates = new int[maxstates][9][9];

    //represents the current state
    static int[][] board = new int[9][9];

    public static void main(String[] args) {

        System.out.println("here we attempt to solve sudoku\n");
        generate();
        print();
        //instead of guessing forever, can guess "steps" times
        //int steps = 0;
        //guess until solution is found
        while (true){
            guess();
            //steps++;
        }
    }

    public static void generate() {
        //initializes random starting state
        for (int i=1; i<10; i++) {
            SecureRandom rand = new SecureRandom();
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            board[row][col] = i;
        }
    }

    public static void guess() {
        //fill in a random zero on the board with a guess &
        //resolve correct solutions & re-attempt incorrect ones

        SecureRandom rand = new SecureRandom();
        //positional coordinates &
        //threshold assumes no more zeros left on board
        int X, Y, threshold = 0;
        do {
            X = rand.nextInt(9);
            Y = rand.nextInt(9);
            threshold++;
            if (threshold == 1000) {
                int error = 0;
                //final check for full solution
                //send each coordinate through checker
                for (int i=0; i<9; i++) {
                    for (int j=0; j<9; j++) {
                        error = check(i, j);
                    }
                }
                if (error == 0) System.out.println("SOLVED");
                print(); System.exit(1);
            }
        } while (board[X][Y] != 0);

        //found random empty position
        //now try all possible numbers
        int n=1;
        while (n < 10) {

            board[X][Y] = n;
            if (check(X, Y) == 0) break;
            n++;
        }

        //if check yeilds 0: no errors (save state)
        //yet if n=10, no numbers work (load state)
        if (n==10){
            board[X][Y] = 0;
            //System.out.println("no possible value @ "+X+","+Y+"="+board[X][Y]);
            pass = 0; //reset pass count
            loadstate();
        }
        else {
            //System.out.println("successful guess @ "+X+","+Y+"="+board[X][Y]);
            pass = 0; //reset pass count
            savestate();
        }
    }

    public static int check(int x, int y) {
        //checks the randomized guess for row, col, and box validity

        //check row
        if (dup(x, y,0) == 1) {return 1;}

        //check col
        if (dup(x, y,1) == 1) {return 1;}

        //check box
        //get box parameters
        int xbox = coordinate2box(x); //1,1
        int ybox = coordinate2box(y);

        //set checks for duplicates in O(n)
        Set<Integer> set = new HashSet<>();
        for (int j = (ybox*3 -1); j >= (ybox*3 -3); j--) {

            for (int i = (xbox*3 -1); i >= (xbox*3 -3); i--) {

                if ((board[i][j] != 0) && (set.add(board[i][j]) == false)) {
                    if (verbose) System.out.println("Dup in box " + getbox(x,y) +": "+ board[i][j]);
                    //print();
                    return 1;
                }
            }
        } set.clear();

        //here we make sure the given value passes all 3 checks by insisting on one clean pass
        pass++; //if only first pass, run one clean run, if 2nd pass, return successful choice
        if (pass == 1) {check(x, y);}

        return 0;
    }

    public static void savestate() {
        //saves current state (gamestates[s]) upon correct guess & increments number of states (s)

        if (s < maxstates-1) {
            //copy current state to saved state
            //java's array.clone() method works for load, not for save
            for (int i=0; i<9; i++) {
                for (int j=0; j<9; j++) {
                    gamestates[s][i][j] = board[i][j];
                }
            }

            if (effects) effect();
            else System.out.println("STATE SAVED : s="+s);
            //print();

        }
        else {System.out.println("Saves have been exhausted"); System.exit(1);}

        //update number of saved states & reset fail streak
        s++;
        oops = 0;
    }

    public static void loadstate() {
        //loads saved state & go back number of times messed up consecutively

        oops++;
        //program generally hovers around mid game (s ~ 40)
        if ((s-oops-1) < 0) s = 0;
        else s = s-oops-1;
        board = gamestates[s].clone();

        if (effects) effect();
        else System.out.println("STATE LOADED: s="+s+" oops="+oops);
        //print();

    }

    //utility functions

    public static void print() {
        //prints current game state
        for (int[] row: board) {
            for (int element: row) {
                System.out.print(" " + element + " ");
            }
            System.out.println();
        }
    }

    public static int coordinate2box(int i){
        //returns which 'third' of board given corrdinate is in
        //i.e., (1st 2nd or 3rd row) or (1st 2nd or 3rd col)
        //which is then used to determine which 'box' via getbox()
        int ibox;
        if (i > 2){
            if (i > 5){
                ibox = 3;
            } else ibox = 2;
        } else ibox = 1;
        return ibox;
    }

    public static int getbox(int x, int y) {
        //reference "boxes" are as such:
        // 1  2  3
        // 4  5  6
        // 7  8  9
        //set reference boxes
        int box[][] = new int[3][3];
        int b=1; //box number
        for (int j=0; j<3; j++) {
            for (int i=0; i<3; i++) {
                box[i][j] = b;
                b++;
            }
        }

        //this return() is confusing yes, but it's because of the contrast
        //between how we label boxes (left to right, then top to bottom) and
        //how we start from x (row (top to bottom)) then go to y (col (left to right))
        return box[coordinate2box(y)-1][coordinate2box(x)-1];
    }

    public static int dup(int x, int y, int f) {
        //simply scans row or col for duplicates, with f flag to distinguish
        //returning 1 indicates error, 0 is zero errors / dups

        int error = 0;
        Set<Integer> set = new HashSet<>();

        if (f==0) {
            for (int n = 0; n < 9; n++) {
                if (board[x][n] != 0 && !set.add(board[x][n])) {
                    if (verbose) System.out.println("Dup in row " + x + ": " + board[x][n]);
                    //print();
                    error = 1;
                }
            }
        }
        else {
            for(int n=0; n<9; n++) {
                if (board[n][y] != 0 && !set.add(board[n][y])) {
                    if (verbose) System.out.println("Dup in col " + y + ": " + board[n][y]);
                    //print();
                    error = 1;
                }
            }
        }

        set.clear();

        if (error == 0) return 0;
        else return 1;
    }

    static void effect() {
        //enables visualization for state progression
        for (int i=0; i<s; i++) {
            System.out.print("o");
        } System.out.println();
    }
}
//END
//Johnathan von der Heyde, 2020
