import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.AbstractTableModel;

import java.awt.event.*;
import java.awt.geom.Ellipse2D;

class MainGUI {

	
	
} //end RandomColors

class MainFrame extends JFrame {

	//have an array list of all the possible looks
	// login, signup, viewItem, addItem, viewAuctions
	
	public MainFrame(String title){
		super(title);
	}

	public void init(){
		
	

		LoginGUI p = new LoginGUI(this);
		this.setContentPane(p);
		
		JMenuBar menu = new MainMenuBar(this);
		this.setJMenuBar(menu);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ; 	
		setSize(900,400);
		setResizable(false);
		
		setVisible(true); 	
	}	
	
	public void changePain(JPanel newPanel){
	
		this.setContentPane(newPanel);
		this.revalidate();
		if(Main.debug)
		System.out.println("yeah function changePain called");	
	}
	

	
} //end RCFrame


class LoginGUI extends JPanel {
	
	MainFrame main;
	
	public LoginGUI(MainFrame main) {
		this.main=main;
		init();}
	
	public void init(){
		GridLayout gbl = new GridLayout(2,1);
		this.setLayout(gbl);
		
		this.add(new LogoImage());
		this.add(new LoginPanel(this.main));
	}
}

class SignUpGUI extends JPanel {
	
	MainFrame main;
	
	public SignUpGUI(MainFrame main) {
		this.main=main;
		init();}
	
	public void init(){
		//Sign Up Screen
		GridLayout gbl = new GridLayout(2,1);
		this.setLayout(gbl);
		
		this.add(new LogoImage());
		this.add(new RegisterUser(this.main));
	}
	
}

class ViewItemGUI extends JPanel {
	
	MainFrame main;
	Item item;
	ArrayList<Bid> relevantBids;
	
	public ViewItemGUI(MainFrame main, Item item, ArrayList<Bid> relevantBids) {
		this.main=main;
		this.item=item;
		this.relevantBids=relevantBids;
		init();}
	
	public void init(){
		FlowLayout gbl = new FlowLayout();
		this.setLayout(gbl);
		this.setLayout(new BorderLayout());
		this.add(new SeeItem(main, item, relevantBids), BorderLayout.WEST);
		this.add(new BidsInfo(main, item), BorderLayout.SOUTH);
		this.add(new PlaceBids(main,relevantBids), BorderLayout.EAST);
		this.add(new ItemGreetingBar(main), BorderLayout.NORTH);
		
		//this.add(new SeeItem(main));
		//this.add(new BidsInfo(main));ItemGreetingBar
	}
}

class AddItemGUI extends JPanel {
	
	MainFrame main;
	
	public AddItemGUI(MainFrame main) {
	this.main=main;
	init();}
	
	public void init(){
		this.add(new AddItemInfo(this.main));
	}
}

class MyProfileGUI extends JPanel{
	
	MainFrame main;
	
	public MyProfileGUI(MainFrame main) {
		this.main=main;
		init();
		
	}
	
	public void init(){
		this.setLayout(new BorderLayout());
		
		this.add(new GreetingsBar(main), BorderLayout.NORTH);
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		JPanel profile = new MyProfile(main);
		profile.setBorder(new EmptyBorder(0, 20, 250, 0));
	this.add(profile);
		
		//this.add(new MyProfile(main), BorderLayout.LINE_START);
		
	}
	
	
}

class ViewAuctionsGUI extends JPanel {
	
	MainFrame main;
	
	public ViewAuctionsGUI(MainFrame main) {
		this.main=main;
		init();}
	
	public void init(){
	//	 GridLayout gL = new GridLayout(2,1);
	//	this.setLayout(gL);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.add(new SearchBar(main));
		this.add(new AuctionsList(main));
	}
}

   
class MainMenuBar extends JMenuBar {
	
	
	
	//JMenuBar menuBar;
	JMenu home;
	JMenu myProfile;
	JMenu addItem;
	JMenu notifications;
	MainFrame main;
	
	
	public MainMenuBar(MainFrame main) {
		this.main=main;
		init();
	}
	
	public void init(){

		home = new JMenu("Home");
		myProfile = new JMenu("My Profile");
		addItem = new JMenu("Add Item");
		notifications = new JMenu("About");
	
		

		this.add(home);
		this.add(myProfile);
		this.add(addItem);
		this.add(notifications);
	

		//add Action Listeners
		ButtonListener bl = new ButtonListener();
		home.addMenuListener(bl);
		myProfile.addMenuListener(bl);
		addItem.addMenuListener(bl);
		notifications.addMenuListener(bl);
		
	}
	
	
	class ButtonListener implements MenuListener{


		public ButtonListener() {}
		
		@Override
		public void menuCanceled(MenuEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void menuDeselected(MenuEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		
		@Override
		public void menuSelected(MenuEvent e) {
		if(e.getSource().equals(home)){
			if (Client.isLoggedIn) main.changePain(new ViewAuctionsGUI(main));
				
				if(Main.debug)
				System.out.println("Home..pressed");
	            
			} else if (e.getSource().equals(addItem)){
				if (Client.isLoggedIn)	main.changePain(new AddItemGUI(main));
				
				if(Main.debug){
					System.out.println("add Item... pressed");

				}
				
				if(true)
					if (Client.isLoggedIn)	main.changePain(new AddItemGUI(main));
				
			} else if(e.getSource().equals(notifications)){
				if(Main.debug){
					System.out.println("notifications... pressed");

				}
				
				JOptionPane.showMessageDialog(main, "This is LightWeight Auction System developed by if2g11 \n for Programming 2 module at University of Southampton ECS 2013");
				//if(true)
					//main.changePain(new ViewItemGUI(main));
			} else if(e.getSource().equals(myProfile)){
				
				if (Client.isLoggedIn)	main.changePain(new MyProfileGUI(main));
				
			}
		}
		
		
		
	}
	
} //ends RCPanel



