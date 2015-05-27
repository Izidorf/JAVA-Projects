import java.io.*;
import java.util.*;

public class Conductor {
	
	static ArrayList<Notes> list;
	// We will need a sound system to play it through.
	static SoundSystem soundSystem;

	
public Conductor(String file) {
		//Create a file reader to read from the music file and create an ArrayList of objects with all the relevant information
		ReadMusic readMusic = new ReadMusic(file);
		this.list = readMusic.getMusicInfo();
		// Initialise the sound system
		this.soundSystem = new SoundSystem();
		soundSystem.init(true);
		
	}
	
	void start(){
	
		//The conductor makes a new orchestra
				Orchestra orchestra = new Orchestra();
				
				//Loop through list if Musician Info to get all the music info required
				int count = 0; 
				for (Notes l : list) {
					System.out.println(l.getInstrument()+ " " +l.getLoudness() + " " + l.getNotes());
					//Create musicians
					if(l.getInstrument().equalsIgnoreCase("leadviolin")){
						LeadViolinist leadViolin = new LeadViolinist(soundSystem, count); //Change that
						orchestra.addMusician(leadViolin); //add leadViolin to orchestra
						leadViolin.readMusic(l.getNotes()); //get notes for lead violin
						leadViolin.setLoudness(l.getLoudness()); //get loudness for lead violin
				
						
					} else if(l.getInstrument().equalsIgnoreCase("violin")) {
						Violinist violin = new Violinist(soundSystem, count);
						orchestra.addMusician(violin); 
						violin.readMusic(l.getNotes()); 
						violin.setLoudness(l.getLoudness());
					
						
						
					} else if(l.getInstrument().equalsIgnoreCase("cello")){
						Cellist cellist = new Cellist(soundSystem, count);
						orchestra.addMusician(cellist); 
						cellist.readMusic(l.getNotes());
						cellist.setLoudness(l.getLoudness());
						
					} else if(l.getInstrument().equalsIgnoreCase("trumpet")){
						Trumpeter trumpet = new Trumpeter(soundSystem, count);
						orchestra.addMusician(trumpet); 
						trumpet.readMusic(l.getNotes());
						trumpet.setLoudness(l.getLoudness());
						
					} else if(l.getInstrument().equalsIgnoreCase("piano")){
						Pianist pianist = new Pianist(soundSystem, count);
						orchestra.addMusician(pianist); 
						pianist.readMusic(l.getNotes());
						pianist.setLoudness(l.getLoudness());
					} else {
						System.err.println("Shit happens, sometimes! "+l.getInstrument()+ " is not a valid musician.");
						
					}

					count++; //increse the count after every round to position musician to one seat higher
				
				}
				
			
				
				//Now give the musicians the notes to play 
				//Get the lenght of the notes of the first musician an loop through the notes
				for(int n=0; n<orchestra.getMusician(0).getMusic().length; n++)
						{	
							
					//Do another loop to get all musicians out of the orchestra and tell them to play music
					for (int i=0; i<orchestra.size(); i++){
						orchestra.getMusician(i).playNextNote();
						
						//Check that all musicians have the equal number of notes.
						if (orchestra.getMusician(0).getMusic().length < orchestra.getMusician(i).getMusic().length || orchestra.getMusician(0).getMusic().length > orchestra.getMusician(i).getMusic().length){
							System.err.println("Some of the musicians run out of music, ensure that all musicians have the same number of notes.");
							System.exit(1);
						}
						
					} 
					
						
						
							// Tell the musicians to play their notes at half a second intervals.
							try
							{
								Thread.sleep(150);
							}
							catch (InterruptedException e)
							{
							}
						}
						
						//Deleate these two lines
						System.out.println();
						System.out.println("The orchestra size is: "+orchestra.size());
						System.out.println("On the first seat is sitting: "+orchestra.getMusician(0).getType());
						
		
	}
	
	

	
	
	public static void main(String[] args) {
	
		//Checks that there is really only one parameter
		if(args.length != 1) {
		  System.err.println("Invalid command line, exactly one argument required");
		  System.exit(1);
		}
		
		//pass the file into readFile class to parse out musician, loudness and music.
		String file = args[0];


		//Create new conductor
		Conductor conductor = new Conductor(file);	//tell him whom he conducts
		conductor.start(); //Tell him to start conducting
		
	
		
	} //ends Main
}//ends Conductor
	