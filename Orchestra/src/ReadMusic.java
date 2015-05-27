import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadMusic {
	
protected  BufferedReader reader;
protected int count=0; //to test if there are more then two leadviolinists

//constructor which reads from the music file..which is passed in as a parameter
public ReadMusic(String file) {
		try {
			this.reader= new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Exception: file does not exist!");
		}
	}

	//read the first line of the file
	public String getLine() { 
		try {
			return reader.readLine();
		} catch (IOException e) {
			System.err.println("Can not get a line!");
			return "false";
		}
	}
	
	//boolean method to find out whether file is ready to be read
	public boolean isFileReady() {
		try {
			        return reader.ready();
		} catch (IOException ex) {
			    	  System.err.println("Exception: file is not ready!");
			    	  return false;
			      }
	}
	
	//This function adds a flashcard to an array list and returns the array list.
	public ArrayList<Notes> getMusicInfo(){ 
		ArrayList<Notes> musicianInfo = new ArrayList<Notes>(); //make new array of flashcards
		
	
		String strLine;
	      while ((strLine = getLine()) != null) { //read lines until the line is empty
	  		
	  	// Print the content on the console
			String [] line2 = strLine.split(":"); //Split into array where :
			String musician = line2[0];
			String loudness = line2[1];
			String notes = line2[2];
           //System.out.println (musician+loudness+notes); 
           int [] notesArray = getNotesArray(notes);
           
           //throw exception if there is more then one leadviolin
          
           if(musician.equalsIgnoreCase("leadviolin")){
        	   this.count++;
        	   if(this.count >= 2){
        		  try { throw new Exception("There can be only be one leadviolin, change your .mus file!"); }
        		  catch (Exception e) {
        			  System.err.println(e);
        			  System.exit(1);
        		  }
        	   }
           }
	  		
          
	  	//now pass it into constructor
	  		Notes info = new Notes(musician, loudness, notesArray);
			
			//now add it to the array list
	  		musicianInfo.add(info);
	    	  
	    }
		
		return musicianInfo; //returns an Array of FlashCards
	}
	
	//This method converts a String of notes into an array of int
		public int[] getNotesArray(String notes) {
			String [] items = notes.split(","); //get an array of strings of numbers
			
			int[] results = new int[items.length]; //make a new array of int whith the same lenght as number of elements in the String array

			for (int i = 0; i < items.length; i++) {
			    try {
			        results[i] = Integer.parseInt(items[i]); //Converts Strings to integetrs and put them into integer array
			    } catch (NumberFormatException nfe) {
			    	System.err.println("Error: The notes are not written in the correct format" + nfe.getMessage());
			    };
			}
			
			return results;	
		}
	
	
	

}
