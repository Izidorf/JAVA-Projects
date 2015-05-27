import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import java.util.regex.*;


public class SeeItem extends JPanel{


	JLabel itemName;
	JLabel itemNameText;
	JLabel auctionStart;
	JLabel auctionStartText;
	JLabel auctionEnd;
	JLabel auctionEndText;
	JLabel description;
	JTextField descriptionText;
	JLabel price;
	JLabel priceText;
	JLabel seller;
	JLabel sellerText;

	//display all the active bids for this item
	//	JList bids;


	MainFrame main;
	ArrayList<Bid> relevantBids;
	Item item;

	public SeeItem(MainFrame main,Item item, ArrayList<Bid> relevantBids) {

		this.main=main;
		this.item=item;
		this.relevantBids=relevantBids;
		initContentContainer();
		initBidsContainer();

	}

	public void initContentContainer(){
		// The following just adds a bit of padding
		Border eb = new EmptyBorder(100,100,100,100);
		this.setBorder(eb);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridbag);

		itemName = new JLabel("Item Name");
		itemNameText = new JLabel(this.item.getTitle());
		auctionStart=new JLabel("Start Date");
		auctionStartText=new JLabel(this.item.getStartTime());
		auctionEnd=new JLabel("End Date");
		auctionEndText=new JLabel(this.item.getEndTime());
		seller=new JLabel("seller");
		sellerText=new JLabel(this.item.getVendorID());
		description=new JLabel("Description");
		descriptionText=new JTextField(40);
		descriptionText.setText(this.item.getDescription());
		descriptionText.setEditable(false);
		price = new JLabel("Price");
		priceText=new JLabel(Integer.toString(this.item.getReservedPrice()));

		addComponent(itemName, gridbag, c, 0,0,0,0);
		addComponent(itemNameText, gridbag, c, 0,1,0,0);
		addComponent(auctionStart, gridbag, c, 1,0,0,0);
		addComponent(auctionStartText, gridbag, c, 1,1,0,0);
		addComponent(auctionEnd, gridbag, c, 2,0,0,0);
		addComponent(auctionEndText, gridbag, c, 2,1,0,0);
		addComponent(seller, gridbag, c, 3,0,0,0);
		addComponent(sellerText, gridbag, c, 3,1,0,0);
		addComponent(price, gridbag, c, 4,0,0,0);
		addComponent(priceText, gridbag, c, 4,1,0,0);
		addComponent(description, gridbag, c, 5,0,0,0);
		//	c.ipady=40;
		c.gridwidth = 4;
		addComponent(descriptionText, gridbag, c, 6,0,0,0);// c.ipady=0; c.gridwidth=0;

		//	this.add(descriptionText);
		//now add listeners


		//		String[] data = {"one", "two", "three", "four"};
		//		bids = new JList(data);
		//		addComponent(description, gridbag, c, 1,4,0,0);

	}

	public void initBidsContainer(){

	}

	public void addComponent(Component com, GridBagLayout gridbag, GridBagConstraints c, int row, int colum, int width, int height){
		c.gridx=colum;
		c.gridy=row;
		gridbag.setConstraints(com, c);
		add(com);
	}


}

class ItemGreetingBar extends JPanel{

	JLabel welcome;
	MainFrame main;

	public ItemGreetingBar(MainFrame main) {
		this.main=main;
		init();
	}

	public void init(){

		welcome = new JLabel("Welcome "+ Client.getUserID() + "! you can find information about the item below.");
		this.add(welcome);
	}

}

class BidsInfo extends JPanel{


	MainFrame main;
	JLabel bid;
	JTextField bidValue;
	JButton placeBid;
	Item item;

	public BidsInfo(MainFrame main, Item item) {
		this.main=main;
		this.item=item;
		init();
	}

	public void init(){

		bid = new JLabel("Your bid: ");
		bidValue = new JTextField(10);
		placeBid = new JButton("Place your Bid");

		this.add(bid);
		this.add(bidValue);
		this.add(placeBid);

		//Add listeners
		BidPlacingListener bpl = new BidPlacingListener();
		placeBid.addActionListener(bpl);

	}

	class BidPlacingListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Place your Bid")){
				if(Main.debug)System.out.println("The value of placed bid is: "+bidValue.getText());
				
				boolean validation =true;
				//add sth for validating input
				boolean isInteger =  bidValue.getText().matches("[+]?\\d*?");
				if(!isInteger){
					JOptionPane.showMessageDialog(main, "Bid must be an integer");
					validation=false;
				}
				
				if(validation){
				Bid bid = new Bid(Client.userID, item.getItemID(), Integer.parseInt(bidValue.getText()));
				
				PlaceBidMessage pbm = new PlaceBidMessage(bid);
				Comms.sendMessage(pbm);
				boolean placed = (Boolean) ((BooleanMessage)Comms.receiveMessage()).getBoolean();
				
				if(placed){
					JOptionPane.showMessageDialog(main, "Bid placed Successfully!");
					main.changePain(new ViewAuctionsGUI(main));
				} else{
					JOptionPane.showMessageDialog(main, "Your bid must be integer higher then existing bids!");
				}
				
			}
			}
			
		}

	}


}

class PlaceBids extends JPanel{
	JLabel title;
	JList bids;
	MainFrame main;
	ArrayList<Bid> relevantBids;
	public PlaceBids(MainFrame main,ArrayList<Bid> relevantBids) {
		this.main=main;
		this.relevantBids=relevantBids;
		init();
	}

	public void init(){
		//this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		String[] data = new String[relevantBids.size()];//{"User izi bid £10", "User pete bid £11", "User jack bid £12", "User izi bid £13"};
		for(int i=0; i<relevantBids.size(); i++)
			data[i] = "User "+relevantBids.get(i).getBidderID()+" bade "+ relevantBids.get(i).getBid();

		title = new JLabel("Bids:");
		bids = new JList(data);
		bids.setLayoutOrientation(JList.VERTICAL);
		bids.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bids.setVisibleRowCount(-1);

		this.setBorder(new EmptyBorder(90, 0, 0, 100));

		//	this.add(title);
		this.add(bids);
	}

}
