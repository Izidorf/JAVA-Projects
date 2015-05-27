
public class Trumpeter extends Musician{



//constructor which references super constructor for the purpose of code reusability	
public Trumpeter(SoundSystem soundSystem, int seat) {
		super(soundSystem, seat);
		soundSystem.setInstrument(seat, 57);	
		this.seat = seat; 
		this.soundSystem = soundSystem; 
		this.type="trumpet";
}	
		
	public void playSoft() {
		this.loudness=50;
	}
		
	public void playLoud() {
		this.loudness=100;	
	}
	

	
	
}
