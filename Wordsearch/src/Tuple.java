// This class is used to store a tuple
public class Tuple<X,Y>{
	private X a;
	private Y b;
	
	Tuple(){
		
	}
	
	Tuple(X x, Y y){
		a=x;
		b=y;
	}
	
	public X getFirst() {
		return a;
	}
	
	public Y getSecond() {
		return b;
	}
}
