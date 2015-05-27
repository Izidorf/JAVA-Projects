

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServerTest {
	
	public static void main(String[] args) {
//		if (System.getSecurityManager() == null) {
//		    System.setSecurityManager(new SecurityManager());
//		}
		//System.setSecurityManager(new RMISecurityManager());
			
		try{
			Registry registry = LocateRegistry.createRegistry(1099);

			NotificationSource nSource= new NotificationSource("apple");
		
			registry.rebind("source0", nSource); // Naming service: Binds local object to global
	
			System.out.println("remoteThingServer ready");
			
			Thread.sleep(3000);
			

			
			while(true){
			//nSource.broadcast(n1);
			String data =  generateStockData("apple");
			nSource.broadcastWithCorrection(data);
			Thread.sleep(4000);
			}
		} catch(Exception e){
			System.err.print(e);
		}
		
		
	
	
	}

	private static String generateStockData(String stockName){
		String stockvalue = stockName+" "+Double.toString(Math.random()*100)+" \n";
		return stockvalue;
	}
	
	
}

