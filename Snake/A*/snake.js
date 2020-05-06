function Snake() {
  this.x = (Math.floor(Math.random() *columns - 1) + 1) * scale;
  this.y = (Math.floor(Math.random() *rows - 1) + 1) * scale;
  this.xSpeed = scale * 1;
  this.ySpeed = 0;
  this.total = 0;
  this.tail = [];


  this.draw = function() {
    ctx.fillStyle = "#34D436";
    for (let i=0; i<this.tail.length; i++) {
      ctx.fillRect(this.tail[i].x,
        this.tail[i].y, scale, scale);
    }

    ctx.fillRect(this.x, this.y, scale, scale);
  }

  this.update = function() {
    for (let i=0; i<this.tail.length - 1; i++) {
      this.tail[i] = this.tail[i+1];
    }

    this.tail[this.total - 1] =
      { x: this.x, y: this.y };

    this.x += this.xSpeed;
    this.y += this.ySpeed;


    if (this.x >= canvas.width) {
      dead = true;
      this.x = 0;
    }

    if (this.y > canvas.height) {
      dead=true;
      this.y = 0;
    }

    if (this.x < 0) {
      dead=true;
      this.x = canvas.width;
    }

    if (this.y < 0) {
      dead=true;
      this.y = canvas.height;
    }
  }

  this.changeDirection = function(direction) {
    // Stops the snake turning back on itself
    if(direction=="Up" && dir != "DOWN"){
      this.xSpeed = 0;
      this.ySpeed = -scale * 1;
      dir="UP";
    }
    if(direction== "Down" && dir !="UP"){
      dir = "DOWN";
      this.xSpeed = 0;
      this.ySpeed = scale * 1;
    }
    if(direction=="Left" && dir!="RIGHT"){
      dir="LEFT";
      this.xSpeed = -scale * 1;
      this.ySpeed = 0;
    }
    if(direction=="Right" && dir != "LEFT"){
      dir = "RIGHT";
      this.xSpeed = scale * 1;
      this.ySpeed = 0;
    }
  }

  this.eat = function(fruit) {
    if (this.x === fruit.x &&
      this.y === fruit.y) {
      this.total++;
      return true;
    }

    return false;
  }

  this.checkCollision = function() {
    for (var i=0; i<this.tail.length; i++) {
      if (this.x === this.tail[i].x &&
        this.y === this.tail[i].y) {
        this.total = 0;
        this.tail = [];
      }
    }
  }

  // Checks if the coordinates are in the snake
  this.isTail = function(x,y){
    console.log("tail length: "+this.tail.length);
    for(let i=0;i<this.tail.length;i++){
      console.log("i: "+i);
      console.log("--- x: "+this.tail[i].x+" y: "+this.tail[i].y);
      if(this.tail[i].x==x && this.tail[i].y == y){
        alert("hit");
        return true;
      }
    }
    return false;

  }

}
