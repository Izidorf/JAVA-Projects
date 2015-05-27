package airproject.controller;

import airproject.model.Airport;
import airproject.model.ModelUpdateListener;
import airproject.model.Obstacle;
import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;

/*
 * Adapter class used to make implementation of model update listener cleaner.
 */
public class ModelUpdateAdapter implements ModelUpdateListener{

	@Override
	public void onAirportUpdate(Airport airport) {
		// Stub method.
	}

	@Override
	public void onRunwayUpdate(Runway runway) {
		// Stub method.
	}

	@Override
	public void onObstacleUpdate(Obstacle obstacle) {
		// Stub method.
	}

	@Override
	public void onRunwayDirectionalUpdate(RunwayDirectionalProperties properties) {
		// Stub method.
	}
	
}
