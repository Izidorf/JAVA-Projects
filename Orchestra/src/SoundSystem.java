import javax.sound.midi.*;
import java.io.*;

public class SoundSystem {
	
	static Synthesizer synth;
	static MidiChannel[] mc;
	static int[] lastNotes;
	static boolean silentMode;
	static int[] instruments;

	SoundSystem()
	{
	}

	void init()
	{
		// They want to hear the pretty music
		silentMode = false;
		
		// We need this to stop the notes that are playing if necessary.
		lastNotes = new int[16];

		// Obtain information about all the installed synthesizers.
		//MidiDevice device = null;
		//MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		
		// Test code to make sure we have some synthesizers available.
		//for (int i = 0; i < infos.length; i++) {
		//	try {
		//		device = MidiSystem.getMidiDevice(infos[i]);
		//	} catch (MidiUnavailableException e) {
		//		  // Handle or throw exception...
		//	}
		//	if (device instanceof Synthesizer) {
		//		System.out.println(infos[i]);
		//	}
		//}

		// Create a new synthesizer and open it ready for playing.
		// Add in the instruments from the default soundbank.
		// This should be good enough as a starting point.
		try
		{
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			mc = synth.getChannels();
			Instrument[] instr = synth.getDefaultSoundbank().getInstruments();
			synth.loadInstrument(instr[90]);
		}
		catch(Exception e)
		{

		}
	}

	void init(boolean playAudibly)
	{
		// If we are in silentMode then just flag this, otherwise initialise the synthesizer.
		if(!playAudibly)
			silentMode = true;
		if(!silentMode)
			init();
		else
			instruments = new int[16];
	}

	void setInstrument(int instrumentPosition, int instrument)
	{
		// Change the appropriate channel to the given instrument or simply keep track for silent mode.
		if(!silentMode)
			mc[instrumentPosition].programChange(instrument);
		else
			instruments[instrumentPosition] = instrument;
	}
	
	void playNote(int instrumentPosition, int note, int length)
	{
		//If we have MIDI then stop the previous note if there was one and play the new one.
		if(!silentMode)
		{
			if(lastNotes[instrumentPosition] != 0)
				stopNote(instrumentPosition, lastNotes[instrumentPosition], 50);
			mc[instrumentPosition].noteOn(note,length);
			lastNotes[instrumentPosition] = note;
		}
		else
		{
			// Otherwise let's print out the note to the console.
			System.out.println("Playing note " + note + " on instrument " + instruments[instrumentPosition]);
		}
	}

	void stopNote(int instrumentPosition, int note, int length)
	{
		if(!silentMode)
			mc[instrumentPosition].noteOff(note,0);
	}

}
