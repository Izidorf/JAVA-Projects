package airproject.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.SwingUtilities;

import airproject.model.Airport;
import airproject.model.Model;
import airproject.model.Obstacle;
import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;
import airproject.model.RunwayResults;
import airproject.model.RunwaySide;
import airproject.model.calculations.CalculationHistory;
import airproject.model.io.Logger;
import airproject.model.io.XMLIO;
import airproject.view.FileActionListener;
import airproject.view.GuiConstants;
import airproject.view.GuiException;
import airproject.view.ObstacleDialog;
import airproject.view.UserManualFrame;
import airproject.view.View;

/*
 * Encapsulates the controller module of our project
 */
public class Controller {

	// The model and the view.
	Model model;
	View view;

	// The top-down view controller logic.
	TopDownController topDownController;
	SideOnController sideOnController;

	AirportDialogController newAirportController;
	AirportDialogController editAirportController;
	NewObstacleController newObstacleController;
	EditObstacleController editObstacleController;

	// The current runway being displayed.
	private Runway currentRunway;
	// The current designator focused on.
	private RunwayDesignator currentDesignator;
	// The calculation parameters.
	private int slopeRatio = 50;
	private float blastProtection = 300f;
	// The calculation history of last calculation performed.
	private CalculationHistory calcHistory;
	private RunwayResults lastResults;

	public Controller(View view) {
		model = Model.getInstance();
		this.view = view;
		// create control modules
		topDownController = new TopDownController(view.getTopDownView());
		sideOnController = new SideOnController(view.getSideOnView());

		newAirportController = new NewAirportController(view,this);
		editAirportController = new EditAirportController(view, this);
		editObstacleController = new EditObstacleController(view, this);

		// Initialise 
		init();
		// Link action listeners to view.
		view.getFrame().setUpdateOriginalButtonActionListener(dirPropertiesUpdateListener);
		view.getFrame().setUpdateObstacleButtonActionListener(obstaclePropertiesUpdateListener);
		view.getFrame().setLogicalRunwaySelectionListener(logicalRunwayUpdateListener);
		view.getFrame().setCalculationPropertiesButtonActionListener(calculationPropertiesUpdateListener);
		
		view.getFrame().setCalculationExportListener(exportCalculationsListener);
		view.getFrame().setAirportSaveListener(saveAirportListener);
		view.getFrame().setObstacleSaveListener(saveObstacleListener);
		view.getFrame().setAirportOpenListener(openAirportListener);
		view.getFrame().setObstacleOpenListener(openObstacleListener);
		view.getFrame().setRemoveObstacleListener(removeObstacleListener);
		view.getFrame().setAddNewObstacleListener(createNewObstacle);

		view.getFrame().setChoosePlainColorActionListener(selectColouringListener);
		view.getFrame().setChooseTexturesAsColorActionListener(selectTexturingListener);
		
		view.getFrame().setManualListener(manualListener);

		view.getFrame().addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				super.windowClosing(arg0);
				Logger.log("Application closed.", Logger.MessageType.START_STOP);
			}

		});

		Logger.log("Application started.", Logger.MessageType.START_STOP);
	}

	//Initialises the interface.
	private void init() {
		Airport airport = new Airport("Southampton", "SOU");
		model.setAirport(airport);// Create runway 09R/27L
		Runway runway = new Runway(new RunwayDesignator(9, RunwaySide.NONE),
				new RunwayDirectionalProperties(307f, 3660.0f, 3660.0f,
						3660.0f, 3353.0f, 240.0f), // Logical representation of
						// 09R
						new RunwayDirectionalProperties(0.0f, 3660.0f, 3660.0f,
								3660.0f, 3660.0f, 240.0f)); // Logical representation of
		// 27L
		Obstacle obstacle = new Obstacle(12f, -50f, 3646f, 0f);
		runway.setObstacle(obstacle);
		airport.addRunway(runway);
		currentRunway = runway;
		currentDesignator = runway.getLowestEquivalentDesignator();
		// view update
		displayCurrentDefaults();
		updateDesignatorList();
		displayAirportName();
		displayCurrentObstacle();
		recalculateAndDisplay();
		// Display the calculation parameters.
		view.getFrame().setCalculationParameters(blastProtection, slopeRatio);
	}

	/*
	 * Displays the airport details in the GUI.
	 */
	private void displayAirportName() {
		// Update GUI in EDT.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Sets the airport name on screen.
				view.getFrame().setAirportName(model.getAirport().getName());
			}
		});
	}

	/*
	 * Displays the current default properties in the GUI.
	 */
	private void displayCurrentDefaults() {
		// Update GUI in EDT.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Gets the directional properties in the direction selected.
				RunwayDirectionalProperties properties = currentRunway
						.getDirectionalProperties(currentDesignator);
				// Set runway's parameters in the side bar.
				view.getFrame().setRunwayParameters(properties.getDefaultTORA(),
						properties.getDefaultTODA(), properties.getDefaultASDA(),
						properties.getDefaultLDA(),
						properties.getDefaultDisplacementThreshold());
			}
		});
	}

	/*
	 * Recalculated relevant properties and displays them in the GUI.
	 */
	public void recalculateAndDisplay() {
		// Recalculate the results.
		final RunwayResults results = currentRunway.recalculate(currentDesignator,
				blastProtection, slopeRatio);// TODO: get rid of these hard coded vals
		lastResults = results;
		// Update GUI in EDT.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Update the results table and calculations text area.
				view.getFrame().updateResultsTable(results.getRedeclaredTORA(),
						results.getRedeclaredTODA(), results.getRedeclaredASDA(),
						results.getRedeclaredLDA());
				view.getFrame().updateCalulationsTextArea(
						results.getCalculationHistory().buildString());
			}
		});
		// Notify the top-down controller that an update has occured.
		topDownController.updateHasOccured(currentRunway, currentDesignator, results);
		sideOnController.updateHasOccured(currentRunway, currentDesignator, results);
		// Set the history.
		calcHistory = results.getCalculationHistory();
	}

	/*
	 * Displays the current obstacle properties in the GUI.
	 */
	public void displayCurrentObstacle() {
		if( !currentRunway.obstacleExits()){
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					// Perform the removal of the obstacle information if needed.
					view.getFrame().performRemovalOfObstacle();
				}
			});
		}else{
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					// Get the current obstacle.
					Obstacle obstacle = currentRunway.getObstacle();
					// Add the obstacle table if needed.
					view.getFrame().performAddingOfTheObstacle();
					// Set the current obstacles properties.
					view.getFrame().setObstacleParameters(obstacle.getHeight(), 
							obstacle.getDisplacementFromLowestTreshold(), 
							obstacle.getDisplacementFromHighestThreshold(), 
							obstacle.getDisplacementFromCenterline());
				}
			});
		}
	}

	/*
	 * Updates the airport's designators in the drop-down menu.
	 */
	private void updateDesignatorList() {
		final String[] desig = new String[2 * model.getAirport()
		                                  .getRunwayCount()];
		// Iterate through all of the airports designators.
		int i = 0;
		for (Runway runway : model.getAirport()) {
			// Add the pair of designator's for each runway.
			desig[i] = runway.getLowestEquivalentDesignator().toString();
			desig[i + 1] = runway.getHighestEquivalentDesignator().toString();
			i += 2;
		}
		// Update GUI in EDT.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Update the obstacle table.
				view.getFrame().setLogicalRunways(desig, 0);
			}
		});
	}



	/*
	 * Direction properties update listener:
	 * This is invoked whenever the default runway properties are updated.
	 */
	ActionListener dirPropertiesUpdateListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			float[] directionalProperties = null;
			// Attempt to get runway parameters from GUI.
			try {
				directionalProperties = view.getFrame().getRunwayParameters();
			} catch (GuiException e1) {
				view.getFrame().displayError(e1);
				return;
			}
			// Extract the Properties.
			float TORA = directionalProperties[GuiConstants.DATA_PROPERTY_TORA];
			float TODA = directionalProperties[GuiConstants.DATA_PROPERTY_TODA];
			float ASDA = directionalProperties[GuiConstants.DATA_PROPERTY_ASDA];
			float LDA = directionalProperties[GuiConstants.DATA_PROPERTY_LDA];
			float THR = directionalProperties[GuiConstants.DATA_PROPERTY_THR];
			// Range checks.
			if (TORA <= 0f) {
				view.getFrame().displayError(
						new Exception("TORA must be above zero!"));
				return;
			}
			if (TODA <= 0f) {
				view.getFrame().displayError(
						new Exception("TODA must be above zero!"));
				return;
			}
			if (ASDA <= 0f) {
				view.getFrame().displayError(
						new Exception("ASDA must be above zero!"));
				return;
			}
			if (LDA <= 0f) {
				view.getFrame().displayError(
						new Exception("LDA must be above zero!"));
				return;
			}
			if (THR < 0f) {
				view.getFrame().displayError(
						new Exception("Landing threshold must be non-negative!"));
				return;
			}
			if( TORA > TODA){
				view.getFrame().displayError(
						new Exception("TORA cannot be greater than TODA (negative stopway)!"));
				return;
			}
			if( TORA > ASDA){
				view.getFrame().displayError(
						new Exception("TORA cannot be greater than ASDA (negative clearway)!"));
				return;
			}
			if( ASDA > TODA){
				view.getFrame().displayError(
						new Exception("ASDA cannot be greater than TODA!"));
				return;
			}
			if( LDA > TORA){
				view.getFrame().displayError(
						new Exception("LDA cannot be greater than TORA!"));
				return;
			}
			// Update the model
			RunwayDirectionalProperties properties = currentRunway
					.getDirectionalProperties(currentDesignator);
			properties.setDefaultTORA(TORA);
			properties.setDefaultTODA(TODA);
			properties.setDefaultASDA(ASDA);
			properties.setDefaultLDA(LDA);
			properties.setDefaultDisplacementThreshold(THR);
			// Recalculate and display.
			recalculateAndDisplay();

			String logString = "Runway directional properties updated for designator "+currentDesignator.toString()+":$";
			logString += String.format("TORA = %.2fm$", TORA);
			logString += String.format("TODA = %.2fm$", TODA);
			logString += String.format("ADSA = %.2fm$", ASDA);
			logString += String.format("LDA = %.2fm", LDA);
			Logger.log(logString, Logger.MessageType.RUNWAY);
		}
	};




	/*
	 * Obstacle properties update listener:
	 * This is invoked whenever the obstacle properties are updated.
	 */
	ActionListener obstaclePropertiesUpdateListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			float obstacleProperties[] = null;
			// Attempt to get the obstacle properties from GUI.
			try {
				obstacleProperties = view.getFrame().getObstacleParameters();
			} catch (GuiException e) {
				view.getFrame().displayError(e);
				return;
			}
			// Extract properties.
			float height = obstacleProperties[GuiConstants.DATA_OBSTACLE_HEIGHT];
			float dispFromLowTHR = obstacleProperties[GuiConstants.DATA_OBSTACLE_DISP_LOW];
			float dispFromHighTHR = obstacleProperties[GuiConstants.DATA_OBSTACLE_DISP_HIGH];
			float dispFromCenter = obstacleProperties[GuiConstants.DATA_OBSTACLE_DISP_CENTER];
			// Range checks.
			if (height <= 0f) {
				view.getFrame().displayError(
						new Exception("Obstacle height must be above zero!"));
				return;
			}
			// Update the model
			Obstacle obs = currentRunway.getObstacle();
			if (obs == null) {
				obs = new Obstacle(height, dispFromLowTHR, dispFromHighTHR,
						dispFromCenter);
				currentRunway.setObstacle(obs);
			}
			obs.setDisplacementFromCenterline(dispFromCenter);
			obs.setDisplacementFromThresholds(dispFromLowTHR, dispFromHighTHR);
			obs.setHeight(height);
			// Recalculate the results.
			recalculateAndDisplay();
			displayCurrentObstacle();
			String logString = "Obstacle properties updated.$";
			logString += String.format("Height = %.2fm$", height);
			logString += String.format("Displacement from low threshold = %.2fm$", dispFromLowTHR);
			logString += String.format("Displacement from high threshold = %.2fm$", dispFromHighTHR);
			logString += String.format("Displacement from centerline = %.2fm", dispFromCenter);
			Logger.log(logString, Logger.MessageType.OBSTACLE);
		}
	};
	
	
	/*
	 * Calculation properties update listener:
	 * This is invoked whenever the calculation properties are updated.
	 */
	ActionListener calculationPropertiesUpdateListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			float calcProperties[] = null;
			// Attempt to get the obstacle properties from GUI.
			try {
				calcProperties = view.getFrame().getCalculationParameters();
			} catch (GuiException e) {
				view.getFrame().displayError(e);
				return;
			}
			// Extract properties.
			float blast = calcProperties[GuiConstants.DATA_BLAST_PROTECTION];
			int slope = (int)calcProperties[GuiConstants.DATA_SLOPE_RATIO];
			// Range checks.
			if (blast < 0f) {
				view.getFrame().displayError(
						new Exception("Blast radius cannot be negative!"));
				return;
			}
			if( slope <= 0f){
				view.getFrame().displayError(
						new Exception("Slope ratio must be a positive integer!"));
				return;
			}
			blastProtection = blast;
			slopeRatio = slope;
			// Recalculate the results.
			recalculateAndDisplay();
			displayCurrentObstacle();
			String logString = String.format("Calculation properties updated.$Blast protection = %.2fm.$Slope ratio= %d", blastProtection, slope);
			Logger.log(logString, Logger.MessageType.CALCULATION);
		}
	};

	/*
	 * Logical runway update listener:
	 * This is invoked whenever a runway designator is chosen from the drop-down.
	 */
	ActionListener logicalRunwayUpdateListener = new ActionListener() {
		private boolean isFirst = true;

		@Override
		public void actionPerformed(ActionEvent e) {
			// Get the selected designator string.
			String selectedString = view.getFrame().getSelectedLogicalRunway();
			// Convert to a designator object.
			RunwayDesignator designator = RunwayDesignator
					.parseDesignator(selectedString);
			// Set the current designator.
			currentDesignator = designator;
			for( Runway runway : model.getAirport()){
				if( runway.getLowestEquivalentDesignator().equals(designator)
						|| runway.getHighestEquivalentDesignator().equals(designator)){
					currentRunway = runway;
				}
			}
			// Update to display.
			displayCurrentDefaults();
			recalculateAndDisplay();
			displayCurrentObstacle();

			if (isFirst) {
				isFirst = false;
			} else {
				Logger.log(String.format("Runway switched to %s.", selectedString), Logger.MessageType.RUNWAY_SWITCH);
			}
			
		}
	};


	/*
	 * Handels removing the obstacle and updating the GUI
	 * */
	ActionListener removeObstacleListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent event) {
			System.out.println("Removing obstacle");
			currentRunway.deleateObstacle();
			view.getFrame().performRemovalOfObstacle();
			displayCurrentObstacle();
			recalculateAndDisplay();
			Logger.log("Obstacle removed from runway.", Logger.MessageType.OBSTACLE);
		}
	};

	/*
	 * Handles changing to the colouring mode.
	 * */
	ActionListener selectColouringListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {

			view.getTopDownView().setUsingTextures(false);
			view.getSideOnView().setUsingTextures(false);
			recalculateAndDisplay();

			Logger.log("View changed to COLOUR mode.", Logger.MessageType.VIEW);
		}
	};

	/*
	 * Handles changing to texturing mode.
	 */
	ActionListener selectTexturingListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			view.getTopDownView().setUsingTextures(true);
			view.getSideOnView().setUsingTextures(true);
			recalculateAndDisplay();

			Logger.log("View changed to TEXTURE mode", Logger.MessageType.VIEW);
		}
	};



	/*
	 * Handles Creation of new Obstacle
	 * */

	ActionListener createNewObstacle = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Creating new Obstacle");

			if(!currentRunway.obstacleExits()){
				//create newObstacleDialog in the main frame
				ObstacleDialog newObstacleDialog = new ObstacleDialog(view.getFrame(), "New Obstale",true,false);
				System.out.println("This gets executed when Jdialog is closed");
				NewObstacleController noc = new NewObstacleController(newObstacleDialog, getController());
				newObstacleDialog.setVisible(true);
				System.out.println("Creating new Obstacle");
			} else {

				view.getFrame().displayError(
						new Exception("Obstacle already Exists! Remove it first."));
				return;
			}

		}

	};


	/*
	 * Handles exporting calculations to file.
	 */
	FileActionListener exportCalculationsListener = new FileActionListener() {

		@Override
		public void actionPerformed(File file) {
			// Attempt to save the results to a file.
			try {
				calcHistory.saveToFile(file);
				Logger.log("Calculations exported to " + file.getAbsolutePath(), Logger.MessageType.IMPORT_EXPORT);
			} catch (FileNotFoundException e) {
				view.getFrame().displayError(e);
			}
		}
	};

	/*
	 * Handles saving airport to file.
	 */
	FileActionListener saveAirportListener = new FileActionListener() {

		@Override
		public void actionPerformed(File file) {
			// Get current airpot.
			Airport airport = model.getAirport();
			// Write it to the given file.
			if (!file.getName().toLowerCase().endsWith(".xml"))
				file = new File(file.getAbsolutePath() + ".xml");
			XMLIO.writeXml(Airport.class, airport, file);
			Logger.log("Exported Airport XML to " + file.getAbsolutePath(), Logger.MessageType.IMPORT_EXPORT);
		}
	};

	/*
	 * Handles saving airport to file.
	 */
	FileActionListener saveObstacleListener = new FileActionListener() {

		@Override
		public void actionPerformed(File file) {
			// Get obstacle of current runway.
			Obstacle obs = currentRunway.getObstacle();
			// Write it to the given file.
			if (!file.getName().toLowerCase().endsWith(".xml"))
				file = new File(file.getAbsolutePath() + ".xml");
			XMLIO.writeXml(Obstacle.class, obs, file);
			Logger.log("Exported Obstacle XML to " + file.getAbsolutePath(), Logger.MessageType.IMPORT_EXPORT);
		}
	};

	/*
	 * Handles loading airport from file.
	 */
	FileActionListener openAirportListener = new FileActionListener() {

		@Override
		public void actionPerformed(File file) {
			// Read airport object from file.
			try{
				model.loadAirport(file);
			}catch(Exception e){
				view.getFrame().displayError(new Exception("Invalid xml file!"));
				return;
			}
			// Set current runway and designator.
			currentRunway = model.getAirport().iterator().next();
			currentDesignator = currentRunway.getLowestEquivalentDesignator();
			// Updates the view.
			displayCurrentDefaults();
			updateDesignatorList();
			displayAirportName();
			displayCurrentObstacle();
			recalculateAndDisplay();
			Logger.log("Airport loaded from XML file " + file.getAbsolutePath(), Logger.MessageType.IMPORT_EXPORT);
		}
	};

	/*
	 * Handles loading airport from file.
	 */
	FileActionListener openObstacleListener = new FileActionListener() {

		@Override
		public void actionPerformed(File file) {
			// Read obstacle object from file.
			Obstacle obs = null;
			try{
				obs = XMLIO.readXml(Obstacle.class, file);
			}catch(Exception e){
				view.getFrame().displayError(new Exception("Invalid xml file!"));
				return;
			}
			// Adds this obstacle to the runway.
			currentRunway.setObstacle(obs);
			view.getFrame().performAddingOfTheObstacle();
			view.getFrame().setUpdateObstacleButtonActionListener(
					obstaclePropertiesUpdateListener);
			view.getFrame().setRemoveObstacleListener(removeObstacleListener);
			// Updates the view.
			displayCurrentObstacle();
			recalculateAndDisplay();
			Logger.log("Obstacle loaded from XML file " + file.getAbsolutePath(), Logger.MessageType.IMPORT_EXPORT);
		}
	};



	
	/*
	 * Listener to open manual
	 * */
	ActionListener manualListener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			new UserManualFrame();
		}
		
		
	};
	
	private Controller getController(){
		return this;
	}

	/*
	 * Getters which allow other classes of the controller to reuse action listeners
	 *  from the main Controller class
	 */

	public Runway getCurrentRunway(){
		return currentRunway;
	}

	public ActionListener getUpdateObstacleListener(){
		return obstaclePropertiesUpdateListener;
	}

	public ActionListener getRemoveObstacleListener(){
		return removeObstacleListener;
	}


	/*
	 * Updates Controllers Current Runway and Designator to the one chosen in the GUI
	 * */

	public void updateCurrentRunwayAndDesignator(){
		// Get the selected designator string.
		String selectedString = view.getFrame().getSelectedLogicalRunway();
		// Convert to a designator object.
		RunwayDesignator designator = RunwayDesignator
				.parseDesignator(selectedString);
		// Set the current designator.
		currentDesignator = designator;
		for( Runway runway : model.getAirport()){
			if( runway.getLowestEquivalentDesignator().equals(designator)
					|| runway.getHighestEquivalentDesignator().equals(designator)){
				currentRunway = runway;
			}
		}
	}

	public void updateMainFrame(){

		updateCurrentRunwayAndDesignator();

		currentRunway.deleateObstacle();
		view.getFrame().performRemovalOfObstacle();
		displayCurrentDefaults();
		updateDesignatorList();
		displayAirportName();
		displayCurrentObstacle();
		recalculateAndDisplay();
	}


}
