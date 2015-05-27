package airproject.model;

public enum RunwaySide {
	
	LEFT, RIGHT, CENTER, NONE;
	
	/*
	 * Gets the opposite side.
	 */
	public RunwaySide getOpposite(){
		switch (this) {
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		case CENTER:
			return CENTER;
		default:
			return NONE;
		}
	}
	
	/*
	 * Gets the symbol representation of a side.
	 */
	public String toSymbol() {
		switch (this) {
		case LEFT:
			return "L";
		case RIGHT:
			return "R";
		case CENTER:
			return "C";
		default:
			return "";
		}
	}
	
	/*
	 * Gets the word representation of a side.
	 */
	public String toString() {
		switch (this) {
		case LEFT:
			return "Left";
		case RIGHT:
			return "Right";
		case CENTER:
			return "Center";
		default:
			return "";
		}
	}
	
}

