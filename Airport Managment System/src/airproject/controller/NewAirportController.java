package airproject.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import airproject.model.Airport;
import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;
import airproject.view.GuiConstants;
import airproject.view.GuiException;
import airproject.view.RunwayDialog;
import airproject.view.View;

public class NewAirportController extends AirportDialogController {

	public NewAirportController(View view, Controller c) {
		super(view, c);
		
		//Attach listeners to MainFrame File >> New >>Airport
		view.getFrame().setCreateNewAirportListener(openairportDialogPanel);	
	}

	
	/*---------------------------------------------------------------------------------
	 *------------------------  MainFrame NewAirport Listener   ---------------------------
	 --------------------------------------------------------------------------------*/
	/*
	 * Open NewAirpotDialog for File >> New >> Airport
	 * */
	ActionListener openairportDialogPanel = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Opening new Airport Dialog");
			//Make sure all properties of this class are reset
			resetAirportController();
			
			//Create new airport dialog
			airportDialog = view.getFrame().openCreationOfNewAirport();
			
			//Attach all listeners
			airportDialog.setOkListener(createNewAirport);
			airportDialog.setNewRunwayListener(openNewRunwayDialogPanel);
			airportDialog.setRemoveRunwayListener(removeSelectedRunway);
			airportDialog.setEditRunwayListener(editSelectedRunway);
			
			//Make it visible
			
			
			airportDialog.setVisible(true);
			System.out.println("Airport Dialog made visible");
		}
		
	};
	
	

	

	
	
	
	

	

	
}
