

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;

public class NotificationSource extends UnicastRemoteObject
								implements NotificationSourceInterface {
	

	private static final long serialVersionUID = 1L;
	private String name;
	private List<NotificationSinkInterface> registeredSinks = new Vector<NotificationSinkInterface>();
	private List<ArrayBlockingQueue<Notification>> notifications = new Vector<ArrayBlockingQueue<Notification>>();
	
	public NotificationSource(String name) throws RemoteException{
		super();
		this.name=name;		
	}


	@Override
	public synchronized void registerSink(NotificationSinkInterface callback)
			throws RemoteException {
		if(!this.registeredSinks.contains(callback)){
			registeredSinks.add(callback);
			notifications.add(new ArrayBlockingQueue<Notification>(20));
		}
		
	}

	@Override
	public synchronized void deRegisterSink(NotificationSinkInterface callback)
			throws RemoteException {
		if(this.registeredSinks.contains(callback)){
			notifications.remove(registeredSinks.indexOf(callback));
			registeredSinks.remove(callback);
			
		}
	}
	
	public void printSourceName(){
		System.out.print("The name of the source is: "+ this.name);
	}
	
	
	public void broadcast(Notification n) 
			throws RemoteException{
		for(NotificationSinkInterface nfi : registeredSinks)
			nfi.notifySink(n);
		
	}
	
	public synchronized void broadcastWithCorrection(Object o) throws RemoteException{
		Notification n = new Notification(this.name, o);
		for(int i=0; i<this.registeredSinks.size(); i++){
			NotificationSinkInterface sink = registeredSinks.get(i);
			if(notifications.get(i).size() > NotificationSourceInterface.NUM_TRIES) 
				deRegisterSink(sink);
			else {
				notifications.get(i).add(n);
				sendWithCorrection(sink, i);
			}
		}
	}
	
	public void sendWithCorrection(NotificationSinkInterface sink, int sinkId){
	ArrayBlockingQueue<Notification> queue = this.notifications.get(sinkId);
	Notification n =queue.poll();
	 try {
		sink.notifySink(n);
	} catch (RemoteException e) {
		queue.add(n);
	}
	
	}
	
	
}
