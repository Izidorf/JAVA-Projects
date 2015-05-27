package airproject.model;

public interface ModelUpdateListener {
	
	public void onAirportUpdate(Airport airport);
	public void onRunwayUpdate(Runway runway);
	public void onObstacleUpdate(Obstacle obstacle);
	public void onRunwayDirectionalUpdate(RunwayDirectionalProperties properties);
}
