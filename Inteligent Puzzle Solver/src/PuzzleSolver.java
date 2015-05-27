import java.util.Vector;


public class PuzzleSolver {
	
	public static void main(String[] args) {
		 PuzzleState p = new PuzzleState();
		 p.printState();
		 Vector<State> suc = p.genSuccesors();
		// System.out.println(p.canMoveWest(goalState));
		 System.out.println(suc.size());
		  
		 
		  System.out.println();
		  System.out.println("Original State");
		 p.printState();
		 System.out.println();
		 System.out.println("Changed State");
		// suc.get(0).printState();
		// suc.get(1).printState();
		 for(State s : suc){
			 System.out.println();
			 s.printState();
			 System.out.println();
		 }
		 
		 String[][] s = {{"","","A",""},
					{"","","",""},
					{"","B","",""},
					{"","C",":-)",""}};
		 
		
			
		 String[][] s1 = {{"", "+"},
				 			{":-)","A"}};
		 
		 String[][] s2 = {{"", "+"},
		 			{"A",":-)"}};
		 
		 
	//	 System.out.println(" Is there solution?: ");
	//	 BFS b = new BFS();
	//	b.bfs(new PuzzleState(s)).displaySolutionData();
	//	b.bfs(new PuzzleState(s1,s2)).displaySolutionData();
		 
		 DFS dfs = new DFS();
		 dfs.dfs(new PuzzleState(s)).displaySolutionData();
		 
		 IDS ids = new IDS();
	//	 ids.iterativeDeepeningSearch(new PuzzleState(s), 12).displaySolutionData();
		 
		 AStar aStar = new AStar();
	//	 aStar.aStar(new PuzzleState(s)).displaySolutionData();
		 
		}

}
