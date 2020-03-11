// - GRID GAME - has you move around a 2D grid, and try to reach the goal.
// Moving out of bounds randomizes your position and the goal's position.
// And there's a move limit, so it might be necessary to take the risk.
// Sometimes there are multiple goals, but only one is real.
// Highscore is displayed if you lose. 
// How many games can you win? Have fun!

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define X 12                // board dimensions (do not adjust)
#define Y 12                // X == row, Y == collum
#define LIMIT 9             // adjust move limit for difficutly

int main() {

    int i, j;               // indexes
    int x, y;               // player position
    int m, n;               // goal position
    srand(time(NULL));      // random start
    int limit = 0;          // move limit count
    int score = 0;          // highscore game count

 // set the board

    char* board[X][Y];

    for (i = 1; i < X-1; i++) {             // first and last X and Y left blank
        for (j = 1; j < Y-1; j++) {         // for boundary conditions
            board[i][j] = " ☖ ";
        }
    }

    START:                                  // out-of-bound move resets here
 
 // start position

    x = (rand() % 10);
    y = (rand() % 10);

    board[x][y] = " ☗ ";

 // goal position

    m = (rand() % 10);
    n = (rand() % 10);

    board[m][n] = " ♡ ";

    UPDATE:                                 // in-bound move resets here

 // boundary conditions

    if ((((x > 0) && (x < 11)) && ((y > 0) && (y < 11)))
     && (((m > 0) && (m < 11)) && ((n > 0) && (n < 11)))) {

        for (i = 1; i < X-1; i++) {
            for (j = 1; j < Y-1; j++) {
                printf("%s", board[i][j]);
            }
            printf("\n");
        }
        
     // movement loop

        while (limit < LIMIT) {
             
            char move;    
            printf("\nUse 'w a s d' to move around the board\n");
           
            scanf(" %c", &move);
            
         // adjust x y based on the player's move

            if (move == 'w') 
                x--;
            else if (move == 's') 
                x++;
            else if (move == 'a')
                y--;
            else if (move == 'd')
                y++;
            else {return 0;}

            board[x][y] = " ☗ ";

            limit++;                                // add one to move limit

         // reaching goal

            if ((m==x) && (n==y)) {

                score++;                            // add one to score
                limit = 0;                          // reset move limit        

                board[x][y] = " ☆ ";                // reprint board with star

                for (i = 1; i < X-1; i++) {
                    for (j = 1; j < Y-1; j++) {
                        printf("%s", board[i][j]);
                    }
                    printf("\n");
                }

                printf("\n!YOU WIN! ---- Play agin? (y=yes!)\n");

                char play;
                scanf(" %c", &play);

                if (play == 'y') {
                    
                    for (i = 1; i < X-1; i++) {     // reset board
                        for (j = 1; j < Y-1; j++) {
                            board[i][j] = " ☖ ";
                        }
                    }

                    goto START;                     // start new game
                }
                
                else {return 0;}                    // quit if not 'yes'
            }
         // not yet reaching goal
           
            goto UPDATE;                            // update board for next move
        }
    }

    else {goto START;}                              // if out of bounds, re-randomize player and goal

 // running out of moves

    printf("\n!YOU LOSE! ---- HIGHSCORE = %d\n\n", score);

    board[x][y] = " ✕ ";                            // reprint board with cross

    for (i = 1; i < X-1; i++) {
        for (j = 1; j < Y-1; j++) {
            printf("%s", board[i][j]);
        }
        printf("\n");
    }
    
    printf("\n\n");

    return 0;
}

// program has minor bugs - all due to the random function's potential for overflow
