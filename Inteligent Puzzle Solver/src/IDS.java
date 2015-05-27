import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;


public class IDS {
	
	private long startTime;
	
	public IDS(){
	this.startTime=System.currentTimeMillis();
	}
	
	public Solution iterativeDeepeningSearch(State startState, int depthLimit){
		for(int i=0; i<depthLimit; i++){
			Solution s = depthLimitedSearch(startState, i);
			if(s.containsSolution()){
				return s;
			}
		}
		return new Solution(false);
	}
	

	public Solution depthLimitedSearch(State startState, int depthLimit){
		Stack<SearchNode> toVisit = new Stack<SearchNode>();
		Set<State> visited = new HashSet<State>();
		
		SearchNode root = new SearchNode(startState);
		
		toVisit.push(root);
		visited.add(root.getCurrentState());
		
		int spaceCount = 0;
		
		while(!toVisit.isEmpty()){
			SearchNode current = toVisit.pop();
			
			if(current.getCurrentState().isGoal()) {					
				return new Solution(getSolutionPath(current), spaceCount, current.getCost(), current.getDepth(), (System.currentTimeMillis() - startTime)); //return SOLUTION spaceCount
			}
			
			if(!(current.getDepth() == depthLimit)){
				
			
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
