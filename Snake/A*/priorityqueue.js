function PriorityQueue(){
    this.queue = [];

    // Inserts a tile into its correct ordered position in the queue
    this.add = function(tile){
      // console.log(" ");
      // this.print();
      //
      // console.log(" ");
      var newQueue = [];

      let i=0;
      let inserted=false;

      // Traverse the list and move into new queue
      while(i<this.queue.length){
        // Already have the tile
        if(this.queue[i].x == tile.x && this.queue[i].y==tile.y){
          i++;
          continue;
        }
        // Only insert into this position if the current item is strictly less than the current
        if(inserted==false && tile.f<this.queue[i].f){
          //console.log("pushing tile");
          newQueue.push(tile);
          inserted=true;
        }
        // If they are the same, then we need to compare h costs (prioritise the one closest to the goal)
        else if(inserted==false && tile.f==this.queue[i].f){
          // Tile is closer to goal so gets inserted first
          if(tile.h<this.queue[i].h){
            newQueue.push(tile);
            inserted=true;
          }
          // Tile is further away so inserted after
          else{
            newQueue.push(this.queue[i]);
            i++;
          }
        }
        // The current queue element is smaller
        else{
          //console.log("adding element at "+i);
          newQueue.push(this.queue[i]);
          i++;
        }
      }

      // New element is the biggest in the priority queue
      if(!inserted){
        newQueue.push(tile);
      }

      this.queue = newQueue;
    }

    // returns the size
    this.size = function(){
      return this.queue.length;
    }

    // Removes and returns the first item if one exists
    this.extractMin = function(){
      if(this.queue.length>0){
        var item=this.queue[0];
        this.queue.shift();
        return item;
      }
    }

    // Removes the first item
    this.remove = function(){
      if(this.queue.length>0){
          this.queue.shift();
      }
    }

    // Clears the queue
    this.clear = function(){
      this.queue=[];
    }

    // Checks whether the queue is empty
    this.isEmpty = function(){
      if(this.queue.length==0){
        return true;
      }else{
        return false;
      }
    }

    // Prints all elements of the array into the console
    this.print = function(){
      for (let i =0; i<this.queue.length;i++){
        console.log("x: "+this.queue[i].x+" y: "+this.queue[i].y+" f: "+this.queue[i].f);
      }
    }
}

//
//
// // Have to traverse the list to find its location to enter if new
// // or the updated location if it currently exists
// var index =0;
// var foundIndex=false;
// for (let i=0; i<this.queue.length;i++){
//   // Found its place to enter
//   if(tile.f<this.queue[i].f && !foundIndex){
//     index = i;
//     foundIndex=true;
//   }
//   // Delete the old tile if it exists
//   if(this.queue[i].x==tile.x && this.queue[i].y==tile.y){
//     this.queue.splice(i,1);
//   }
// }
//
// // Add the new element
// this.queue.splice(index,0,tile);
