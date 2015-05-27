
public class Pianist extends Musician {

//constructor which references super constructor for the purpose of code reusability
public Pianist(SoundSystem soundSystem, int seat) {
	super(soundSystem, seat);
	soundSystem.setInstrument(seat, 1);	
	this.seat = seat; 
	this.soundSystem = soundSystem; 
	this.type="pianist";
}	
		
		public void playSoft() {
			this.loudness=100;
		}
		
		public void playLoud() {
			this.loudness=200;	
		}
}
