import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;

import javax.swing.*;


public class MyProfile extends JPanel {
	
	MainFrame main;
	JLabel usrName;
	JLabel usrNameText;
	JLabel firstName;
	JLabel firstNameText;
	JLabel lastName;
	JLabel lastNameText;
	JLabel userBids;
	JList bids;
	SendUserInfo sui;
	
	public MyProfile(MainFrame main) {	
		this.main=main;
		RequestUserInfo rui = new RequestUserInfo(Client.getUserID());
		Comms.sendMessage(rui);
		sui = (SendUserInfo) Comms.receiveMessage();
		init();
	}
	
	
	public void init() {
		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		
		usrName = new JLabel("User name: ");
		usrNameText = new JLabel(Client.getUserID());
		firstName = new JLabel("First Name: ");
		firstNameText = new JLabel(sui.getName());
		lastName = new JLabel("Last Name: ");
		lastNameText = new JLabel(sui.getLastname());
		userBids = new JLabel("List of Bids of This user");
		bids = new JList();
		
		addComponent(usrName, gridbag, c, 0,0,0,0);
		addComponent(usrNameText, gridbag, c, 0,1,0,0);
		addComponent(firstName, gridbag, c, 1,0,0,0);
		addComponent(firstNameText, gridbag, c, 1,1,0,0);
		addComponent(lastName, gridbag, c, 2,0,0,0);
		addComponent(lastNameText, gridbag, c, 2,1,0,0);
		addComponent(userBids, gridbag, c, 0,4,0,0);
		addComponent(bids, gridbag, c, 1,4,0,0);
		
	}
	
	public void addComponent(Component com, GridBagLayout gridbag, GridBagConstraints c, int row, int colum, int width, int height){
		c.gridx=colum;
		c.gridy=row;
		gridbag.setConstraints(com, c);
		add(com);
	}

}

class GreetingsBar extends JPanel{
	
	JLabel welcome;
	MainFrame main;
	
	public GreetingsBar(MainFrame main) {
		this.main=main;
		init();
	}
	
	public void init(){
			
		welcome = new JLabel("Welcome "+ Client.getUserID() + "! you can find your information below.");
		this.add(welcome);
	}
	
}
