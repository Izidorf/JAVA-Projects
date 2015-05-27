package airproject.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

public class MainFrame extends JFrame {
	
	public static final int BOTH = 0, TOP = 1, SIDE = 2;

	public MainFrame(String title) {
		super(title);
	}

	private JMenuBar menuBar;
	//new
		JMenuItem newAirport;
		JMenuItem newObstacle;
	JMenuItem print;
	JMenuItem removeObstalce;
	
	JMenuItem editAirport;
	JMenuItem editObstacle;
	
	
	//view->colouring
	JMenuItem plain;
	JMenuItem texture;
	
	private int current_screen_configuration = BOTH;

	private JPanel leftPanel;
	private JLabel currentAirport;
	private JComboBox<String> currentRunway;
	private JTable originalValuesTable;
	private JButton updateOriginal;
	private JTable redeclaredValuesTable;
	
	// Create panel for obstacle table.
	private boolean obstacleExists=true;
	
	//Obstacle exists components
	JPanel obstVals = new JPanel();
	private JTable obstacleValuesTable;
	private JButton updateObstacle;
	private JButton removeObstacle;
	
	// Calculation parameters.
	private JTable calculationParamsTable;
	private JButton updateCalcVars;
	
	//Obstacle doesnt exist
	private JPanel addObstaclePanel;
	private JButton addObstacle;

	private JTabbedPane centralPanel;
	private JTextArea calculationsArea;

	private JMenuItem menuItemCalculations;
	private FileActionListener exportCalculationsListener;

	//Help tab
	private JMenuItem manual;
	private JMenuItem about;


	private FileActionListener saveAirportListener;
	private FileActionListener saveObstacleListener;
	private FileActionListener openAirportListener;
	private FileActionListener openObstacleListener;

	private JPanel mainContentPanel;

	private JPanel visualPanel;
	private TopDownViewPanel topDownArea;
	private SideOnViewPanel sideOnArea;
	
	//Links to other frames;
	private AirportDialog addAiport;
	private AirportDialog editingAirport;
	private ObstacleDialog editObstacleDialog;

	public void init() {
		initMenuBar();
		initContentPanel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 700);
		setVisible(true);
	}

	/*
	 * MENU BAR
	 */
	public void initMenuBar() {
		

		// creates the menu bar
		menuBar = new JMenuBar();
		// build the first menu
		JMenu menu1 = new JMenu("File");
		// Add Elements of the first menu
		// FILE -> NEW
		JMenu createNew = new JMenu("New");
		createNew.setIcon(createImageIcon("/icons/new.png", "new"));
		// Add sub-elements
		
			//FILE ->NEW -> Airport
			newAirport = new JMenuItem("Airport");
			newAirport.setIcon(createImageIcon("/icons/airport.png","airport"));
			createNew.add(newAirport);
			
			//FILE ->NEW -> Obstacle
			newObstacle = new JMenuItem("Obstacle");
			newObstacle.setIcon(createImageIcon("/icons/obstacle.png", "obstacle"));
			createNew.add(newObstacle);
			
		JMenu open = new JMenu("Open");
		open.setIcon(createImageIcon("/icons/open.png","open"));
		// Add sub-elements
		JMenuItem airport_open = new JMenuItem("Airport");
		airport_open.setIcon(createImageIcon("/icons/airport.png","airport"));
		airport_open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"xml", "xml");
				chooser.setFileFilter(filter);
				String path= "./airports/";
				chooser.setCurrentDirectory(new File(path));
				int returnVal = chooser.showOpenDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (openAirportListener != null)
						openAirportListener.actionPerformed(file);
				}
			}
		});
		open.add(airport_open);
		JMenuItem obstacle_open = new JMenuItem("Obstacle");
		obstacle_open.setIcon(createImageIcon("/icons/obstacle.png","obstacle"));
		obstacle_open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"xml", "xml");
				chooser.setFileFilter(filter);
				String path= "./obstacles/";
				chooser.setCurrentDirectory(new File(path));
				int returnVal = chooser.showOpenDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (openObstacleListener != null)
						openObstacleListener.actionPerformed(file);
				}
			}
		});
		open.add(obstacle_open);
		JMenu save = new JMenu("Save");
		save.setIcon(createImageIcon("/icons/save.png","save"));
		// Add sub-elements
		JMenuItem airport_save = new JMenuItem("Airport");
		airport_save.setIcon(createImageIcon("/icons/airport.png","airport"));
		airport_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"xml", "xml");
				chooser.setFileFilter(filter);
				String path= "./airports/";//getClass().getResource("/airports/").getPath();
				chooser.setCurrentDirectory(new File(path));
				int returnVal = chooser.showSaveDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (saveAirportListener != null)
						saveAirportListener.actionPerformed(file);
				}
			}
		});
		save.add(airport_save);
		JMenuItem obstacle_save = new JMenuItem("Obstacle");
		obstacle_save.setIcon(createImageIcon("/icons/obstacle.png","obstacle"));
		obstacle_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"xml", "xml");
				chooser.setFileFilter(filter);
				String path="./obstacles/";
				chooser.setCurrentDirectory(new File(path));
				int returnVal = chooser.showSaveDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (saveObstacleListener != null)
						saveObstacleListener.actionPerformed(file);
				}
			}
		});
		save.add(obstacle_save);
		JMenu export = new JMenu("Export");
		export.setIcon(createImageIcon("/icons/export.png","export"));
		menuItemCalculations = new JMenuItem("Calculations");
		menuItemCalculations.setIcon(createImageIcon("/icons/calculations1.png", "calculations"));
		// Gets the file with a dialogue and calls the file action listener
		// Needs cleaning up...
		menuItemCalculations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showSaveDialog(MainFrame.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (exportCalculationsListener != null)
						exportCalculationsListener.actionPerformed(file);
				}
			}
		});
		export.add(menuItemCalculations);

		print = new JMenuItem("Print");
		print.setIcon(createImageIcon("/icons/print.png", "print"));
		
		JMenuItem exit = new JMenuItem("Exit");
		exit.setIcon(createImageIcon("/icons/exit.png", "exit"));
		
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Stop the application.
				System.exit(0);
			}
		});

		menu1.add(createNew);
		menu1.add(open);
		menu1.add(save);
		menu1.add(export);
		//menu1.add(print);
		menu1.addSeparator();
		menu1.add(exit);
		menuBar.add(menu1);
		
		
		// build the second menu
		JMenu menu2 = new JMenu("Edit");
		editAirport = new JMenuItem("Airport");
		editAirport.setIcon(createImageIcon("/icons/airport.png", "airport"));
		JMenuItem editRunway = new JMenuItem("Runway");
		editRunway.setIcon(createImageIcon("/icons/runway.jpg", "runway"));
		editObstacle = new JMenuItem("Obstacle");
		editObstacle.setIcon(createImageIcon("/icons/obstacle.png", "obstacle"));

		removeObstalce = new JMenuItem("Remove Obstacle");
		removeObstalce.setIcon(createImageIcon("/icons/delete.png", "delete"));

		menu2.add(editAirport);
		//menu2.add(editRunway);
		menu2.add(editObstacle);
		menu2.addSeparator();
		menu2.add(removeObstalce);


		menuBar.add(menu2);


		// build the third menu
		JMenu menu3 = new JMenu("View");
		
		JMenu chooseColor = new JMenu("Choose Colouring");
		plain =  new JMenuItem("Plain");
		texture =  new JMenuItem("Texture");
		
		chooseColor.add(plain);
		chooseColor.add(texture);
		
		menu3.add(chooseColor);
		
		menuBar.add(menu3);

		// build the help menu
		JMenu menu4 = new JMenu("Help");
		manual = new JMenuItem("Show Manual");
		manual.setIcon(createImageIcon("/icons/help.png", "help"));
		about = new JMenuItem("About");
		about.setIcon(createImageIcon("/icons/about.png", "about"));

		about.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Developed by SEG 10 in 2014 \n" +
													"Izidor Flajsman \n" +
													"Sam Holder \n" +
													"James Buck \n" +
													"Almat Kuanysh \n" +
													"Contact: seg10@soton.ac.uk" +
													"","ABOUT",JOptionPane.PLAIN_MESSAGE);
										
			}
		});
		
		menu4.add(manual);
		menu4.addSeparator();
		menu4.add(about);

		 menu4.setMnemonic('h');
		menuBar.add(menu4);//DEMO

		this.setJMenuBar(menuBar);

	}

	/*
	 * CONTENT PANEL
	 */
	public void initContentPanel() {
		mainContentPanel = new JPanel();
		mainContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainContentPanel.setLayout(new BorderLayout());

		this.setContentPane(mainContentPanel);
		leftPanel();
		rightPanel();

		// repaint visuals
		topDownArea.repaint();
		sideOnArea.repaint();
	}

	/*
	 * Construct the left-hand panel.
	 */
	public void leftPanel() {
		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(5, 1));
		// Create Area with airport data.
		JPanel airportData = new JPanel();
		airportData.setLayout(new GridLayout(2, 2));
		airportData.setBorder(new TitledBorder(null, "AirportData",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// Create airport name Labels.
		JLabel airportName = new JLabel("Airport Name");
		currentAirport = new JLabel("<none>");
		// Create runway dropdown menu.
		JLabel runwayName = new JLabel("Select Runway:");
		// Inner BoxLayout panel to prevent combobox taking maximum vertical height in the grid
		JPanel runwayComboPanel = new JPanel();
		runwayComboPanel.setLayout(new BoxLayout(runwayComboPanel, BoxLayout.PAGE_AXIS));
		currentRunway = new JComboBox<String>();
		ComboBoxModel<String> comboModel = new DefaultComboBoxModel<>();
		currentRunway.setModel(comboModel);
		runwayComboPanel.add(Box.createGlue());
		runwayComboPanel.add(currentRunway);
		runwayComboPanel.add(Box.createGlue());
		// Add components to airport data area.
		airportData.add(airportName);
		airportData.add(currentAirport);
		airportData.add(runwayName);
		airportData.add(runwayComboPanel);
		// Add the airport data area.
		leftPanel.add(airportData);
		// Create table with original parameters.
		originalValuesTable = new SelectAllTable();
		originalValuesTable.setBorder(new EmptyBorder(5, 5, 5, 5));
		originalValuesTable.setRowSelectionAllowed(false);
		originalValuesTable.setEnabled(true);
		originalValuesTable.setModel(new DefaultTableModel(new Object[][] {
				{ "TORA", "" }, { "TODA", "" }, { "ASDA", "" }, { "LDA", "" },
				{ "Displaced Threshold", "" } }, new String[] { "New column",
		"New column" }){
			
			@Override
			public boolean isCellEditable(int row, int col) {
			     switch (col) {
			         case 0:
			        	 return false;
			         case 1:
			             return true;
			         default:
			             return false;
			      }
			}
			
		});
		originalValuesTable.getColumnModel().getColumn(0)
		.setPreferredWidth(150);
		// Create panel for original parameters table.
		JPanel orgVals = new JPanel();
		orgVals.add(originalValuesTable);
		// Add update button to panel.
		updateOriginal = new JButton("Update");
		updateOriginal.setSize(10, 80);
		orgVals.add(updateOriginal);
		orgVals.setBorder(new TitledBorder(null, "Original Values",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// Add to the left panel.
		leftPanel.add(orgVals);

		// Create table with redeclared parameters.
		redeclaredValuesTable = new JTable();
		redeclaredValuesTable.setBorder(new EmptyBorder(5, 5, 5, 5));
		redeclaredValuesTable.setEnabled(false);
		redeclaredValuesTable.setRowSelectionAllowed(false);
		redeclaredValuesTable.setModel(new DefaultTableModel(
				new Object[][] { { "TORA", "" }, { "TODA", "" },
						{ "ASDA", "" }, { "LDA", "" } }, new String[] {
						"New column", "New column" }));
		redeclaredValuesTable.getColumnModel().getColumn(0)
		.setPreferredWidth(150);
		// Create panel for redeclared values table.
		JPanel redecVals = new JPanel();
		redecVals.add(redeclaredValuesTable);
		redecVals.setBorder(new TitledBorder(null, "Redeclared Values",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		// Add to the left panel.
		leftPanel.add(redecVals);

		// Create table with obstacle info.
		obstacleValuesTable = new SelectAllTable();
		obstacleValuesTable.setEnabled(true);
		obstacleValuesTable.setRowSelectionAllowed(false);
		obstacleValuesTable.setModel(new DefaultTableModel(new Object[][] {
				{ "height:", "" }, { "distance from low threshold:", "" },
				{ "distance from high threshold:", "" },
				{ "distance from centerline:", "" }
		}, new String[] { "New column", "New column" }){	
				@Override
				public boolean isCellEditable(int row, int col) {
				     switch (col) {
				         case 0:
				        	 return false;
				         case 1:
				             return true;
				         default:
				             return false;
				      }
				
				}
		});

		updateObstacle = new JButton("update");
		removeObstacle = new JButton("remove");
		
		if(obstacleExists)
			obstacleInfoIfExists();
		else
			obstacleInfoIfDoesntExist();
		// Add to the left panel.
		leftPanel.add(obstVals);

		
		JPanel calculationVariables = new JPanel();
		// Create table with calculation parameter info.
		calculationParamsTable = new SelectAllTable();
		calculationParamsTable.setBorder(new EmptyBorder(5, 5, 5, 5));
		calculationParamsTable.setEnabled(true);
		calculationParamsTable.setRowSelectionAllowed(false);
		calculationParamsTable.setModel(new DefaultTableModel(new Object[][] {
				{ "Blast protection:", "" }, { "Slope ratio:", "" }
		}, new String[] { "", "" }){	
				@Override
				public boolean isCellEditable(int row, int col) {
				     switch (col) {
				         case 0:
				        	 return false;
				         case 1:
				             return true;
				         default:
				             return false;
				      }
				
				}
		});
		calculationParamsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		calculationParamsTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		calculationVariables.add(calculationParamsTable);
		calculationVariables.setBorder(new TitledBorder(null, "Calculation Variables",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));


		updateCalcVars = new JButton("update");
		calculationVariables.add(updateCalcVars);
		
		leftPanel.add(calculationVariables);

		// Add the left panel to the main content panel.
		mainContentPanel.add(leftPanel, BorderLayout.LINE_START);
		
	}
	
	public void obstacleInfoIfExists(){
				// Clear the obstacle values panel.
				obstVals.removeAll();
				// Add the obstacle values table.
				obstVals.add(obstacleValuesTable);
				// Add update button to panel.
				JPanel twoButtons = new JPanel();
				twoButtons.setLayout(new BoxLayout(twoButtons, BoxLayout.PAGE_AXIS));
				// Add the update obstacle button.
				//updateObstacle = new JButton("update");
				twoButtons.add(updateObstacle);
				// Add the remove obstacle button.
				//removeObstacle = new JButton("remove");
				twoButtons.add(removeObstacle);
				// Add the two buttons
				obstVals.add(twoButtons);
				// Set the border
				obstVals.setBorder(new TitledBorder(null, "Obstacle Information",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
				obstacleValuesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
				obstVals.updateUI();
	}
	
	public void obstacleInfoIfDoesntExist(){
				// Remove all obstacle values
				obstVals.removeAll();
				// Create the "add new obstacle button"
				addObstacle = new JButton("Import new obstacle");
				addObstacle.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						final JFileChooser chooser = new JFileChooser();
						FileNameExtensionFilter filter = new FileNameExtensionFilter(
								"xml", "xml");
						chooser.setFileFilter(filter);
						String path="./obstacles/";
						chooser.setCurrentDirectory(new File(path));
						int returnVal = chooser.showOpenDialog(MainFrame.this);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = chooser.getSelectedFile();
							if (openObstacleListener != null)
								openObstacleListener.actionPerformed(file);
								obstacleExists=true;
								leftPanel.updateUI();
						}
					}
				});
				// Add the new button
				obstVals.add(addObstacle);
				// Set the border.
				obstVals.setBorder(new TitledBorder(null, "Obstacle Information",
						TitledBorder.LEADING, TitledBorder.TOP, null, null));
				obstVals.updateUI();
	}
	

	/*
	 * Constructs the central panel.
	 */
	public void rightPanel() {
		centralPanel = new JTabbedPane();
		// Create visual panel for visual displays.
		visualPanel = new JPanel();
		topDownArea = new TopDownViewPanel(this);
		sideOnArea = new SideOnViewPanel(this);
		arrangeVisualPanelBoth();
		// Create calculations text area.
		calculationsArea = new JTextArea(38, 60);
		JScrollPane sp = new JScrollPane(calculationsArea);
		LoggerPanel logPanel = new LoggerPanel();
		// Add the tabs.
		centralPanel.addTab("Visuals", visualPanel);
		centralPanel.addTab("Calculations", sp);
		centralPanel.addTab("Action History", logPanel);
		// Add the central panel to the main content panel.
		mainContentPanel.add(centralPanel, BorderLayout.CENTER);
	}
	
	/*
	 * Arranges the visual panel for both views.
	 */
	public void arrangeVisualPanelBoth(){
		visualPanel.removeAll();
		visualPanel.setLayout(new GridLayout(2, 1));
		// Create top-down view.
		visualPanel.add(topDownArea);
		// Create side-on view.
		visualPanel.add(sideOnArea);
		current_screen_configuration = BOTH;
		revalidate();
		repaint();
	}
	
	/*
	 * Arranges the visual panel for top down.
	 */
	public void arrangeVisualPanelTop(){
		visualPanel.removeAll();
		visualPanel.setLayout(new BorderLayout());
		// Create top-down view.
		visualPanel.add(topDownArea, BorderLayout.CENTER);
		current_screen_configuration = TOP;
		revalidate();
		repaint();
	}
	
	/*
	 * Arranges the visual panel for side on.
	 */
	public void arrangeVisualPanelSide(){
		visualPanel.removeAll();
		visualPanel.setLayout(new BorderLayout());
		// Create top-down view.
		visualPanel.add(sideOnArea, BorderLayout.CENTER);
		current_screen_configuration = SIDE;
		revalidate();
		repaint();
	}
	
	public void toggleArrangeTop(){
		if( current_screen_configuration == BOTH){
			arrangeVisualPanelTop();
		}else{
			arrangeVisualPanelBoth();
		}
	}
	
	public void toggleArrangeSide(){
		if( current_screen_configuration == BOTH){
			arrangeVisualPanelSide();
		}else{
			arrangeVisualPanelBoth();
		}
	}

	public void enforceOneOpenMenu(AbstractViewPanel panel) {
		if( panel == topDownArea ){
			sideOnArea.setButtonMenuOpen(false);
		}else if(panel == sideOnArea){
			topDownArea.setButtonMenuOpen(false);
		}
	}
	/*
	 * Returns the instance of top-down view.
	 */
	public TopDownViewPanel getTopDownView() {
		return topDownArea;
	}

	/*
	 * Returns the instance of side-on view.
	 */
	public SideOnViewPanel getSideOnView(){
		return sideOnArea;
	}


	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
			String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/*
	 * Display an error pop-up to the user.
	 */
	public void displayError(final Exception e){
		// Display error in EDT.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create prompt with message from Exception.
				JOptionPane.showMessageDialog(MainFrame.this, e.getMessage());
			}
		});
	}

	/*----------------------------------------------------------------------------------
	 *------------------------  GUI Public Listener control   --------------------------
	 ---------------------------------------------------------------------------------*/


	/*
	 * Sets the listener which handles when the update button is pressed within the calculation properties
	 * panel.
	 */
	public void setCalculationPropertiesButtonActionListener(
			ActionListener calculationPropertiesUpdateListener) {
		updateCalcVars.addActionListener(calculationPropertiesUpdateListener);
	}
	
	/*
	 * Sets the listener which handles when the update button is pressed within the original values
	 * panel.
	 */
	public void setUpdateOriginalButtonActionListener(ActionListener al) {
		updateOriginal.addActionListener(al);
	}

	/*
	 * Sets the listener which handles when the update button is pressed within the obstacle values
	 * panel.
	 */
	public void setUpdateObstacleButtonActionListener(ActionListener al) {
		updateObstacle.addActionListener(al);
	}
	
	/*
	 * Sets the listner which removes obstacle
	 * */
	
	public void setRemoveObstacleButtonActionListener(ActionListener al){
		removeObstacle.addActionListener(al);
	}
	
	/*
	 * Sets choose color action listener
	 * */
	public void setChoosePlainColorActionListener(ActionListener al){
		plain.addActionListener(al);
	}
	
	/*
	 * Sets textures action listener
	 * */
	public void setChooseTexturesAsColorActionListener(ActionListener al){
		texture.addActionListener(al);
	}

	public void setAddNewObstacleListener(ActionListener al){
		newObstacle.addActionListener(al); 
	}
	
	
	
	/*
	 * Sets the listener which handles when the calculations export file is chosen via the menu.
	 */
	public void setCalculationExportListener(FileActionListener fal) {
		exportCalculationsListener = fal;
	}

	/*
	 * Sets the listener which handles when airport save file is chosen via the menu.
	 */
	public void setAirportSaveListener(FileActionListener fal) {
		saveAirportListener = fal;
	}

	/*
	 * Sets the listener which handles when the obstacle save file is chosen via the menu.
	 */
	public void setObstacleSaveListener(FileActionListener fal) {
		saveObstacleListener = fal;
	}

	/*
	 * Sets the listener which handles when the airport open file is chosen via the menu.
	 */
	public void setAirportOpenListener(FileActionListener fal) {
		openAirportListener = fal;
	}

	/*
	 * Sets the listener which handels removal of the obstacle 
	 * */
	public void setRemoveObstacleListener(ActionListener al){
		removeObstacle.addActionListener(al);
		removeObstalce.addActionListener(al);
	}
	
	/*
	 * Sets the listener which handles when the obstacle open file is chosen via the menu.
	 */
	public void setObstacleOpenListener(FileActionListener fal) {
		openObstacleListener = fal;
	}


	/*
	 * Sets the listener which handles when a runway is selected via the drop-down menu.
	 */
	public void setLogicalRunwaySelectionListener(ActionListener al) {
		currentRunway.addActionListener(al);
	}
	
	/*
	 * Sets the listener for creation of the new airport
	 * */
	public void setCreateNewAirportListener(ActionListener ls){
		newAirport.addActionListener(ls);
	}
	
	/*
	 * Sets the listener for creation of the editin of the airport
	 * */
	public void setEditAirportListener(ActionListener al){
		editAirport.addActionListener(al);
	}
	
	/*
	 * Sets the listener for the manual
	 * */
	public void setManualListener(ActionListener al){
		manual.addActionListener(al);
	}
	
	/*
	 * Sets the listener for editing of the current obstacle.
	 * */
	public void setEditObstacleListener(ActionListener al){
		editObstacle.addActionListener(al);
	}


	/*----------------------------------------------------------------------------------
	 *------------------------  GUI Public Update Methods   ----------------------------
	 ---------------------------------------------------------------------------------*/

	/*
	 * Updates the values stored in the results table.
	 * This should only be called from within the EDT.
	 */
	public void updateResultsTable(Float tora, Float toda, Float asda, Float lda) {
		TableModel redeclaredValues = this.redeclaredValuesTable.getModel();
		redeclaredValues.setValueAt(tora.toString(), 0, 1);
		redeclaredValues.setValueAt(toda.toString(), 1, 1);
		redeclaredValues.setValueAt(asda.toString(), 2, 1);
		redeclaredValues.setValueAt(lda.toString(), 3, 1);
	}

	/*
	 * Updates the values stored in the default runway parameters table.
	 * This should only be called from within the EDT.
	 */
	public void setRunwayParameters(Float tora, Float toda, Float asda,
			Float lda, Float displaced) {
		TableModel originalValues = this.originalValuesTable.getModel();
		originalValues.setValueAt(tora.toString(), 0, 1);
		originalValues.setValueAt(toda.toString(), 1, 1);
		originalValues.setValueAt(asda.toString(), 2, 1);
		originalValues.setValueAt(lda.toString(), 3, 1);
		originalValues.setValueAt(displaced.toString(), 4, 1);

	}

	/*
	 * Updates the contents of the calculations area.
	 * This should only be called from within the EDT.
	 */
	public void updateCalulationsTextArea(String text) {
		this.calculationsArea.setText(text);
	}

	/*
	 * Updates the values stored in the obstacle table.
	 * This should only be called from within the EDT.
	 */
	public void setObstacleParameters(Float height, Float distanceLow,
			Float distanceHigh, Float distCent) {
		TableModel obVals = this.obstacleValuesTable.getModel();
		obVals.setValueAt(height.toString(), 0, 1);
		obVals.setValueAt(distanceLow.toString(), 1, 1);
		obVals.setValueAt(distanceHigh.toString(), 2, 1);
		obVals.setValueAt(distCent.toString(), 3, 1);
	}

	/*
	 * Updates the runway designators in the drop-down menu.
	 * This should only be called from within the EDT.
	 */
	public void setLogicalRunways(String[] designators, int selectedIndex) {
		ComboBoxModel<String> comboModel = new DefaultComboBoxModel<>(
				designators);
		currentRunway.setModel(comboModel);
		currentRunway.setSelectedIndex(selectedIndex);
	}

	/*
	 * Updates the airport name.
	 * This should only be called from within the EDT.
	 */
	public void setAirportName(String name) {
		currentAirport.setText(name);
	}
	
	/*
	 * Updates the values stored in the calculation variables table.
	 * This should only be called from within the EDT.
	 */
	public void setCalculationParameters(Float blastProtection, Integer slopeRatio) {
		TableModel cpVals = this.calculationParamsTable.getModel();
		cpVals.setValueAt(blastProtection.toString(), 0, 1);
		cpVals.setValueAt(slopeRatio.toString(), 1, 1);
	}

	/*----------------------------------------------------------------------------------
	 *------------------------  GUI Public Access Methods   ----------------------------
	 ---------------------------------------------------------------------------------*/

	/*
	 * Gets the default runway parameters in the form of an array.
	 * The indexes use are described in GuiConstants.
	 * Throws a GuiException if a parameter is poorly formatted.
	 */
	public float[] getRunwayParameters() throws GuiException {
		float[] originalParams = new float[5];
		if( originalValuesTable.isEditing())
			originalValuesTable.getCellEditor().stopCellEditing();
		TableModel originalValues = this.originalValuesTable.getModel();
		// Attempt to convert TORA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_TORA] = Float
					.parseFloat((String) originalValues.getValueAt(0, 1));
		} catch (NumberFormatException e) {
			throw new GuiException(
					"The TORA value input is not a valid number!");
		}
		// Attempt to convert TODA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_TODA] = Float
					.parseFloat((String) originalValues.getValueAt(1, 1));
		} catch (NumberFormatException e) {
			throw new GuiException(
					"The TODA value input is not a valid number!");
		}
		// Attempt to convert ASDA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_ASDA] = Float
					.parseFloat((String) originalValues.getValueAt(2, 1));
		} catch (NumberFormatException e) {
			throw new GuiException(
					"The ASDA value input is not a valid number!");
		}
		// Attempt to convert LDA text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_LDA] = Float
					.parseFloat((String) originalValues.getValueAt(3, 1));
		} catch (NumberFormatException e) {
			throw new GuiException("The LDA value input is not a valid number!");
		}
		// Attempt to convert displacement threshold text to float.
		try {
			originalParams[GuiConstants.DATA_PROPERTY_THR] = Float
					.parseFloat((String) originalValues.getValueAt(4, 1));
		} catch (NumberFormatException e) {
			throw new GuiException("The landing threshold value input is not a valid number!");
		}

		return originalParams;
	}

	/*
	 * Gets the obstacle parameters in the form of an array.
	 * The indexes use are described in GuiConstants.
	 * Throws a GuiException if a parameter is poorly formatted.
	 */
	public float[] getObstacleParameters() throws GuiException {
		TableModel obVals = this.obstacleValuesTable.getModel();
		if( obstacleValuesTable.isEditing())
			obstacleValuesTable.getCellEditor().stopCellEditing();
		float[] params = new float[4];
		// Attempt to convert height text to float.
		try {
			params[GuiConstants.DATA_OBSTACLE_HEIGHT] = Float
					.parseFloat((String) obVals.getValueAt(0, 1));
		} catch (NumberFormatException e) {
			throw new GuiException("The obstacle height value input is not a valid number!");
		}

		// Attempt to convert height text to float.
		try {
			params[GuiConstants.DATA_OBSTACLE_DISP_LOW] = Float
					.parseFloat((String) obVals.getValueAt(1, 1));
		} catch (NumberFormatException e) {
			throw new GuiException("The obstacle displacement from lowest threshold value input is not a valid number!");
		}

		// Attempt to convert height text to float.
		try {
			params[GuiConstants.DATA_OBSTACLE_DISP_HIGH] = Float
					.parseFloat((String) obVals.getValueAt(2, 1));
		} catch (NumberFormatException e) {
			throw new GuiException("The obstacle displacement from highest threshold value input is not a valid number!");
		}

		// Attempt to convert height text to float.
		try {
			params[GuiConstants.DATA_OBSTACLE_DISP_CENTER] = Float
					.parseFloat((String) obVals.getValueAt(3, 1));
		} catch (NumberFormatException e) {
			throw new GuiException("The obstacle centerline displacement value input is not a valid number!");
		}
		return params;
	}
	/*
	 * Gets the calculation parameters in the form of an array.
	 * The indexes use are described in GuiConstants.
	 * Throws a GuiException if a parameter is poorly formatted.
	 */
	public float[] getCalculationParameters() throws GuiException {
		TableModel obVals = this.calculationParamsTable.getModel();
		if( calculationParamsTable.isEditing())
			calculationParamsTable.getCellEditor().stopCellEditing();
		float[] params = new float[2];
		// Attempt to convert height text to float.
		try {
			params[GuiConstants.DATA_BLAST_PROTECTION] = Float
					.parseFloat((String) obVals.getValueAt(0, 1));
		} catch (NumberFormatException e) {
			throw new GuiException("The blast protection input is not a valid number!");
		}

		// Attempt to convert height text to float.
		try {
			params[GuiConstants.DATA_SLOPE_RATIO] = Integer
					.parseInt((String) obVals.getValueAt(1, 1));
		} catch (NumberFormatException e) {
			throw new GuiException("The slope ratio value input is not a valid number!");
		}

		return params;
	}
	
	
	/*
	 * Gets the selected logical runway in the drop-down menu.
	 */
	public String getSelectedLogicalRunway() {
		return (String) currentRunway.getSelectedItem();
	}
	
	/*
	 * If needed, formats the obstacle panel to not contain an obstacle.
	 */
	public void performRemovalOfObstacle(){
		if( this.obstacleExists == true ){
			this.obstacleExists=false;
			obstacleInfoIfDoesntExist();
		}
	}
	
	/*
	 * If needed, formats the obstacle panel to contain an obstacle.
	 */
	public void performAddingOfTheObstacle(){
		if( this.obstacleExists == false ){
			this.obstacleExists=true;
			obstacleInfoIfExists();
		}
	}

	
	/*
	 * 
	 * Gets the panel responsible for creating new airport
	 * 
	 * */
	public AirportDialog openCreationOfNewAirport(){
		return this.addAiport= new AirportDialog(MainFrame.this, "New airport", false);
	}
	
	public AirportDialog getCreationOfNewAirportDialog(){
		return this.addAiport;
	}
	
	public AirportDialog openEditingOfAirport(){
		return this.editingAirport=new AirportDialog(MainFrame.this, "Edit airport", false);
	}
	
	
	public ObstacleDialog openEditingOfObstacle(){
		return this.editObstacleDialog = new ObstacleDialog(this,"Edit Obstale",true,false);
	}
	
	/*
	 * Gets the frame responsible for creating of new obstacle
	 * 
	 * */
//	public NewAirporFrame getAddNewObstacleFrame(){
//		return this.addObstacleFrame;
//	}

	public class SelectAllTable extends JTable
	{
		public void changeSelection(int row, int column, boolean toggle, boolean extend) {
			super.changeSelection(row, column, toggle, extend);

			if (editCellAt(row, column)) {
				Component editor = getEditorComponent();
				editor.requestFocusInWindow();
				((JTextComponent)editor).selectAll();
			}
		}
	}




}
