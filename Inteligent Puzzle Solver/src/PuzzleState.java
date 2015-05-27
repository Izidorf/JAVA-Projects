import java.util.Arrays;
import java.util.Vector;


public class PuzzleState implements State{

	public String[][] currentState = {{"","","",""},
			{"","","",""},
			{"","","",""},
			{"A","B","C",":-)"}};

	public static String[][] goalState = {{"","","",""},
		{"","","A",""},
		{"","B","",""},
		{"","C","",":-)"}};


	public String lastOperator="";
	
	public PuzzleState() {
	}
	
	public PuzzleState(String[][] state) {
		this.currentState=state;
	}
	
	public PuzzleState(String[][] start, String[][] goal){
		this.goalState=goal;
		this.currentState=start;
	}
	
	public PuzzleState(String[][] state, String lastOperator) {
		this.currentState=state;
		this.lastOperator=lastOperator;
	}

	public void printState(){
		for(String[] row : currentState){
			for(String column : row){
				if(!column.equals(""))
					System.out.print(" "+column+" ");
				else
					System.out.print(" * ");
			}
			System.out.print("\n"); //ends row
		}

	}

	public void printState(String[][] state){
		for(String[] row : state){
			for(String column : row){
				if(!column.equals(""))
					System.out.print(" "+column+" ");
				else
					System.out.print(" * ");
			}
			System.out.print("\n"); //ends row
		}

	}

	public boolean canMoveNorth(int i, int j){
		if(!(i==0))
			return  (!currentState[i-1][j].equals("+"));
		else
			return false;
	}

	public boolean canMoveSouth(int i, int j){
		if (!(i== currentState.length-1))
			return (!currentState[i+1][j].equals("+"));
		else
			return false;
	}

	public boolean canMoveWest(int i, int j){		
		if(!(j==0))
			return (!currentState[i][j-1].equals("+"));
		else
			return false;
	} 
	
	public boolean canMoveEast(int i, int j){
		if(!(j==currentState[i].length-1))
			return (!currentState[i][j+1].equals("+"));
		else
			return false;
	} 

	


	public Vector<State> genSuccesors(){
		Vector<State> sucessors = new Vector<State>();

		for(int i=0; i<currentState.length; i++){
			for(int j=0; j<currentState[i].length;  j++){	
				
				if(currentState[i][j].equals(":-)")){ 
					if(canMoveWest(i,j))
						swapElements(i, j, i, j-1, sucessors, "L");
					
					if(canMoveEast(i,j))
						swapElements(i, j, i, j+1, sucessors, "R");
					
					if(canMoveSouth(i,j)) 
						swapElements(i, j, i+1, j, sucessors, "D");
					
					if(canMoveNorth(i,j)) 
						swapElements(i, j, i-1, j, sucessors, "U");
					
				}			
			}
		}
		return sucessors;
	}

	public void swapElements(int p1a, int p1b, int p2a, int p2b, Vector<State> sucessors, String operator){
		String[][] sucessor = copyState(this.currentState);
		String temp1 = currentState[p1a][p1b];
		//String temp2 = currentState[p2a][p2b];
		sucessor[p2a][p2b]=temp1;
		sucessor[p1a][p1b] = currentState[p2a][p2b]; //temp2
		PuzzleState sol = new PuzzleState(sucessor, operator);
		sucessors.add(sol);
	}
	
	public String[][] copyState(String[][] state){
		String[][] temp = new String[state.length][state[0].length];	
		for(int i=0; i<state.length; i++){
			for(int j=0; j<state[i].length;  j++){	
				temp[i][j]= state[i][j];				
			}
		}
		return temp;

	}
	
	//replace with constants presenting the grid size
	public int getManDistc(){
		int totalDistance=0;
		for(int yC=0; yC<this.currentState.length; yC++)
			for(int xC=0; xC<this.currentState[0].length; xC++){
				
				//for every element in the puzzle find its final position and distance to it
				for(int yG=0; yG<this.currentState.length; yG++)
					for(int xG=0; xG<this.currentState[0].length; xG++)
						if(currentState[xC][yC].equals(goalState[yG][xG]) && !(currentState[xC][yC].equals(""))){
								totalDistance+=Math.abs(xG-yC)+Math.abs(xC-yG);	
						}
			}
		return totalDistance;	
	}

	public String[][] getCurrentSate(){
		return this.currentState;
	}

	@Override
	public boolean isGoal() {
		return Arrays.deepEquals(currentState, goalState);
	}

	@Override
	public boolean equals(State s) {
		return Arrays.deepEquals(currentState,s.getCurrentSate());
	}

	@Override
	public int findCost() {
		return 0;
	}
	
	public void setLastOperator(String lastOperator) {
		this.lastOperator = lastOperator;
	}
	
	public String getLastOperator() {
		return lastOperator;
	}



}
