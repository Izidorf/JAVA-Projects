
public class Cellist extends Musician {

//constructor which references super constructor for the purpose of code reusability
public Cellist(SoundSystem soundSystem, int seat) {
		super(soundSystem, seat);
		soundSystem.setInstrument(seat, 43);	
		this.seat = seat; 
		this.soundSystem = soundSystem; 
		this.type="cellist";
	}		

		public void playSoft() {
			this.loudness=50;
		}
		
		public void playLoud() {
			this.loudness=100;	
		}

	
	
}
