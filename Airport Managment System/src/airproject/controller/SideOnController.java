package airproject.controller;

import javax.swing.SwingUtilities;

import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;
import airproject.model.RunwayResults;
import airproject.view.SideOnViewPanel;

public class SideOnController {
	
	SideOnViewPanel sideOn;
	
	public SideOnController(SideOnViewPanel view) {
		sideOn = view;
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
				sideOn.setDefaultProperties(
						facing.getDefaultTORA(),
						facing.getDefaultTODA(),
						facing.getDefaultASDA(),
						facing.getDefaultLDA(),
						facing.getDefaultDisplacementThreshold());
				// Set the view's redeclared properties.
				sideOn.setRedeclaredProperties(
						results.getRedeclaredTORA(), 
						results.getRedeclaredTODA(), 
						results.getRedeclaredASDA(), 
						results.getRedeclaredLDA());
				// Set the view's obstacle flag.
				sideOn.setBeforeObstacle(results.isBeforeObstacle());
				// Set the view's designator string.
				sideOn.setDesignatorStrings(runway.getLowestEquivalentDesignator().getAngleString(), runway.getLowestEquivalentDesignator().getSide().toSymbol(), runway.getHighestEquivalentDesignator().getAngleString(),runway.getHighestEquivalentDesignator().getSide().toSymbol());
				// Set the view's obstacle properties.
				if( runway.getObstacle() == null ){
					sideOn.removeObstacle();
				}else{
					sideOn.setObstacleProperties(runway.getObstacle().getDisplacementFromThreshold(desig), runway.getObstacle().getDisplacementFromCenterline(), runway.getObstacle().getHeight());
				}
				// Set the view's required strip-end and stopways.
				sideOn.setFacingStripEnd(facing.getStripEnd());
				sideOn.setOppositeStopway(opposite.getStopway());
				sideOn.setOppositeStripEnd(opposite.getStripEnd());
				// Set the view's designator in focus.
				if( desig.isHigh() ){
					sideOn.showHighDesignator();
				}else{
					sideOn.showLowDesignator();
				}
				sideOn.setSlopeLength(results.getSlopeLength());
				// Reset the drag position
				sideOn.resetDrag();
				// Repaint the top-down view.
				sideOn.repaint();
			}
		});
	}
}
