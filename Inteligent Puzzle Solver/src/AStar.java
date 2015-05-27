import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;


public class AStar {
	
	private long startTime;
	
	public AStar() {
		this.startTime=System.currentTimeMillis();
	}

	public Solution aStar(State startState){
		Queue<SearchNode> toVisit = new PriorityQueue<SearchNode>();
		Set<State> visited = new HashSet<State>();
		
		SearchNode root = new SearchNode(startState);
		
		toVisit.add(root);
		visited.add(root.getCurrentState());
		
		
		
		while(!toVisit.isEmpty()){
			SearchNode current = toVisit.poll();
			
			if(current.getCurrentState().isGoal()) {		
			//	System.out.println("Time Cost is: " + (System.currentTimeMillis() - startTime)+ " ms");
				return new Solution(getSolutionPath(current), toVisit.size(), current.getCost(), current.getDepth(), (System.currentTimeMillis() - startTime)); //return SOLUTION spaceCount
			}
			
			//generate successors
			List<State> sucessors = current.getCurrentState().genSuccesors();
			
			//temporary SearchNodes
			List<SearchNode> tempSuc = new ArrayList<SearchNode>();
			
			//loop through successors
			for(State successor : sucessors){
				if(!visited.contains(successor.getCurrentSate())){
				//	visited.add(successor);
					SearchNode toBeVisited = new SearchNode(successor, current, current.getCost(), current.getCurrentState().getLastOperator(), current.getDepth(), current.getCurrentState().getManDistc());
					
					//if successor is goal stop the search
					if(toBeVisited.getCurrentState().isGoal()){
						//System.out.println("Time Cost is: " + (System.currentTimeMillis() - startTime));
						return new Solution(getSolutionPath(toBeVisited), toVisit.size(), toBeVisited.getCost(), toBeVisited.getDepth(), (System.currentTimeMillis() - startTime)); //return SOLUTION spaceCount
					}
					
					if(!visited.contains(successor))
					//tempSuc.add(toBeVisited);
						toVisit.add(toBeVisited);
				}	
			
			}				
		}
		
		
		return new Solution(false); //return SOLUTION
	}
	
	public Stack<SearchNode> getSolutionPath(SearchNode solution){
		Stack<SearchNode> solutionPath = new Stack<SearchNode>();
		solutionPath.push(solution);
		
		SearchNode parent = solution.getParentNode();
		
		if(parent != null)
		while(parent.getParentNode() != null){
			solutionPath.push(parent);
			parent = parent.getParentNode();
		}
		
		solutionPath.push(parent);
		
		return solutionPath;
		
	}
	

	
	

}