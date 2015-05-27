import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

public class SearchBar extends JPanel {

	MainFrame main;

	public SearchBar(MainFrame main) {
		super(new GridLayout(1, 1));
		this.main=main;

		JTabbedPane tabbedPane = new JTabbedPane();

		JPanel panel1 = new SearchItem();
		JPanel panel2 = new SearchCategory();
		JPanel panel3 = new SearchUser();
		JPanel panel4 = new SearchDate();
		JPanel panel5 = new SearchItemCode();


		tabbedPane.addTab("Search by Item", null, panel1,
				"Does nothing");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);


		tabbedPane.addTab("Search by User", null, panel2,
				"Does twice as much nothing");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);


		tabbedPane.addTab("Search by Category", null, panel3,
				"Still does nothing");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);



		tabbedPane.addTab("Search by Date", null, panel4,
				"Does nothing at all");
		tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

		panel5.setPreferredSize(new Dimension(410, 30));
		tabbedPane.addTab("Search by Item Code", null, panel5,
				"Does nothing at all");
		tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);


		this.add(tabbedPane);

		//The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	protected JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	class SearchItem extends JPanel{
		JLabel itemSearch;
		JLabel enterCode;
		JTextField itemId;
		JButton searchItem;
		public SearchItem() {

			cereateItemSearch();
		}

		//search item by unique item code
		public void cereateItemSearch(){


			itemSearch = new JLabel("Search By Item");
			enterCode = new JLabel ("Enter item name:");
			itemId = new JTextField(10);
			searchItem = new JButton("Search");

			this.add(itemSearch);
			this.add(enterCode);
			this.add(itemId);
			this.add(searchItem);
			
			//add Listeners
			SearchItemListener scl = new SearchItemListener();
			searchItem.addActionListener(scl);
		}

		class SearchItemListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Search")){
					if(Main.debug)
						if(Main.debug) System.out.println("Search..pressed, ItemId is: "+ itemId.getText());
					
					
				}
				
			}
	
			
		}
		

	}

	class SearchUser extends JPanel{

		JLabel userSearch;
		JLabel chooseUsr;
		JTextField userId;
		JButton searchUser;

		public SearchUser() {
			cereateUserSearch();
		}

		//search items from a particular seller
		public void cereateUserSearch(){
			userSearch = new JLabel("Search by User");
			chooseUsr = new JLabel ("Choose category");
			userId = new JTextField(10);
			searchUser = new JButton("Search");

			this.add(userSearch);
			this.add(chooseUsr);
			this.add(userId);
			this.add(searchUser);
			
			//add listeners
			SearchUserListener sul = new SearchUserListener();
			searchUser.addActionListener(sul);
		}
		
		class SearchUserListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Search")){
					if(Main.debug)
						if(Main.debug) System.out.println("Search..pressed, Username is:"+ userId.getText());
					
					
				}
				
			}
	
			
		}
	}

	class SearchCategory extends JPanel{
		JLabel categorySearch;
		JLabel chooseCategory;
		JTextField categoryId;
		JButton searchCategory;

		public SearchCategory() {
			cereateCategorySearch();
		}

		//search item by category
		public void cereateCategorySearch(){
			categorySearch = new JLabel("Search By Category");
			chooseCategory = new JLabel ("Choose category");
			categoryId = new JTextField(10);
			searchCategory = new JButton("Search");

			this.add(categorySearch);
			this.add(chooseCategory);
			this.add(categoryId);
			this.add(searchCategory);
			
			//add listeners
			SearchCategoryListener scl = new SearchCategoryListener();
			searchCategory.addActionListener(scl);
			
			
		}
		
		class SearchCategoryListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Search")){
					if(Main.debug)
						if(Main.debug) System.out.println("Search..pressed, Category is:"+ categoryId.getText());
					
					
				}
				
			}
	
			
		}

	}

	class SearchDate extends JPanel{
		JLabel dateSearch;
		JLabel chooseDate;
		JSpinner spinner;
		JButton searchDate;

		public SearchDate() {
			createDateSearch();
		}

		//search items created after a given date time
		public void createDateSearch(){
			dateSearch = new JLabel("Search By Date (h:mm a MM/dd/yy)");
			chooseDate = new JLabel ("after: ");
		
			searchDate = new JButton("Search");
			
			SpinnerDateModel model = new SpinnerDateModel();
			model.setCalendarField(Calendar.MINUTE);

			spinner= new JSpinner();
			spinner.setModel(model);
			spinner.setEditor(new JSpinner.DateEditor(spinner,"h:mm a MM/dd/yy"));
			
			this.add(dateSearch);
			this.add(chooseDate);
			this.add(spinner);
			this.add(searchDate);
			
			//add action listener
			 SearchDateListener sdl = new  SearchDateListener();
			 searchDate.addActionListener(sdl);
		}

		class SearchDateListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Search")){
					if(Main.debug)
						if(Main.debug) System.out.println("Search..pressed, Username is:"+ spinner.getValue());
					
					
				}
				
			}
	
			
		}

	}

	class SearchItemCode extends JPanel{
		JLabel itemCodeSearch;
		JLabel chooseCode;
		JTextField code;
		JButton search;
		JLabel result;

		public SearchItemCode() {
			createDateSearch();
		}

		//search items created after a given date time
		public void createDateSearch(){
			itemCodeSearch = new JLabel("Search By Item Code");
			chooseCode = new JLabel ("Item Code: ");
			code = new JTextField(20);
			search = new JButton("Search");
			result = new JLabel("blablabla");

			this.add(itemCodeSearch);
			this.add(chooseCode);
			this.add(code);
			this.add(search);
			this.add(result);
			
			//add listeners
			SearchItemCodeListener scl = new SearchItemCodeListener();
			search.addActionListener(scl);

		}


		class SearchItemCodeListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Search")){
					if(Main.debug)
						System.out.println("Search..pressed, Item code is:"+ code.getText());
					
					
				}
				
			}
	
			
		}
		
		

	}
	

	
//	class SearchUserListener implements ActionListener{
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			if(e.getActionCommand().equals("Search")){
//				if(Main.debug)
//					System.out.println("Submit..pressed"+(String) categBox.getSelectedItem());
//				
//			}
//			
//		}
//		
//		
//		
//	}
	

}	

