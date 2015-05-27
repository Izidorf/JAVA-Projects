/*
 * The purpose of this class is to store the information from the music file that was parsed.
 */
public class Notes {
	
public String instrument;
public String loudness;
public int[] notes;

public Notes(String instrument, String loudness, int[] notes) {
	this.instrument=instrument;
	this.loudness=loudness;
	this.notes=notes;
}
	
	public String getInstrument() {
		return this.instrument;
	}
	
	public String getLoudness(){
		return this.loudness;
	}
	
	public int[] getNotes() {
		return this.notes;
	}
	
	

}
