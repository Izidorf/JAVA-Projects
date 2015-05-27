package airproject.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Represents an obstacle which is on the runway.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Obstacle {
	
	private float height;
	private float displacementFromLowTreshold;
	private float displacementFromHighTreshold;
	private float displacementFromCenterline;
	
	private Obstacle() {
	}

	public Obstacle(float height, float displacementAlongFromLow, float displacementAlongFromHigh, float displacementOutwards) {
		setHeight(height);
		setDisplacementFromThresholds(displacementAlongFromLow, displacementAlongFromHigh);
		setDisplacementFromCenterline(displacementOutwards);
	}

	/*
	 * Sets the displacement of the obstacle from the centre line.
	 */
	public void setDisplacementFromCenterline(float displacementFromCenterline) {
		this.displacementFromCenterline = displacementFromCenterline;
		update();
	}
	
	/*
	 * Sets the displacement of the obstacle from the first side of the runway.
	 */
	public void setDisplacementFromThresholds(float displacementAlongFromLow, float displacementAlongFromHigh) {
		this.displacementFromLowTreshold = displacementAlongFromLow;
		this.displacementFromHighTreshold = displacementAlongFromHigh;
		update();
	}
	
	/*
	 * Gets the obstacle's displacement from the lower threshold.
	 */
	public float setDisplacementFromLowestThreshold(){
		return displacementFromLowTreshold;
	}
	/*
	 * Gets the obstacle's displacement from the higher threshold.
	 */
	public float getDisplacementFromHighestThreshold(){
		return displacementFromHighTreshold;
	}
	
	/*
	 * Sets the height of the obstacle.
	 */
	public void setHeight(float height) {
		// Check that the height is valid
		if( height <= 0 ){
			throw new IllegalArgumentException("Obstacle height must be a positive number!");
		}
		this.height = height;
		update();
	}
	
	/*
	 * Gets the displacement of the obstacle from the centre line of the runway.
	 */
	public float getDisplacementFromCenterline() {
		return displacementFromCenterline;
	}
	
	/*
	 * Gets the displacement of the obstacle from the lowest threshold
	 * */
	public float getDisplacementFromLowestTreshold(){
		return displacementFromLowTreshold;
	}
	
	/*
	 * Gets the displacement of the obstacle from the first side of the runway.
	 */
	public float getDisplacementFromThreshold(RunwayDesignator desig) {
		if( desig.isHigh() ){
			return this.displacementFromHighTreshold;
		}else{
			return this.displacementFromLowTreshold;
		}
	}
	
	/*
	 * Gets the height of the obstacle.
	 */
	public float getHeight() {
		return height;
	}
	
	/*
	 * Called whenever the obstacle is updated.
	 */
	private void update(){
		// Alert the listener of an updated obstacle.
		Model.getInstance().getListener().onObstacleUpdate(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ Float.floatToIntBits(displacementFromCenterline);
		result = prime * result
				+ Float.floatToIntBits(displacementFromLowTreshold);
		result = prime * result
				+ Float.floatToIntBits(displacementFromHighTreshold);
		result = prime * result + Float.floatToIntBits(height);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Obstacle other = (Obstacle) obj;
		if (Float.floatToIntBits(displacementFromCenterline) != Float
				.floatToIntBits(other.displacementFromCenterline))
			return false;
		if (Float.floatToIntBits(displacementFromLowTreshold) != Float
				.floatToIntBits(other.displacementFromLowTreshold))
			return false;
		if (Float.floatToIntBits(displacementFromHighTreshold) != Float
				.floatToIntBits(other.displacementFromHighTreshold))
			return false;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		return true;
	}

}
