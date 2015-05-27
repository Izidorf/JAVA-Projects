package airproject.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import airproject.model.Model;
import airproject.model.Obstacle;
import airproject.model.Runway;
import airproject.view.GuiConstants;
import airproject.view.GuiException;
import airproject.view.AirportDialog;
import airproject.view.ObstacleDialog;
import airproject.view.ObstacleDialog;
import airproject.view.RunwayDialog;
import airproject.view.View;

public class EditObstacleController {

	
	Model model;
	View view;
	Controller control; //needed so that its method can be reused
	
	//Controlled GUI Panels
	private ObstacleDialog editObstacleDialog;

	

	public EditObstacleController(View view, Controller c){
		this.model= Model.getInstance();
		this.view = view;
		this.control=c;
		
		//Attach listeners to MainFrame File >> New >>Airport
		view.getFrame().setEditObstacleListener(editObstacleDialogPanel);	

	}
	
	

	/*---------------------------------------------------------------------------------
	 *------------------------  MainFrame EditObstacle Listener   ---------------------------
	 --------------------------------------------------------------------------------*/
	/*
	 * Open EditObstacleDialog for File >> Edit >> Obstacle
	 * */
	ActionListener editObstacleDialogPanel = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Opening edit Obstacle Frame");
			
			//Throw an error if obstacle does not exist
			if(!control.getCurrentRunway().obstacleExits()){
				view.getFrame().displayError(new GuiException("You need to create an obstacle before you can edit it!"));
				return;
			}
			
			//Edit obstacle Frame
			editObstacleDialog = view.getFrame().openEditingOfObstacle();
			
			//fill it with obstacle data
			float[] params = new float[4];
			Obstacle curObst = control.getCurrentRunway().getObstacle();
			params[GuiConstants.DATA_OBSTACLE_HEIGHT]=curObst.getHeight();
			params[GuiConstants.DATA_OBSTACLE_DISP_LOW]=curObst.getDisplacementFromLowestTreshold();
			params[GuiConstants.DATA_OBSTACLE_DISP_HIGH]=curObst.getDisplacementFromHighestThreshold();
			params[GuiConstants.DATA_OBSTACLE_DISP_CENTER]=curObst.getDisplacementFromCenterline();
			
			editObstacleDialog.setObstacleParameters(params);
			
			//Attach all listeners
			editObstacleDialog.setCreateListener(createButtonListener);
				
			//Make it visible
			editObstacleDialog.setVisible(true);
			
		}
		
	};
	
	ActionListener createButtonListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Creating new Obstacle");
			
			float[] obstacleParams = null;
			//sanitise input
			try {
				 obstacleParams= editObstacleDialog.getObstacleParameters();
				
				
				
				
			} catch (GuiException e1) {
				view.getFrame().displayError(e1);
				return;
			}
			
			// Extract properties.
			float height = obstacleParams[GuiConstants.DATA_OBSTACLE_HEIGHT];
			float dispFromLowTHR = obstacleParams[GuiConstants.DATA_OBSTACLE_DISP_LOW];
			float dispFromHighTHR = obstacleParams[GuiConstants.DATA_OBSTACLE_DISP_HIGH];
			float dispFromCenter = obstacleParams[GuiConstants.DATA_OBSTACLE_DISP_CENTER];
			
			
			System.out.println(height);
			System.out.println(dispFromLowTHR);
			System.out.println(dispFromHighTHR);
			System.out.println(dispFromCenter);
			
			// Range checks.
			if (height <= 0f) {
			view.getFrame().displayError(
			new Exception("Obstacle height must be above zero!"));
			return;
			}
			// Update the model
			Obstacle obs = control.getCurrentRunway().getObstacle();
			if (obs == null) {
				obs = new Obstacle(height, dispFromLowTHR, dispFromHighTHR,
						dispFromCenter);
				control.getCurrentRunway().setObstacle(obs);
			}
			obs.setDisplacementFromCenterline(dispFromCenter);
			obs.setDisplacementFromThresholds(dispFromLowTHR, dispFromHighTHR);
			obs.setHeight(height);
			// Recalculate the results.
			view.getFrame().performAddingOfTheObstacle();
			view.getFrame().setUpdateObstacleButtonActionListener(
					control.getUpdateObstacleListener());
			view.getFrame().setRemoveObstacleListener(control.getRemoveObstacleListener());
			control.displayCurrentObstacle();
			control.recalculateAndDisplay();
			
			editObstacleDialog.dispose();
		}
	};
}
