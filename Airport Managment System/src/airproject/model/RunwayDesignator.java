package airproject.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Represents a runway orientation designator.
 * e.g. 14R, 05L, 02R
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RunwayDesignator {

	private RunwaySide side;
	private int angle;

	public RunwayDesignator() {
	}
	
	public RunwayDesignator(int angle) {
		this(angle, RunwaySide.NONE);
	}

	
	public RunwayDesignator(int angle, RunwaySide side) {
		// Check a valid number is given.
		if (angle < 1 || angle > 36) {
			throw new IllegalArgumentException(
					"Runway designator number must be between 1 and 36!");
		}
		// Null check on the runway side.
		if (side == null) {
			side = RunwaySide.NONE;
		}
		// Set the properties of the runway designator.
		this.side = side;
		this.angle = angle;
	}
	
	/*
	 * Gets whether the designator is a high one.
	 */
	public boolean isHigh(){
		return angle > 18;
	}

	/*
	 * Returns the logically equivalent designator in the opposite direction.
	 */
	public RunwayDesignator getOpposite() {
		// Get the angle with 180 degrees difference.
		int oppositeAngle = ((angle + 18 - 1) % 36) + 1;
		return new RunwayDesignator(oppositeAngle, side.getOpposite());
	}

	/*
	 * Gets the number of the runway designator.
	 */
	public int getAngle() {
		return angle;
	}
	
	/*
	 * Gets the number in string form.
	 */
	public String getAngleString(){
		String s = ""+angle;
		if( s.length() == 1 ){
			s = "0"+s;
		}
		return s;
	}

	/*
	 * Gets the side of the runway designator.
	 */
	public RunwaySide getSide() {
		return side;
	}

	/*
	 * Converts the runway designator to a string.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%02d%s", getAngle(), getSide().toSymbol());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + angle;
		result = prime * result + ((side == null) ? 0 : side.hashCode());
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
		RunwayDesignator other = (RunwayDesignator) obj;
		if (angle != other.angle)
			return false;
		if (side != other.side)
			return false;
		return true;
	}

	/*
	 * Converts a string to a designator
	 */
	public static RunwayDesignator parseDesignator(String s) {
		Integer angle;
		String dirString = "";
		try{
			// Input length 3.
			if( s.length() == 3 ){
				angle = Integer.parseInt(s.substring(0, 2));
				dirString = s.substring(2,3);
			}
			// Input length 2.
			else if( s.length() == 2 ){
				try{
					angle = Integer.parseInt(s);
				}
				catch(NumberFormatException e){
					angle = Integer.parseInt(s.substring(0,1));
					dirString = s.substring(1, 2);
				}
			// Input length 1.
			}else{
				angle = Integer.parseInt(s);
			}
			// Get the side corresponding to the string.
			switch( dirString ){
			case "L":
				return new RunwayDesignator(angle, RunwaySide.LEFT);
			case "R":
				return new RunwayDesignator(angle, RunwaySide.RIGHT);
			case "C":
				return new RunwayDesignator(angle, RunwaySide.CENTER);
			case "":
				return new RunwayDesignator(angle, RunwaySide.NONE);
			}
		}catch(NumberFormatException e){}
		throw new IllegalArgumentException("Not a valid designator.");
	}
	
	public void setDesignator(int angle, String side){
		this.angle=angle;
		if(side.equals(""))
			this.side = RunwaySide.NONE;
		else if(side.equals("L"))
			this.side = RunwaySide.LEFT;
		else if(side.equals("R"))
			this.side = RunwaySide.RIGHT;
	}

}
