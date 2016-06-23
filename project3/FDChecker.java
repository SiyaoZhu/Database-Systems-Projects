import java.util.*;

public class FDChecker {

	/**
	 * Checks whether a decomposition of a table is dependency
	 * preserving under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is dependency preserving, false otherwise
	 **/
	
	private static AttributeSet intersection(AttributeSet as1, AttributeSet as2) {
		AttributeSet intersect = new AttributeSet();
		for (Attribute attr : as1) {
			if (as2.contains(attr)) {
				intersect.add(attr);
			}
		}
		return intersect;
	}
		
	public static boolean checkDepPres(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		//your code here
		//a decomposition is dependency preserving, if local functional dependencies are
		//sufficient to enforce the global properties
		//To check a particular functional dependency a -> b is preserved, 
		//you can run the following algorithm
		//result = a
		//while result has not stabilized
		//	for each table in the decomposition
		//		t = result intersect table 
		//		t = closure(t) intersect table
		//		result = result union t
		//if b is contained in result, the dependency is preserved
		
		boolean Change = true;
		
		for(FunctionalDependency fd: fds){
			AttributeSet result = fd.left;
			Attribute   beta= fd.right;
			AttributeSet now = new AttributeSet();
			while(Change){
				now=result;

	                AttributeSet t11=(AttributeSet) t1.clone();
					t11.retainAll(result);
					t11=closure(t11, fds);
					t11.retainAll(t1);
					result.addAll(t11);

					
					AttributeSet t22=(AttributeSet) t2.clone();
					t22.retainAll(result);
					t22=closure(t22, fds);
					t22.retainAll(t2);
					result.addAll(t22);
				if(now.containsAll(result)){
					Change=false;
				}
			}
			if (!result.contains(beta)){
				return false;
			}
			Change=true;
		}
		return true;
	}
	

	/**
	 * Checks whether a decomposition of a table is lossless
	 * under the set of functional dependencies fds
	 * 
	 * @param t1 one of the two tables of the decomposition
	 * @param t2 the second table of the decomposition
	 * @param fds a complete set of functional dependencies that apply to the data
	 * 
	 * @return true if the decomposition is lossless, false otherwise
	 **/
	public static boolean checkLossless(AttributeSet t1, AttributeSet t2, Set<FunctionalDependency> fds) {
		//your code here
		//Lossless decompositions do not lose information, the natural join is equal to the 
		//original table.
		//a decomposition is lossless if the common attributes for a superkey for one of the
		//tables.
		AttributeSet intersec =intersection(t1, t2);
		if(closure(intersec,fds).containsAll(t1) || closure(intersec,fds).containsAll(t2)){
			return true;
		}
		else{
			return false;
		}
	}

	//recommended helper method
	//finds the total set of attributes implied by attrs

	private static AttributeSet closure(AttributeSet attrs, Set<FunctionalDependency> fds) {
		if(attrs == null){
			return attrs;
		}
		
		AttributeSet previous= (AttributeSet) attrs.clone();
		AttributeSet now;
		boolean Change=true;
		
		while(Change){
			Iterator<FunctionalDependency> fds_iterator = fds.iterator();
			now=(AttributeSet) previous.clone();
			while(fds_iterator.hasNext()){
				FunctionalDependency fd = fds_iterator.next();
				AttributeSet left = fd.left;
				if(previous.containsAll(left)){
					if(!previous.contains(fd.right)){
						previous.add(fd.right);
					}
				}
			}
			
			if(previous.containsAll(now) && now.containsAll(previous)){
				Change=false;
			}
			else{
				Change=true;
			}
		}
		return previous;
	}
}
