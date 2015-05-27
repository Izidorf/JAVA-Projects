package airproject.controller;

import javax.swing.SwingUtilities;

import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;
import airproject.model.RunwayResults;
import airproject.view.AbstractViewPanel;
import airproject.view.SideOnViewPanel;
import airproject.view.TopDownViewPanel;

public class TopDownController {
	
	// The instance of top-down view.
	TopDownViewPanel topDown = null;
	
	public TopDownController(TopDownViewPanel topDown) {
		this.topDown = topDown;
	}
	
	/*
	 * Called whenever the runway, designator or results change.
	 */
	public void updateHasOccured(final Runway runway, final RunwayDesignator desig, final RunwayResults results){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Get the facing direction properties.
				RunwayDirectionalProperties facing = runway.getDirectionalProperties(desig);
				// Get the opposite direction properties.
				RunwayDirectionalProperties opposite = runway.getOppositeDirectionalProperties(facing);
				// Set the view's default properties.
				topDown.setDefaultProperties(
						facing.getDefaultTORA(),
						facing.getDefaultTODA(),
						facing.getDefaultASDA(),
						facing.getDefaultLDA(),
						facing.getDefaultDisplacementThreshold());
				// Set the view's redeclared properties.
				topDown.setRedeclaredProperties(
						results.getRedeclaredTORA(), 
						results.getRedeclaredTODA(), 
						results.getRedeclaredASDA(), 
						results.getRedeclaredLDA());
				// Set the view's obstacle flag.
				topDown.setBeforeObstacle(results.isBeforeObstacle());
				// Set the view's designator string.
				topDown.setDesignatorStrings(runway.getLowestEquivalentDesignator().getAngleString(), runway.getLowestEquivalentDesignator().getSide().toSymbol(), runway.getHighestEquivalentDesignator().getAngleString(),runway.getHighestEquivalentDesignator().getSide().toSymbol());
				topDown.setAutoRotation(runway.getLowestEquivalentDesignator().getAngle()*10+90);
				// Set the view's obstacle properties.
				if( runway.getObstacle() == null ){
					topDown.removeObstacle();
				}else{
					topDown.setObstacleProperties(runway.getObstacle().getDisplacementFromThreshold(desig), runway.getObstacle().getDisplacementFromCenterline(), runway.getObstacle().getHeight());
				}
				// Set the view's required strip-end and stopways.
				topDown.setFacingStripEnd(facing.getStripEnd());
				topDown.setOppositeStopway(opposite.getStopway());
				topDown.setOppositeStripEnd(opposite.getStripEnd());
				// Set the view's designator in focus.
				if( desig.isHigh() ){
					topDown.showHighDesignator();
				}else{
					topDown.showLowDesignator();
				}
				// Reset the drag position
				topDown.resetDrag();
				topDown.repaint();
			}
		});
	}
	

}
