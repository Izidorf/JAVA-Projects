package airproject.view;

import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;



public class RunwayDialog extends JDialog{

	private JPanel desigPanel;
	private JPanel lowCalcPanel;
	private JPanel highCalcPanel;
	private JPanel cfgPanel;
	private JComboBox desigComboBox;
	
	private ButtonGroup bg;
	JRadioButton radioLeft;
	JRadioButton radioRight;
	JRadioButton radioDefault;
	JRadioButton radioCenter;

	private JTextField txtStripWidth;
	private JTextField txtGradDist;
	private JTextField txtClearDist;
	
	//High Runway Properties
	private JTextField txtHTora;
	private JTextField txtHToda;
	private JTextField txtHResa;
	private JTextField txtHStripEnd;
	private JTextField txtHLda;
	private JTextField txtHAsda;
	private JTextField txtHDispTHR;
	
	// Low Runway Properties
	private JTextField txtLTora;
	private JTextField txtLToda;
	private JTextField txtLResa;
	private JTextField txtLStripEnd;
	private JTextField txtLLda;
	private JTextField txtLAsda;
	private JTextField txtLDispTHR;
	
	//top runway designator
	String topRunwayDesignator;
	String bottomRunwayDesignator;

	public static void main(String[] args) {
		RunwayDialog n = new RunwayDialog(null, "New runway",true);
	}

	public RunwayDialog(JDialog owner, String title,boolean modal) {
		super(owner, title, modal);
		this.setSize(new Dimension(417, 440));
		
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);

		this.init();
	}

	private void init(){
		getContentPane().setLayout(null);
		this.addDesigPanel();
		this.addLowCalcPanel();
		this.addHighCalcPanel();
		this.addCGA();
		this.addActionBtn();
		this.setVisible(true);
	}

	private void addDesigPanel(){
		desigPanel = new JPanel();
		desigPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		desigPanel.setBounds(6, 6, 404, 61);
		getContentPane().add(desigPanel);
		desigPanel.setLayout(null);

		JLabel lblDesig = new JLabel("Designators:");
		lblDesig.setBounds(6, 6, 87, 16);
		desigPanel.add(lblDesig);

		desigComboBox = new JComboBox();
		String[] strComboBox;

		desigComboBox.setModel(new DefaultComboBoxModel(this.getNumbering()));
		desigComboBox.setBounds(94, 2, 87, 27);
		desigPanel.add(desigComboBox);

		radioLeft = new JRadioButton("Left");
		radioLeft.setSelected(true);
		radioLeft.setBounds(94, 34, 66, 23);
		radioLeft.setActionCommand("L");
		desigPanel.add(radioLeft);

		radioRight = new JRadioButton("Right");
		radioRight.setBounds(162, 34, 66, 23);
		radioRight.setActionCommand("R");
		desigPanel.add(radioRight);	
		
		radioCenter = new JRadioButton("Center");
		radioCenter.setActionCommand("C");
		radioCenter.setBounds(240, 34, 78, 23);
		desigPanel.add(radioCenter);
		
		radioDefault = new JRadioButton("Default");
		radioDefault.setActionCommand("");
		radioDefault.setBounds(320, 34, 78, 23);
		desigPanel.add(radioDefault);

		bg = new ButtonGroup();
		//bg.setModel(new ButtonModel());
		bg.add(radioLeft);
		bg.add(radioRight);
		bg.add(radioCenter);
		bg.add(radioDefault);
		
		 
		class TitleListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent e) {
				int int11 = Integer.parseInt((String)desigComboBox.getSelectedItem());
				String str12 = bg.getSelection().getActionCommand();
				int int21 = ((int11 + 18 - 1) % 36) + 1;
				String str22;
				switch(str12){
				case("R"):	 str22 = "L";
							 break;
				case("L"):   str22 = "R";
				 			 break;
				case("C"):   str22 = "C";
							 break;
				default: 	 str22 = "";
				}
				String lowPaneTitle =  int11+""+str12;
				String highPaneTitle = int21+""+str22;
				
				bottomRunwayDesignator=highPaneTitle;
				topRunwayDesignator=lowPaneTitle;
				
				lowCalcPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), lowPaneTitle, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				highCalcPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), highPaneTitle, TitledBorder.LEADING, TitledBorder.TOP, null, null));
			}
		}	
		TitleListener tListener = new TitleListener();
		desigComboBox.addActionListener(tListener);
		radioLeft.addActionListener(tListener);
		radioRight.addActionListener(tListener);
		radioCenter.addActionListener(tListener);
		radioDefault.addActionListener(tListener);	
	}

	private void addLowCalcPanel(){
		lowCalcPanel = new JPanel();
		topRunwayDesignator="1L";
		lowCalcPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "1L", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lowCalcPanel.setBounds(6, 68, 404, 122);
		getContentPane().add(lowCalcPanel);
		lowCalcPanel.setLayout(null);

		JLabel lblLTora = new JLabel("TORA");
		lblLTora.setBounds(6, 14, 42, 25);
		lowCalcPanel.add(lblLTora);

		JLabel lblLToda = new JLabel("TODA");
		lblLToda.setBounds(6, 39, 42, 25);
		lowCalcPanel.add(lblLToda);

		JLabel lblLResa = new JLabel("RESA");
		lblLResa.setBounds(6, 64, 42, 25);
		lowCalcPanel.add(lblLResa);

		txtLTora = new JTextField();
	//	txtLTora.setText("3660.0");
		txtLTora.setColumns(10);
		txtLTora.setBounds(60, 15, 125, 23);
		lowCalcPanel.add(txtLTora);

		txtLToda = new JTextField();
	//	txtLToda.setText("3660.0");
		txtLToda.setColumns(10);
		txtLToda.setBounds(60, 39, 125, 23);
		lowCalcPanel.add(txtLToda);

		txtLResa = new JTextField();
	//	txtLResa.setText("240.0");
		txtLResa.setColumns(10);
		txtLResa.setBounds(60, 64, 125, 23);
		lowCalcPanel.add(txtLResa);

		txtLStripEnd = new JTextField();
	//	txtLStripEnd.setText("60.0");
		txtLStripEnd.setColumns(10);
		txtLStripEnd.setBounds(273, 64, 125, 23);
		lowCalcPanel.add(txtLStripEnd);

		txtLLda = new JTextField();
	///	txtLLda.setText("3353.0");
		txtLLda.setColumns(10);
		txtLLda.setBounds(273, 39, 125, 23);
		lowCalcPanel.add(txtLLda);

		txtLAsda = new JTextField();
	//	txtLAsda.setText("3660.0");
		txtLAsda.setColumns(10);
		txtLAsda.setBounds(273, 14, 125, 23);
		lowCalcPanel.add(txtLAsda);

		JLabel lblLAsda = new JLabel("ASDA");
		lblLAsda.setBounds(213, 14, 42, 25);
		lowCalcPanel.add(lblLAsda);

		JLabel lblLLda = new JLabel("LDA");
		lblLLda.setBounds(213, 39, 42, 25);
		lowCalcPanel.add(lblLLda);

		JLabel lblLStripEnd = new JLabel("Strip end");
		lblLStripEnd.setBounds(197, 64, 66, 25);
		lowCalcPanel.add(lblLStripEnd);

		JLabel lblLDispThr = new JLabel("Displaced THR");
		lblLDispThr.setBounds(164, 87, 113, 27);
		lowCalcPanel.add(lblLDispThr);

		txtLDispTHR = new JTextField();
	//	txtLDispTHR.setText("307.0");
		txtLDispTHR.setColumns(10);
		txtLDispTHR.setBounds(273, 88, 125, 23);
		lowCalcPanel.add(txtLDispTHR);

		JButton btnLClear = new JButton("Clear");
		btnLClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtLToda.setText("");
				txtLTora.setText("");
				txtLResa.setText("");
				txtLAsda.setText("");
				txtLLda.setText("");
				txtLStripEnd.setText("");
				txtLDispTHR.setText("");
			}
		});
		btnLClear.setBounds(6, 87, 113, 29);
		lowCalcPanel.add(btnLClear);
	}

	public void addHighCalcPanel(){
		highCalcPanel = new JPanel();
		highCalcPanel.setLayout(null);
		bottomRunwayDesignator="19R";
		highCalcPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "19R", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		highCalcPanel.setBounds(6, 190, 404, 122);
		getContentPane().add(highCalcPanel);

		JLabel lblHtora = new JLabel("TORA");
		lblHtora.setBounds(6, 14, 42, 25);
		highCalcPanel.add(lblHtora);

		JLabel lblHtoda = new JLabel("TODA");
		lblHtoda.setBounds(6, 39, 42, 25);
		highCalcPanel.add(lblHtoda);

		JLabel lblHresa = new JLabel("RESA");
		lblHresa.setBounds(6, 64, 42, 25);
		highCalcPanel.add(lblHresa);

		txtHTora = new JTextField();
//		txtHTora.setText("3660.0");
		txtHTora.setColumns(10);
		txtHTora.setBounds(60, 14, 125, 23);
		highCalcPanel.add(txtHTora);

		txtHToda = new JTextField();
//		txtHToda.setText("3660.0");
		txtHToda.setColumns(10);
		txtHToda.setBounds(60, 39, 125, 23);
		highCalcPanel.add(txtHToda);

		txtHResa = new JTextField();
//		txtHResa.setText("240.0");
		txtHResa.setColumns(10);
		txtHResa.setBounds(60, 64, 125, 23);
		highCalcPanel.add(txtHResa);

		txtHStripEnd = new JTextField();
//		txtHStripEnd.setText("60.0");
		txtHStripEnd.setColumns(10);
		txtHStripEnd.setBounds(273, 64, 125, 23);
		highCalcPanel.add(txtHStripEnd);

		txtHLda = new JTextField();
//		txtHLda.setText("3353.0");
		txtHLda.setColumns(10);
		txtHLda.setBounds(273, 39, 125, 23);
		highCalcPanel.add(txtHLda);

		txtHAsda = new JTextField();
//		txtHAsda.setText("3660.0");
		txtHAsda.setColumns(10);
		txtHAsda.setBounds(273, 14, 125, 23);
		highCalcPanel.add(txtHAsda);

		JLabel lblHasda = new JLabel("ASDA");
		lblHasda.setBounds(213, 14, 42, 25);
		highCalcPanel.add(lblHasda);

		JLabel lblHlda = new JLabel("LDA");
		lblHlda.setBounds(213, 39, 42, 25);
		highCalcPanel.add(lblHlda);

		JLabel lblHstripEnd = new JLabel("Strip end");
		lblHstripEnd.setBounds(197, 64, 66, 25);
		highCalcPanel.add(lblHstripEnd);

		JLabel lblHdisplTHR = new JLabel("Displaced THR");
		lblHdisplTHR.setBounds(164, 87, 113, 27);
		highCalcPanel.add(lblHdisplTHR);

		txtHDispTHR = new JTextField();
//		txtHDispTHR.setText("307.0");
		txtHDispTHR.setColumns(10);
		txtHDispTHR.setBounds(273, 88, 125, 23);
		highCalcPanel.add(txtHDispTHR);

		JButton btnHclear = new JButton("Clear");
		btnHclear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtHAsda.setText("");
				txtHTora.setText("");
				txtHToda.setText("");
				txtHLda.setText("");
				txtHResa.setText("");
				txtHStripEnd.setText("");
				txtHDispTHR.setText("");
			}
		});
		btnHclear.setBounds(6, 87, 113, 29);
		highCalcPanel.add(btnHclear);
	}

	private void addCGA(){
		cfgPanel = new JPanel();
		cfgPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		cfgPanel.setBounds(6, 324, 275, 83);
		getContentPane().add(cfgPanel);
		cfgPanel.setLayout(null);

		txtStripWidth = new JTextField();
		txtStripWidth.setText("60.0");
		txtStripWidth.setColumns(10);
		txtStripWidth.setBounds(147, 55, 122, 23);
		cfgPanel.add(txtStripWidth);

		JLabel lblStripWidth = new JLabel("Strip width");
		lblStripWidth.setBounds(6, 54, 105, 27);
		cfgPanel.add(lblStripWidth);

		txtGradDist = new JTextField();
		txtGradDist.setText("150.0");
		txtGradDist.setColumns(10);
		txtGradDist.setBounds(147, 31, 122, 23);
		cfgPanel.add(txtGradDist);

		txtClearDist = new JTextField();
		txtClearDist.setText("105.0");
		txtClearDist.setColumns(10);
		txtClearDist.setBounds(147, 6, 122, 23);
		cfgPanel.add(txtClearDist);

		JLabel lblClearedDistance = new JLabel("Cleared distance");
		lblClearedDistance.setBounds(6, 6, 129, 25);
		cfgPanel.add(lblClearedDistance);

		JLabel lblGradedDist = new JLabel("Graded distance");
		lblGradedDist.setBounds(6, 31, 129, 25);
		cfgPanel.add(lblGradedDist);
	}


	JButton btnAdd;
	JButton btnCancel;

	private void addActionBtn(){

		btnAdd = new JButton("Add");

		btnAdd.setBounds(293, 378, 117, 29);
		getContentPane().add(btnAdd);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				RunwayDialog.this.dispose();
			}
		});
		btnCancel.setBounds(293, 322, 117, 29);
		getContentPane().add(btnCancel);
	}

	private String[] getNumbering(){
		String[] numbering = new String[36];
		for(int i=0;i<36;i++){
			numbering[i] = Integer.toString((i+1));
		}
		return numbering;
	}

	/*---------------------------------------------------------------------------------
	 *------------------------  GUI Public Listener control   -------------------------
	 --------------------------------------------------------------------------------*/
	
	public void setAddRunwayListener(ActionListener al) {
		btnAdd.addActionListener(al);

	}

	/*---------------------------------------------------------------------------------
	 *------------------------  GUI Public Access Methods   ---------------------------
	 --------------------------------------------------------------------------------*/

	/*
	 * Gets the default runway parameters in the form of an array
	 * The indexes use are described in GuiConstants
	 * Throws a GuiException if a parameter is poorly formatted
	 */
	
	
	public float[] getGeneralRunwayParameter() throws GuiException {
		float[] generalParams = new float[3];
		//Attempt to convert distance Cleared to float
		try {
			generalParams[GuiConstants.DATA_PROPERTY_DIS_CLEARED]= Float
					.parseFloat((String) txtClearDist.getText());
		} catch (NumberFormatException e) {
			throw new GuiException(
					"Cleared distance value input is not a valid number!");
		}
		
		//Attempt to convert distance graded to float
		try {
			generalParams[GuiConstants.DATA_PROPERTY_DIS_GRADED]= Float
					.parseFloat((String) txtGradDist.getText());
		} catch (NumberFormatException e) {
			throw new GuiException(
					"Cleared distance value input is not a valid number!");
		}
		//Attempt to convert strip width  to float
		try {
			generalParams[GuiConstants.DATA_PROPERTY_WIDTH]= Float
					.parseFloat((String) txtStripWidth.getText());
		} catch (NumberFormatException e) {
			throw new GuiException(
					"Cleared distance value input is not a valid number!");
		}
		
		return generalParams;
	}
	
	/*
	 * Return the side of the runway designator
	 * */
	
	public String getTopRunwayDesignator() {
	//	System.out.println("Top designator is: "+bg.getSelection().getActionCommand());
		return bg.getSelection().getActionCommand();
	}
	
	public int getTopRunwayAngle(){
		return Integer.parseInt((String)desigComboBox.getSelectedItem());
	}
	
	public String getBottomRunwayDesignator() {
		String temp = getTopRunwayDesignator();
		switch(temp){
			case("R"):	 return "L";
			case("L"):   return "R";
			case("C"):	 return "C";		
			default: 	 return "";
		}
	}
	
	public int getBottomRunwayAngle(){
		return (((getTopRunwayAngle() + 18 - 1) % 36) + 1);
		
	}

	public float[] getTopRunwayParameters() throws GuiException {
		float[] originalParams = new float[6];
		// Attempt to convert TORA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_TORA] = Float
					.parseFloat((String) txtLTora.getText());
		} catch (NumberFormatException e) {
			throw new GuiException(
					"The TORA value input is not a valid number!");
		}
		// Attempt to convert TODA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_TODA] = Float
					.parseFloat((String) txtLToda.getText());
		} catch (NumberFormatException e) {
			throw new GuiException(
					"The TODA value input is not a valid number!");
		}
		// Attempt to convert ASDA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_ASDA] = Float
					.parseFloat((String) txtLAsda.getText());
		} catch (NumberFormatException e) {
			throw new GuiException(
					"The ASDA value input is not a valid number!");
		}
		// Attempt to convert LDA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_LDA] = Float
					.parseFloat((String) txtLLda.getText());
		} catch (NumberFormatException e) {
			throw new GuiException("The LDA value input is not a valid number!");
		}
		// Attempt to convert displacement threshold text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_THR] = Float
					.parseFloat((String) txtLDispTHR.getText());
		} catch (NumberFormatException e) {
			throw new GuiException("The landing threshold value input is not a valid number!");
		}
		// Attempt to convert RESA  text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_RESA] = Float
					.parseFloat((String) txtLResa.getText());
		} catch (NumberFormatException e) {
			throw new GuiException("The landing threshold value input is not a valid number!");
		}

			

		return originalParams;

	}
	
	public float[] getBottomRunwayParameters() throws GuiException {
		float[] originalParams = new float[6];
		// Attempt to convert TORA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_TORA] = Float
					.parseFloat((String) txtHTora.getText());
		} catch (NumberFormatException e) {
			throw new GuiException(
					"The TORA value input is not a valid number!");
		}
		// Attempt to convert TODA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_TODA] = Float
					.parseFloat((String) txtHToda.getText());
		} catch (NumberFormatException e) {
			throw new GuiException(
					"The TODA value input is not a valid number!");
		}
		// Attempt to convert ASDA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_ASDA] = Float
					.parseFloat((String) txtHAsda.getText());
		} catch (NumberFormatException e) {
			throw new GuiException(
					"The ASDA value input is not a valid number!");
		}
		// Attempt to convert LDA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_LDA] = Float
					.parseFloat((String) txtHLda.getText());
		} catch (NumberFormatException e) {
			throw new GuiException("The LDA value input is not a valid number!");
		}
		// Attempt to convert displacement threshold text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_THR] = Float
					.parseFloat((String) txtHDispTHR.getText());
		} catch (NumberFormatException e) {
			throw new GuiException("The landing threshold value input is not a valid number!");
		}
		// Attempt to convert RESA  text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_RESA] = Float
					.parseFloat((String) txtHResa.getText());
		} catch (NumberFormatException e) {
			throw new GuiException("The landing threshold value input is not a valid number!");
		}

		return originalParams;

	}
	
	/*---------------------------------------------------------------------------------
	 *------------------------  GUI Public Update Methods   ---------------------------
	 --------------------------------------------------------------------------------*/
	
	public void setRunwayDesignator(int angle, String designator){	
		desigComboBox.getModel().setSelectedItem(Integer.toString(angle));
		if(designator.equals("Left"))
		{
			radioLeft.getModel().setSelected(true);
			radioRight.getModel().setSelected(false);
			radioDefault.getModel().setSelected(false);
			radioCenter.getModel().setSelected(false);
		}
		else if(designator.equals("Right")){
			radioLeft.getModel().setSelected(false);
			radioRight.getModel().setSelected(true);
			radioDefault.getModel().setSelected(false);
			radioCenter.getModel().setSelected(false);
		}
		else if(designator.equals("Center")){
			radioLeft.getModel().setSelected(false);
			radioRight.getModel().setSelected(false);
			radioDefault.getModel().setSelected(false);
			radioCenter.getModel().setSelected(true);
		} 
		else if(designator.equals("")){
			radioLeft.getModel().setSelected(false);
			radioRight.getModel().setSelected(false);
			radioDefault.getModel().setSelected(true);
			radioCenter.getModel().setSelected(false);
		}
														
		
	}
	
	public void setGeneralRunwayParameters(float[] params){
		txtClearDist.setText(Float.toString(params[GuiConstants.DATA_PROPERTY_DIS_CLEARED]));
		txtGradDist.setText(Float.toString(params[GuiConstants.DATA_PROPERTY_DIS_GRADED])); 
		txtStripWidth.setText(Float.toString(params[GuiConstants.DATA_PROPERTY_WIDTH])); 
	}
	
	public void setBottomRunwayParameters(float[] params){
		System.out.println("General run params are: \n"+
				"TORA: "+params[GuiConstants.DATA_PROPERTY_TORA]+" \n"
				+"TODA: "+params[GuiConstants.DATA_PROPERTY_TODA]+" \n"
				+"LDA: "+params[GuiConstants.DATA_PROPERTY_LDA]+" \n"
				+"ASDA: "+params[GuiConstants.DATA_PROPERTY_ASDA]+" \n"
				+"THR: "+params[GuiConstants.DATA_PROPERTY_THR]+" \n"
				+"RESA: "+params[GuiConstants.DATA_PROPERTY_RESA]+" \n");
		txtHTora.setText(Float.toString(    params[GuiConstants.DATA_PROPERTY_TORA]));
		txtHToda.setText(Float.toString( 	params[GuiConstants.DATA_PROPERTY_TODA]));
		txtHLda.setText(Float.toString( 	params[GuiConstants.DATA_PROPERTY_LDA]));
		txtHAsda.setText(Float.toString( 	params[GuiConstants.DATA_PROPERTY_ASDA]));
		txtHDispTHR.setText(Float.toString(	params[GuiConstants.DATA_PROPERTY_THR] ));
		txtHResa.setText(Float.toString( 	params[GuiConstants.DATA_PROPERTY_RESA]));
		
	}
	
	public void setTopRunwayParameters(float[] params){
		txtLTora.setText(Float.toString(    params[GuiConstants.DATA_PROPERTY_TORA]));
		txtLToda.setText(Float.toString( 	params[GuiConstants.DATA_PROPERTY_TODA]));
		txtLLda.setText(Float.toString( 	params[GuiConstants.DATA_PROPERTY_LDA]));
		txtLAsda.setText(Float.toString( 	params[GuiConstants.DATA_PROPERTY_ASDA]));
		txtLDispTHR.setText(Float.toString(	params[GuiConstants.DATA_PROPERTY_THR] ));
		txtLResa.setText(Float.toString( 	params[GuiConstants.DATA_PROPERTY_RESA]));
		
	}
}
