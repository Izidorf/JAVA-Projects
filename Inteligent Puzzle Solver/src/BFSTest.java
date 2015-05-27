import static org.junit.Assert.*;

import org.junit.Test;


public class BFSTest {

	@Test
	public void test() {
		String[][] s1 = {{"","","A",""},
				{"","","",""},
				{"","B","",""},
				{"","C",":-)",""}};
		
		String[][] s2 = {{"","","",":-)"},
				{"","A","",""},
				{"","B","",""},
				{"","C","",""}};
		
		String[][] s3 = {{"","","A",""},
				{"","","",""},
				{"","B","",""},
				{"","C",":-)",""}};
		
		String[][] s4 = {{"","","A",""},
				{"","","",""},
				{"","B","",""},
				{"","C",":-)",""}};
		
	 
		assertEquals("UUULDRRDD", new BFS().bfs(new PuzzleState(s1)).getPath());
		assertEquals("DDD", new BFS().bfs(new PuzzleState(s2)).getPath());
	
	
	
	
	
	}

}
