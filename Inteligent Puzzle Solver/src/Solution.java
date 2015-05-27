import java.util.*;


public class Solution {

	Stack<SearchNode> solutionPath = new Stack<SearchNode>();
	int spaceSize;
	int cost;
	int depth;
	boolean containsSolution;
	long time;
	
	public Solution(Stack<SearchNode> solutionPath, int spaceSize, int cost, int depth, long time) {
		this.solutionPath=solutionPath;
		this.spaceSize=spaceSize;
		this.cost=cost;
		this.depth=depth;
		this.containsSolution=true;
		this.time=time;
	}
	
	public Solution(boolean f){
		if(!f){
			this.containsSolution=f;
		}
	}
	
	public void displaySolutionData(){
		if(containsSolution){
		String path =getPath();
		System.out.println("The path is: "+ path);
		System.out.println("The time required is: "+ getTime() + "ms");
		System.out.println("The space size is: "+ getSpaceSize());
		System.out.println("The solution cost is: "+ getCost());
		System.out.println("The solution depth is: "+ getDepth());
		} else {
			System.out.println("No solution found!");
		}
	}
	
	// U - up, L - left, D - down, R - right
	public String getPath(){
		Stack<SearchNode> path = this.solutionPath;
		String operations = "";
		if(path.size() > 0)
		if(path.peek() != null)
		while(!path.isEmpty()) {
			operations =operations.concat(path.pop().getCurrentState().getLastOperator());
		}
		return operations;
		
	}
	
	
	public int getDepth() {
		return this.depth;

	}
	
	public int getCost() {
		return cost; //or return getPath().size();

	}
	
	public int getSpaceSize(){
		return this.spaceSize; 
	}
	
	public boolean containsSolution(){
		return this.containsSolution;
	}
	
	public long getTime() {
		return time;
	}
	
	
	

}
