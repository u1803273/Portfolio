// This stores the wordsearch
import java.util.ArrayList;
public class Wordsearch {

	// This is the actual wordsearch
	private char [] [] puzzle;
	private int height;
	private int width;
	// This holds a list of all the words
	ArrayList<String> words = new ArrayList<String>();

	
	Wordsearch(){
		// Passes in default values of 10 x 10
		WordsearchCreator wc = new WordsearchCreator(10);
		puzzle = wc.create();
		words = wc.getWords();
		height=10;
		width=10;
	}
	
	Wordsearch(int h){
		// Creates a square wordsearch of size l
		WordsearchCreator wc = new WordsearchCreator(h);
		puzzle = wc.create();
		words = wc.getWords();
		height=h;
		width=h;
	}
	
	Wordsearch(int h, int w){
		// Creates the rectangular wordsearch of width h x w
		WordsearchCreator wc = new WordsearchCreator(h,w);
		puzzle = wc.create();
		words = wc.getWords();
		height=h;
		width=w;
	}
	
	// Returns the height 
	public int getHeight() {
		return height;
	}
	
	// Returns the width
	public int getWidth() {
		return width;
	}
	
	// Returns the list of words to be found
	public ArrayList<String> getWords() {
		return words;
	}
	
	// This function prints the wordsearch to the screen
	public void print() {
		for (int i=0; i<height;i++) {
			for (int j=0; j<width;j++) {
				System.out.print(puzzle[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println(" ");
		System.out.println("The words to find are: ");
		System.out.println(" ");
		for (int i=0; i<words.size();i++) {
			System.out.println(words.get(i));
		}
	}
	
	// Returns the character in the wordsearch at index x,y
	public char get(int x, int y) {
		return puzzle[x][y];
	}
}
