/*
 * Ochestra class models seats in the orchestra, there are 16 seats represented by the ArrayLisy seats with indexes from 0-15.
 * Furthermore, the leadviolin always seats on the 0th seat...if there is no space once he is added the last musician has to leave the orchestra.
 */

import java.util.*;


public class Orchestra {

	ArrayList<Musician> seats = new ArrayList<Musician>();
	
	
	//adds musician to the next available seat and always puts leadviolinis to the seat number 0
	public void addMusician(Musician musician) {
		if (seats.size() < 16 && !musician.getType().equalsIgnoreCase("leadviolin")) { //If there are 16 musicians then do not add another one
		seats.add(musician);
		} else if(seats.size() == 16 && musician.getType().equalsIgnoreCase("leadviolin")){
			seats.remove(15);
			seats.add(0, musician);
		} else if (seats.size() < 16 && musician.getType().equalsIgnoreCase("leadviolin")) {
			seats.add(0, musician);
		} else {
			System.err.println("There are only 16 seats (from 0-15) available in the orchestra");
		}
		
	}
	
	//adds musician to the specified seat and always puts leadviolinis to the seat number 0
	public void addMusician(Musician musician, int seatNo) {
		if(seats.size() -1 < seatNo && seatNo < 16 && !musician.getType().equalsIgnoreCase("leadviolin")) {		//if the index is too high add musician to next available spot.
			seats.add(musician);
		} else if(seats.size() - 1 >= seatNo && seatNo < 16 && !musician.getType().equalsIgnoreCase("leadviolin")) {//if enough seats were allready created allow musician to seat on someone else seat
			if(getMusician(0).equals("Lead Violinist")){
				System.err.print("Hey this seat is taken, I'm lead violinist...you have to seat on the next seat");	
				seats.add(musician);
			} else {
				seats.add(seatNo, musician);
			}
		} else if (seats.size() == 16 && musician.getType().equalsIgnoreCase("leadviolin")) { //if there are 16 musitians allready allow lead violinist to replace person siting on the first seat
			seats.remove(15);
			seats.add(0, musician);
		} else if (seats.size() < 16 && musician.getType().equalsIgnoreCase("leadviolin")){ //If there are less then 16 musicians then put lead violinist to the first place
			seats.add(0, musician);
		} else if(getMusician(0).equals("Lead Violinist")) {
			
		}
	
	}
	
	//This method returns the number of the musicians in the orchestra
	public int size() {
		return seats.size(); //Using size() from ArrayList API
	}
	
	//returns a musician at a particular seat
	public Musician getMusician(int seatNo) {
		return seats.get(seatNo);		
	}
	
	//this method sets set for the musicians should they need to be moved...to use type ex. violinist.setSeat(int whereToPositionHim)
	public void setSeat(int seat) {
		int mus = this.seats.indexOf(this);		
		Musician musician= this.getMusician(mus);
		seats.add(seat, musician);
		
	}
	
	
}
