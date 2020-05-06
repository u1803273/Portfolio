function Fruit() {
  this.x;
  this.y;

  // generate a random location that is not the location of the snake
  this.pickLocation = function() {
    this.x = snake.x;
    this.y = snake.y;
    while(this.x==snake.x || this.y == snake.y){
      this.x = (Math.floor(Math.random() *
        columns - 1) + 1) * scale;
      this.y = (Math.floor(Math.random() *
        rows - 1) + 1) * scale;
    }
  }

  this.draw = function() {
    ctx.fillStyle = "#DF1D3A";
    ctx.fillRect(this.x, this.y, scale, scale)
  }
}
