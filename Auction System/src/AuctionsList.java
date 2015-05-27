
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;



@SuppressWarnings("serial")
public class AuctionsList extends JPanel  implements ActionListener, TableModelListener{

	MainFrame main;
	ArrayList<Item> items = new ArrayList<Item>();
	JTable table;
	private JButton getValue;



	public AuctionsList(MainFrame main) {	
		//	super();
		this.main=main;
		//first get the data to fill the tables
		Comms.sendMessage(new RequestAllAuctions());
		this.items= ((GetAllAuctions) Comms.receiveMessage()).getItemsList();

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		table = new JTable(new AuctionsTable());
		table.setFillsViewportHeight(true);

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		//Add the scroll pane to this panel.
		add(scrollPane);

		getValue = new JButton( "View Item" );


		getValue.addActionListener( this );


		this.add(getValue);



		init();



	}

	public void actionPerformed( ActionEvent evt ) {
		int row = table.getSelectedRow();
		
		if ( evt.getSource() == getValue ) {
			String itemName = String.valueOf( table.getValueAt(row,0) );
			String ownerID = String.valueOf(table.getValueAt(row, 6));
			String itemID = String.valueOf(table.getValueAt(row, 7));
			if(Main.debug)	System.out.println(
					"Value at (" + row + "," + 0 + ") is " + "\'" + itemName + "\' nd the owner is " + ownerID+ " the item id is: "+ itemID);
		
		//Make a request to the server to receive a data obout this item by sending item ID
		RequestItemInfo rii = new RequestItemInfo(Integer.parseInt(itemID));	
		Comms.sendMessage(rii);
		SendItemInfo sii= ((SendItemInfo) Comms.receiveMessage());
		
			
	//	if(answer)
		main.changePain(new ViewItemGUI(main, sii.getItem(), sii.getRelevantBids()));
			
		}
	}


	public void init(){
		this.setOpaque(true);
		if(Main.debug) System.out.print("The number of items displayed is: "+items.size()+"<<! ");
		
	}



	@SuppressWarnings("serial")
	class AuctionsTable extends AbstractTableModel{

		private String[] columnNames = {"Name",
				"Category",
				"Price",
				"Bid",
				"Start",
				"End",
				"Owner",
		"ItemID"};
		private Object[][] data = {
				{"Kathy", "Smith",
					"Snowboarding", new Integer(5), new Boolean(false), "Smith",
				"Snowboarding"},
				{"John", "Doe",
					"Rowing", new Integer(3), new Boolean(true), "Smith",
				"Snowboarding"},
				{"Sue", "Black",
					"Knitting", new Integer(2), new Boolean(false), "Smith",
				"Snowboarding"},
				{"Jane", "White",
					"Speed reading", new Integer(20), new Boolean(true), "Smith",
				"Snowboarding"},
				{"Joe", "Brown",
					"Pool", new Integer(10), new Boolean(false), "Smith",
				"Snowboarding"},
				{"asdf", "dfs",
					"dfs", new Integer(12), new Boolean(true), "Smith",
				"Snowboarding"	}
		};

		public AuctionsTable() {

			//	super(new MyTableModel());


			init();
		}

		public void init(){
			//first get the data to fill the tables
			Comms.sendMessage(new RequestAllAuctions());
			items= ((GetAllAuctions) Comms.receiveMessage()).getItemsList();

			Object[][] object = new Object[items.size()][columnNames.length];
			for(int i=0; i<items.size(); i++){
				Object[] tmp =	{items.get(i).getTitle(), items.get(i).getCategoryChosen(),items.get(i).getReservedPrice(), "Bids info", items.get(i).getStartTime(), items.get(i).getEndTime(),items.get(i).getVendorID(), items.get(i).getItemID()};
				object[i]=  tmp;
			}

			setData(object); //now fill the table with that data
		}



		public String[] getColumnNames() {
			return columnNames;
		}

		public void setColumnNames(String[] columnNames) {
			this.columnNames = columnNames;
		}

		public Object[][] getData() {
			return data;
		}

		public void setData(Object[][] data) {
			this.data = data;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}






	}



	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub

	}

}