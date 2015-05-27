import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
class ClientWorker implements Runnable {
	private Socket client;
	private JTextArea textArea;
	private ServerDatabase database;
	private DataPersistence persistance = new DataPersistence();
	
	ClientWorker(Socket client, JTextArea textArea, ServerDatabase database) {
		this.client = client;
		this.textArea = textArea;
		this.database=database;
	}
	public void run() {
		Message message;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}
		while (true) {
			try {
				message = (Message) in.readObject();
				if(Main.debug)
				textArea.append("Successfully received message from CLIENT-->>"+message.getClass()+"\n");
				
				//Process data and send message back to the client
				if(message instanceof LoginMessage){
					if(Main.debug) textArea.append("LoginMessage data received as: "+((LoginMessage) message).getUsername()+"  "+ ((LoginMessage) message).getPassword()+"\n");
					persistance.SaveLog("LoginMessage data received as: "+((LoginMessage) message).getUsername()+"  ");
					boolean authenticate = database.authenticateUser(((LoginMessage) message).getUsername(), ((LoginMessage) message).getPassword());
					if(Main.debug) textArea.append("The server will send back boolean:"+authenticate+""+"\n");
					message=new BooleanMessage(authenticate);
					
				}
				
				if(message instanceof RegisterMessage){
					if(Main.debug) textArea.append("RegisterMessage data received as: "+((RegisterMessage) message).getUser().getUsername()+" password: "+((RegisterMessage) message).getUser().getPassword()+" First Name:"+((RegisterMessage) message).getUser().getFirstName()+" Last Name: "+ ((RegisterMessage) message).getUser().getFamilyName()+"\n");
					persistance.SaveLog("RegisterMessage data received as: "+((RegisterMessage) message).getUser().getUsername());
					boolean registerUser = database.registerUser(((RegisterMessage) message).getUser());
					message= new BooleanMessage(registerUser);
				}
				
				if(message instanceof AddItemMessage ){
					if(Main.debug) textArea.append("AddItemMessage data received as: "+((AddItemMessage) message).getItem().getItemID()+""+((AddItemMessage) message).getItem().getTitle()+""+((AddItemMessage) message).getItem().getDescription()+""+((AddItemMessage) message).getItem().getReservedPrice()+""+((AddItemMessage) message).getItem().getStartTime()+""+"\n");
					persistance.SaveLog("AddItemMessage data received");
					boolean addItem = database.addItem(((AddItemMessage) message).getItem());
					message=new BooleanMessage(addItem);
				}
				
				if(message instanceof RequestAllAuctions){
					ArrayList<Item> items = database.getItems();
					message=new GetAllAuctions(items);
				}
				
				if(message instanceof RequestItemInfo){
					if(Main.debug) textArea.append("RequestItemInfo data received as: "+((RequestItemInfo) message).getItemID());
					Item item = database.getItemById(((RequestItemInfo) message).getItemID());
					ArrayList<Bid> bids = database.getItemBids(((RequestItemInfo) message).getItemID());
					if(Main.debug) textArea.append("Server Will send back: "+item+bids);
					message= new SendItemInfo(item, bids);
				}
				
				if(message instanceof PlaceBidMessage){
					if(Main.debug) textArea.append("PlaceBidMessage data received as: Bid with bidder id: "+((PlaceBidMessage) message).getBid().getBidderID()+" item id: "+((PlaceBidMessage) message).getBid().getItemID()+" and bid value: "+((PlaceBidMessage) message).getBid().getBid());
					Bid bid = ((PlaceBidMessage) message).getBid();
					boolean addedSuc = database.placeBid(bid);
					message=new BooleanMessage(addedSuc);
				
				}
				
				if(message instanceof RequestUserInfo){
					if(Main.debug) textArea.append("PlaceBidMessage data received as: Bid with bidder id: "+((RequestUserInfo) message).getObjectContent());
					String firstName = database.getFirstName(((RequestUserInfo) message).getUserId());
					String lastName = database.getLastName(((RequestUserInfo) message).getUserId());
					ArrayList<Bid> usersBids = database.getUsersBids(((RequestUserInfo) message).getUserId());
					ArrayList<Item> usersItems = database.getUsersItems(((RequestUserInfo) message).getUserId());
					message = new SendUserInfo(firstName, lastName, usersBids, usersItems);
								
			}
			
				out.writeObject(message);
				
				textArea.append(message.toString());
			} catch (Exception e) {
				System.out.println("Read failed");
				System.exit(-1);
			}
		}
	}
	
	public Message processMessage(Message ms){
		
		return null;
	}
	
}
@SuppressWarnings("serial")
public class Server extends JFrame{
	JLabel label;
	JButton generateReport;
	JPanel panel;
	JTextArea textArea;
	ServerSocket server = null;
	ServerDatabase database = new ServerDatabase();
	Server(String name){ //Begin Constructor
		super(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ; 	
		setSize(900,400);
		
		createJPanel();
	} //End Constructor
	
	public void createJPanel(){
		
		panel = new JPanel();
	
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.white);
		getContentPane().add(panel);
		
		
		label = new JLabel("Text received over the socket: ");
		textArea = new JTextArea();
		textArea.setSize(400, 400);
		generateReport = new JButton("Generate Report");
		generateReport.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
					if(e.getActionCommand().equals("Generate Report"))
						database.generateReport();
					JOptionPane.showMessageDialog(panel, "Report generated look in report.txt file");
			}
			
		});
		
		panel.add("North", label);
		panel.add("Center", textArea);
		panel.add("South", generateReport);
	}
	
	public void listenSocket() {
		try {
			server = new ServerSocket(4444);
		} catch (IOException e) {
			System.out.println("Could not listen on port 4444");
			System.exit(-1);
		}
		while (true) {
			ClientWorker w;
			try{
				w = new ClientWorker(server.accept(), textArea, database);
				Thread t = new Thread(w);
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
				System.exit(-1);
			}
		}
	}
	protected void finalize(){
		//Objects created in run method are finalized when
		//program terminates and thread exits
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}
	public static void main(String[] args) {
		Server frame = new Server("Server");
		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		frame.addWindowListener(l);
		
		frame.setVisible(true);
		frame.listenSocket();
		
		//database sd = new database();
	}
}