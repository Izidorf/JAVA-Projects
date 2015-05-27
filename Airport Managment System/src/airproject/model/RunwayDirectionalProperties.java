package airproject.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RunwayDirectionalProperties {

	private float displacedThreshold, TORA, ASDA, LDA, TODA;
	private float RESA, stripEnd;

	private RunwayDirectionalProperties() {
	}
	
	public RunwayDirectionalProperties(float displacementThreshold, float TORA,
			float TODA, float ASDA, float LDA, float RESA, float stripEnd) {
		setDefaultDisplacementThreshold(displacementThreshold);
		setDefaultTORA(TORA);
		setDefaultTODA(TORA);
		setDefaultASDA(ASDA);
		setDefaultLDA(LDA);
		setRESA(RESA);
		setStripEnd(stripEnd);
	}
	
	public RunwayDirectionalProperties(float displacementThreshold, float TORA,
			float TODA, float ASDA, float LDA, float RESA) {
		// Use default resa of 60.
		this(displacementThreshold, TORA, TODA, ASDA, LDA, RESA, 60);
	}

	/*
	 * Sets the displacement threshold.
	 */
	public void setDefaultDisplacementThreshold(float displacedThreshold) {
		// Range check for clearway length
		if (displacedThreshold < 0) {
			throw new IllegalArgumentException(
					"Displacement threshold must be a positive number or zero!");
		}
		this.displacedThreshold = displacedThreshold;
		update();
	}

	/*
	 * Gets the clearway distance.
	 */
	public float getClearway() {
		return TODA - TORA;
	}

	/*
	 * Gets the stopway distance.
	 */
	public float getStopway() {
		return ASDA - TORA;
	}

	/*
	 * Gets the displacement distance.
	 */
	public float getDefaultDisplacementThreshold() {
		return displacedThreshold;
	}

	/*
	 * Gets the default TORA.
	 */
	public float getDefaultTORA() {
		return TORA;
	}

	/*
	 * Sets the default TORA.
	 */
	public void setDefaultTORA(float TORA) {
		this.TORA = TORA;
		update();
	}

	/*
	 * Gets the default TORA.
	 */
	public float getDefaultASDA() {
		return ASDA;
	}

	/*
	 * Sets the default TORA.
	 */
	public void setDefaultASDA(float ASDA) {
		this.ASDA = ASDA;
		update();
	}

	/*
	 * Gets the default TORA.
	 */
	public float getDefaultLDA() {
		return LDA;
	}

	/*
	 * Sets the default LDA.
	 */
	public void setDefaultLDA(float LDA) {
		this.LDA = LDA;
		update();
	}

	/*
	 * Gets the default TODA.
	 */
	public float getDefaultTODA() {
		return TODA;
	}

	/*
	 * Sets the default TODA.
	 */
	public void setDefaultTODA(float TODA) {
		this.TODA = TODA;
		update();
	}

	/*
	 * Gets the RESA length.
	 */
	public float getRESA() {
		return RESA;
	}

	/*
	 * Sets the RESA length.
	 */
	public void setRESA(float resa) {
		this.RESA = resa;
		update();
	}
	
	/*
	 * Sets the strip end length.
	 */
	public void setStripEnd(float stripEnd) {
		this.stripEnd = stripEnd;
		update();
	}
	
	/*
	 * Gets the stripend length.
	 */
	public float getStripEnd() {
		return stripEnd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(ASDA);
		result = prime * result + Float.floatToIntBits(LDA);
		result = prime * result + Float.floatToIntBits(RESA);
		result = prime * result + Float.floatToIntBits(TODA);
		result = prime * result + Float.floatToIntBits(TORA);
		result = prime * result + Float.floatToIntBits(displacedThreshold);
		result = prime * result + Float.floatToIntBits(stripEnd);
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
		RunwayDirectionalProperties other = (RunwayDirectionalProperties) obj;
		if (Float.floatToIntBits(ASDA) != Float.floatToIntBits(other.ASDA))
			return false;
		if (Float.floatToIntBits(LDA) != Float.floatToIntBits(other.LDA))
			return false;
		if (Float.floatToIntBits(RESA) != Float.floatToIntBits(other.RESA))
			return false;
		if (Float.floatToIntBits(TODA) != Float.floatToIntBits(other.TODA))
			return false;
		if (Float.floatToIntBits(TORA) != Float.floatToIntBits(other.TORA))
			return false;
		if (Float.floatToIntBits(displacedThreshold) != Float
				.floatToIntBits(other.displacedThreshold))
			return false;
		if (Float.floatToIntBits(stripEnd) != Float
				.floatToIntBits(other.stripEnd))
			return false;
		return true;
	}

	/*
	 * Called whenever the properties are updated.
	 */
	private void update(){
		// Alert the listener that a set of directional properties have been updated.
		Model.getInstance().getListener().onRunwayDirectionalUpdate(this);
	}
	

}
