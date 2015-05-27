import java.util.*;

public abstract class Musician {

protected int instrument;
protected final int MAXNOTES=200;
protected int loudness;
protected SoundSystem soundSystem;
protected int seat;
protected int[] music;
protected int count =0;
protected String type;

//constructor for musician
public Musician(SoundSystem soundSystem, int seat) {
		soundSystem.setInstrument(seat, this.instrument);
		this.seat=seat;
		this.soundSystem=soundSystem;
}	

	//stores music in the instance variable within a class
	public void readMusic(int[] array){
		this.music=array; //assigns the notes to the instance variable music
	}

	//method which plays next note in the array of integers
	public void playNextNote() {
		//EXSTENTION
		//System.out.println("Press enter to play next note!");
		//create scanner and read input	
		//Scanner input = new Scanner(System.in);
		//String next = input.nextLine(); //read input from user
	//	if (next.equals("")) {
		//if there is 0 do not play the note
		if (music[count] == 0) {
			this.soundSystem.stopNote(this.seat, music[count], 150);
		} else if (count >= this.MAXNOTES) {
			System.err.println("Your musicians are too tired, they  can only play " + this.MAXNOTES + " notes. Play song again or increase the number of notes they can play by changing the value of MAXNOTES in Musician Class");
			System.exit(1);
		} else{ //play next note
			//insert in seat number, notes and loudness
			this.soundSystem.playNote(this.seat, music[count], this.loudness); //passes seatnumber, notes and volume to the sound system
		
		}
		
	//	} else {
	//			 System.out.println("Wrong key pressed press enter to play next note!");
	//			}
		
		//Pint out notes and the loudness to demonstrate that everything is working
		System.out.print("note: " + music[count]+", "); //This is here for the testing puroses
		System.out.println("loudnes: " + this.loudness);
		
		count++;
		
	}
	
	public abstract void playSoft(); 
	
	public  abstract void playLoud();
	
	public void setSeat(int seatNo) {
		
		
	}
	
	public int[] getMusic () {
		return this.music;
	}
	
	public int getSeat(){
		return this.seat;
	}
	
	public int getLoudness() {
		return this.loudness;
	}
	
	//This method sets loudness according to the file that is passed as an input argument
	public void setLoudness(String loudness){
		if(loudness.equalsIgnoreCase("loud")){
			playLoud();
		} else if(loudness.equalsIgnoreCase("soft")) {
			playSoft();
		} else {
			//THROW AN EXCEPTION????
			System.err.println("The loudness is wrongly defined it should be either loud of soft");
		}
		
	}
	
	//Return a type of the musician violin, piano...
	public String getType() {	
		return this.type;
	}

}
