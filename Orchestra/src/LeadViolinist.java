
public class LeadViolinist extends Violinist{

//constructor which references super constructor for the purpose of code reusability	
public LeadViolinist(SoundSystem soundSystem, int seat) {
		super(soundSystem, seat);
		soundSystem.setInstrument(seat, 41);	
		this.seat = seat; 
		this.soundSystem = soundSystem; 
		this.instrument=41;
		this.type="leadviolin";
}

	public void playSoft() {
		this.loudness=50;
	}
	
	public void playLoud() {
		this.loudness=100;	
	}

}
