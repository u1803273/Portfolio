This is a program to create a sudoku solver.

A sudoku is a puzzle in which every number from 1-9 is present exactly once in each row, column and box.
The program is hardcoded to take a sudoku and then solve it.

//    Plan    //

Takes the sudoku as a 2d array of numbers

Create a struct called
box{
  bool filled;
  int boxNumber (This is used as a coordinate system to determine which box is currently being examined)
  Square* [9]
}

square{
  int number
}

Each box will contain nine squares. A square will contain a value and

array of boxes needing to be iterated over
array of row numbers to be iterated over as well
array of columns to be iterated
Loop (while boxes are not completed){
  check boxes
  check rows
  check columns
}








things to do:
do checkboxes(check the box and remove any numbers from an individual squares possible values)
do check rows
do check columns
add boxes 
