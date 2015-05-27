

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationSourceInterface extends Remote {
	
	//Provides error handling for the messages that do not reach the destination
	//public final int TIME_BETWEEN=2000; Optionally could implement this variable as well
	public final int NUM_TRIES=5;
	
	//Allows sinks to obtain a reference to the remote object
	public void registerSink(NotificationSinkInterface callback) throws RemoteException;
	
	//Allows sinks to delete a reference to the remote object if it exists
	public void deRegisterSink(NotificationSinkInterface callbackId) throws RemoteException;
	
	//Allows sinks to print sources name
	public void printSourceName()  throws RemoteException;

}
