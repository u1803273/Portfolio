const canvas = document.querySelector(".canvas");
const ctx = canvas.getContext("2d");
const scale = 10
const rows = canvas.height / scale;
const columns = canvas.width / scale;
var snake;
// Stores the high score
var highScore=0;
// Stores the previous direction entered
var dir;

// Stores whether the snake is dead or not
var dead = false;

(function setup() {
  snake = new Snake();
  fruit = new Fruit();
  fruit.pickLocation();


  window.setInterval(() => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    fruit.draw();

    // Gets the next move
    nextMove();

    snake.update();
    snake.draw();

    // check if dead
    if(dead){
      alert("");
      dead=false;
      if(snake.total>highScore){
        document.querySelector(".highScore").innerText = snake.total;
      }
      // Reset the snake
      snake = new Snake();
      fruit = new Fruit();
      dir = "";
      fruit.pickLocation();

    }else{
      // If snake on the square with the fruit
      if (snake.eat(fruit)) {
        fruit.pickLocation();
      }

      snake.checkCollision();

      // Update the score
      document.querySelector('.score').innerText = snake.total;
    }


  }, 100);
}());



/*
window.addEventListener('keydown', ((evt) => {
  const direction = evt.key.replace('Arrow', '');
  snake.changeDirection(direction);
}));
*/



// Calculates the next move
let nextMove = function(){

  // Can move in any of the four directions

  // Score from any of the four choices
  var left =0;
  var up =0;
  var right =0;
  var down =0;

  // Can only move in three directions (cannot go back on yourself)
  var canLeft=true;
  var canRight=true;
  var canUp=true;
  var canDown = true;

  switch(dir){
    case "UP":
      canDown=false;
      break;
    case "DOWN":
      canUp = false;
      break;
    case "LEFT":
      canRight=false;
      break;
    case "RIGHT":
      canLeft=false;
      break;
  }

  // Checks whether that direction is the tail

  var tail = snake.tail;
  for(let i=0;i<tail.length;i++){
    if(tail[i].x==snake.x-scale && tail[i].y==snake.y){
      canLeft=false;
    }
    else if(tail[i].x==snake.x+scale && tail[i].y==snake.y){
      canRight=false;
    }
    else if(tail[i].x==snake.x && tail[i].y==snake.y-scale){
      canUp=false;
    }
    else if(tail[i].x==snake.x && tail[i].y==snake.y+scale){
      canDown=false;
    }
  }

  // Now we calculate the scores for moving in each of the four directions

  // Go left and does not hit the wall
  if(this.x-1 != 0){
    left = Math.abs(snake.x-1-fruit.x) + Math.abs(snake.y-fruit.y);
  }
  // Go right and does not hit a wall
  if(this.x+1 != canvas.width){
    right = Math.abs(snake.x+1-fruit.x) + Math.abs(snake.y-fruit.y);
  }

  // Go up and not hit a wall
  if(this.y -1 != 0){
    up = Math.abs(snake.x-fruit.x) + Math.abs(snake.y-1-fruit.y);
  }

  // Go down and not hit a wall
  if(this.y +1 != canvas.height){
    down = Math.abs(snake.x-fruit.x) + Math.abs(snake.y+1-fruit.y);
  }

  var scores = [];

  // check each isn't 0 and only add score from direction we CAN go
  if(left!=0 && canLeft){scores.push(left);}
  if(right!=0 && canRight){scores.push(right);}
  if(up!=0 && canUp){scores.push(up);}
  if(down!=0 && canDown){scores.push(down);}

  var smallest=Math.min.apply(null, scores)
  var directions = []

  // Have to check we can go in the direction again as may have the same score forward
  // and back, and so can only go forward
  if(left==smallest && canLeft){
    directions.push("Left");
  }
  if(right == smallest && canRight){
    directions.push("Right");
  }
  if(up ==smallest && canUp){
    directions.push("Up");
  }
  if(down == smallest && canDown){
    directions.push("Down");
  }

  // Now chose a random element from directions
  var index = Math.floor(Math.random() * directions.length);
  snake.changeDirection(directions[index]);

}
