import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel  {

	ImageIcon image;
	JLabel login;
	JLabel username;
	JLabel password;
	JPasswordField passText;
	JTextField usrText;
	JButton enter;
	JButton signUp;
	MainFrame main;
	


	public LoginPanel(MainFrame main){ 
		this.main=main;
		init();}

	//add new Shapes in this method
	public void init(){

		

		GridBagLayout gridbag = new GridBagLayout();
		this.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();

		login = new JLabel("Login");
		login.setForeground(Color.red);
		username = new JLabel("Username");
		usrText = new JTextField(10);
		password = new JLabel("Password");
		passText = new JPasswordField(10);
		passText.setEchoChar('*');
		signUp = new JButton("Sign Up");
		enter = new JButton("Log In");

		addComponent(login, gridbag, c, 0,0,0,0);
		addComponent(username, gridbag, c, 1,0,0,0);
		addComponent(usrText, gridbag, c, 1,1,0,0);
		addComponent(password, gridbag, c, 2,0,0,0);
		addComponent(passText, gridbag, c, 2,1,0,0);
		addComponent(signUp, gridbag, c, 3,0,2,2);
		addComponent(enter, gridbag, c, 3,1,2,2);	


		//now add listeners
		MainButtonListener ll = new MainButtonListener();
		signUp.addActionListener(ll);
		enter.addActionListener(ll);
	}

	public void addComponent(Component com, GridBagLayout gridbag, GridBagConstraints c, int row, int colum, int width, int height){
		c.gridx=colum;
		c.gridy=row;
		gridbag.setConstraints(com, c);
		add(com);
	}


	class MainButtonListener implements ActionListener{



		public MainButtonListener() {}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Sign Up")){

				System.out.println(username.getText()+", "+password.getText());
				main.changePain(new SignUpGUI(main));

				if(Main.debug)
					System.out.println("Sign Up..pressed");

			} else if (e.getActionCommand().equals("Log In")){
				
			//	Comms.sendMessage(lm);
				if(Main.debug){
					System.out.println("Log In..pressed");
					System.out.println("This data is sent to the server: "+usrText.getText()+"  "+passText.getText());
				}
				
				//send login details to the server for authentication
				Message lm = new LoginMessage(usrText.getText(),passText.getText());
				Comms.sendMessage(lm);
				boolean authenticated = (Boolean) ((BooleanMessage)Comms.receiveMessage()).getBoolean();
			//	(LoginMessage) message).getUsername()
				
				System.out.println(authenticated);
				
				//if user exists in a database on the server log him in
				Client.isLoggedIn=authenticated;
				if(authenticated) {
					main.changePain(new ViewAuctionsGUI(main));
					Client.setUserID(usrText.getText());		
				}

			}

		}



	}


}

@SuppressWarnings("serial")
class LogoImage extends JPanel {

	public LogoImage() {


		init();
	}
	public void init(){
		//this.setBorder(BorderFactory.createLineBorder(Color.red, 10));
		
		JLabel label = new JLabel();  
		label.setIcon(new ImageIcon("auction.jpg"));
		label.setBorder(new EmptyBorder(10, 10, 10, 10) );
		this.add(label);
	}

}

