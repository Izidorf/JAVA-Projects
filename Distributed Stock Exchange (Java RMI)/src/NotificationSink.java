

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public abstract class NotificationSink extends UnicastRemoteObject
									implements NotificationSinkInterface {

	private static final long serialVersionUID = 1L;

	protected NotificationSink() throws RemoteException {
		super();
	}
	
	public abstract Object notifySink(Notification ntf) throws RemoteException;

	
	
}
