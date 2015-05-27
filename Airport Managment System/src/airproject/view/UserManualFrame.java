package airproject.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class UserManualFrame extends JFrame{

	private JEditorPane nav;
	private JEditorPane body;
	private JScrollPane leftPane;
	private JScrollPane rightPane;
	private JSplitPane splPane;
	private JPanel mainPane;


	public static void main(String[] args) {
		UserManualFrame umd = new UserManualFrame();
	}

	public UserManualFrame() {
		super("User Manual");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(true);
		setBounds(100, 100, 900, 550);

		initGUI();	

		this.setVisible(true);
	}

	/*
	 * Initialises the GUI.
	 */
	public void initGUI(){
		// Creates the main panel.
		mainPane = new JPanel();
		mainPane.setLayout(new BorderLayout(0, 0));
		mainPane.setBorder(new EmptyBorder(7, 7, 7, 7));
		setContentPane(mainPane);
		// Create the split panel.
		splPane = new JSplitPane();
		splPane.setResizeWeight(0.28);
		mainPane.add(splPane, BorderLayout.CENTER);
		// Create the left hand panel.
		leftPane = new JScrollPane();
		leftPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		splPane.setLeftComponent(leftPane);
		// Create the navigation.
		nav = new JEditorPane();
		nav.setEditable(false);
		nav.setContentType("text/html");
		leftPane.setViewportView(nav);
		// Create the right hand panel.
		rightPane = new JScrollPane();
		rightPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		splPane.setRightComponent(rightPane);
		// Create the body.
		body = new JEditorPane();
		body.setContentType("text/html\n\r");
		body.setEditable(false);
		rightPane.setViewportView(body);
		// Load the HTML files.
		loadFiles();
		// Add hyperlink click listener.
		nav.addHyperlinkListener(new LinkListner());
	}

	public void loadFiles(){
		try {	
			body.setPage(getClass().getResource("/manual/index.html"));
			nav.setPage(getClass().getResource("/manual/navigation.html"));
		} catch (IOException e) {
			System.err.println("File not found!");
			dispose();
			return;
		}
	}

//	public void setHtmlLinkListener(HyperlinkListener ll){
//		nav.addHyperlinkListener(ll);
//	}


	class LinkListner implements HyperlinkListener{

		@Override
		public void hyperlinkUpdate(HyperlinkEvent e) {		
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				try {
					body.setPage(e.getURL());
				} catch (IOException e1) {

					e1.printStackTrace();
				}

			}

		}
	}

}
