
public class SearchNode implements Comparable<SearchNode> {
	
	protected State currentState;
	protected SearchNode parentNode;
	protected String operator;
	protected int depth;
	protected int cost;
	protected int hCost;
	protected int fCost;
	
	/*
	 * Creates a root node
	 * */
	public SearchNode(State state){
		this.currentState=state;
		this.parentNode=null;
		this.operator="";
		this.cost=0;
		this.depth=0;
	}
	
	public SearchNode(State currentState, SearchNode parentState, int cost, String operator, int depth){
		this.currentState=currentState;
		this.parentNode=parentState;
		this.operator=operator;
		this.cost+=cost+1;
		this.depth+=depth+1;
		
	}
	
	public SearchNode(State currentState, SearchNode parentState, int cost, String operator, int depth, int hCost){
		this.currentState=currentState;
		this.parentNode=parentState;
		this.operator=operator;
		this.cost+=cost+1;
		this.depth+=depth+1;
		this.hCost+=hCost;
		this.fCost=cost+hCost;
		
	}
	

	public State getCurrentState() {
		return currentState;

	}
	
	public int getCost() {
		return cost;
	}
	
	public int getDepth(){
		return depth;
	}
	
	public SearchNode getParentNode() {
		return parentNode;
	}
	
	public int getfCost() {
		return fCost;
	}

	@Override
	public int compareTo(SearchNode n) {
		 int n2=n.getfCost(); int n1 = this.getfCost();
         if (n1 > n2)
                 return 1;
         if (n1 < n2)
                 return -1;
         return 0;
		
	}
	
	
	

}
