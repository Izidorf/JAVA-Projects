import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;




@SuppressWarnings("serial")
public class AddItemInfo extends JPanel{

	JLabel itemName;
	JTextField itemNameText;
	JLabel category;
	JComboBox<String> categBox;
	JLabel auctionStart;
	JTextField startText;
	JSpinner startText1;
	JSpinner endText1;
	JLabel auctionEnd;
	JTextField endText;
	JLabel description;
	JTextField descriptionText;
	JLabel reservedPrice;
	JTextField reservedPriceText;
	JButton submit;
	MainFrame main;


	public AddItemInfo(MainFrame main) {
		init();
		this.main=main;
	}

	public void init(){
		// The following just adds a bit of padding
		Border eb = new EmptyBorder(100,100,100,100);
		this.setBorder(eb);

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridbag);

		itemName = new JLabel("Item Name");
		itemNameText = new JTextField(20);
		category = new JLabel("Category");
		String[] categoriesStrings = { "arts", "clothes", "antiquites", "furniture", "other" };
		categBox = new JComboBox<String>(categoriesStrings);
		auctionStart=new JLabel("Start Date (h:mm a MM/dd/yy)");
		startText = new JTextField(20);
		SpinnerDateModel model1 = new SpinnerDateModel();
		model1.setCalendarField(Calendar.MINUTE);

		startText1= new JSpinner();
		startText1.setModel(model1);
		startText1.setEditor(new JSpinner.DateEditor(startText1,"h:mm a MM/dd/yy"));
		auctionEnd=new JLabel("End Date (h:mm a MM/dd/yy)");
		endText = new JTextField(20);
		SpinnerDateModel model = new SpinnerDateModel();
		model.setCalendarField(Calendar.MINUTE);

		endText1= new JSpinner();
		endText1.setModel(model);
		endText1.setEditor(new JSpinner.DateEditor(endText1,"h:mm a MM/dd/yy"));
		reservedPrice = new JLabel("Reserved Price: ");
		reservedPriceText = new JTextField(20);
		description=new JLabel("Description");
		descriptionText = new JTextField(20);
		submit = new JButton("Submit");


		addComponent(itemName, gridbag, c, 0,0);
		addComponent(itemNameText, gridbag, c, 0,1);
		addComponent(category, gridbag, c, 1,0);
		addComponent(categBox, gridbag, c, 1,1);
		addComponent(auctionStart, gridbag, c, 2,0);
		addComponent(startText1, gridbag, c, 2,1);
		addComponent(auctionEnd, gridbag, c, 3,0);
		addComponent(endText1, gridbag, c, 3,1);
		addComponent(reservedPrice, gridbag, c, 4,0);
		addComponent(reservedPriceText, gridbag, c, 4,1);
		addComponent(description, gridbag, c, 5,0); 
		c.ipady=40;
		c.gridwidth = 2;
		addComponent(descriptionText, gridbag, c, 5,1); c.ipady=0; c.gridwidth=0;
		addComponent(submit, gridbag, c, 6,1);

		//now add action listeners
		ButtonListener bl= new ButtonListener();
		submit.addActionListener(bl);


	}




	public void addComponent(Component com, GridBagLayout gridbag, GridBagConstraints c, int row, int colum){
		c.gridx=colum;
		c.gridy=row;
		//	c.gridwidth=width;
		//	c.gridheight=height;
		gridbag.setConstraints(com, c);
		add(com);
	}


	class ButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Submit")){
				if(Main.debug)
					System.out.println("Submit..pressed"+(String) categBox.getSelectedItem());

				//main.changePain(new LoginGUI(main)); tell that item was succesfully added and then display auctions screen
				Item item = new Item(Client.getUserID(), itemNameText.getText(), Integer.parseInt(reservedPriceText.getText()), (String) categBox.getSelectedItem(), startText1.getValue().toString(), endText1.getValue().toString(),descriptionText.getText());
				AddItemMessage aim = new AddItemMessage(item);
				Comms.sendMessage(aim);
				Boolean addedSuccesfully = (Boolean) ((BooleanMessage)Comms.receiveMessage()).getBoolean();

				if(Main.debug) System.out.println("The message received from the server is: "+addedSuccesfully);


			}

		}
	}


}
