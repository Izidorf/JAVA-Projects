package airproject.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Represents a runway
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Runway {

	private RunwayDesignator lowestEquivalentDesignator;
	private float stripWidth, distCleared, distGraded;
	
	//Obstacle Stuff
	private Obstacle obstacle;
	
	private RunwayDirectionalProperties highDirectionalProperties;
	private RunwayDirectionalProperties lowDirectionalProperties;

	private Runway(){
	}
	
	public Runway(RunwayDesignator desig, 
					float width, float distCleared, float distGraded, 
								RunwayDirectionalProperties directionalPropertiesOfDesignatorEnd,
								RunwayDirectionalProperties directionalPropertiesOfOtherEnd) {
		setDesignator(desig);
		setWidth(width);
		setDistGraded(distGraded);
		setDistCleared(distCleared);
		setDirectionalProperties(desig, directionalPropertiesOfDesignatorEnd);
		setDirectionalProperties(desig.getOpposite(), directionalPropertiesOfOtherEnd);
		setObstacle(null);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(distCleared);
		result = prime * result + Float.floatToIntBits(distGraded);
		result = prime
				* result
				+ ((highDirectionalProperties == null) ? 0
						: highDirectionalProperties.hashCode());
		result = prime
				* result
				+ ((lowDirectionalProperties == null) ? 0
						: lowDirectionalProperties.hashCode());
		result = prime
				* result
				+ ((lowestEquivalentDesignator == null) ? 0
						: lowestEquivalentDesignator.hashCode());
		result = prime * result
				+ ((obstacle == null) ? 0 : obstacle.hashCode());
		result = prime * result + Float.floatToIntBits(stripWidth);
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
		Runway other = (Runway) obj;
		if (Float.floatToIntBits(distCleared) != Float
				.floatToIntBits(other.distCleared))
			return false;
		if (Float.floatToIntBits(distGraded) != Float
				.floatToIntBits(other.distGraded))
			return false;
		if (highDirectionalProperties == null) {
			if (other.highDirectionalProperties != null)
				return false;
		} else if (!highDirectionalProperties
				.equals(other.highDirectionalProperties))
			return false;
		if (lowDirectionalProperties == null) {
			if (other.lowDirectionalProperties != null)
				return false;
		} else if (!lowDirectionalProperties
				.equals(other.lowDirectionalProperties))
			return false;
		if (lowestEquivalentDesignator == null) {
			if (other.lowestEquivalentDesignator != null)
				return false;
		} else if (!lowestEquivalentDesignator
				.equals(other.lowestEquivalentDesignator))
			return false;
		if (obstacle == null) {
			if (other.obstacle != null)
				return false;
		} else if (!obstacle.equals(other.obstacle))
			return false;
		if (Float.floatToIntBits(stripWidth) != Float
				.floatToIntBits(other.stripWidth))
			return false;
		return true;
	}
		
	public Runway(RunwayDesignator desig, RunwayDirectionalProperties high, RunwayDirectionalProperties low){
		// Construct runway with default width, and shaded/cleared distances.
		this(desig, 75, 150, 100, high, low);
	}

	/*
	 * Calculates the runways dependent properties and then returns a result object to query them.
	 */
	public RunwayResults recalculate(RunwayDesignator desig, float blastProtection, int slopeRatio) {
		return new RunwayResults(this, desig, blastProtection, slopeRatio);
	}
	
	/*
	 * gets the properties of one end of the runway, in the direction of a given designator.
	 */
	public RunwayDirectionalProperties getDirectionalProperties(RunwayDesignator desig){
		// Check the designtor passed in is a correct one.
		if( !desig.equals(lowestEquivalentDesignator) 
				&& ! desig.equals(lowestEquivalentDesignator.getOpposite())){
			throw new IllegalArgumentException("Runway designator is not valid for this runway.");
		}
		// Handle based upon whether the designator is high or low.
		if( desig.isHigh() ){
			return highDirectionalProperties;
		}else{
			return lowDirectionalProperties;
		}
	}

	/*
	 * Sets the properties of one end of the runway, in the direction of a given designator.
	 */
	public void setDirectionalProperties(RunwayDesignator desig,
			RunwayDirectionalProperties prop) {
		// Check the designtor passed in is a correct one.
		if (!desig.equals(lowestEquivalentDesignator)
				&& !desig.equals(lowestEquivalentDesignator.getOpposite())) {
			throw new IllegalArgumentException(
					"Runway designator is not valid for this runway.");
		}
		// Handle based upon whether the designator is high or low.
		if (desig.isHigh()) {
			highDirectionalProperties = prop;
		} else {
			lowDirectionalProperties = prop;
		}
		update();
	}
	


	/*
	 * Gets the width of the runway.
	 */
	public float getWidth() {
		return this.stripWidth;
	}

	/*
	 * Sets the width of the runway.
	 */
	public void setWidth(float width) {
		// Range check of width.
		if( width <= 0 ){
			throw new IllegalArgumentException("Runway width must be a positive number.");
		}
		this.stripWidth = width;
		update();
	}

	/*
	 * Gets the runway obstacle.
	 */
	public Obstacle getObstacle() {
		return obstacle;
	}
	
	/*
	 * Sets the runway obstacle.
	 */
	public void setObstacle(Obstacle obstacle) {
		this.obstacle = obstacle;
		update();
	}
	
	/*
	 * Gets the runway's distance to the graded area from the centreline.
	 */
	public float getDistGraded() {
		return distGraded;
	}

	/*
	 * Sets the runway's distance to the graded area from the centreline.
	 */
	public void setDistCleared(float distCleared) {
		if( distCleared <= 0 ){
			throw new IllegalArgumentException("Graded distance must be a positive number.");
		}
		this.distCleared = distCleared;
		update();
	}
	
	/*
	 * Gets the runway's distance to the cleared area from the centreline.
	 */
	public float getDistCleared() {
		return distCleared;
	}
	
	/*
	 * Sets the runways distance to the graded area from the centreline.
	 */
	public void setDistGraded(float distGraded) {
		// Range check of graded distance.
		if( distGraded <= 0 ){
			throw new IllegalArgumentException("Graded distance must be a positive number.");
		}
		this.distGraded = distGraded;
		update();
	}
	


	/*
	 * Gets the runways highest equivalent designator, the logical 
	 * equivalent of the lowest designator.
	 */
	public RunwayDesignator getHighestEquivalentDesignator(){
		return lowestEquivalentDesignator.getOpposite();
	}
	
	/*
	 * Gets the runways lowest equivalent designator
	 */
	public RunwayDesignator getLowestEquivalentDesignator() {
		return lowestEquivalentDesignator;
	}
	
	/*
	 * Sets the runways lowest equivalent designator
	 */
	public void setDesignator(
			RunwayDesignator designator) {
		// Check for high and if so, set the designator to low.
		if( designator.isHigh() ){
			this.lowestEquivalentDesignator = designator.getOpposite();
		}else{
			this.lowestEquivalentDesignator = designator;
		}
		update();
	}
	
	/*
	 * Gets the properties of the end of the runway which the higher designator points to.
	 */
	public RunwayDirectionalProperties getHighDirectionalProperties() {
		return highDirectionalProperties;
	}
	
	/*
	 * Sets the properties of the end of the runway which the higher designator points to.
	 */
	public void setHighDirectionalProperties(
			RunwayDirectionalProperties highDirectionalProperties) {
		this.highDirectionalProperties = highDirectionalProperties;
		update();
	}
	
	/*
	 * Gets the properties of the end of the runway which the lower designator points to.
	 */
	public RunwayDirectionalProperties getLowDirectionalProperties() {
		return lowDirectionalProperties;
	}
	
	/*
	 * Sets the properties of the end of the runway which the lower designator points to.
	 */
	public void setLowDirectionalProperties(
			RunwayDirectionalProperties lowDirectionalProperties) {
		this.lowDirectionalProperties = lowDirectionalProperties;
		update();
	}
	
	/*
	 * Given a runway property, gets the properties at the other end
	 */
	public RunwayDirectionalProperties getOppositeDirectionalProperties(RunwayDirectionalProperties properties){
		if( properties == lowDirectionalProperties ){
			return highDirectionalProperties;
		}else if(properties == highDirectionalProperties ){
			return lowDirectionalProperties;
		}else{
			throw new IllegalArgumentException("The directional properties given as input were not of this runway.");
		}
	}

	public boolean obstacleExits(){
		return obstacle != null;
	}
	
	public void deleateObstacle(){
		this.obstacle=null;
	}
	
	public String getStringValueOfDesignator(){
		return lowestEquivalentDesignator.toString();
	}
	
	
	/*
	 * Called whenever the runway is updated.
	 */
	private void update(){
		// Alert the listener of an updated runway.
		Model.getInstance().getListener().onRunwayUpdate(this);
	}
	
	
	
	
}
