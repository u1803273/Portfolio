import java.util.ArrayList;
import java.util.Scanner; 
import java.io.BufferedReader;
import java.io.FileReader;

public class WordsearchApplication {
	
	
	public static void main(String [] args) {
		
		// Using Scanner for Getting Input from User 
        Scanner scanner = new Scanner(System.in); 
  
        System.out.println("Enter the height of the wordsearch:");
        int h = scanner.nextInt(); 
        
        System.out.println("Enter the width of the wordsearch:");
        int w = scanner.nextInt(); 
        
        // Now we instantiate the wordsearch of the given size
        Wordsearch wordsearch = new Wordsearch(h,w);

        
        // Now we print the puzzle 
        wordsearch.print();
       
        
        scanner.close();
        
        System.out.println("  ");
        System.out.println("-----------------------  ");
        System.out.println("  ");
        
  
        
        System.out.println("This is trying to solve all words going up");
        WordsearchSolver ws = new WordsearchSolver();
        ArrayList<Triplet<String,Coordinate,Integer>> foundWords = ws.solve(wordsearch);
        System.out.println("length of found words: "+foundWords.size());
        for(Triplet<String,Coordinate,Integer> triple : foundWords) {
        	System.out.println("Found "+triple.getFirst()+" at index "+triple.getSecond().getx()+ " "+triple.getSecond().gety()+ " and it reads in direction "+triple.getThird());
        }
        

		
		
	}
	
}




/*
 * 
 * 		System.out.println("Hello \u001b[1;31mred\u001b[0m world!");
 * 
 */
 

