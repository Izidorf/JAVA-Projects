

import java.rmi.Naming;


public class StockApp {
	

	void sendDataToClients(NotificationSource ns, Object data, String sourceId) {
		try{
		
			Naming.rebind("source"+sourceId, ns); // Naming service: Binds local object to global
			ns.broadcastWithCorrection(data);
			
		} catch(Exception e){
			System.err.print(e);
		}
		

	}

}
