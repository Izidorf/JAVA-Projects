


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class StockAppServer {
	
	
	public static final int REGISTRY_PORT=1098;

	public static void main(final String[] args) {
		/*	If the framework is running on several machines, contrary to only one, the RMISecurity manager has to be used.
		 * 
		 *	if (System.getSecurityManager() == null) {
		 *  System.setSecurityManager(new RMISecurityManager());
		 *	}
		 */
		try {
			 LocateRegistry.createRegistry(REGISTRY_PORT);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
		if(args.length != 2){
			System.out.println("You need to enter two arguments! java StockAppServer [int: sourceid] [String: stockname]");
			System.out.println("Eg. java StockAppServer 0 Apple");
			System.out.println("Eg. java StockAppServer 1 Micrisoft");
			System.out.println("Eg. java StockAppServer 2 Bloomberg");
			System.exit(0);
		}
		
		JFrame stockFrame = new JFrame("COMP2207 if2g11 - Distributed Stock App - "+ args[1]);
		stockFrame.setSize(600,600);
		stockFrame.setVisible(true);
		stockFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		StockAppPanel source = new StockAppPanel(args[0], args[1]);
		stockFrame.setContentPane(source);
			}
	    });
	}
	


}
