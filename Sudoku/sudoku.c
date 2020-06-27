#include <stdio.h>
#include<stdlib.h>
#include <stdbool.h>
#include "sudoku.h"


// This is going to run the sudoku solver
void main(){
  // This creates the puzzle: the sudoku and a 2d array of the state of the boxes
  Square ** sudoku = createPuzzle();
  printf("\n empty %d\n", emptySquare);
  printPuzzle(sudoku);

  // Now that we have our sudoku, we need to solve it
  sudoku = solve(sudoku);

  printPuzzle(sudoku);
  printf("\n empty %d\n", emptySquare);




}

