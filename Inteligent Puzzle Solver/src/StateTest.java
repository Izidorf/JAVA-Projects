import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;


public class StateTest {

	@Test
	public void corNumSuccessors() {
		String[][] state4 = {{"","","",""},
				{"","",":-)",""},
				{"","","",""},
				{"A","B","C",""}};
		PuzzleState p = new PuzzleState(state4);
		Vector<State> suc = p.genSuccesors();
		assertEquals(suc.size(), 4);

		String[][] state2 = {{":-)","","",""},
				{"","","",""},
				{"","","",""},
				{"A","B","C",""}};
		PuzzleState p2 = new PuzzleState(state2);
		Vector<State> suc2 = p2.genSuccesors();
		assertEquals(suc2.size(), 2);




	}
	
	@Test
	public void isGoal(){
		String[][] startState1 = {{"","","",""},
				{"","",":-)",""},
				{"","","",""},
				{"A","B","C",""}};
		
		String[][] startState2 = {{"","","",""},
				{"","A","",""},
				{"","B","",""},
				{"","C","",":-)"}};
		
		PuzzleState p1 = new PuzzleState(startState1);
		PuzzleState p2 = new PuzzleState(startState2);
		
		assertEquals(false,p1.isGoal());
		assertEquals(true, p2.isGoal());
		
		
		
		
	}
	
	@Test
	public void testEquals(){
		String[][] startState1 = {{"","","",""},
				{"","",":-)",""},
				{"","","",""},
				{"A","B","C",""}};
		
		String[][] startState2 = {{"","","",""},
				{"","A","",""},
				{"","B","",""},
				{"","C","",":-)"}};
		
		String[][] startState3 = {{"","","",""},
				{"","",":-)",""},
				{"","","",""},
				{"A","B","C",""}};
		
		PuzzleState p1 = new PuzzleState(startState1);
		PuzzleState p2 = new PuzzleState(startState2);
		PuzzleState p3 = new PuzzleState(startState3);
		
		assertFalse(p1.equals(p2));
		assertTrue(p1.equals(p3));
		assertFalse(p2.equals(p3));
		
		
		
		
	}

	
	@Test
	public void testManhDist(){
		String[][] startState1 = {{"","","",""},
				{"","A","",""},
				{"","B","",":-)"},
				{"","C","",""}};
		
		String[][] startState2 = {{"","","",""},
				{"","A","",""},
				{"","B","",""},
				{"","C","",":-)"}};
		
		String[][] startState3 = {{"",":-)","",""},
				{"","A","",""},
				{"","B","",""},
				{"","C","",""}};
		
		String[][] startState4 = {{"",":-)","",""},
				{"A","","",""},
				{"","","","B"},
				{"","","","C"}};
		
		PuzzleState p1 = new PuzzleState(startState1);
		PuzzleState p2 = new PuzzleState(startState2);
		PuzzleState p3 = new PuzzleState(startState3);
		PuzzleState p4 = new PuzzleState(startState4);
		
		assertEquals(0, p2.getManDistc());
		assertEquals(1, p1.getManDistc());
		assertEquals(5, p3.getManDistc());
		assertEquals(10, p4.getManDistc());
		
	}

}
