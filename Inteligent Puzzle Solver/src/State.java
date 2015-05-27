import java.util.Vector;


public interface State {
	
	/* */
	public boolean isGoal();
	
	/* */
	public Vector<State> genSuccesors();
	
	/* */
	public void printState();
	
	/*
	 * Tells whether two states equal each other
	 * */
	public boolean equals(State s);
	
	/* */
	public String[][] getCurrentSate();
	
	/* */
	public int findCost();
	
	public String getLastOperator();
	
	public int getManDistc();
	
	
	
	

}
