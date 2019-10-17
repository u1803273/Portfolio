// This is a generic type that allows users to store three elements of different types together as a triplet
public class Triplet<E,F,G> {
	private E element1;
	private F element2;
	private G element3;
	
	Triplet(){
		
	}
	
	Triplet (E elem1, F elem2, G elem3){
		element1=elem1;
		element2=elem2;
		element3=elem3;
	}

	public E getFirst() {
		return element1;
	}

	public void setFirst(E element1) {
		this.element1 = element1;
	}

	public F getSecond() {
		return element2;
	}

	public void setSecond(F element2) {
		this.element2 = element2;
	}

	public G getThird() {
		return element3;
	}

	public void setThird(G element3) {
		this.element3 = element3;
	}
}
