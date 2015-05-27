import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;


public class DFS {
	
	private long startTime;
	
	public DFS() {
		this.startTime=System.currentTimeMillis();
	}
	
	public Solution dfs(State startState){
		Stack<SearchNode> toVisit = new Stack<SearchNode>();
		Set<State> visited = new HashSet<State>();
		
		SearchNode root = new SearchNode(startState);
		
		toVisit.push(root);
		visited.add(root.getCurrentState());
		
		int spaceCount = 0;
		
		while(!toVisit.isEmpty()){
			SearchNode current = toVisit.pop();
			
			if(current.getCurrentState().isGoal()) {	
				//System.out.println("Time Cost is: " + (System.currentTimeMillis() - startTime)+ " ms");
				return new Solution(getSolutionPath(current), spaceCount, current.getCost(), current.getDepth(), (System.currentTimeMillis() - startTime)); //return SOLUTION spaceCount
			}
			
			//generate successors
			List<State> sucessors = current.getCurrentState().genSuccesors();
			
			//loop through successors
			for(State successor : sucessors){
				if(!visited.contains(successor.getCurrentSate())){
					visited.add(successor);
					SearchNode toBeVisited = new SearchNode(successor, current, current.getCost(), current.getCurrentState().getLastOperator(), current.getDepth());
					toVisit.push(toBeVisited);
				}	
				spaceCount++;
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
