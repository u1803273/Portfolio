#include <stdio.h>
#include<stdlib.h>
#include <stdbool.h>

// This is a global variable to store the number of squares left to be filled
int emptySquare;

typedef struct Box{
  bool filled;
  int numbers [9];
  // Co-ordinates of the top left corner
  int i,j;
}Box;

// This structure deals with an individual square
typedef struct Square{
  int number;
  int values [9];
  int i;
  int j;
  Box * box;

}Square;


typedef struct Puzzle{
  Square ** sudoku;
  Box * boxes;
}Puzzle;

Square ** createPuzzle();
void printPuzzle(Square ** sudoku);

Square ** solve(Square ** sudoku);
