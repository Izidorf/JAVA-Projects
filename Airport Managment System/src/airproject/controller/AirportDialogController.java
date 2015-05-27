package airproject.controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.omg.CORBA.DATA_CONVERSION;

import airproject.model.Airport;
import airproject.model.Model;
import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;
import airproject.model.RunwaySide;
import airproject.view.*;

public class AirportDialogController {
	
	Model model;
	View view;
	Controller control; //needed so that its method can be reused
	
	//Controlled GUI Panels
	AirportDialog airportDialog;
	RunwayDialog newRunwayDialog;
	
	
	String selectedRunwayKey;
	
	//ListiOfRunways
	Map<String , Runway> runways= new HashMap<String , Runway>(); //String designator - K
	
	
	public AirportDialogController(View view, Controller c){
		this.model= Model.getInstance();
		this.view = view;
		this.control=c;
		
		

	}
	
	
	/*---------------------------------------------------------------------------------
	 *------------------------  NewAirpotDialog Listeners   ---------------------------
	 --------------------------------------------------------------------------------*/
	
	/*
	 * Listener for OK button (File >> New >> Airport  >> OK)
	 * If clicked creates a new Airport, sets its Runways and updates the View
	 * */
	ActionListener createNewAirport = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Creating new Airport");
						
			//Get Airport
			String[] airportParams = airportDialog.getAiportParameters();
			//Sanitize input
			//Check the name is not empty
			if(airportParams[GuiConstants.DATA_AIRPORT_NAME].equals("")){
				view.getFrame().displayError(new GuiException("Airport name can not be blank"));
				return;
			} else if(airportParams[GuiConstants.DATA_AIRPORT_CODE].equals("")){
				view.getFrame().displayError(new GuiException("Every airport needs to have a code"));
				return;
			}
			
			//Check that airport has at least one runway
			if(runways.keySet().size()==0){
				view.getFrame().displayError(new GuiException("The airport needs to have at least one runway"));
				return;
			}
			
			model.setAirport(
					new Airport(
							airportParams[GuiConstants.DATA_AIRPORT_NAME],
							airportParams[GuiConstants.DATA_AIRPORT_CODE]));
			
			
			
			//set runways - They are stored in the HashMap
			for(String key : runways.keySet())
				model.getAirport().addRunway(runways.get(key));
		
			//close the airportDialog
			airportDialog.dispose();
			
			//Update the main GUI
			control.updateMainFrame();
			
		}
		
	};
	
	/*
	 * Listener for Add button (File >> New >> Airport  >> Add)
	 * This method open new NewRunwayDialog window
	 * */
	ActionListener openNewRunwayDialogPanel = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Oppening Add Runway Dialog");
			//Create new NewRunwayDialog
			newRunwayDialog = airportDialog.openCreationOfNewRunway();
			
			//Add listeners to it
			newRunwayDialog.setAddRunwayListener(addRunwayListener);
			
			//Make it visible
			newRunwayDialog.setVisible(true);
			
			
			
		}
		
	};
	
	/*
	 * Listener for Edit button (File >> New >> Airport  >> Edit)
	 * */
	ActionListener editSelectedRunway = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Editing Runway");
			
			//Check if any runways are selected
			if(airportDialog.getIsElementSelectedJList()){
			view.getFrame().displayError(new GuiException("No runway is selected."));
			return;
			}
			
			//Get the selected runway from the hasmap
			selectedRunwayKey= airportDialog.getSelectedElementJList();
			Runway runway = runways.get(selectedRunwayKey);

			//Open editRunwayDialog
			newRunwayDialog = new RunwayDialog(airportDialog, "Edit Runway", false);
			
			//Attach listeners
			newRunwayDialog.setAddRunwayListener(editOKRunwayListener);
			
			//Get the runway data from the HasmMap		
				float[] generalParams = new float[3];
				generalParams[GuiConstants.DATA_PROPERTY_DIS_CLEARED]=runway.getDistCleared();
				generalParams[GuiConstants.DATA_PROPERTY_DIS_GRADED]=runway.getDistGraded();
				generalParams[GuiConstants.DATA_PROPERTY_WIDTH]=runway.getWidth();
				
				
				RunwayDirectionalProperties highDirectional=runway.getHighDirectionalProperties();
				float[] highRunway = new float[6];
				highRunway[GuiConstants.DATA_PROPERTY_THR] = highDirectional.getDefaultDisplacementThreshold();
				highRunway[GuiConstants.DATA_PROPERTY_TORA] = highDirectional.getDefaultTORA();
				highRunway[GuiConstants.DATA_PROPERTY_TODA] = highDirectional.getDefaultTODA();
				highRunway[GuiConstants.DATA_PROPERTY_ASDA] = highDirectional.getDefaultASDA();
				highRunway[GuiConstants.DATA_PROPERTY_LDA] = highDirectional.getDefaultLDA();
				highRunway[GuiConstants.DATA_PROPERTY_RESA] = highDirectional.getRESA();
				
				RunwayDirectionalProperties lowDirectional=runway.getLowDirectionalProperties();
				float[] lowRunway = new float[6];
				lowRunway[GuiConstants.DATA_PROPERTY_THR] = lowDirectional.getDefaultDisplacementThreshold();
				lowRunway[GuiConstants.DATA_PROPERTY_TORA] = lowDirectional.getDefaultTORA();
				lowRunway[GuiConstants.DATA_PROPERTY_TODA] = lowDirectional.getDefaultTODA();
				lowRunway[GuiConstants.DATA_PROPERTY_ASDA] = lowDirectional.getDefaultASDA();
				lowRunway[GuiConstants.DATA_PROPERTY_LDA] = lowDirectional.getDefaultLDA();
				lowRunway[GuiConstants.DATA_PROPERTY_RESA] = lowDirectional.getRESA();
			
		
				
			//Fill the NewRunwayDialog with data	
			newRunwayDialog.setRunwayDesignator(runway.getLowestEquivalentDesignator().getAngle(), 
												runway.getLowestEquivalentDesignator().getSide().toString());
			newRunwayDialog.setGeneralRunwayParameters(generalParams);
			newRunwayDialog.setTopRunwayParameters(lowRunway);
			newRunwayDialog.setBottomRunwayParameters(highRunway);
			
			
			
			newRunwayDialog.setVisible(true);
			
		}
		
	};
	
	public boolean isNumberPositive(float n){
		if (n>=0)
				return true;
			else
				return false;
	}
	
	
	/*
	 * Listener for Remove button (File >> New >> Airport  >> Remove)
	 * */
	ActionListener removeSelectedRunway = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Removing Selected Runway");
			
			String keyFromJList;
			//if no runway is selected then display warning
			if(airportDialog.getIsElementSelectedJList()){
				view.getFrame().displayError(new GuiException("No runway is selected."));
				return;
			} else {
				//Get runway designator from GUI and deleate corresponding value in the HashMap
				keyFromJList = airportDialog.getSelectedElementJList();
				
				runways.remove(keyFromJList);
			}
			
			//Update the JList of Runways in the airportDialog
			updateAirportDialogRunwaysList();
			
		}
		
	};
	
	/*---------------------------------------------------------------------------------
	 *------------------------  NewRunwayDialog Listeners   ---------------------------
	 --------------------------------------------------------------------------------*/
	/*
	 * Listener for OK button (File >> New >> Airport >> Add >> OK)
	 * This method creates a new Runway, stores in in the HashMap in this class and updates the JList of runways
	 * The Hashmap stores the Default Runway Designator as the Key, and Runway object as Value
	 * */
	ActionListener addRunwayListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Adding new runway");
			
			//Get the data from the GUI and sanitize it
			System.out.println(newRunwayDialog.getTopRunwayDesignator()+" and botom "+newRunwayDialog.getBottomRunwayDesignator());
			
			Runway newRunway =getDataFromRunwayDialogAndCreateRunway();
			if(newRunway == null)
				return;
			
			//Check that the runway with such designator does not exist already
			
				if(runways.containsKey(newRunway.getLowestEquivalentDesignator().toString())) {
					view.getFrame().displayError(new GuiException ("Two runways cannot have the same designator"));
					return;
				}
			
			//Add runway to the HashSet of runways where its String Designator serves as a key.
			runways.put(newRunway.getLowestEquivalentDesignator().toString(), newRunway);
					
			//Close the NewRunwayWindow
			newRunwayDialog.dispose();
			
			//Update the JList of Runways in the airportDialog
			updateAirportDialogRunwaysList();
			
			
			
		}

		
		
	};
	
	/*---------------------------------------------------------------------------------
	 *------------------------  EditRunwayDialog Listeners   ---------------------------
	 --------------------------------------------------------------------------------*/
	ActionListener editOKRunwayListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			Runway newRunway =getDataFromRunwayDialogAndCreateRunway();
			if(newRunway == null){
				view.getFrame().displayError(
						new Exception("All values must be postive!"));
				return;
			}
			runways.remove(selectedRunwayKey);
			
			runways.put(newRunway.getStringValueOfDesignator(), newRunway);
			
			//Close the NewRunwayWindow
			newRunwayDialog.dispose();
			
			//Update the JList of Runways in the airportDialog
			updateAirportDialogRunwaysList();
				
		}
		
	};
	
	protected RunwayDesignator getDesignator(int angle, String side){
		//Get angle
		if(side.equals("L")){
			return new RunwayDesignator(angle, RunwaySide.LEFT);
		} else if(side.equals("R")){
			return new RunwayDesignator(angle, RunwaySide.RIGHT);
		} else if(side.equals("C")){
			return new RunwayDesignator(angle, RunwaySide.CENTER);
		} else if (side.equals("")){
			return new RunwayDesignator(angle, RunwaySide.NONE);
		}
		
		//this case should never happen
		return null;
	}
	
	
	protected void updateAirportDialogRunwaysList(){
		//Get the keys from the hash map
	//	this.runways
	ArrayList<String> tmp = new ArrayList<String>();
	for (String key : runways.keySet()){
		tmp.add(key);
	}
	
	//Convert ArrayList to String[]
	String[] result = new String[tmp.size()];
	for(int i=0; i<tmp.size(); i++)
		result[i]=tmp.get(i);
		
		
	airportDialog.updateGUIRunwaysJList(result);
	
	
	}
	
	protected void resetAirportController(){
		selectedRunwayKey=null;
		runways.clear();
	}
	
	protected Runway getDataFromRunwayDialogAndCreateRunway() {
		//Create both RunwayDesignators
		String topDesignator = newRunwayDialog.getTopRunwayDesignator();
		int topAngle = newRunwayDialog.getTopRunwayAngle();
		String bottomDesignator = newRunwayDialog.getBottomRunwayDesignator();
		int bottomAngle = newRunwayDialog.getBottomRunwayAngle();
		
		System.out.println("The data received: "+topAngle+" "+topDesignator+" , "+ " "+ bottomAngle + " " + bottomDesignator);
		
		RunwayDesignator top = getDesignator(topAngle, topDesignator);
		//RunwayDesignator bottom = getDesignator(bottomAngle, bottomDesignator);
			
		//Get general Runway data
		float[] generalRunwayData;
		//Get DirectionalPropertiesData
		float[] topRunway;
		float[] bottomRunway;
		try {
			generalRunwayData = newRunwayDialog.getGeneralRunwayParameter();
			topRunway = newRunwayDialog.getTopRunwayParameters();
			bottomRunway = newRunwayDialog.getBottomRunwayParameters();
		} catch (GuiException e1) {
			view.getFrame().displayError(e1);
			return null;
		}
		
		RunwayDirectionalProperties topDirectional;
		RunwayDirectionalProperties bottomDirectional;
		
		topDirectional = new RunwayDirectionalProperties(	
				topRunway[GuiConstants.DATA_PROPERTY_THR],
				topRunway[GuiConstants.DATA_PROPERTY_TORA],
				topRunway[GuiConstants.DATA_PROPERTY_TODA],
				topRunway[GuiConstants.DATA_PROPERTY_ASDA],
				topRunway[GuiConstants.DATA_PROPERTY_LDA],
				topRunway[GuiConstants.DATA_PROPERTY_RESA]);
		bottomDirectional = new RunwayDirectionalProperties(	
				bottomRunway[GuiConstants.DATA_PROPERTY_THR],
				bottomRunway[GuiConstants.DATA_PROPERTY_TORA],
				bottomRunway[GuiConstants.DATA_PROPERTY_TODA],
				bottomRunway[GuiConstants.DATA_PROPERTY_ASDA],
				bottomRunway[GuiConstants.DATA_PROPERTY_LDA],
				bottomRunway[GuiConstants.DATA_PROPERTY_RESA]);
		
		//check that runway params are positive
		for(int i=0; i<6; i++){
			if(i!=4) // ignore distance from treshold
			if(!(isNumberPositive(topRunway[i])) || !(isNumberPositive(bottomRunway[i]))){
						return null;
			}
		}
		//check general params as well
		if(!(isNumberPositive(generalRunwayData[GuiConstants.DATA_PROPERTY_WIDTH]))
			|| !(isNumberPositive(generalRunwayData[GuiConstants.DATA_PROPERTY_DIS_CLEARED]))
			|| !(isNumberPositive(generalRunwayData[GuiConstants.DATA_PROPERTY_DIS_GRADED]))){
			return null;
		}
		
		//Create runway where designator and first DirectionalProperties are the same
		Runway newRunway = new Runway(
				top,
				generalRunwayData[GuiConstants.DATA_PROPERTY_WIDTH],
				generalRunwayData[GuiConstants.DATA_PROPERTY_DIS_CLEARED],
				generalRunwayData[GuiConstants.DATA_PROPERTY_DIS_GRADED],
				topDirectional,
				bottomDirectional);
		
		return newRunway;
		
	}
	

}
