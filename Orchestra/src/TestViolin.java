
public class TestViolin {

	// Need a scale of notes to get our violin to play
		static int[] notes = new int[] {60,62,64,65,67,69,71,72};

		// We will need a sound system to play it through.
		static SoundSystem soundSystem;

		// In the constructor create a new sound system
		public TestViolin()
		{
			soundSystem = new SoundSystem();
		}

		void start(boolean audible)
		{
			// Initialise the sound system
			soundSystem.init(audible);

			// Make a violinist
			Violinist violin;
			violin = new Violinist(soundSystem,1);
			
			//Pianist pianist = new Pianist(soundSystem, 2);/***************** *********/
			
			// Give the violinist some music to play
			violin.readMusic(notes);
			//pianist.readMusic(notes); /***************** *********/

			// Tell the violinist to play their notes at half a second intervals.
			for(int n=0; n<notes.length; n++)
			{	
				violin.playLoud();
				violin.playNextNote();
				//pianist.playLoud();		/***************** *********/
				//pianist.playNextNote(); /***************** *********/
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
				}
			}
			System.out.println("Test completed successfully");
		}


		public static void main(String[] args)
		{
			// Create a new tester class and call the start method on it.

			TestViolin testViolin = new TestViolin();

			// If the command was java TestViolin -s then play silently.
			if((args.length > 0) && (args[0].equals("-s")))
				testViolin.start(false);
			else
				testViolin.start(true);
		}
		
}//Ends TestViolin
