/*
  setInterval determines the speed: add a slider in that increases/decreases the speed
  and set this value to a var and then change the var depending on the slider
*/

const canvas = document.querySelector(".canvas");
const ctx = canvas.getContext("2d");
// ctx.canvas.width  = window.innerWidth;
//   ctx.canvas.height = window.innerHeight;
const scale = 10;
const rows = canvas.height / scale;
const columns = canvas.width / scale;
var snake;
// Stores the high score
var highScore=0;
// Stores the previous direction entered
var dir;

// Stores whether the snake is dead or not
var dead = false;

// Stores the game board
var board=[];
// Stores the path determined by the a* algorithm
var path=[];
// Stores the list of currently active cells
var openTiles;

// Stores whether the game has just started
var started = true;


// This function is run first
(function setup() {
  snake = new Snake();
  fruit = new Fruit();
  fruit.pickLocation();

  snake.x=0*scale;
  snake.y=3*scale;
  fruit.x=3*scale;
  fruit.y=3*scale;


  // Initialises the priority queue
  openTiles = new PriorityQueue();

  // Update could cause it to die, yet we are still drawing it

  window.setInterval(() => {
     ctx.clearRect(0, 0, canvas.width, canvas.height);
     fruit.draw();

     // If the game has just started then the initial path needs to be generated
     if(started){
        generatePath();
        started = false;
     }

     // Take the next move
     snake.changeDirection(path[0]);
     path.shift();
     snake.update();
     snake.draw();

     // Check if reached the fruit
     if (snake.eat(fruit)) {
       fruit.pickLocation();
       // Generate the new path to the next fruit
       generatePath();
     }

     // Check if hit itself
     snake.checkCollision();

     // Check if the snake has died (either hit a wall or itself)
     if(dead){
       dead=false;

       // Spawn in random location
       snake= new Snake();
       started=true;

       // Set high score
       if(snake.total>highScore){
         document.querySelector('.highScore').innerText = snake.total;
         highScore=snake.total;
       }

       // Clear the variables, so that any current directions left do not affect the next run
       clearVariables();

     }else{
       // Update the score
       document.querySelector('.score').innerText = snake.total;
     }

  }, 10);
}());

// This is used to clear all the variables
let clearVariables = function(){
  // Clears the priority queue
  openTiles.clear();
  // Resets the board array
  clearBoard();
  // resets the path
  path=[];
}

// This function generates the new path
let generatePath=function(){
  // Clears the priority queue
  openTiles.clear();
  // Resets the board array
  clearBoard();
  // Adds the snake to the priority queue
  openTiles.add(board[snake.x/scale][snake.y/scale]);
  // Calculates the path
  findPath();
}

// Generates the path
let findPath = function(){
  // Stores whether the goal has been reached
  var finished=false;

  // While still a value
  while(!openTiles.isEmpty() && finished==false){

    // Extract the minimum value
    var currentTile = openTiles.extractMin();
    // Sets the currentTile to visited
    currentTile.visited= true;

    // Checks to see whether the tile is the goal
    if(currentTile.x==fruit.x/scale && currentTile.y==fruit.y/scale){
      finished=true;
    }
    // Examines each of the eight directions
    else{

      // Checks to see whether the tile is valid, on the board and not previously visited
      var currentX=currentTile.x;
      var currentY=currentTile.y;

      // Left
      if(currentX-1>=0 && board[currentX-1][currentY].valid==true && board[currentX-1][currentY].visited==false){
        console.log("left");
        // Calculate the heuristic
        board[currentX-1][currentY].f = heuristic(currentX-1,currentY);
        board[currentX-1][currentY].visited = true;
        board[currentX-1][currentY].parentX=currentX;
        board[currentX-1][currentY].parentY=currentY;
        openTiles.add(board[currentX-1][currentY]);
      }

      // Top left
      // if(currentX-1>=0 && currentY-1>=0 && board[currentX-1][currentY-1].valid==true && board[currentX-1][currentY-1].visited==false){
      //   console.log("t left");
      //   // Calculate the heuristic
      //   board[currentX-1][currentY-1].f = heuristic(currentX-1,currentY-1);
      //   board[currentX-1][currentY-1].visited=true;
      //   board[currentX-1][currentY-1].parentX=currentX;
      //   board[currentX-1][currentY-1].parentY=currentY;
      //   openTiles.add(board[currentX-1][currentY-1]);
      // }

      // Above
      if(currentY-1>=0 && board[currentX][currentY-1].valid==true && board[currentX][currentY-1].visited==false){
        console.log("above");
        // Calculate the heuristic
        board[currentX][currentY-1].f = heuristic(currentX,currentY-1);
        board[currentX][currentY-1].visited = true;
        board[currentX][currentY-1].parentX=currentX;
        board[currentX][currentY-1].parentY=currentY;
        openTiles.add(board[currentX][currentY-1]);
      }

      // Top right
      // if(currentX+1<rows && currentY-1>=0 && board[currentX+1][currentY-1].valid==true && board[currentX+1][currentY-1].visited==false){
      //   console.log("t right");
      //   // Calculate the heuristic
      //   board[currentX+1][currentY-1].f = heuristic(currentX+1,currentY-1);
      //   board[currentX+1][currentY-1].visited=true;
      //   board[currentX+1][currentY-1].parentX=currentX;
      //   board[currentX+1][currentY-1].parentY=currentY;
      //   openTiles.add(board[currentX+1][currentY-1]);
      // }

      // Right
      if(currentX+1<rows && board[currentX+1][currentY].valid==true && board[currentX+1][currentY].visited==false){
        console.log("right");
        // Calculate the heuristic
        board[currentX+1][currentY].f = heuristic(currentX+1,currentY);
        board[currentX+1][currentY].visited=true;
        board[currentX+1][currentY].parentX=currentX;
        board[currentX+1][currentY].parentY=currentY;
        openTiles.add(board[currentX+1][currentY]);
      }

      // Bottom right
      // if(currentX+1<rows && currentY+1<columns && board[currentX+1][currentY+1].valid==true && board[currentX+1][currentY+1].visited==false){
      //   console.log("b right");
      //   // Calculate the heuristic
      //   board[currentX+1][currentY+1].f = heuristic(currentX+1,currentY+1);
      //   board[currentX+1][currentY+1].visited=true;
      //   board[currentX+1][currentY+1].parentX=currentX;
      //   board[currentX+1][currentY+1].parentY=currentY;
      //   openTiles.add(board[currentX+1][currentY+1]);
      // }

      // Down
      if(currentY+1<columns && board[currentX][currentY+1].valid==true && board[currentX][currentY+1].visited==false){
        console.log("down");
        // Calculate the heuristic
        board[currentX][currentY+1].f = heuristic(currentX,currentY+1);
        board[currentX][currentY+1].visited = true;
        board[currentX][currentY+1].parentX=currentX;
        board[currentX][currentY+1].parentY=currentY;
        openTiles.add(board[currentX][currentY+1]);
      }

      // Bottom left
      // if(currentX-1>=0 && currentY+1<columns && board[currentX-1][currentY+1].valid==true && board[currentX-1][currentY+1].visited==false){
      //   console.log("b left");
      //   // Calculate the heuristic
      //   board[currentX-1][currentY+1].f = heuristic(currentX-1,currentY+1);
      //   board[currentX-1][currentY+1].visited=true;
      //   board[currentX-1][currentY+1].parentX=currentX;
      //   board[currentX-1][currentY+1].parentY=currentY;
      //   openTiles.add(board[currentX-1][currentY+1]);
      // }
    }

  }

  // If finished == true then we found a path to the goal
  if(finished){
    // Create an array of tiles
    var tiles =[];
    // Work backwards
    var currentTile = board[fruit.x/scale][fruit.y/scale];
    tiles.push(currentTile);

    // Adds all tiles to the path, pushing to the front as we're working backwards
    while(!(currentTile.x==snake.x/scale && currentTile.y==snake.y/scale)){
      currentTile=board[currentTile.parentX][currentTile.parentY];
      tiles.unshift(currentTile);
    }

    // Now iterate forward through the array, adding directions of how to traverse between tiles
    currentTile=tiles[0];
    var nextTile;
    for(let i=1;i<tiles.length;i++){
      console.log(tiles[i].x+" "+tiles[i].y);

      nextTile = tiles[i];
      currentX=currentTile.x;
      currentY=currentTile.y;
      var nextX = nextTile.x;
      var nextY = nextTile.y;
      // Check the different directions

      // Left
      if(currentX-1==nextX && currentY==nextY){
        path.push("Left");
      }

      // Top left
      // if(currentX-1==nextX && currentY-1==nextY){
      //   // Can get to this diagonal square via the two intermediary squares
      //   var possibleTiles =[];
      //   if(board[currentX-1][currentY].valid==true){
      //     possibleTiles.push(board[currentX-1][currentY]);
      //   }
      //   if(board[currentX][currentY-1].valid==true){
      //     possibleTiles.push(board[currentX][currentY-1]);
      //   }
      //
      //   var tile;
      //   if(possibleTiles.length==1){
      //     tile=possibleTiles[0];
      //   }else{
      //     var index = Math.floor(Math.random()*2);
      //     tile=possibleTiles[index];
      //   }
      //
      //   // Now tile stores the intermediary tile
      // }

      // Right
      if(currentX+1==nextX && currentY==nextY){
        path.push("Right");
      }

      // Up
      if(currentX==nextX && currentY-1==nextY){
        path.push("Up");
      }

      if(currentX==nextX && currentY+1==nextY){
        path.push("Down");
      }

      currentTile=nextTile;

    }

    console.log("directions:");
    for(let j=0;j<path.length;j++){
      console.log(path[j]);
    }

  }
  // Otherwise there is no path
  else{
    // SURVIVOR MODE
    alert("location of fruit is: "+fruit.x/scale + " "+fruit.y/scale);
    alert(snake.isTail(fruit.x/scale,fruit.y/scale));
  }

}

// Resets the board
let clearBoard = function(x,y){
  board=[];
  // Initialise the game board
  for (let i=0;i<rows;i++){
    var row = [];
    for (let j=0;j<columns;j++){
      var t = new Tile();
      t.x=i;
      t.y=j;
      row.push(t);
    }
    board.push(row);

  }

  // Add the snake's tail into the board (as a wall)
  snake.addTail();
}

// Calculates the heuristic value (euclidean distance)
let heuristic = function(x,y){
  // Counts the number of available squares
  var numSquares = countSquares(x,y);
  console.log(numSquares+" at "+x+" "+y);
  // Calculates the distance between them using pythagorus
  var distance = Math.sqrt(Math.pow(Math.abs(fruit.x/scale-x),2) + Math.pow(Math.abs(fruit.y/scale-y),2));
  // The value is multipled by 10 and then floored
  return Math.floor(distance*10);
}

// Calculates the h cost (distance from square to goal)
let hCost = function(x,y){
  return Math.abs(x-fruit.x/scale) + Math.abs(y-fruit.y/scale);
}

// Calculates the g cost (distance from start to square)
let gCost = function(x,y){
  return Math.abs(x-snake.x/scale) + Math.abs(y-snake.y/scale);
}

// Calculates how many squares the snake can reach from the current tile
var countSquares = function(){
  
}

// Checks the closed Tile list to see if the tile has already been visited
let closedTile=function(closedTiles,xco,yco){
  for(let i=0;i<closedTiles.length;i++){
    if(closedTiles[i].x==xco && closedTiles[i].y==yco){
      return true;
    }
  }
  return false;
}
