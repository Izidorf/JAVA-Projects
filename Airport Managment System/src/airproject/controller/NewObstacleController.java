package airproject.controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import airproject.model.Model;
import airproject.model.Obstacle;
import airproject.view.GuiConstants;
import airproject.view.GuiException;
import airproject.view.ObstacleDialog;
import airproject.view.ObstacleDialog;
import airproject.view.View;

public class NewObstacleController {
	
	// The model and the view.
	Model model;
	View view;
	Controller contr;

	ObstacleDialog newObstacleFrame;
	
	public NewObstacleController(ObstacleDialog newObstacleFrame, Controller c) {
		this.newObstacleFrame=newObstacleFrame;
		model = Model.getInstance();
		view = c.view;
		this.newObstacleFrame.setCreateListener(createButtonListener);
		this.contr=c;
	}
	
	public void createNewObstacle(){
		
		
		//newObstacleFrame.getTxtName();

		
	}
	
	
	ActionListener createButtonListener = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Creating new Obstacle");
			
			float[] obstacleParams = null;
			//sanitise input
			try {
				 obstacleParams= newObstacleFrame.getObstacleParameters();
				
				
				
				
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
			Obstacle obs = contr.getCurrentRunway().getObstacle();
			if (obs == null) {
				obs = new Obstacle(height, dispFromLowTHR, dispFromHighTHR,
						dispFromCenter);
				contr.getCurrentRunway().setObstacle(obs);
			}
			obs.setDisplacementFromCenterline(dispFromCenter);
			obs.setDisplacementFromThresholds(dispFromLowTHR, dispFromHighTHR);
			obs.setHeight(height);
			// Recalculate the results.
			view.getFrame().performAddingOfTheObstacle();
			view.getFrame().setUpdateObstacleButtonActionListener(
					contr.getUpdateObstacleListener());
			view.getFrame().setRemoveObstacleListener(contr.getRemoveObstacleListener());
			contr.displayCurrentObstacle();
			contr.recalculateAndDisplay();
			
			 newObstacleFrame.dispose();
		}
		
	};
	
	
	
	

}
