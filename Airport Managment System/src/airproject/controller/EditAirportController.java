package airproject.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.html.HTMLDocument.Iterator;

import airproject.model.Airport;
import airproject.model.Runway;
import airproject.view.View;

public class EditAirportController extends AirportDialogController{

	public EditAirportController(View view, Controller c) {
		super(view, c);
		//Attach listeners to MainFrame File >> Edit >>Airport
		view.getFrame().setEditAirportListener(openEditAirportDialogPanel);	
	}
	
	/*---------------------------------------------------------------------------------
	 *------------------------  MainFrame EditAirport Listener   ---------------------------
	 --------------------------------------------------------------------------------*/
	/*
	 * Open NewAirpotDialog for File >> Edit >> Airport
	 * */
	ActionListener openEditAirportDialogPanel = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Opening new Airport Dialog");
			//Make sure all properties of this class are reset
			resetAirportController();
			updateController();
			//Create new airport dialog
			airportDialog = view.getFrame().openEditingOfAirport();
//			
//			//Attach all listeners
			airportDialog.setOkListener(createNewAirport);
			airportDialog.setNewRunwayListener(openNewRunwayDialogPanel);
			airportDialog.setRemoveRunwayListener(removeSelectedRunway);
			airportDialog.setEditRunwayListener(editSelectedRunway);
//			
		
			Airport airport = model.getAirport();
			
			//fill it with data
			airportDialog.updateAirportName(airport.getName());
			airportDialog.updateAirportCode(airport.getCode());
			
			String[] runwayKeys = getRunwaysKeys();
			airportDialog.updateGUIRunwaysJList(runwayKeys);
			
			
			//Make it visible
			airportDialog.setVisible(true);
			
		}
		
	};
	
	public String[] getRunwaysKeys(){
		Airport airport = model.getAirport();
		String[] runwayKeys = new String[airport.getRunwayCount()];
		int i=0;
		java.util.Iterator<Runway> iter = airport.getRunways().iterator();
		while (iter.hasNext()) {
			  runwayKeys[i]=iter.next().getLowestEquivalentDesignator().toString();
			  System.out.print(runwayKeys[i]+" ");
			  i++;
			}
		return runwayKeys;
	}
	
	public Runway[] getRunways(){
		Airport airport = model.getAirport();
		Runway[] runways = new Runway[airport.getRunwayCount()];
		int i=0;
		java.util.Iterator<Runway> iter = airport.getRunways().iterator();
		while (iter.hasNext()) {
			  runways[i]=iter.next();
			  i++;
			}
		return runways;
	}
	
	public void updateController(){
		//Fill runways HasmMap with runways
		for(Runway r : getRunways()){
		this.runways.put(r.getLowestEquivalentDesignator().toString(), r);	
		}
		
		
	}
	

}
