/*
  This version attempts to solve the problem whereby the robot turns and finds the line, and then still oscilates left and right before stabilising, whereas it should just turn the way it had been successful previosuly
*/
typedef volatile unsigned int ioreg;

//sets up registers so they can be used

#define	PIO_PER		(ioreg *) 0xfffff400	// PIO Enable Registe
#define	PIO_OER	  (ioreg *) 0xfffff410	// Output Enable Register
#define	PIO_SODR  (ioreg *) 0xfffff430	// Set Output Data Register
#define	PIO_CODR  (ioreg *) 0xfffff434	// Clear Output Data Register
#define	PIO_IFER  (ioreg *) 0xfffff420
#define	PMC_PCER  (ioreg *) 0xfffffC10  //this is for clock (needed for later)
#define PIO_PDSR  (ioreg *) 0xfffff43C
// define the constants

#define DELAY (1<<14)
#define LEFT_MOTOR_DRIVE  (1<<0)
#define RIGHT_MOTOR_DRIVE (1<<1)
#define RIGHT_MOTOR_DIRECTION  (1<<2)
#define LEFT_MOTOR_DIRECTION  (1<<3)
#define OPTO (1<<20)
#define OUTPUTS LEFT_MOTOR_DRIVE | RIGHT_MOTOR_DRIVE | RIGHT_MOTOR_DIRECTION | LEFT_MOTOR_DIRECTION
#define INPUTS OPTO

int main (void){
  // Enables pins
  *PIO_PER = OUTPUTS | INPUTS;
  // Enable output drivers
  *PIO_OER = OUTPUTS;
  // Sets inputs
  *PIO_IFER = INPUTS;

  // Call the method control (which will be looped)
  control();
}// End of main

int control (){
  // 0 is left and 1 is right
  int previousTurn= 0;
  // Iterates indefinetely
  while (1){
    // Iterates as long as the robot is on the line
    while ((*PIO_PDSR & OPTO) == OPTO){
      // Calls a method to deal with on the line situations
      onLine();
    }

    // This code runs when the first while loop finished executing (or when the robot has fallen off the line)
    // Iterates as long as the robot is off the line
    while ((*PIO_PDSR & OPTO) != OPTO){
      // We need to rotate the robot to try and find the line. However, on a straight line, the likelihood is that the robot has fallen off the line by a subtle amount, and
      // so will only need a small adjustment to refind. However, we do not know which direction it has fallen, and so we will turn the robot in increasing oscillations until we
      // locate the line, remembering of course to turn the robot twice the way it just went so that it counteracts the turn it took from its initial position
      // This variable acts as a boolean flag

      // rotateCounter acts as a flag to break out of the previousturn==0 if statement, as without the flag, the robot
      // will never not go left, and so it will never rotate right
      int rotateCounter=0;
      int found =0;
      int turn=2;
      int counter;
      // The first time through, turns twice left, and then four right. Calculating the amount needed to turn each amount means that the left turn will always be 2(turn)-2
      // and right will always be 2(turn)
      while (found!=1){
        // If it was left previously turn left first
        int i = 0;
        if ( (previousTurn ==0) && (rotateCounter==0)){
          // rotate the buggy on the spot in the left direction
          counter= (2*turn)-2;
          while ( (i<= counter) && (found!=1)){
            turnRobot (RIGHT_MOTOR_DIRECTION, LEFT_MOTOR_DIRECTION);
            i++;
            // This checks whether the line has been found
            if ((*PIO_PDSR & OPTO) == OPTO){
              found=1;
              previousTurn=0;
            }
          }
          // It did not find the line and so we need to go right
          rotateCounter=1;
        }// else it needs to turn right

          i=0;
          // rotate the buggy on the spot in the right direction
          counter = 2*turn;
          while ( (i<= counter) && (found !=1) ){
            turnRobot(LEFT_MOTOR_DIRECTION, RIGHT_MOTOR_DIRECTION);
            i++;
            if ((*PIO_PDSR & OPTO) == OPTO){
              found=1;
              previousTurn=1;
            }
          }

        turn=turn+15;
      }

    }

  }
}// End of control

int turnRobot(int direction1, int direction2){
  // Sets the two motors, and sets one of the directions CW
  *PIO_SODR = direction1 | RIGHT_MOTOR_DRIVE | LEFT_MOTOR_DRIVE;
  // Sets the other direction ACW (and so the will be opposite and the robot should rotate)
  *PIO_CODR = direction2;
  // Now apply the delay
  delay (DELAY);
  // Now we reverse the affect by clearing the set bits, and setting the cleared bits
  *PIO_CODR = direction1 | RIGHT_MOTOR_DRIVE | LEFT_MOTOR_DRIVE;
  *PIO_SODR = direction2;
  delay (DELAY);

}


// For when the robot is on the line
int onLine(){

  // This is setting all inputs, including the two direction pins. If this fails to work, then try defining a second output
  // variable consisting of soley the drive variables, and like last week, this should drive it forward

  *PIO_SODR = OUTPUTS;
  delay (DELAY);
  *PIO_CODR = OUTPUTS;
  delay (DELAY);
}

void delay(int count)
{
	register int i;
	for (i=count;i>0;i--)
       		;
}
