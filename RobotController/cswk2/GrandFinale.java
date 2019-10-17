import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;

public class GrandFinale implements IRobotController {
    // the robot in the maze
    private IRobot robot;
    // a flag to indicate whether we are looking for a path
    private boolean active = false;
    // a value (in ms) indicating how long we should wait
    // between moves
    private int delay;

    // 1= explore 0= backtrack
    private int explorerMode;

    private StackFinale stack;

    // Stores the x co-ordinate of start
    private int startX;
    // Stores the y co-ordinate of start
    private int startY;



    // this method is called when the "start" button is clicked
    // in the user interface
    public void start() {
        this.active = true;
        // Sets the co-ordinates of start
        this.startX = this.robot.getLocation().x;
        this.startY = this.robot.getLocation().y;
        // Set explorer mode as we are at the beginning of the run
        this.explorerMode=1;
        if (this.robot.getRuns()==0){
          // Initialises the stack
          this.stack = new StackFinale();

          // This runs if this is the first time through the maze (acts as explorer)
          while(!robot.getLocation().equals(robot.getTargetLocation()) && active) {
            //System.out.println("run");
              run();
          }
          // This outputs the stack once the target is met
          int size = this.stack.getSize();
          for (int i=0; i<size;i++){
            System.out.println("Junction: "+i);
            this.stack.printJunction(i);
            System.out.println("");
            System.out.println("");
          }


        }// When not the first time, then it follows the path created by the first time
        else{
          while(!robot.getLocation().equals(robot.getTargetLocation()) && active) {
            target();
          }
        }
    }

    // This function directs the robot straight to the end
    public void target(){
      // This.stack stores the stack, and every [4] entry is the direction to go at each junction
      int exits = 4 - exits(IRobot.WALL);
      // Only time exit ==1 could be a deadend is at the beginning
      if (exits==1){
        deadEnd();
        robot.advance();
      }else if (exits==2){
        // If at beginning and at a corner, then needs to retrieve the direction we went at the beginning
        if ( (this.robot.getLocation().x == this.startX) && (this.robot.getLocation().y==this.startY) ){
          int direction = this.stack.searchJunctionDirection(robot.getLocation().x, robot.getLocation().y);
          robot.setHeading(direction);
          robot.advance();
        }else{
          corridor();
          robot.advance();
        }
      }// Is a junction (therefore needs stack info)
      else{
        // direction stores the last direction saved in the stack (the one that finally led to the target)
        int direction = this.stack.searchJunctionDirection(robot.getLocation().x, robot.getLocation().y);
        System.out.println("direction: "+direction);
        robot.setHeading(direction);
        robot.advance();
      }
    }

    public void run(){
      // The robot is in explorer mode
      //System.out.println("mode: "  + this.explorerMode);
      if (this.explorerMode==1){
        // Calls the explorer control method
        //System.out.println("exploreControl");
        exploreControl();
        // This has turned the robot into the correct direction
      }else{
        // Go into backtracking mode
        //System.out.println("backtrackControl");
        backtrackControl();
      }
      // Moves the robot forward
      //robot.advance();
      //System.out.println("Size of stack: " + this.stack.getSize());
      // wait for a while if we are supposed to
      if (delay > 0)
          robot.sleep(delay);

    }// End of run


    public void exploreControl(){
      // exits() returns the no. of walls. Therefore, nonwalls is 4- answer
      int exits = 4 - exits(IRobot.WALL);
      // Deadend
      if (exits ==1){
        // There is a deadend. The if statement checks if it is the beginning of the loop, and if so, keeps controller
        // within the explorer control function
        if (this.robot.getLocation().x != 1 | this.robot.getLocation().y != 1){
          this.explorerMode=0;
        }else{
          deadEnd();
          robot.advance();
        }
      }// corridor
      else if (exits==2){
        // This is just a check to see whether at beginning of maze, and if so, then add which direction it went into
        if ( (this.robot.getLocation().x == this.startX) && (this.robot.getLocation().y==this.startY) ){
          corridor();
          stack.recordJunction(robot.getLocation().x, robot.getLocation().y, 0, this.robot.getHeading());
          robot.advance();
        }else{
          if (robot.look(IRobot.AHEAD)==IRobot.BEENBEFORE){
            // Reverse direction
            robot.face(IRobot.BEHIND);
            this.explorerMode=0;
          }else{
            corridorExplore();
            robot.advance();
          }
        }
      }// junction
      else{

        // we solved a similar error in explorer 3 where if returning to a junction, it stores the junction twice (this will affect the backtracking)

        // We haven't been here before, so update Stack
        //if (stack.searchJunctionInitial(robot.getLocation().x, robot.getLocation().y)==-1){
          // This code adds the new junction to the stack (and a 0 for the chosen direction (as there isnt one yet))
        stack.recordJunction(robot.getLocation().x, robot.getLocation().y, robot.getHeading(),0);
        //}
        if (exits(IRobot.PASSAGE)==0){
          this.explorerMode=0;
        }else{
          // Calls the junction method
          junction();
          // As in explorer method, it means there is at least one more passage to explore, and at a junction, it means
          // the previous passage did not lead to the target. Therefore, store the currently facing direction (will
          // change if this direction is bad and then a new direction is tried)
          //System.out.println("direction: "+ robot.getHeading());
          this.stack.setDirection(robot.getHeading());
          //this.stack.printJunction();
          robot.advance();
        }
      }
    }// End of explorerControl

    public void backtrackControl(){
      int exits = 4-exits(IRobot.WALL);
      if (exits==1){
        deadEnd();
        robot.advance();
      }// corridor
      else if (exits==2){
        // Checks to see if we have backtracked to the beginning, and if so, updates the stack entry regarding it
        if ( (this.robot.getLocation().x == this.startX) && (this.robot.getLocation().y==this.startY) ){
          corridor();
          stack.setDirection(this.robot.getHeading());
          robot.advance();
          this.explorerMode=1;
        }else{
          corridor();
          robot.advance();
        }

      }// Junction or crossroads
      else{
        if (exits(IRobot.PASSAGE)>0){
          this.explorerMode=1;
          junction();
          // makes a move, therefore update array
          this.stack.setDirection(robot.getHeading());
          robot.advance();
        }else{

          // Add code in that retrieves the junction from the array and checks how to bacltrack, and then REMOVES
          // the entry from the stack

          int initialHeading = stack.searchJunctionInitial(robot.getLocation().x, robot.getLocation().y);
          //System.out.println("initial heading: "+initialHeading);
          switch (initialHeading){
            case IRobot.NORTH:
              robot.setHeading(IRobot.SOUTH);
              break;
            case IRobot.EAST:
              robot.setHeading(IRobot.WEST);
              break;
            case IRobot.SOUTH:
              robot.setHeading(IRobot.NORTH);
              break;
            case IRobot.WEST:
              robot.setHeading(IRobot.EAST);
              break;
            default:
            // Returned -1
              this.active=false;
          }
          this.stack.removeJunction();
          robot.advance();
        }
      }

    }// End of backtrackControl



public int exits(int lookFor) {
    int count=0;
    // the first direction to be checked is ahead
    int direction=IRobot.AHEAD;
    while (direction<=IRobot.LEFT){
      // if the direction equals lookFor, increment count
      if (robot.look(direction) == lookFor){
        count++;
      }
      // IRobot.AHEAD +1 is IRobot.RIGHt etc, and so will iterate, and
      // check every direction, up to and including IRobot.LEFT
      direction++;
    }
    return count;
}


public void deadEnd(){
  // To allow the start position to be moved anywhere in the maze, the function must consist of a loop that
  // checks all four exits
  int direction=IRobot.AHEAD;
  while (direction<=IRobot.LEFT){
    // if the direction does not equal a wall, face that way
    if (robot.look(direction) != IRobot.WALL){
      robot.face(direction);
      // Breaks so no more iterations
      break;
    }
    direction++;
  }
}

public void corridorExplore(){
  int nonWalls= 4- exits(IRobot.WALL);
  // If ahead is wall, then need to turn left/right
  if (robot.look(IRobot.AHEAD)==IRobot.WALL){
    // At corner
    if (robot.look(IRobot.LEFT) == IRobot.WALL){
      // check if right is a passages
      if (robot.look(IRobot.RIGHT)== IRobot.PASSAGE){
        robot.face(IRobot.RIGHT);
      }// not a passage and not a wall. Therefore a BB so need to backtrack!
      else{
        robot.face(IRobot.BEHIND);
        this.explorerMode=0;
      }
    }// Otherwise, check left
    else{
      if (robot.look(IRobot.LEFT)== IRobot.PASSAGE){
        robot.face(IRobot.LEFT);
      }// not a passage or wall so a BB
      else{
        robot.face(IRobot.BEHIND);
        this.explorerMode=0;
      }
    }
  }// Ahead is not a wall, and so no need to change direction
}


public void corridor(){
  int nonWalls= 4- exits(IRobot.WALL);
  // If ahead is wall, then need to turn left/right
  if (robot.look(IRobot.AHEAD)==IRobot.WALL){
    if (robot.look(IRobot.LEFT) == IRobot.WALL){
      // go right
      robot.face(IRobot.RIGHT);
    }// Otherwise, go left
    else{
      robot.face(IRobot.LEFT);
    }
  }// Ahead is not a wall, and so no need to change direction
}

  // This function deals with either junctions or crossroads
  public void junction(){
    // Holds the number of available passages to exploreControl
    // Will never need 0, as in backtrack control, the backtracking algorithm will be applied
    int passages = exits(IRobot.PASSAGE);
    // Creates an array storing all the possible directions to turn to
    int [] options = findPassages(passages);
    // Creates a random number to select a value from the array
    int average = randomNumber(passages);
    robot.face(options[average]);
  }

  public int[] findPassages(int passages){
    // Makes an array the length of passages (stores the exact number of passages available)
    int [] options = new int[passages];
    int direction = IRobot.AHEAD;
    int count=0;
    // Iterates through all directions
    while (direction <= IRobot.LEFT){
      if (robot.look(direction) == IRobot.PASSAGE){
        // Adds direction where there was a passage to the array
        options[count]= direction;
        count++;
      }
      direction++;
    }
    return options;
  }

  public int randomNumber (int n){
    // Random generates a number between 0 and 1. Multiplying by n gives a random number between
    // 0 and n. Applying the flooring function produces integer results of 0,2...n-1 (indexes of an array)
    int average = (int) Math.floor(Math.random() * n);
    return average;
  }// End of randomNumber

    // this method returns a description of this controller
    public String getDescription() {
       return "A controller which explores the maze in a structured way";
    }

    // sets the delay
    public void setDelay(int millis) {
       delay = millis;
    }

    // gets the current delay
    public int getDelay() {
       return delay;
    }

    // stops the controller
    public void reset() {
       this.stack.resetJunctionCounter();
    }

    // sets the reference to the robot
    public void setRobot(IRobot robot) {
       this.robot = robot;
    }
}
