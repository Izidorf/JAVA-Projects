
import java.rmi.*;

public interface NotificationSinkInterface extends Remote {
	
	
	//Callback purpose - this method may be invoked by server whenever changes occur
	public Object notifySink(Notification ntf) throws RemoteException; //Notification

}
