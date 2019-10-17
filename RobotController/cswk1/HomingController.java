import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;

public class HomingController implements IRobotController {
    // the robot in the maze
    private IRobot robot;
    // a flag to indicate whether we are looking for a path
    private boolean active = false;
    // a value (in ms) indicating how long we should wait
    // between moves
    private int delay;

    // this method is called when the "start" button is clicked
    // in the user interface
    public void start() {

        this.active = true;

      while(!robot.getLocation().equals(robot.getTargetLocation()) && active) {

        //calling determineHeading will tell the robot which way to go
        int direction = determineHeading();
        //returned an absolute direction
        robot.setHeading(direction);
        //sets the robot on the heading that it needs to go


        //moves the robot forward
        robot.advance();

        // wait for a while if we are supposed to4
        if (delay > 0)
            robot.sleep(delay);


      }//end of while
    }

    // this method returns 1 if the target is north of the
    // robot, -1 if the target is south of the robot, or
    // 0 if otherwise.
    public byte isTargetNorth() {
      int y = robot.getLocation().y;
      //gets the robots current y position
      int target = robot.getTargetLocation().y;
      //gets the target y co-ordinate
      if (y>target){
        //if y is bigger than the destination, the robot is south
        //of the target
        return 1;
      }else if (y==target){
        return 0;
        //on the same latitude
      }else{
        return -1;
        //robot north of target
      }
    }

    // this method returns 1 if the target is east of the
    // robot, -1 if the target is west of the robot, or
    // 0 if otherwise.
    public byte isTargetEast() {
      int x = robot.getLocation().x;
      //gets the robots current x position
      int target = robot.getTargetLocation().x;
      //gets the target x co-ordinate
      if (x>target){
        //if x is bigger than the destination, the robot is east
        //of the target
        return -1;
      }else if (x==target){
        return 0;
        //on the same latitude
      }else{
        return 1;
        //robot west of target
      }

    }

    // this method causes the robot to look to the absolute
    // direction that is specified as argument and returns
    // what sort of square there is
    public int lookHeading(int absoluteDirection) {
        int facing = robot.getHeading();
        //this returns where the robot is looking (or equivalent to the ahead direction)

        if (facing == absoluteDirection){
          //the robot is looking in the same direction to the absolute direction entered
          return robot.look(IRobot.AHEAD);
          //returns the state of the maze directly in front of the robot
        }
        //the while loop later on adds 1 to the direction given (as north + 1 is east etc)
        //but clearly specified in the code is that west + 1 != north
        //this also got me realising that when the direction faced is greater than the
        //absolute direction, adding one to facing means it will never equal absolute,
        //as absolute was initially smaller

        //therefore this code is for this scenario, and SUBTRACTS from facing until it reaches
        //absolute, and then works out how many times it had to subtract

        else if (absoluteDirection<facing){
          int count =0;
          while (facing!= absoluteDirection){
            facing =facing-1;
            count=count+1;
          }
          if (count ==1){
              return robot.look(IRobot.LEFT);
            }else if (count ==2){
              return robot.look(IRobot.BEHIND);
            }else{
              return robot.look(IRobot.RIGHT);
            }

        }
        else{
            //for when facing is less than the absolute direction, adding one will
            //eventually cause the two values to be the same, and then the
            //relative headings can be calculated
            int count=0;
          while (facing != absoluteDirection){
              //in essence, this loop repeats by essentially 'turning' the robot until we
              //find the absolute heading, and then counting how many turns it took,
              //so we know which relative heading to set it to

              facing = facing+1;
              count=count+1;
              //turns the robot to the next direction
          }//end of while

          //at this point, facing equalled the direction the robot should be going
          //and count was the number of turns it took
          //imagine already facing the direction as 0 turns
          //then looking right would require 1 turns
          //looking behind would require 2
          //looking left would require 3

          if (count ==1){
              return robot.look(IRobot.RIGHT);
            }else if (count ==2){
              return robot.look(IRobot.BEHIND);
            }else{
              return robot.look(IRobot.LEFT);


          }//end of IF


        }



        //return IRobot.WALL;
    }

    // this method determines the heading in which the robot
    // should head next to move closer to the target
    public int determineHeading() {
        int directionN =0;
        int directionE=0;
        //given values as i got an initialisation error
        int n = isTargetNorth();
        int e = isTargetEast();
        //therefore n holds which way vertically to go
        //therefore e holds which way horizontally to go

        //we now need to assign the value from isTargetNorth/East to an absolute direction
        switch (n){
            case 1:
              directionN = IRobot.NORTH;
              break;
            case 0:
              directionN = 10; //should be set to null, but as integer, ive set null to 10
              break;
            case -1:
              directionN = IRobot.SOUTH;
              break;

        }

        switch (e){
            case 1:
              directionE = IRobot.EAST;
              break;
            case 0:
              directionE = 10; //should be set to null, but as integer, ive set null to 10
              break;
            case -1:
              directionE = IRobot.WEST;
              break;

        }

        //at this point direction holds the absolute direction the robot needs
        //to travel in.
        //now all we need to do is work out if the robot can move in either of these
        //directions

        //when planning out these scenarios, there are 8 possibilities.
        //a couple of them utilise a random direction generator, and so
        //for efficiency, I declared my own function, randomDirection

        if (directionN==10){//if north is void (only need to go east/west)
          if (lookHeading(directionE)==IRobot.WALL){
            //if the only other direction to move us closer to the target is blocked
            //need random direction
            //randomDirection returns the direction to go in (absolute value)
            return randomDirection(); //returning absolute
          }else{
            //if the direction to the east is not a wall then we need to move that way
            return directionE;  //returns absolute value
          }
        } //end of dN if

        if (directionE==10){//if the east/west is void we only need to move up or down
          //never both void as that is the target
          if (lookHeading(directionN)==IRobot.WALL){
            //going either up or down hits the wall, we need random
            return randomDirection(); //returning absolute value
          }else{
            return directionN;
          }
        }

        if ( (lookHeading(directionN)==IRobot.WALL) & (lookHeading(directionE)==IRobot.WALL) ){
          //both directions to get us closer to the target are walls then we need random
          return randomDirection();
        }else if ( (lookHeading(directionN)==IRobot.WALL) & (lookHeading(directionE)!=IRobot.WALL) ){
          //north is blocked
          //therefore must go east
          return directionE;
        }else if ((lookHeading(directionN)!= IRobot.WALL) & (lookHeading(directionE) == IRobot.WALL)){
          //east is blocked
          //therefore must go north
          return directionN;
        }else{
          //both options are available so you can go either way
          //to prove this is working, use empty maze in the control window
          int rand = (int) Math.round(Math.random());
          if (rand==0){
            return directionN;
          }else{
            return directionE;
          }


        }
    }//end of method


    public int randomDirection(){
      int direction;
      int randno;
      //direction = IRobot.EAST;

      do {
          randno = (int) Math.round(Math.random()*3);

          // change the direction based on the random number
          if (randno == 0){
              direction = IRobot.NORTH;
              //robot.face(direction);
            }
          else if (randno == 1){
              direction = IRobot.EAST;
              //robot.face(direction);
            }
          else if (randno == 2){
              direction = IRobot.SOUTH;
              //robot.face(direction);
            }
          else{
              direction = IRobot.WEST;
              //robot.face(direction);
            }

            //robot.face(direction);
            //moved outside IF structure to increase efficiency


      } while (lookHeading(direction)==IRobot.WALL);

      return direction;
    }

    // this method returns a description of this controller
    public String getDescription() {
       return "A controller which homes in on the target";
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
       active = false;
    }

    // sets the reference to the robot
    public void setRobot(IRobot robot) {
       this.robot = robot;
    }
}
