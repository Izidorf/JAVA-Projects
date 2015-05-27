package airproject.model;

import java.io.File;

import airproject.model.io.XMLIO;

/*
 * Encapsulates the model module of the project
 */



public class Model {
	
	private static Model instance;
	
	/*
	 * Factory method
	 */
	public static Model getInstance(){
		if( instance == null ){
			instance = new Model();
		}
		return instance;
	}

	/*
	 * This code is here to prevent the tests from breaking until we set up this properly!
	 */
	private ModelUpdateListener listener
	= new ModelUpdateListener() {
		
		@Override
		public void onRunwayUpdate(Runway runway) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onRunwayDirectionalUpdate(RunwayDirectionalProperties properties) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onObstacleUpdate(Obstacle obstacle) {
			
		}
		
		@Override
		public void onAirportUpdate(Airport airport) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private Airport airport;
	
	private Model() {
	}
	
	/*
	 * Sets the model's update listener.
	 */
	public void setListener(ModelUpdateListener listener) {
		this.listener = listener;
	}
	
	/*
	 * Gets the model's update listener.
	 */
	public ModelUpdateListener getListener(){
		return listener;
	}
	
	/*
	 * Gets the model's airport.
	 */
	public Airport getAirport() {
		return airport;
	}
	
	/*
	 * Sets the model's airport.
	 */
	public void setAirport(Airport airport) {
		this.airport = airport;
	}
	
	public void loadAirport(File file) {
		airport = XMLIO.readXml(Airport.class, file);
	}
	public void saveAirport(Airport airport, File file) {
		XMLIO.writeXml(Airport.class, airport, file);
	}
	
	public void saveObstacle(Obstacle obstacle, File file) {
		XMLIO.writeXml(Obstacle.class, obstacle, file);
	}
	
}
