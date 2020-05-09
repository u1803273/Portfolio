function Tile(){
    // Cost to move
    this.f = rows + columns +1;
    this.x;
    this.y;
    this.parentX=0;
    this.parentY=0;
    this.valid=true;
    this.visited = false;

    // Stores the direction needed to get to this tile
    // Used to calculate the path
    this.direction;

    this.updateF=function(gCost, hCost){
      // Updates h cost
      if(hCost<this.h){
        this.h=hCost;
      }
      // Updates f cost
      if(gCost+hCost<this.f){
        this.f=gCost+hCost;
      }
    }
}
