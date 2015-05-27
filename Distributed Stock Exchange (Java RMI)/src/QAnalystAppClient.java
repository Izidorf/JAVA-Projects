

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class QAnalystAppClient {
	
	//Set to Locate registry
	public static final int PORT=1098;
	public static final String ADDRESS="localhost";
	
	/*How many times should sink try to reconnect before it stops trying 
	  Provides error handling on the sink side							*/
	public static final int MAX_NUM_TRIES = 5;
	public static final int TIME_BETWEEN_TRIES = 2000; //two seconds
	
	public static void main(String[] args) {
		/*	If the framework is running on several machines, contrary to only one, the RMISecurity manager has to be used.
		 * 
		 *	if (System.getSecurityManager() == null) {
		 *  System.setSecurityManager(new RMISecurityManager());
		 *	}
		 */
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
		
		
		JFrame sinkFrame = new JFrame("COMP2207 if2g11 - Distributed Stock App - Quantitative Analyst Client App");
		sinkFrame.setSize(600,600);
		sinkFrame.setVisible(true);
		sinkFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		QAnalistAppPane sinks = new QAnalistAppPane();
		sinkFrame.setContentPane(sinks);
			}
	    });
	
	}
}
