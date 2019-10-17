#include <stdio.h>
#include<stdlib.h>
#include <stdbool.h>
#include "sudoku.h"

// ---------- Auxiliary Functions ---------- //

// This function returns the divisor when dividing num1 by num2
int MyOwnDiv(int num1, int num2){
  int count =0;
  while(num1>=num2){
    num1=num1-num2;
    count++;
  }
  return count;
}

// This function takes an array and returns how many elements are non zero
int nonZeroElements(int* array){
  int count=0;
  for (int i=0; i<9; i++){
    if (array[i]!=0){
      count++;
    }
  }
  return count;
}

// This function takes an array and returns the only non zero element
int singleElement(int* array){
  for (int i=0; i<9;i++){
    if (array[i]!=0){
      return array[i];
    }
  }
}

// ---------- These functions are used for updating values within the sudoku ---------- //

// This function is used for updating the value of each squares pointer to point to the new value of the boxes
// Takes in the co-ordinates of the top left square of the box
void updateBoxes(Square ** sudoku, int i, int j, Box box){
  for (int a =0;a<3; a++){
    for (int b=0; b<3; b++){
      *sudoku[i + a][j + b].box = box;
    }
  }
}

// This functin updates the value in a sudoku square and deals with the appropriate variables
// Does not remove from rows, columns or boxes as this will change depending on circumstance
void updateSquare(Square ** sudoku, int i, int j, int number){
  // Assigns the square the value
  sudoku[i][j].number = number;
  // We set the box as we have a new number now
  Box box = *sudoku[i][j].box;
  box.numbers[number-1]=number;
  // Decrements the global var
  emptySquare--;
  // Now we have a new box, we need to update all the squares within the box
  updateBoxes(sudoku,box.i, box.j, box);
}

// ---------- These functions are used for testing ---------- //

// This outputs the contents of the box
void printBox(Box box){
  printf("%d %d %d %d %d %d %d %d %d \n",box.numbers[0], box.numbers[1],box.numbers[2],box.numbers[3],box.numbers[4],box.numbers[5],box.numbers[6],box.numbers[7],box.numbers[8]);
}

// This outputs the contents of a squares values array
void printValues(int * array){
  for (int i=0;i<9;i++){
    printf("%d ",array[i]);
  }
  printf("\n");
}



// This function creates the puzzle
Square ** createPuzzle(){
  // Initialise the global count for the number of empty squares
  emptySquare=0;

  int table[9][9]={
    {0,0,2,   0,0,0,    0,0,3},
    {4,0,1,   0,7,2,    0,9,0},
    {8,0,0,   6,0,0,    1,0,0},
    {0,0,4,   0,9,3,    0,0,1},
    {0,0,0,   0,0,0,    0,0,0},
    {5,0,0,   4,8,0,    7,0,0},
    {0,0,6,   0,0,5,    0,0,9},
    {0,8,0,   7,1,0,    3,0,2},
    {1,0,0,   0,0,0,    8,0,0}

  };

  // This creates all the box objects as a 2d array
  Box ** boxes;
  boxes = (Box **)malloc(sizeof(Box *)*3);
  for (int a =0; a<3;a++){
    boxes[a] = (Box*)malloc(sizeof(Box)*3);
    for (int b=0;b<3;b++){
      boxes[a][b].filled=false;
      boxes[a][b].i=a*3;
      boxes[a][b].j=b*3;
      // We now need to set the boxes array numbers to all 0
      for (int c=0;c<9;c++){
        boxes[a][b].numbers[c]=0;
      }
    }
  }

  Square ** sudoku;
  // Assigns the sudoku the space of 9 Square arrays (one for each of the rows)
  sudoku = (Square**) malloc(sizeof(Square*)*9);
  // Iterates through the rows
  for(int i=0;i<9;i++){
    // For each row, assigns the array the space for 9 squares
    sudoku[i]=(Square*)malloc(sizeof(Square)*9);

    // Iterate through each Square
    for (int j=0; j<9; j++){
      // Assigns the number from the 2d number array to the square array
      sudoku[i][j].number = table[i][j];
      // Assigns the i and j co-ordinates
      sudoku[i][j].i=i;
      sudoku[i][j].j=j;

      // If the value is 0, sets the value array to 1-9 (as could be any number)
      if (sudoku[i][j].number==0){
        for(int k=0;k<9;k++){
          sudoku[i][j].values[k]=k+1;
        }

        // This is a global variable to store how many squares of the entire sudoku are left to be filled in
        emptySquare++;
      }else{
        // If the number is not 0, then we need to update the boxes array to assign a value to it
        int boxi = MyOwnDiv(i,3);
        int boxj = MyOwnDiv(j,3);
        int value = sudoku[i][j].number;
        boxes[boxi][boxj].numbers[value-1] = value;
      }

    }
  }

  // We have iterated through every square and updated the boxes array accordingly. However, none of the squares point to their box yet (this is because if we pointed the topleft square to a box, it would not contain the up-to-date box.values array)
  // This is why we alter all nine boxes, and then point every square to its correct one
  for (int i =0; i<9; i++){
    for (int j=0;j<9;j++){
      // We are now going to assign the square a pointer to the box it is in
      int boxi = MyOwnDiv(i,3);
      int boxj = MyOwnDiv(j,3);
      sudoku[i][j].box = &boxes[boxi][boxj];
    }
  }

  return sudoku;
}

// This function outputs the puzzle to the screen
void printPuzzle(Square ** sudoku){
  // Iterates through the sudoku and outputs the values inside of each square
  int x=0;
  int y=0;
  printf("\n\n------------------------------------\n");
  for (int i=0; i<9;i++){
    if (x==0){
      printf("|");
    }
    for (int j=0; j<9; j++){
      printf (" %d ",sudoku[i][j].number);
      x++;
      if (x==3){
        // Put the dividing line break in to style the sudoku
        printf(" | ");
        x=0;
      }
    }
    y++;
    printf("\n");
    if (y==3){
      printf("------------------------------------\n");
      y=0;
    }
  }
}

// ---------- These functions all take a value and remove it from a construct from the sudoku ---------- //

// Takes a number and removes it from all the squares in the row
void removeRow(Square ** sudoku, int value, int i){
  // We are going to go horizontally along the sudoku and access each square and reduce its values array
  for (int col = 0; col<9;col++){
    if (sudoku[i][col].number==0){
      // The number 1 is stored at index 0 etc
      sudoku[i][col].values[value-1]=0;
    }
  }
}

// Takes a number and removes it from all the squares in the column
void removeColumn(Square ** sudoku, int value, int j){
  for(int row=0; row<9;row++){
    if (sudoku[row][j].number==0){
      sudoku[row][j].values[value-1]=0;
    }
  }
}

// Takes a number and removes it from all the squares in the box
void removeBox(Square ** sudoku, int value, int i, int j){
  // We get the square and then get the box the square points to
  Box box = *sudoku[i][j].box;
  // We get the i and j co-ordinates of the top left hand corner
  int icord = box.i;
  int jcord = box.j;
  // Now we iterate through the 9 squares and subtract the value from each of their values lists
  for (int i=icord; i<icord+3;i++){
    for (int j=jcord;j<jcord+3;j++){
      sudoku[i][j].values[value-1]=0;
    }
  }
}

// This function goes through the sudoku and when it finds a number, subtracts that number from every square in the row, column and box
void resolveSquares(Square ** sudoku){
  // Initialises the global count of the number of squares left to resolve
  emptySquare =0;
  // Goes through each row
  for (int i=0; i<9;i++){
    // Goes through each column
    for (int j=0; j<9; j++){
      // It is an actual number
      if (sudoku[i][j].number!=0){
        // We remove the number from all squares in the row
        removeRow(sudoku, sudoku[i][j].number, i);
        // We remove the number from all squares in the column
        removeColumn(sudoku, sudoku[i][j].number,j);
        // We remove the sudoku from all squares in the box
        removeBox(sudoku, sudoku[i][j].number, i,j);
      }else{
        emptySquare++;
      }
    }
  }
}

// ---------- These functions are all for calculating values within sudoku boxes ---------- //

// This function is used to calculate naked singles
void nakedSingle(Square ** sudoku, int i, int j, Box box){
  for (int a =0;a<3; a++){
    for (int b=0; b<3; b++){
      // There is exactly one possible value the square could be and the value has not been assigned
      if (sudoku[i*3 + a][j*3 +b].number==0 && nonZeroElements(sudoku[i*3 + a][j*3 +b].values)==1){
        int num = singleElement(sudoku[i*3 + a][j*3 +b].values);
        // Updates the square with the new value
        updateSquare(sudoku,i*3+a, j*3+b,num);

        // Now we need to remove it from all rows, columns and boxes
        removeRow(sudoku, num, i*3+a);
        removeColumn(sudoku, num,j*3+b);
        removeBox(sudoku, num, i*3+a,j*3+b);
      }
    }
  }
}

// This function checks a box and tries to calculate some of the missing nine numbers it has to have
void evaluateBox(Square ** sudoku, int i, int j, Box box){
  int possible=0;
  for (int index=0;index<9;index++){
    int value = box.numbers[index];
    // We do not have the number (value +1) in the sudoku
    if (value==0){
      possible=0;
      int tempi,tempj;
      // We now iterate through the nine squares, checking the values array to see if the square can take the value
      for (int a =0;a<3; a++){
        for (int b=0; b<3; b++){
          // If the square is 0 and the index in squares isn't 0, then it can still take that value
          if (sudoku[i*3+a][j*3+b].number==0 && sudoku[i*3 + a][j*3 + b].values[index]!=0){
            possible++;
            tempi=i*3 + a;
            tempj = j*3 +b;
          }
        }
      }
      // Now we've checked every square to see if it can take the number
      // If possible ==1, then only one square could take it, and the co ordinates are stored in tempi and tempj. If possible>1 then more squares could have taken it and so we can do nothing
      if (possible==1){
        // Updates the value in the sudoku
        updateSquare(sudoku,tempi,tempj,index+1);

        // Now we adjust the column and row (we don't need to adjust the box as this was the only square that could have taken the value)
        removeRow(sudoku, index+1, sudoku[tempi][tempj].i);
        removeColumn(sudoku, index+1,sudoku[tempi][tempj].j);
      }

    }

  }
}

// This function is used to try and calculate every square within a box
void evaluateBoxes(Square ** sudoku){
  // To iterate through every box, we go to the top left of each box and take the box that it is pointing to.
  // Then we can go through each square and check the values, and if we can evaluate any squares, we update the box and all other squares on the row and column

  // This iterates box by box
  for (int i=0;i<3;i++){
    for (int j=0;j<3;j++){
      Box box = *sudoku[i*3][j*3].box;
      // We only check the box if it isn't already filled
      if (box.filled==false){
        int a,b;
        // This iterates through the nine squares and checks if any have only one possible value (a naked single)
        nakedSingle(sudoku, i,j,box);
        // This goes through the box and tries to fill in the missing numbers
        evaluateBox(sudoku, i, j, box);

        // Now we've adjusted the box, we check if we have filled it, so that we don't have to readjust it later
        if (nonZeroElements(box.numbers)==9){
          box.filled=true;
        }

      }

    }
  }
}

// ---------- These functions are all for calculating values within sudoku rows ---------- //

// This function goes through the row and tries to calculate values for missing squares
void row(Square ** sudoku, int i){
  int values[9]={0,0,0,0,0,0,0,0,0};
  for (int j=0;j<9;j++){
    if (sudoku[i][j].number!=0){
      values[sudoku[i][j].number-1] = sudoku[i][j].number;
    }
  }

  // Now value stores all number in the row
  // Now we go through each number missing, and for each check all the squares to see if that square can take the value
  Square square;
  int possible=0;
  for (int j=0;j<9; j++){
    possible=0;
    int tempi,tempj;
    // The value of j+1 is missing from the row
    if (values[j]==0){
      // Goes through each square
      for (int k=0;k<9;k++){
        // Square does not have a value and could take the number
        if (sudoku[i][k].number==0 && sudoku[i][k].values[j]==j+1){
          possible++;
          tempi = i;
          tempj= k;
        }
      }
    }

    // If possible ==1, then exactly one square could take the value. We update this and adjust the columns and boxes accordingly (not row as only one square could take the value)
    if (possible==1){
      // Updates the value in the sudoku
      updateSquare(sudoku,tempi,tempj,j+1);
      // Removes from the column and the box
      removeColumn(sudoku,j+1,sudoku[tempi][tempj].j);
      removeBox(sudoku, j+1,sudoku[tempi][tempj].i, sudoku[tempi][tempj].j );

    }
  }
}

// This function is used to try and calculate the missing numbers from a row
void evaluateRow(Square ** sudoku){

  // Iterates through every row
  for (int i=0; i<9;i++){
    // Checks for naked singles
    // nakedSingleRow()
    // Tries to calculate the rest of the numbers
    row(sudoku,i);
  }

}

// ---------- These functions are all for calculating values within sudoku columns ---------- //

// This function goes through the column and tries to calculate values for missing columns
void column(Square ** sudoku, int j){
  int values[9]={0,0,0,0,0,0,0,0,0};
  for(int i=0;i<9;i++){
    if (sudoku[i][j].number!=0){
      values[sudoku[i][j].number-1] = sudoku[i][j].number;
    }
  }

  // Now value stores all numbers in the column
  // Now we iterate through each number missing and see if we can deduce any of the values
  Square square;
  int possible=0;
  for (int i=0;i<9;i++){
    possible=0;
    int tempi,tempj;
    // The value of i+1 is missing from the array
    if (values[i]==0){
      // Goes through each squares
      for (int k=0;k<9;k++){
        // Square does not have a value and could take the number
        if (sudoku[k][j].number==0 && sudoku[k][j].values[i]==i+1){
          possible++;
          tempi = k;
          tempj= j;
        }

      }
    }

    // If possible ==1 then exactly one square could take the value. We update this and adjust the rows and boxes accordingly (not columns as this is the only square that could have taken the value)
    if (possible==1){
      // Updates the value in the sudoku
      updateSquare(sudoku, tempi, tempj, i+1);
      // Removes the value frim the row and the box
      removeRow(sudoku, i+1, sudoku[tempi][tempj].i);
      removeBox(sudoku, i+1, sudoku[tempi][tempj].i, sudoku[tempi][tempj].j);

    }

  }

}

// This function is used to try and calculate the missing numbers from a column
void evaluateColumn(Square ** sudoku){
  // We iterate through every column
  for (int j=0;j<9;j++){
    // Checks for naked singles
    // nakedSingleColumn()
    // Evaluates the rest of the empty squares
    column(sudoku,j);
  }
}



// This function solves the sudoku
Square ** solve(Square ** sudoku){
  // Firstly, we need to go through our sudoku and use the numbers already present to reduce each squares possibility list
  resolveSquares(sudoku);

  int iteration=0;
  // Now, each square holds its number, and if a 0, then the list of all numbers it can be
  // Now we deduce numbers using this information
  while (emptySquare!=0 && iteration<20){
    // This checks all the boxes and tries to fill in the gaps
    evaluateBoxes(sudoku);
    // This checks every row
    evaluateRow(sudoku);
    // This checks every column
    evaluateColumn(sudoku);


    iteration++;
  }

  if (iteration==20){
    printf("failed to complete\n");
  }

  printf("no. of iterations= %d",iteration);
  return sudoku;
}
