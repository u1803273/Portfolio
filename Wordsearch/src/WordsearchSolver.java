// This class takes in a wordsearch as input and solves it

import java.util.ArrayList;
public class WordsearchSolver {

	// This is the puzzle that we will be attempting to solve
	private Wordsearch puzzle;
	/*
	 * 		This arraylist stores all the words we've found
	 * 		It will store a triplet of:
	 * 			- The word we've found
	 * 			- The coordinates of the first character
	 * 			- An integer stating which direction the word reads in (0 for north, 1 for east ...)
	 */
	private ArrayList<Triplet<String,Coordinate,Integer>> foundWords = new ArrayList <Triplet<String,Coordinate,Integer>> ();
	// This stores a tree of all the words from the text file words.txt
	private BinaryTree tree;
	
	WordsearchSolver(){}
	
	// This function is used to solve the puzzle
	public ArrayList<Triplet<String,Coordinate,Integer>> solve(Wordsearch p){
		puzzle=p;
		
		// Create the word tree
		tree = new BinaryTree();
		// We add the arraylist in, so that the tree stores only the words we need to find
		tree.add(puzzle.getWords());
		findWord();
		
		return foundWords;
	}
	
	// This function finds a word in the wordsearch
	private void findWord() {
		// There are only four directions that the words could be in
		findNorth();
		findEast();
		findSouth();
		//findEast();
	}
	
	// This reads through the wordsearch from (9,0) to (0,0), checking if any words read upwards
	private void findNorth() {
		for (int y=0;y<puzzle.getWidth();y++) {
			for (int x=puzzle.getHeight()-1;x>=0;x--) {
				// Now we're at the square, we need to see if the word reads upwards	
				String possibleWord = "";
				int tempx=x;
				/* Flag will hold:
				 * 0: Word is not in the tree at all
				 * 1: The partial word is in the tree, but needs more letters to reach a full word
				 * 2: The full word is in the tree and so we've located it
				 */
				int flag =1;
				boolean found=false;
				// While we haven't reached the top of the column and so far, we are spelling a word from the word list
				while (tempx>= 0 && flag!=0) {
					possibleWord = possibleWord.concat(String.valueOf(puzzle.get(tempx, y)));
					flag = tree.partialWord(possibleWord);
					if (flag ==0) {
						// Exits the loop
						break;
					}else if (flag==1) {
						// We have a partial word and so need to check the above square
						tempx--;
					}else {
						// We found the word
						found = true;
						break;
					}
						
				}
				
				// We found a word reading upwards from the current square
				if (found==true) {
					createTriplet(possibleWord,x,y,0);
				}
				
			}
		}
	}
	
	// This reads through the wordsearch from (0,0) to (0,9), checking if any words read right
	private void findEast() {
		for (int x=0;x<puzzle.getHeight();x++) {
			for (int y=0; y<puzzle.getWidth();y++) {
				// Now we're at the square, we need to see if the word reads upwards	
				String possibleWord = "";
				int tempy=y;
				/* Flag will hold:
				 * 0: Word is not in the tree at all
				 * 1: The partial word is in the tree, but needs more letters to reach a full word
				 * 2: The full word is in the tree and so we've located it
				 */
				int flag =1;
				boolean found=false;
				// While we haven't reached the top of the column and so far, we are spelling a word from the word list
				while (tempy< puzzle.getWidth() && flag!=0) {
					possibleWord = possibleWord.concat(String.valueOf(puzzle.get(x, tempy)));
					flag = tree.partialWord(possibleWord);
					if (flag ==0) {
						// Exits the loop
						break;
					}else if (flag==1) {
						// We have a partial word and so need to check the next square
						tempy++;
					}else {
						// We found the word
						found = true;
						break;
					}
						
				}
				
				// We found a word reading upwards from the current square
				if (found==true) {
					createTriplet(possibleWord,x,y,1);
				}
				
			}
		}
	}
	
	// This reads through the wordsearch from (0,0) to (9,0), checking if any words read down
	private void findSouth() {
		for (int y=0;y<puzzle.getWidth();y++) {
			for (int x=0;x<puzzle.getHeight();x++) {
				// Now we're at the square, we want to see if the word reads downwards
				String possibleWord = "";
				int tempx=x;
				/* Flag will hold:
				 * 0: Word is not in the tree at all
				 * 1: The partial word is in the tree, but needs more letters to reach a full word
				 * 2: The full word is in the tree and so we've located it
				 */
				int flag =1;
				boolean found=false;
				// While we haven't reached the bottom of the column and so far, we are spelling a word from the word list 
				while (tempx<puzzle.getHeight() && flag!=0) {
					possibleWord = possibleWord.concat(String.valueOf(puzzle.get(tempx, y)));
					flag = tree.partialWord(possibleWord);
					if (flag==0) {
						// Exits the loop
						break;
					}else if(flag==1) {
						// We have a partial word and so need to check the below square
						tempx++;
					}else {
						// We found the word
						found = true;
						break;
					}
				}
				
				// We found a word reading downwards from the current square
				if (found==true) {
					createTriplet(possibleWord,x,y,2);
				}
			}
		}
	}
	
	// This reads through the wordsearch from (0,9) to (0,0), checking if any words read left
	private void findWest() {
		for (int x=puzzle.getHeight()-1;x>=0;x--) {
			for (int y=puzzle.getWidth()-1;y>=0;y--) {
				// Now we're at the square, we need to see if the word reads upwards	
				String possibleWord = "";
				int tempy=y;
				/* Flag will hold:
				 * 0: Word is not in the tree at all
				 * 1: The partial word is in the tree, but needs more letters to reach a full word
				 * 2: The full word is in the tree and so we've located it
				 */
				int flag =1;
				boolean found=false;
				// While we haven't reached the top of the column and so far, we are spelling a word from the word list
				while (tempy>=0 && flag!=0) {
					possibleWord = possibleWord.concat(String.valueOf(puzzle.get(x, tempy)));
					flag = tree.partialWord(possibleWord);
					if (flag ==0) {
						// Exits the loop
						break;
					}else if (flag==1) {
						// We have a partial word and so need to check the next square
						tempy++;
					}else {
						// We found the word
						found = true;
						break;
					}
				}
				
				// We found a word reading upwards from the current square
				if (found==true) {
					createTriplet(possibleWord,x,y,3);
				}
			}
		}
	}
	
	// This creates the triplet to add to foundWords
	private void createTriplet(String possibleWord, int x, int y, int direction) {
		// We add this word to our found word array list, along with the co-ords etc
		Triplet<String, Coordinate,Integer> triplet = new Triplet<String, Coordinate,Integer>();
		triplet.setFirst(possibleWord);
		triplet.setSecond(new Coordinate(x,y));
		triplet.setThird(direction);
		foundWords.add(triplet);
	}
	
	
	
	
	
	
}
