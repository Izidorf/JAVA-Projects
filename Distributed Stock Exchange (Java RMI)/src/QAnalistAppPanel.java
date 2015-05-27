

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class QAnalistAppPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	protected JButton register = new JButton("register");
	protected JTextArea area = new JTextArea();
	protected JScrollPane sp = new JScrollPane(area);
	protected int sourceId;


	public QAnalistAppPanel(int sourceId) {
		this.sourceId=sourceId;
		init();
	}

	public void init(){
		// The following just adds a bit of padding
		Border eb = new EmptyBorder(50,50,50,50);
		this.setBorder(eb);

		area.setSize(this.getHeight()-10, this.getWidth()-10);

		JPanel buttons = new JPanel();
		buttons.add(register);


		this.setLayout(new BorderLayout());

		this.add(sp, BorderLayout.CENTER );
		this.add(buttons, BorderLayout.SOUTH);

		//now add listeners
		RegisterListener listener = new RegisterListener();
		register.addActionListener(listener);


	}


	/*Inner Class to add listner to the button */
	class RegisterListener implements ActionListener {

		QAnalystApp analyst;

		public RegisterListener() {
			try {
				this.analyst=new QAnalystApp(sourceId);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}


		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("register")){
				register.setText("deregister");
				analyst.register();


			}  else if(e.getActionCommand().equals("deregister")) {
				register.setText("register");

				analyst.deRegister();
			}



		}
	}

	class QAnalystApp extends NotificationSink {



		protected QAnalystApp(int sourceId) throws RemoteException {
			super();
		}

		private static final long serialVersionUID = 1L;

		public void register(){
			try {
				NotificationSourceInterface source=
						(NotificationSourceInterface)
						Naming.lookup("source"+sourceId);

				source.registerSink(this);
				register.setText("deregister");

			} catch (Exception e) {
				if (retryRegistration("source"+sourceId)) register.setText("deregister");
			}
		}

		public void deRegister(){
			try {
				NotificationSourceInterface source=
						(NotificationSourceInterface)
						Naming.lookup("source"+sourceId);

				source.deRegisterSink(this);
				register.setText("register");

			} catch (Exception e) {
				if (retryDeregistration("source"+sourceId)) register.setText("register");
			}



		}

		@Override
		public Object notifySink(Notification ntf) throws RemoteException {
			area.append((String) ntf.getInfo());
			return null;
		}


		public boolean retryRegistration(String sourceId){
			for(int i=0; i<QAnalystAppClient.MAX_NUM_TRIES; i++){
				NotificationSourceInterface source;
				try {
					Thread.sleep(QAnalystAppClient.TIME_BETWEEN_TRIES);
					source = (NotificationSourceInterface)
							Naming.lookup("source"+sourceId);
					source.registerSink(this);
					return true;
				} catch (MalformedURLException | RemoteException
						| NotBoundException | InterruptedException e) {
				}	
			}
			register.setText("register");
			return false;
		}

		public boolean retryDeregistration(String sourceId){
			for(int i=0; i<QAnalystAppClient.MAX_NUM_TRIES; i++){
				NotificationSourceInterface source;
				try {
					Thread.sleep(QAnalystAppClient.TIME_BETWEEN_TRIES);
					source = (NotificationSourceInterface)
							Naming.lookup("source"+sourceId);
					source.deRegisterSink(this);
					return true;
				} catch (MalformedURLException | RemoteException
						| NotBoundException | InterruptedException e) {
				}	
			}
			register.setText("deregister");
			return false;
		}




	}

}


