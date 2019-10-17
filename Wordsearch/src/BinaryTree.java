import java.util.ArrayList;
/*
 *  This class models a binary tree. It was taken from a separate binary 
 *  tree file and so only the relevant functions were brought across
 */


public class BinaryTree{
	// This class holds the nodes of the binary tree
	private class Node{
		private Node left;
		private Node right;
		private String element;
		
		public Node() {
			left=null;
			right=null;
			element=null;
		}
		
		public Node(String element) {
			left=null;
			right=null;
			this.element=element;
		}
		
		public Node(Node left, Node right, String element) {
			this.left = left;
			this.right = right;
			this.element = element;
		}
		
		public Node getLeft() {
			return left;
		}
		
		public void setLeft(Node left) {
			this.left = left;
		}
		
		public Node getRight() {
			return right;
		}
		
		public void setRight(Node right) {
			this.right = right;
		}
		
		public String getElement() {
			return element;
		}
		
		public void setElement(String element) {
			this.element = element;
		}
		
		public int numChildren() {
			int count =0;
			if (left!=null) {
				count++;
			}
			if(right!=null) {
				count++;
			}
			return count;
		}
		
	}

	private Node root;
	// This stores the number of nodes in the tree
	private int number=0;

	public BinaryTree() {
		root=null;
	}
	
	// This function adds a node into the tree, sorting it into it's correct position
	public void add(String elem) {
		root = add(root,elem);
	}
	
	// This method adds every element from an arraylist into the binarytree
	public void add(ArrayList<String> strings) {
		for (String string : strings) {
			add(string);
		}
	}
	
	private Node add(Node node, String elem){
		// We are at a leaf
		if (node==null) {
			Node newNode = new Node (elem);
			// We've increased the number of nodes in the tree
			number++;
			return newNode;
		}
		// We are at a node
		else {
			// We then check whether the element is less than the value at the current node
			if (node.getElement().compareTo(elem)<0) {
				// The element being added is greater than the current node, so move it right
				node.setRight(add(node.getRight(),elem));
			}else if(node.getElement().compareTo(elem)>0) {
				// Go left
				node.setLeft(add(node.getLeft(),elem));
			}
			
			return node;
		}
	}

	// This function returns the number of nodes within the tree
	public int numNodes() {
		return number;
	}
	
	/*
	 * 		This function checks whether the inputted string matches any of the nodes
	 * 		0: Matches no nodes
	 * 		1: Partially matches a node but needs more letters
	 * 		2: Fully matches a word
	 */
	public int partialWord(String word) {
		return partialWordRecursive(root,word);
	}
	
	private int partialWordRecursive(Node node, String word) {
		// We can't operate on leaves
		if (node!=null) {
			int leftSubtreeValue = partialWordRecursive(node.getLeft(), word);
			// They found a match in the left subtree so no need to check the right
			if (leftSubtreeValue == 2) {
				return 2;
			}
			// They either found no match or only a partial. Therefore we need to check right subtree for a match
			else {
				int rightSubtreeValue = partialWordRecursive(node.getRight(), word);
				// We found a full match
				if (rightSubtreeValue==2) {
					return 2;
				}else {
					// There have only been 0's and 1's in both subtrees. Therefore, we need to calculate the score for the current node
					int temp = compareString(word, node.getElement());
					// Return whichever score is greatest
					return Math.max(Math.max(leftSubtreeValue,rightSubtreeValue),temp);
				}
			}
		}
		return 0;

	}

	// Takes two strings, and returns 2 if equal, 1 if string a is a substring of string b, or 0 if else
	private int compareString(String string1, String string2) {
		if (string1.equals(string2)) {
			return 2;
		}else {
			int count =0;
			// While they have equal characters
			while(count<string1.length() && string1.charAt(count)==string2.charAt(count)) {
				// Increment count to check the next character
				count++;
			}
			// If count = the length of string1, then the entirety of string 1 was matching (i.e a partial match)
			if (count==string1.length()) {
				return 1;
			}
		}
		return 0;
	}

	// Prints all elements out
	public void inOrderTraversal() {
		inOrderTraversal(root);
	}
	
	private void inOrderTraversal(Node node) {
		if (node!=null) {
			inOrderTraversal(node.getLeft());
			System.out.println(node.getElement());
			inOrderTraversal(node.getRight());
		}
	}
	
	public boolean isIn(String elem) {
		return isIn(root,elem);
	}
	
	private boolean isIn(Node node, String elem) {
		if (node.getElement().equals(elem)) {
			return true;
		}else {
			boolean flag = false;
			if (node.getLeft()!=null) {
				flag= isIn(node.getLeft(),elem);
			}
			if (flag==true) {
				return true;
			}else {
				if(node.getRight()!=null){
					flag= isIn(node.getRight(),elem);
				}
				return flag;
			}

		}
		
	}
}
