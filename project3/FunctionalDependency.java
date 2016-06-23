
public class FunctionalDependency {
	public AttributeSet left;
	public Attribute right;
	
	public FunctionalDependency(AttributeSet l, Attribute r) {
		left = l;
		right = r;
	}
	
	public String toString(){
		return left+" <> "+right;
	}
}
