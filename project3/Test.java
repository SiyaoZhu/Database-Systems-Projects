
import static org.junit.Assert.*;
import java.util.*;

public class Test {

	@org.junit.Test
	public void depPresBasictest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t2.add(new Attribute("b"));
		
		fds.add(new FunctionalDependency(t1,new Attribute("a")));

		// tables
		// a
		// b
		// fds
		// a -> a
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		
		fds.add(new FunctionalDependency(t1, new Attribute("b")));
		// tables
		// a
		// b
		// fds
		// a -> a
		// a -> b
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));
	}
	
	@org.junit.Test
	public void losslesstest1(){
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		AttributeSet left1 = new AttributeSet();
		AttributeSet left2 = new AttributeSet();
		left1.add(new Attribute("B"));
		left2.add(new Attribute("D"));
		
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		t1.add(new Attribute("B"));
		t1.add(new Attribute("C"));
		t2.add(new Attribute("A"));
		t2.add(new Attribute("D"));
		
		fds.add(new FunctionalDependency(left1, new Attribute("C")));
		fds.add(new FunctionalDependency(left2, new Attribute("A")));
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
	}
	
	@org.junit.Test
	public void depPrestest2(){
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		AttributeSet left1 = new AttributeSet();
		AttributeSet left2 = new AttributeSet();
		AttributeSet left3 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("A"));
		t1.add(new Attribute("C"));
		t1.add(new Attribute("D"));
		
		t2.add(new Attribute("B"));
		t2.add(new Attribute("C"));
		
		left1.add(new Attribute("A"));
		left1.add(new Attribute("B"));
		left2.add(new Attribute("C"));
		left3.add(new Attribute("C"));
		
		fds.add(new FunctionalDependency(left1, new Attribute("C")));
		fds.add(new FunctionalDependency(left2, new Attribute("A")));
		fds.add(new FunctionalDependency(left3, new Attribute("D")));
		
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));
	}
	
	
	@org.junit.Test
	public void losslesstest2(){
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		AttributeSet left1 = new AttributeSet();
		AttributeSet left2 = new AttributeSet();
		AttributeSet left3 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("A"));
		t1.add(new Attribute("C"));
		t1.add(new Attribute("D"));
		
		t2.add(new Attribute("B"));
		t2.add(new Attribute("C"));
		
		left1.add(new Attribute("A"));
		left1.add(new Attribute("B"));
		left2.add(new Attribute("C"));
		left3.add(new Attribute("C"));
		
		fds.add(new FunctionalDependency(left1, new Attribute("C")));
		fds.add(new FunctionalDependency(left2, new Attribute("A")));
		fds.add(new FunctionalDependency(left3, new Attribute("D")));
		
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
	}
	
	
	
	@org.junit.Test
	public void losslessBasictest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t2.add(new Attribute("b"));
		
		// tables
		// a
		// b
		// fds
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t1.add(new Attribute("b"));
		// tables
		// a b
		// b
		// fds
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
	}

	@org.junit.Test
	public void depPresFDtest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t2.add(new Attribute("b"));
		
		fds.add(new FunctionalDependency(t1,new Attribute("b")));

		// tables
		// a b
		// b
		// fds
		// ab -> b
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		
		
		fds.add(new FunctionalDependency(t2, new Attribute("a")));
		// tables
		// a b
		// b
		// fds
		// ab -> b
		// b -> a
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
	}

	
	@org.junit.Test
	public void losslesstest() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t2.add(new Attribute("b"));
		t2.add(new Attribute("c"));
		t2.add(new Attribute("d"));
		
		AttributeSet temp = new AttributeSet();
		temp.add(new Attribute("b"));
		fds.add(new FunctionalDependency(temp,new Attribute("c")));
		// tables
		// a b
		// b c d
		// fds
		// b -> c
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		fds.add(new FunctionalDependency(temp, new Attribute("d")));
		// tables
		// a b
		// b c d
		// fds
		// b -> c
		// b -> d
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
	}
	
	@org.junit.Test
	public void test1() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t1.add(new Attribute("c"));
		t2.add(new Attribute("b"));
		t2.add(new Attribute("c"));
		
		fds.add(new FunctionalDependency(t1,new Attribute("b")));
		fds.add(new FunctionalDependency(t2,new Attribute("c")));

		// tables
		// a c
		// b c
		// fds
		// a,c -> b 
		// b,c -> c
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));	
	}
	
	@org.junit.Test
	public void test2(){
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		AttributeSet t3 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		t1.add(new Attribute("c"));
		fds.add(new FunctionalDependency(t1,new Attribute("a")));
		fds.add(new FunctionalDependency(t1,new Attribute("d")));
		t1.add(new Attribute("a")); t1.add(new Attribute("d"));
		t2.add(new Attribute("b")); t2.add(new Attribute("c"));
		t3.add(new Attribute("a")); t3.add(new Attribute("b"));
		fds.add(new FunctionalDependency(t3,new Attribute("c")));
		
		// tables
		// a c d
		// b c
		// fds
		// a,b -> c
		// c -> a
		// c -> d
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));	
	}
	
	@org.junit.Test
	public void test3(){
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		t1.add(new Attribute("c"));
		fds.add(new FunctionalDependency(t1, new Attribute("a")));
		fds.add(new FunctionalDependency(t1, new Attribute("d")));
		t1.add(new Attribute("a")); t1.add(new Attribute("b"));
		t2.add(new Attribute("a")); 
		fds.add(new FunctionalDependency(t2,new Attribute("b")));
		fds.add(new FunctionalDependency(t2,new Attribute("c")));
		t2.add(new Attribute("d")); 
		
		// tables
		// a b c
		// a d
		// fds
		// a -> b,c
		// c -> a,d
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));	
	}
	
	@org.junit.Test
	public void test4(){
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		AttributeSet t3 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		t1.add(new Attribute("a"));
		fds.add(new FunctionalDependency(t1, new Attribute("b")));
		t1.add(new Attribute("b")); 
		t2.add(new Attribute("b")); 
		fds.add(new FunctionalDependency(t2,new Attribute("c")));
		t3.add(new Attribute("c")); 
		fds.add(new FunctionalDependency(t2,new Attribute("d")));
		t3.add(new Attribute("a"));  t3.add(new Attribute("d")); 
		
		// tables
		// a b
		// a c d
		// fds
		// a -> b
		// b -> c
		// c -> d
		assertFalse(FDChecker.checkDepPres(t1, t3, fds));	
	}
	
	
	
	public class testcase1 {

		@org.junit.Test
		public void depPresBasictest() {
			AttributeSet t1 = new AttributeSet();
			AttributeSet t2 = new AttributeSet();
			Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
			
			t1.add(new Attribute("a"));
			t2.add(new Attribute("b"));
			
			fds.add(new FunctionalDependency(t1,new Attribute("a")));

			// tables
			// a
			// b
			// fds
			// a -> a
			assertTrue(FDChecker.checkDepPres(t1, t2, fds));
			
			
			fds.add(new FunctionalDependency(t1, new Attribute("b")));
			// tables
			// a
			// b
			// fds
			// a -> a
			// a -> b
			assertFalse(FDChecker.checkDepPres(t1, t2, fds));
		}

		@org.junit.Test
		public void losslessBasictest() {
			AttributeSet t1 = new AttributeSet();
			AttributeSet t2 = new AttributeSet();
			Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
			
			t1.add(new Attribute("a"));
			t2.add(new Attribute("b"));
			
			// tables
			// a
			// b
			// fds
			assertFalse(FDChecker.checkLossless(t1, t2, fds));
			
			t1.add(new Attribute("b"));
			// tables
			// a b
			// b
			// fds
			assertTrue(FDChecker.checkLossless(t1, t2, fds));
		}
		
		@org.junit.Test
		public void depPresFDtest() {
			AttributeSet t1 = new AttributeSet();
			AttributeSet t2 = new AttributeSet();
			Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
			
			t1.add(new Attribute("a"));
			t1.add(new Attribute("b"));
			t2.add(new Attribute("b"));
			
			fds.add(new FunctionalDependency(t1,new Attribute("b")));

			// tables
			// a b
			// b
			// fds
			// ab -> b
		
			assertTrue(FDChecker.checkDepPres(t1, t2, fds));
			
			
			fds.add(new FunctionalDependency(t2, new Attribute("a")));
			// tables
			// a b
			// b
			// fds
			// ab -> b
			// b -> a
			assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		}

		@org.junit.Test
		public void depPresFDtest1() {
			AttributeSet t1 = new AttributeSet();
			AttributeSet t2 = new AttributeSet();
			AttributeSet temp = new AttributeSet();
			AttributeSet temp1 = new AttributeSet();
			AttributeSet temp2 = new AttributeSet();
			AttributeSet temp3 = new AttributeSet();
			Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
			
			t1.add(new Attribute("c"));
			
			t1.add(new Attribute("b"));
			t1.add(new Attribute("d"));
			t2.add(new Attribute("e"));
			t2.add(new Attribute("c"));
			t2.add(new Attribute("a"));
			
			temp3.add(new Attribute("c"));
			fds.add(new FunctionalDependency(temp3,new Attribute("e")));
			// tables
			// b c d
			// a c e 
			// fds
			// c -> e
		
			assertTrue(FDChecker.checkDepPres(t1, t2, fds));
			temp.add(new Attribute("b"));
			fds.add(new FunctionalDependency(temp,new Attribute("d")));
			
			assertTrue(FDChecker.checkDepPres(t1, t2, fds));
			
			temp1.add(new Attribute("e"));
			fds.add(new FunctionalDependency(temp1,new Attribute("a")));
			
			// tables
			// b c d
			// a c e 
			// fds
			// c-> e
			// b -> d
			//e-->a
			assertTrue(FDChecker.checkDepPres(t1, t2, fds));
			
			temp2.add(new Attribute("a"));
			temp2.add(new Attribute("b"));
			fds.add(new FunctionalDependency(temp2,new Attribute("c")));
			assertFalse(FDChecker.checkDepPres(t1, t2, fds));
			
		}
		@org.junit.Test
		
		public void depPresFDtest2() {
			AttributeSet t1 = new AttributeSet();
			AttributeSet t2 = new AttributeSet();
			AttributeSet temp = new AttributeSet();
			AttributeSet temp1 = new AttributeSet();
			AttributeSet temp2 = new AttributeSet();
			AttributeSet temp3 = new AttributeSet();
			Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
			
			t1.add(new Attribute("a"));
			t1.add(new Attribute("b"));
			t1.add(new Attribute("c"));
			
			t2.add(new Attribute("c"));
			t2.add(new Attribute("d"));
			
			temp3.add(new Attribute("a"));
			fds.add(new FunctionalDependency(temp3,new Attribute("b")));
			// tables
			// a b c
			// c d 
			// fds
			// a -> b
		
			assertTrue(FDChecker.checkDepPres(t1, t2, fds));
			temp2.add(new Attribute("c"));
			fds.add(new FunctionalDependency(temp2,new Attribute("d")));
			// tables
			// a b c
			// c d 
			// fds
			// a -> b
			// c-->d
			
			assertTrue(FDChecker.checkDepPres(t1, t2, fds));
			
		
			
		}
		

		@org.junit.Test
		public void losslesstest() {
			AttributeSet t1 = new AttributeSet();
			AttributeSet t2 = new AttributeSet();
			Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
			
			t1.add(new Attribute("a"));
			t1.add(new Attribute("b"));
			t2.add(new Attribute("b"));
			t2.add(new Attribute("c"));
			t2.add(new Attribute("d"));
			
			AttributeSet temp = new AttributeSet();
			temp.add(new Attribute("b"));
			fds.add(new FunctionalDependency(temp,new Attribute("c")));
			// tables
			// a b
			// b c d
			// fds
			// b -> c
			assertFalse(FDChecker.checkLossless(t1, t2, fds));
			
			fds.add(new FunctionalDependency(temp, new Attribute("d")));
			// tables
			// a b
			// b c d
			// fds
			// b -> c
			// b -> d
			assertTrue(FDChecker.checkLossless(t1, t2, fds));
			
		}
		@org.junit.Test
		public void losslesstest1() {
			AttributeSet t1 = new AttributeSet();
			AttributeSet t2 = new AttributeSet();
			Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
			
			t1.add(new Attribute("a"));
			t1.add(new Attribute("b"));
			t1.add(new Attribute("c"));
			t2.add(new Attribute("a"));
			t2.add(new Attribute("d"));
			
			AttributeSet temp = new AttributeSet();
			temp.add(new Attribute("a"));
			fds.add(new FunctionalDependency(temp,new Attribute("b")));
			AttributeSet temp1 = new AttributeSet();
			temp1.add(new Attribute("a"));
			fds.add(new FunctionalDependency(temp1,new Attribute("c")));

			// tables
			// a b c
			// a d
			// fds
			// a -> c
			//a--->b
			assertTrue(FDChecker.checkLossless(t1, t2, fds));
			
		}
		@org.junit.Test
		public void losslesstest2() {
			AttributeSet t1 = new AttributeSet();
			AttributeSet t2 = new AttributeSet();
			Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
			
			t1.add(new Attribute("a"));
			t1.add(new Attribute("b"));
			t1.add(new Attribute("c"));
			t2.add(new Attribute("a"));
			t2.add(new Attribute("d"));
			
			
			AttributeSet temp1 = new AttributeSet();
			temp1.add(new Attribute("a"));
			fds.add(new FunctionalDependency(temp1,new Attribute("d")));

			// tables
			// a b c
			// a d
			// fds
			// a -> c
			//a--->b
			assertTrue(FDChecker.checkLossless(t1, t2, fds));
			
		}
	}
	
	@org.junit.Test
	public void depPresBasictest11111() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		AttributeSet t3 = new AttributeSet();
		AttributeSet t4 = new AttributeSet();
		AttributeSet t5 = new AttributeSet();
		AttributeSet t6 = new AttributeSet();
		AttributeSet t7 = new AttributeSet();
		AttributeSet t8 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t1.add(new Attribute("b"));
		t1.add(new Attribute("c"));
		t2.add(new Attribute("d"));
		t2.add(new Attribute("e"));
		t3.add(new Attribute("a"));
		t4.add(new Attribute("b"));
		t5.add(new Attribute("c"));
		t6.add(new Attribute("d"));
		t7.add(new Attribute("e"));
		t8.add(new Attribute("f"));
		
		fds.add(new FunctionalDependency(t3,new Attribute("e")));
		// tables
		// abc
		// de
		// fds
		// a -> e
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));
		t3.add(new Attribute("c"));
		fds.add(new FunctionalDependency(t3,new Attribute("b")));
		fds.add(new FunctionalDependency(t3,new Attribute("c")));
		t3.add(new Attribute("e"));
		fds.add(new FunctionalDependency(t3,new Attribute("b")));
		t1.add(new Attribute("e"));
		// tables
		// abce
		// de
		// fds
		// a -> e, ac-> b, ac -> c, ace -> b
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		fds.add(new FunctionalDependency(t3,new Attribute("d")));
		
		// tables
		// abce
		// de
		// fds
		// a -> e, ac-> b, ac -> c, ace -> b, ace -> d
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));
		t1.add(new Attribute("d"));
		assertTrue(FDChecker.checkDepPres(t1, t2, fds));
		t2.add(new Attribute("f"));
		fds.add(new FunctionalDependency(t8,new Attribute("d")));
		fds.add(new FunctionalDependency(t8,new Attribute("b")));
		assertFalse(FDChecker.checkDepPres(t1, t2, fds));

	}

	@org.junit.Test
	public void losslessBasictest1111() {
		AttributeSet t1 = new AttributeSet();
		AttributeSet t2 = new AttributeSet();
		AttributeSet t3 = new AttributeSet();
		AttributeSet t4 = new AttributeSet();
		AttributeSet t5 = new AttributeSet();
		AttributeSet t6 = new AttributeSet();
		AttributeSet t7 = new AttributeSet();
		AttributeSet t8 = new AttributeSet();
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		
		t1.add(new Attribute("a"));
		t2.add(new Attribute("b"));
		
		t3.add(new Attribute("a"));
		t4.add(new Attribute("b"));
		t5.add(new Attribute("c"));
		t6.add(new Attribute("d"));
		t7.add(new Attribute("e"));
		t8.add(new Attribute("a"));
		t8.add(new Attribute("f"));
		fds.add(new FunctionalDependency(t1,new Attribute("b")));
		// tables
		// a
		// b
		// fds
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t1.add(new Attribute("b"));
		// tables
		// a b
		// b
		// fds
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		
		t1.remove(new Attribute("b")); 
		
		// tables
		// a
		// b
		// fds
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t1.add(new Attribute("c"));
		t1.add(new Attribute("f"));
		t2.add(new Attribute("d"));
		t2.add(new Attribute("e"));
		t2.add(new Attribute("g"));
		t2.add(new Attribute("h"));
		// tables
		// acf
		// bdegh
		// fds: a->b
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t1.add(new Attribute("e"));
		fds.add(new FunctionalDependency(t7,new Attribute("a")));
		fds.add(new FunctionalDependency(t7,new Attribute("f")));
		// tables
		// acef
		// bdegh
		// fds: a->b, e->a, e->f
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		fds.add(new FunctionalDependency(t8,new Attribute("c")));
		// tables
		// acef
		// bdegh
		// fds: acef->b, e->a, e->f, af ->c
		System.out.println("!!!");
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		t1.add(new Attribute("b"));
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		
		t1.add(new Attribute("l"));
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		t8.add(new Attribute("e"));
		fds.add(new FunctionalDependency(t8,new Attribute("l")));
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		t1.add(new Attribute("m"));
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		
		t4.add(new Attribute("a"));
		t6.add(new Attribute("b"));
		fds.add(new FunctionalDependency(t4,new Attribute("g")));
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		fds.add(new FunctionalDependency(t6,new Attribute("h")));
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		t1.add(new Attribute("d"));
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
		t1.remove(new Attribute("d"));
		assertFalse(FDChecker.checkLossless(t1, t2, fds));
		fds.add(new FunctionalDependency(t5,new Attribute("d")));
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
	
		assertTrue(FDChecker.checkLossless(t1, t2, fds));
	}
	
	
}
