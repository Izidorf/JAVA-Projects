package airproject.view;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.border.LineBorder;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import javax.swing.border.LineBorder;

import org.omg.CORBA.DATA_CONVERSION;

public class NewAirportDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField txtAirportName;
	private JTextField txtAirportCode;

	private JList<String> lstRunway;
	private DefaultListModel<String> runwayLstModel;
	private RunwayDialog newRunwayFrame;

	public static void main(String[] args) {
		NewAirportDialog f = new NewAirportDialog(null, "New airport", true);
	}

	public NewAirportDialog(JFrame owner, String title, boolean modal) {
		super(owner, title, modal);
		this.setSize(new Dimension(450, 336));

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);

		this.init();
		this.setVisible(true);
	}

	private void init() {
		getContentPane().setLayout(null);

		addNewAiportPanel();
		addRunwaySpecPanel();
		addSpecBtns();
	}

	private void addNewAiportPanel() {
		JPanel airPanel = new JPanel();
		airPanel.setBounds(10, 10, 430, 120);
		airPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(airPanel);

		txtAirportName = new JTextField();
		txtAirportName.setBounds(112, 21, 301, 28);
		txtAirportCode = new JTextField();
		txtAirportCode.setBounds(112, 53, 301, 28);

		JLabel lblAirportName = new JLabel("Airport name:");
		lblAirportName.setBounds(22, 27, 86, 16);
		JLabel lblAirportCode = new JLabel("Airport code:");
		lblAirportCode.setBounds(26, 59, 82, 16);
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(22, 85, 76, 29);
		JLabel emptyLbl = new JLabel("");
		emptyLbl.setBounds(17, 17, 0, 0);

		txtAirportName.setColumns(10);
		txtAirportCode.setColumns(10);
		airPanel.setLayout(null);

		airPanel.add(emptyLbl);
		airPanel.add(lblAirportName);
		airPanel.add(txtAirportName);
		airPanel.add(lblAirportCode);
		airPanel.add(txtAirportCode);
		airPanel.add(btnClear);

		class ClearBtn implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				txtAirportName.setText("");
				txtAirportCode.setText("");
			}
		}
		btnClear.addActionListener(new ClearBtn());
	}

	JButton btnAddRunway;
	JButton btnRemove;
	JButton btnEdit;

	public void addRunwaySpecPanel() {
		JPanel runwayPanel = new JPanel();
		runwayPanel.setBounds(10, 140, 430, 140);
		runwayPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(runwayPanel);
		runwayPanel.setLayout(null);

		runwayLstModel = new DefaultListModel<String>();
		
		
		lstRunway = new JList<String>();
		lstRunway.setBounds(120, 5, 300, 125);
		// Allow to choose only one element of the list at the same moment
		lstRunway.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		runwayPanel.add(lstRunway);

		JLabel lblRunway = new JLabel("Runways:");
		lblRunway.setBounds(50, 15, 60, 15);
		runwayPanel.add(lblRunway);
		btnAddRunway = new JButton("Add new");
		btnAddRunway.setBounds(5, 35, 115, 30);
		runwayPanel.add(btnAddRunway);

		btnEdit = new JButton("Edit");
		btnEdit.setBounds(5, 60, 115, 30);
		runwayPanel.add(btnEdit);


		btnRemove = new JButton("Remove");
		btnRemove.setBounds(5, 100, 115, 30);
		runwayPanel.add(btnRemove);
	}

	JButton btnCancel;
	JButton btnOK;

	// "OK" & "CANCEL" buttons
	public void addSpecBtns() {
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(10, 280, 130, 30);
		getContentPane().add(btnCancel);

		btnOK = new JButton("OK");
		btnOK.setBounds(315, 280, 130, 30);
		getContentPane().add(btnOK);

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewAirportDialog.this.setVisible(false);
				NewAirportDialog.this.dispose();
			}
		});
	}

	/*---------------------------------------------------------------------------------
	 *------------------------  GUI Public Listener control   -------------------------
	 --------------------------------------------------------------------------------*/
	public void setOkListener(ActionListener al) {
		btnOK.addActionListener(al);
	}
	
	public void setNewRunwayListener(ActionListener al){
		btnAddRunway.addActionListener(al);
	}
	
	public void setEditRunwayListener(ActionListener al){
		btnEdit.addActionListener(al);
	}
	
	public void setRemoveRunwayListener(ActionListener al){
		btnRemove.addActionListener(al);
	}

	/*---------------------------------------------------------------------------------
	 *------------------------  GUI Public Update Methods   ---------------------------
	 --------------------------------------------------------------------------------*/
	
	
	/*
	 * Updates the JList of runways with the  use of DefaultListModel
	 * */
	public void updateGUIRunwaysJList(String [] runwayAbrevations){
		//Clear data from the table
		runwayLstModel.clear();
		//Update table with the new data
		for(String var:runwayAbrevations)
			runwayLstModel.addElement(var);
		lstRunway.setModel(runwayLstModel);
	}

	/*---------------------------------------------------------------------------------
	 *------------------------  GUI Public Access Methods   ---------------------------
	 --------------------------------------------------------------------------------*/

	public String getAirportName() {
		return this.txtAirportName.getText();
	}

	public String getAirportCode() {
		return this.txtAirportCode.getText();
	}

	
	public String[] getAiportParameters(){
		String[] airportParams = new String[2];	
		airportParams[GuiConstants.DATA_AIRPORT_NAME] = this.txtAirportName.getText();
		airportParams[GuiConstants.DATA_AIRPORT_CODE] = this.txtAirportCode.getText();
		return airportParams;
	}

	public RunwayDialog openCreationOfNewRunway() {
		newRunwayFrame = new RunwayDialog(NewAirportDialog.this,
				"New runway", false);
		return newRunwayFrame;
	}
	
	public boolean getIsElementSelectedJList(){
		return lstRunway.isSelectionEmpty();
	}
	
	public String getSelectedElementJList(){
		int index = lstRunway.getSelectedIndex();
		return runwayLstModel.get(index); // TO BE IMPLEMENTED
	}

}
