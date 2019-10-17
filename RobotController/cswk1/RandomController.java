import uk.ac.warwick.dcs.maze.logic.*;
import java.awt.Point;

public class RandomController implements IRobotController {
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

        // loop while we haven't found the exit and the agent
        // has not been interrupted
        while(!robot.getLocation().equals(robot.getTargetLocation()) && active) {
            //int rand = (int)Math.round(Math.random()*3);

            int av = (int)Math.round(Math.random()*100);
            //creates a new variable called av and sets it to a random value
            //0 and 100

            if ((robot.look(IRobot.AHEAD)==IRobot.WALL) | (av<13)){
              //this code runs if the wall ahead is a wall (to generate new
              // direction) or if its the time for the random change


            int rand;
            do { //the code contained within here generates the direction and
                 //turns the robot to face that way while the condition checks
                 //to see if the direction is allowed

              // generate a random number between 0-3 (inclusive)
              //updated to create a value between 0-100
              rand = (int)Math.round(Math.random()*100);



            // turn into one of the four directions, as determined
            // by the random number that was generated:
            // 0: ahead     old probability:16%     new: 25%
            // 1: left      old probability: 33%    new: 25%
            // 2: right     old probability: 33%    new: 25%
            // 3: behind    old probability: 16%    new: 25%

            //switch (rand) {
            if (rand<25){
                robot.face(IRobot.AHEAD);
                robot.getLogger().log(IRobot.AHEAD);
                //break;
            }else if ((rand<50)&(rand>=25)){
                robot.face(IRobot.LEFT);
                robot.getLogger().log(IRobot.LEFT);
                //break;
            }else if ((rand<75)&(rand>=50)){
                robot.face(IRobot.RIGHT);
                robot.getLogger().log(IRobot.RIGHT);
                //break;
            }else{
                robot.face(IRobot.BEHIND);
                robot.getLogger().log(IRobot.BEHIND);
                //break;
            }//end of switch

          }while (robot.look(IRobot.AHEAD)==IRobot.WALL);
          //end of do while loop

        }//end of if(robot.look(IRobot.AHEAD)==IRobot.WALL)

        //there is no else, as all else code is simply move forward, which
        //needs to run after the if clause is executed anyways


            // move one step into the direction the robot is facing
            robot.advance();


            //this code is used for the logging of committed steps
            //that is steps that have been taken
            //this is different in value to simply direction generated
            //this is not random as is affected by wall placement

            /*
            switch (rand){
            case 0:
                robot.getLogger().log(IRobot.AHEAD);
                break;
            case 1:
                robot.getLogger().log(IRobot.LEFT);
                break;
            case 2:
                robot.getLogger().log(IRobot.RIGHT);
                break;
            case 3:
                robot.getLogger().log(IRobot.BEHIND);
                break;
          }//end of switch for direction logged
          */




            // wait for a while if we are supposed to
            if (delay > 0)
                robot.sleep(delay);
       }
    }

    // this method returns a description of this controller
    public String getDescription() {
       return "A controller which randomly chooses where to go";
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
