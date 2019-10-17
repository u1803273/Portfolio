// This class is used to create the wordsearch puzzles

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.Random;
import java.util.ArrayList;

import java.util.Scanner; 

public class WordsearchCreator {
	
	// Holds the length of the wordsearch
	private static int height;
	private static int width;
	// Holds the bigger number of the two, for use in rectangles
	private static int size;
	
	// This is the wordsearch
	char [] [] puzzle;
	// This holds a list of all the words
	ArrayList<String> words = new ArrayList<String>();
	
	// This holds the indexes of all the file lines chosen as words (used so that for smaller files it avoids repeating the same word multiple times)
	ArrayList<Integer> indexes = new ArrayList<Integer>();
	// This is a global variable to hold the name of the file to draw all words from (by default words.txt)
	//private String textFile = "words.txt";
	private String textFile = "words.txt";
	// A global variable to hold the length of WordsRefined.txt (this is so it can be used in insertWords() without having to recalculate the number of lines)
	private int numberOfLines = 0;
	
	// If only one value entered, then it is a square
	WordsearchCreator(int l){
		height =l;
		width = l;
		size=l;
	}

	// If two entered, then it is a rectangle
	public WordsearchCreator(int h, int w) {
		height = h;
		width = w;
		if (h>w) {
			size=h;
		}else {
			size=w;
		}
	}
	
	// This method is used to create the wordsearch
	public char [] [] create() {
		// This randomly inserts words into the wordsearch
		createRandom();
		// This fills in the gaps to ensure a sufficient amount of words are chosen 
		insertWords();
		// This fills in the gaps with random letters
		randomLetters();
		
		// returns the puzzle
		return puzzle;
	}

	// This function return the arraylist of words
	public ArrayList<String> getWords(){
		return words;
	}
	
	
	// ---------- These functions are used to create the wordsearch ---------- //
	
	// This creates the random words for the wordsearch
	private void createRandom() {
		// We create the puzzle to be the size entered from the user
		puzzle = new char [height][width];
		
		// This fills the wordsearch with the # character (indicative of a null)
		fill();
		
		// We get the number of words within the text file and set to the global variable
		numberOfLines = numberOfLines();
		
		// We know the function returned an actual value and that there was no error
		if (numberOfLines!=-1) {
			// We now need to populate the wordsearch with actual values 
			/* 	We will try and fit a word into the wordsearch. It we fail to fit it in, then we shall increment 
				the failed counter. If this reaches 5 (as in, we failed to fit five consecutive words into the grid, 
				then we deem the grid too filled up for further words, and so we have added all our words in) */
			int failed = 0;
			String word;
			Coordinate co;	
			boolean usedAllWords = false;
			while (failed<5 && usedAllWords == false) {
				// We get a random word
				word = getRandomWord(numberOfLines);
				
				if(word=="#") {
					usedAllWords = true;
				}else {
					// We get a random co-ordinate
					co = getRandomCoords();
					
					// We try and fit the word in, by checking the four directions until it works
					boolean flag = checkDirections(word, co.getx(), co.gety());
					
					// If we fail, we increment the failed counter
					if (flag==false) {
						failed++;
					}
					// We fitted the word in
					else {
						// Add the word we added to the list of words
						words.add(word);
						failed=0;
					}
				}
				
			}
			
		}
	}
	
	// This inserts words into the gaps in the wordsearch
	private void insertWords() {
		int percentage = calculatePercentage();
		
		// This variable is used to store whether every row or column is saturated (unable to add any words)
		boolean saturatedRow = false;
		boolean saturatedColumn = false;
		boolean saturated = false;
		
		// There are insufficient words in the grid, and so we need to add some more
		while(percentage<65 && saturated == false) {
			Random random = new Random();
			// This decides whether to do rows or columns
			int choice = random.nextInt(2);
			// We will look at the rows and try to fit words in
			if (choice==0 && saturatedColumn==false) {
				// Insert returns false if there was no columns to add the word into
				saturatedColumn = !insert(0);
				
			}
			// This looks at the rows
			else if (saturatedRow==false) {
				saturatedRow = !insert(1);
			}
			
			// Calculates whether the grid is saturated (if both rows and columns are full, saturated is set to true and the loop broken, even if less than 65 characters)
			saturated = saturatedRow & saturatedColumn;
			// Recalculate the percentage after adding the new words
			percentage = calculatePercentage();
		}
	}
	
	// This fills the wordsearch empty squares with random letters
	private void randomLetters() {
		/* 
		 * This stores letters to be added to the wordsearch.
		 * Vowels and common letters appear greater than obscure characters such as q, x, and z.
		 * This therefore alters the frequency at which all characters appear to help mask words
		*/
		char [] letterArray = {'a','a','a','a','a','b','b','c','c','c','d','d','d','e','e','e','e','e','e','f','f','g','g','h','h','i','i','i','i','i','j','j','k','k',
				'l','l','l','l','m','m','m','m','n','n','n','o','o','o','o','o','p','p','p','q','r','r','r','s','s','s','t','t','t','u','u','u','u','v','v','w','w','w',
				'x','y','y','z'};
		Random random = new Random();
		// Iterates through every square and then if the square is null ('#') then it replaces it with a letter from the array
		for (int i=0; i<height;i++) {
			for (int j =0; j<width; j++) {
				// Replaces the square
				if (puzzle[i][j]=='#') {
					puzzle[i][j] = letterArray[random.nextInt(letterArray.length)];
				}
			}
		}
		
	}

	
	
	// ---------- These functions are used in insertWords() ---------- //
	
	// This function is used to calculate the percentage of the wordsearch filled by the words so far
	private int calculatePercentage() {
		int sumOfChars=0;
		for (int i=0; i<words.size();i++) {
			sumOfChars+=words.get(i).length();
		}
		float result = (float) ((float) sumOfChars/(height*width))*100;
		return (int) result; 
	}
	
	// This function tries to insert a word into either the column or row (0 for column and 1 for row)
	private boolean insert(int choice) {
		Random random = new Random();
		boolean flag = false;
		int count =0;
		// We set length to the length of the selected line (either the column or the row)
		int length;
		if (choice==0) {
			length=width;
		}else {
			length=height;
		}
		int [] possibilities = fillArray(length);
		int size = length;
		// While we haven't fit a word in and we still have lines to check
		while (flag==false && count <size) {
			// Selects either the row or the column that we are checking
			int line;
			// If there is only one element left in possibilities we have to try that
			if (length==1) {
				line = possibilities[0];
			}else {
				// This is the column that we're targeting
				line = possibilities[random.nextInt(length)];
			}
			
			// Altering a column
			if(choice==0) {
				// Now we find the longest consecutive streak of empty squares in the column
				// If greater than 5, we enter a word (randomly generated)
				Tuple<Integer,Integer> consecutive = consecutiveColumn(line);
				if (consecutive.getFirst()>=5) {
					// Gets a word smaller than or equal to the number of consecutive free spaces
					String word = getWord(consecutive.getFirst());
					// We've used all words up in the wordsearch
					if (word=="#") {
						return false;
					}else {
						// We insert the word into the column
						int number = random.nextInt(1);
						// Reads downwards
						if (number==0) {
							insertSouth(word,consecutive.getSecond(),line);
						}
						// Reads upwards
						else {
							insertNorth(word,consecutive.getSecond()+word.length()-1,line);
						}
						words.add(word);
						flag=true;
						
					}
				}
			}
			// Altering a row
			else {
				// Now we find the longest consecutive streak of empty squares in the column
				// If greater than 5, we enter a word (randomly generated)
				Tuple<Integer,Integer> consecutive = consecutiveRow(line);
				if (consecutive.getFirst()>=5) {
					String word = getWord(consecutive.getFirst());
					// We've used all words up in the wordsearch
					if (word=="#") {
						return false;
					}else {
						// We insert the word into the row
						int number = random.nextInt(1);
						// Reads to the left
						if (number==0) {
							insertWest(word,line, consecutive.getSecond()+word.length()-1);
						}
						// Reads to the right
						else {
							insertEast(word,line, consecutive.getSecond());
						}
						// Adds the word to the word list
						words.add(word);
						flag=true;
						
					}
				}
			}
			
			// We increment count as another column has been visited 
			count ++;
			// We remove the index from the array 
			possibilities = removeFromArray(possibilities,line);
			// Decrement length as one less element in the possibilities array
			length--;
			
		}
		
		// If no word was added, then it returns false
		return flag;
	}

	// This function returns a tuple, containing the greatest consecutive streak of empty squares in a column, and the index at when they begin
	private Tuple<Integer,Integer> consecutiveColumn(int column) {
		// A temporary variable to store the current streak of empty squares
		int consec =0;
		// Stores the biggest streak found so far
		int biggest=0;
		// A temporary variable to store the beginning of the current streak 
		int tempx=0;
		// Stores the x co-ordinate of the beginning of the streak
		int x =0;
		for (int i=0; i<height;i++) {
			if (puzzle[i][column]=='#') {
				// The first empty square in the current streak
				if (consec ==0) {
					tempx=i;
				}
				consec++;
			}else {
				if (consec>biggest) {
					biggest=consec;
					x=tempx;
				}
				consec=0;
			}
		}
		// If the biggest streak led to the end of the column, the values haven't been transferred to big or x yet
		if (consec>biggest) {
			biggest=consec;
			x=tempx;
		}
		
		return new Tuple<Integer,Integer>(biggest,x);
	}

	// This function returns a tuple, containing the greatest consecutive streak of empty squares in a row, and the index at when they begin
	private Tuple<Integer,Integer> consecutiveRow(int row) {
		// A temporary variable to store the current streak of empty squares
		int consec =0;
		// Stores the biggest streak found so far
		int biggest=0;
		// A temporary variable to store the beginning of the current streak 
		int tempy=0;
		// Stores the y co-ordinate of the beginning of the streak
		int y =0;
		for (int i=0; i<width;i++) {
			if (puzzle[row][i]=='#') {
				// The first empty square in the current streak
				if (consec ==0) {
					tempy=i;
				}
				consec++;
			}else {
				if (consec>biggest) {
					biggest=consec;
					y=tempy;
				}
				consec=0;
			}
		}
		// If the longest streak has led to the end of the row, then the values havent been transferred to biggest or y yet
		if (consec>biggest) {
			biggest=consec;
			y=tempy;
		}
		
		return new Tuple<Integer,Integer>(biggest,y);
	}
			
	// This function cycles through the words in the file until one is found being less in length than the value entered 
	private String getWord (int length) {
		// Set the initial value to something too big to be considered
		int wordLength = 1000;
		String word=null;
		while(wordLength>length) {
			word = getRandomWord(numberOfLines);
			wordLength = word.length();
		}
		return word;
	}
	
	
	
	// ---------- These functions are used in createRandom() ---------- //
	
	/* This function does two things:
		- Goes through the file and checks whether the length of the word is greater than the size variable 
		(and if so, does not add to wordsrefined. This stops unnecessarily long words being checked)
		- It counts every good word, thus returning the number of viable words
	*/
	private int numberOfLines(){
		try {
			BufferedReader words = new BufferedReader(new FileReader(textFile));
			BufferedWriter wordsRefined = new BufferedWriter(new FileWriter("WordsRefined.txt"));
			int lines = 0;
			String line;
			while ((line = words.readLine()) != null) {
				// Checks the length of the line 
				if (line.length()<=size && line.length()>3) {
					// Add to the refined words file
					wordsRefined.append(line);
					wordsRefined.newLine();
					// A new word has been added and so lines is incremented
					lines++;
				}
				
			}
			words.close();
			wordsRefined.close();
			return lines;
		}
		catch (Exception e) {
			System.out.println("Failed to open file.");
			System.out.println(e);
			return -1;
		}
	}

	// This method is used to get a random word from the text file 
	private String getRandomWord(int numWords) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("WordsRefined.txt"));
			
			// We've used every word from the text file
			if (indexes.size()==numberOfLines) {
				return "#";
			}
			// We still have words to choose from 
			else {
				// Creates a new random number and then checks to see whether we've already used this word previously
				int num;
				Random random;
				do {
					random = new Random();
					num = random.nextInt(numWords);
				}while(indexes.contains(num)==true);
				
				// Add the new number to the list
				indexes.add(num);
				
				// Now we loop through the file until we get to the numbered line that was randomly generated
				int i;
				for (i=0; i<num;i++) {
					reader.readLine();
				}
				// Now we read the last line (line number equal to num)
				return reader.readLine();
			}
						
		}catch( Exception e) {
			System.out.println("Failed to open file. Random word");
			System.out.println(e);
			return null;
		}
	}
	
	// This method is used to generate a set of random co-ordinates within the puzzle
	private Coordinate getRandomCoords() {
		Random random = new Random();
		return new Coordinate(random.nextInt(height),random.nextInt(width));
	}
	
	
	
	// ---------- These functions are used to check if it is possible to insert a word in a certain direction ---------- //
	
	// This method checks if the word can fit into a column, reading upwards (north)
	private boolean checkNorth(String word, int x, int y) {
		int length = word.length();
		// We check that there is enough space to add the word
		if ((x+1)>=length) {
			int count =0;
			// There is enough space to add the word, we have to now check that the squares can take them
			for (int i=x; i>x-length;i--) {
				if (puzzle[i][y]=='#' || puzzle[i][y]== word.charAt(count)) {
					count++;
				}else {
					return false;
				}
			}
			// Now there is room to hold the word, we insert it into the puzzle
			insertNorth(word,x,y);
			return true;
		}
		return false;
	}
	
	// This method checks if the word can fit into a column, reading downwards (south)
	private boolean checkSouth(String word, int x, int y) {
		int length = word.length();
		// We check that there is enough space to add the word
		if (height-x>=length) {
			int count =0;
			// There is enough space to add the word, we have to now check that the squares can take them
			for (int i=x; i<x+length;i++) {
				if (puzzle[i][y]=='#' || puzzle[i][y]== word.charAt(count)) {
					count++;
				}else {
					return false;
				}
			}
			// Now there is room to hold the word, we insert it into the puzzle
			insertSouth(word,x,y);
			return true;
		}
		return false;
	}

	// This method checks if the word can fit into a row, reading to the right (East)
	private boolean checkEast(String word, int x, int y) {
		int length = word.length();
		// We check that there is enough space to add the word
		if ((width-y)>=length) {
			int count =0;
			// There is enough space to add the word, we have to now check that the squares can take them
			for (int i=y; i<y+length;i++) {
				if (puzzle[x][i]=='#' || puzzle[x][i]== word.charAt(count)) {
					count++;
				}else {
					return false;
				}
			}
			// Now there is room to hold the word, we insert it into the puzzle
			insertEast(word,x,y);
			return true;
		}
		return false;
	}

	// This method checks if the word can fit into a row, reading to the left (west)
	private boolean checkWest(String word, int x, int y) {
		int length = word.length();
		// We check that there is enough space to add the word
		if ((y+1)>=length) {
			int count =0;
			// There is enough space to add the word, we have to now check that the squares can take them
			for (int i=y; i>y-length;i--) {
				if (puzzle[x][i]=='#' || puzzle[x][i]== word.charAt(count)) {
					count++;
				}else {
					return false;
				}
			}
			// Now there is room to hold the word, we insert it into the puzzle
			insertWest(word,x,y);
			return true;
		}
		return false;
	}
	
	
	
	// ---------- These functions are used to insert words in certain directions ---------- //
	
	// This method inserts a word into the puzzle, reading upwards
	private void insertNorth(String word, int x, int y) {
		int length = word.length();
		int count=0;
		for (int i=x; i>x-length;i--) {
			puzzle[i][y]=word.charAt(count);
			count++;
		}
	}

	// This method inserts a word into the puzzle, reading downwards
	private void insertSouth(String word, int x, int y) {
		// There is room for the word and so we add it
		int count=0;
		int length = word.length();
		for (int i=x; i<x+length;i++) {
			puzzle[i][y]=word.charAt(count);
			count++;
		}
	}
	
	// This method inserts a word into the puzzle reading to the right
	private void insertEast(String word, int x, int y) {
		// There is room for the word and so we add it
		int count=0;
		int length = word.length();
		for (int i=y; i<y+length;i++) {
			//System.out.println("inserting character "+word.charAt(count)+ "at position "+x+" "+i);
			puzzle[x][i]=word.charAt(count);
			count++;
		}
	}
	
	// This method inserts a word into the puzzle, reading to the left
	private void insertWest(String word, int x, int y) {
		// There is room for the word and so we add it
		int count=0;
		int length = word.length();
		for (int i=y; i>y-length;i--) {
			puzzle[x][i]=word.charAt(count);
			count++;
		}
	}
	
	
	// ---------- These functions are used to insert words into the wordsearch---------- //
	
	// This method tries to insert the word into the wordsearch
	private boolean checkDirections(String word, int x, int y) {
		// Holds each direction we can attempt to fit the word
		String[] directions = {"North","East","South", "West"};
		// Counts how many directions we've yet to try
		int count =4;
		// Checks whether we've managed to insert the word yet
		boolean flag=false;
		// Keeps checking directions until we fit the word in or run out of directions to choose from
		while (flag==false && count>1) {
			Random random = new Random();
			int randomDirection = random.nextInt(count-1);
			flag = callDirectionFunction(directions[randomDirection], word, x,y);

			// Removes the direction from the array
			directions = removeFromArray(directions, directions[randomDirection]);
			// We've tried another direction
			count--;
			// When there is one option left and we need to take it
			if (count==1 && flag==false) {
				flag = callDirectionFunction(directions[0], word, x,y);			
			}
		
		}
		
		
		return flag;
	}

	// This method takes a string and switches it, calling the corresponding direction function
	private boolean callDirectionFunction(String direction, String word, int x, int y) {
		switch(direction) {
		case "North":
			return checkNorth(word,x,y);
		case "East":
			return checkEast(word,x,y);
		case "South":
			return checkSouth(word,x,y);
		case "West":
			return checkWest(word,x,y);
		}
		return false;
	}
	
	
	
	// ---------- These are Auxiliary Functions---------- //
	
	// This function takes an array and removes an element from it and returns the array
	private String [] removeFromArray(String [] array, String string) {
		String[] newArray = new String [array.length-1];
		int k=0;
		for (int i=0;i<array.length;i++) {
			if (array[i]!=string) {
				newArray[k]=array[i];
				k++;
			}
		}
		return newArray;
	}

	private int [] removeFromArray(int[] array, int number) {
		int [] newArray = new int [array.length-1];
		int k=0;
		for (int i=0;i<array.length;i++) {
			if (array[i]!=number) {
				newArray[k]=array[i];
				k++;
			}
		}
		return newArray;
	}
	
	// Fills the array with # (the equivical null character)
	private void fill() {
		for (int i=0;i<height;i++) {
			for (int j=0; j<width;j++) {
				puzzle[i][j]='#';
			}
		}
	}

	// This function fills an array up with ascending numbers
	private int[] fillArray(int limit) {
		int [] array = new int[limit];
		for (int i=0; i<limit;i++) {
			array[i]=i;
		}
		return array;
	}

	
	// ---------- These functions were used for testing purposes ---------- //

	// This function prints the content of the WordsRefined file
	private void printFile() {
		try {
			BufferedReader words = new BufferedReader(new FileReader("WordsRefined.txt"));
			String line;
			while ((line = words.readLine()) != null) {
				System.out.println(line);
				
			}
			words.close();
		}
		catch (Exception e) {
			System.out.println("Failed to open file.");
			System.out.println(e);
		}
	}
	




}

