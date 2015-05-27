import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;



public class RegisterUser extends JPanel {
	
	JLabel register;
	JLabel errorMsg;
	JLabel usrName;
	JTextField usrNameText;
	JLabel firstName;
	JTextField firstNameText;
	JLabel lastName;
	JTextField lastNameText;
	JLabel pass1;
	JPasswordField pass1Text;
	JLabel pass2;
	JPasswordField pass2Text;
	JButton back;
	JButton submit;
	MainFrame main;
	
	public RegisterUser(MainFrame main) {	
		this.main=main;
		init();
	}
	
	public void init(){
		

	GridBagLayout gridbag = new GridBagLayout();
	this.setLayout(gridbag);
	GridBagConstraints c = new GridBagConstraints();
	
	register = new JLabel("Register");
	register.setForeground(Color.red);
	usrName = new JLabel("Username: ");
	usrNameText = new JTextField(10);	
	firstName = new JLabel("First Name: ");
	firstNameText = new JTextField(10);	
	lastName = new JLabel("Last Name: ");
	lastNameText = new JTextField(10);	
	pass1 = new JLabel("Password: ");
	pass1Text = new JPasswordField(10);	
	pass2 = new JLabel("Re-enter: ");
	pass2Text = new JPasswordField(10);	
	back = new JButton("Back");
	submit = new JButton("Submit");
	
	addComponent(register, gridbag, c, 0,0,0,0);	
	addComponent(usrName, gridbag, c, 1,0,0,0);
	addComponent(usrNameText, gridbag, c, 1,2,0,0);	
	addComponent(firstName, gridbag, c, 2,0,0,0);
	addComponent(firstNameText, gridbag, c, 2,2,0,0);	
	addComponent(lastName, gridbag, c, 3,0,0,0);
	addComponent(lastNameText, gridbag, c, 3,2,0,0);	
	addComponent(pass1, gridbag, c, 4,0,0,0);
	addComponent(pass1Text, gridbag, c, 4,2,0,0);	
	addComponent(pass2, gridbag, c, 5,0,0,0);
	addComponent(pass2Text, gridbag, c, 5,2,0,0);
	addComponent(back, gridbag, c, 6,0,0,0);
	addComponent(submit, gridbag, c, 6,2,0,0);
	
	//now add action listenrs
	ButtonListener bl = new ButtonListener();
	back.addActionListener(bl);
	submit.addActionListener(bl);
	}
	
	public void addComponent(Component com, GridBagLayout gridbag, GridBagConstraints c, int row, int colum, int width, int height){
		c.gridx=colum;
		c.gridy=row;
		gridbag.setConstraints(com, c);
		add(com);
	}

	
	class ButtonListener implements ActionListener{

		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Back")){
				
				
				main.changePain(new LoginGUI(main));
				
				if(Main.debug)
				System.out.println("Back..pressed");
	            
			} else if (e.getActionCommand().equals("Submit")){
		
			//Send server a registration request and see if it is sucessfull by passing back a boolean	
			RegisterMessage m = new RegisterMessage(new User(firstNameText.getText(), lastNameText.getText(), 
					usrNameText.getText(), pass1Text.getText()));
			try {
				
				Comms.sendMessage(m);
				Boolean registrationSuccesfull = (Boolean) ((BooleanMessage)Comms.receiveMessage()).getBoolean();
				if(Main.debug)
				System.out.println("The registration was: "+ registrationSuccesfull);
				
				if(registrationSuccesfull){
					JOptionPane.showMessageDialog(main, "Registration Successfull!");
					main.changePain(new LoginGUI(main));
				}
				
} catch (Exception e1) { 
				e1.printStackTrace();}
			
			
			
			
			}
			
		}
	}
	

}
