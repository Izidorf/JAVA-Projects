
public class Violinist extends Musician{
	


//constructor which references super constructor for the purpose of code reusability
public Violinist(SoundSystem soundSystem, int seat) {
		super(soundSystem, seat);
		soundSystem.setInstrument(seat, 41);	
		this.seat = seat; 
		this.soundSystem = soundSystem; 
		this.instrument=41;
		this.type="violin";
}
	//set musicians soft loudness
	public void playSoft() {
		this.loudness=50;
	}
	
	//set musicians loud loudness
	public void playLoud() {
		this.loudness=100;	
	}

	

	
}
