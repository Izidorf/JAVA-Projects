package airproject.model;

import java.util.HashMap;
import java.util.Map;

import airproject.model.calculations.CalculationHistory;
import airproject.model.calculations.CalculationSection;

public class RunwayResults {

	private CalculationHistory calculationHistory;
	private float redeclaredTODA, redeclaredTORA, redeclaredASDA, redeclaredLDA;
	private float slopeLength;
	private boolean sloprRequired;
	private boolean NOTAM = false;
	private boolean isBeforeObstacle = false;
	
	protected RunwayResults(Runway runway, RunwayDesignator designator, float blastProtection, int slopeRatio) {
		// Initialise the calculation history.
		calculationHistory = new CalculationHistory("Designator "+designator);
		// Get the directional properties
		RunwayDirectionalProperties towardsProperties = runway.getDirectionalProperties(designator);
		// Check for obstacle free runway, if so, use defaults.
		if( runway.getObstacle() == null ){
			// Log the calculation.
			CalculationSection section = new CalculationSection("Obstacle status");
			section.addLine("No obstacle on the runway.");
			section.addLine("NOTAM not required!" );
			section.addLine("Redeclaration not required!" );
			calculationHistory.add(section);
			// Set the default properties.
			useDefaultsProperties(towardsProperties);
			NOTAM = false;
			return;
		}
		
		float distanceFromCenterline = Math.abs(runway.getObstacle().getDisplacementFromCenterline());
		// check for outside instrument strip.
		if( distanceFromCenterline > 150 ){
			// Log the calculation.
			CalculationSection section = new CalculationSection("Obstacle status");
			section.addLine("Obstacle is outside of instrument strip (150m from centreline).");
			section.addLine(String.format("Distance from obstacle to centreline = %.2f",distanceFromCenterline) );
			section.addLine("NOTAM not required!" );
			section.addLine("Redeclaration not required!" );
			calculationHistory.add(section);
			// Set the default properties.
			useDefaultsProperties(towardsProperties);
			NOTAM = false;
			return;
		}
		// Check for graded or instrument area
		if( distanceFromCenterline > runway.getDistCleared() ){
			// Log the calculation.
			CalculationSection section = new CalculationSection("Obstacle status");
			section.addLine("Obstacle is outside of visual strip ("+runway.getDistCleared()+"m from centreline).");
			section.addLine(String.format("Distance from obstacle to centreline = %.2f",distanceFromCenterline) );
			section.addLine("NOTAM required!" );
			section.addLine("Redeclaration not required!" );
			calculationHistory.add(section);
			// Set the default properties.
			useDefaultsProperties(towardsProperties);
			NOTAM = true;
			return;
		}
		// Log the calculation.
		CalculationSection section = new CalculationSection("Obstacle status");
		section.addLine("Obstacle is within the visual strip ("+runway.getDistCleared()+"m from centreline).");
		section.addLine(String.format("Distance from obstacle to centreline = %.2f",distanceFromCenterline) );
		section.addLine("NOTAM required!" );
		section.addLine("Redeclaration required!" );
		calculationHistory.add(section);
		
		// calculate slope length.
		setSlopeLength(runway.getObstacle().getHeight() * slopeRatio);
		
		// get distance to the obstacle from the current threshold
		float displacementToObstacle = runway.getObstacle().getDisplacementFromThreshold(designator) + towardsProperties.getDefaultDisplacementThreshold();
		//obstacle is before the designator
		if( displacementToObstacle < 0 ){
			// The obstacle must be landed over and taken-off away from.
			// Check if the obstacle is far back enough that it will not effect the distances.
			if( getSlopeLength() <= Math.abs(displacementToObstacle) ){
				useDefaultsProperties(towardsProperties);
				CalculationSection section2 = new CalculationSection("Redeclared values");
				section2.addLine("Obstacle is too far behind the designator to effect the values.");
				section2.addLine("Redeclared values are the same as the originals.");
				calculationHistory.add(section2);
			}else{
				calculateTakeoffAwayLandingOver(runway, designator, blastProtection, slopeRatio);
			}
		}
		//obstacle is on the runway
		else if (displacementToObstacle < towardsProperties.getDefaultTORA()  ) {
			float distanceBeyondObstacle = towardsProperties.getDefaultTORA() - displacementToObstacle;
			// Check if there is more room after of before the obstacle
			if( displacementToObstacle > distanceBeyondObstacle ){
				// Use section before obstacle
				calculateTakeoffTowardsLandingTowards(runway, designator, blastProtection, slopeRatio);
			}else{
				// Use section after obstacle
				calculateTakeoffAwayLandingOver(runway, designator, blastProtection, slopeRatio);
			}
		}
		//obstacle is beyond the other designator
		else{
			calculateTakeoffTowardsLandingTowards(runway, designator, blastProtection, slopeRatio);
		}
		NOTAM = true;
	}
	
	/*
	 * Sets the redeclared properties the same as the original properties.
	 */
	private void useDefaultsProperties(RunwayDirectionalProperties towardsProperties){
		redeclaredTODA = towardsProperties.getDefaultTODA();
		redeclaredTORA = towardsProperties.getDefaultTORA();
		redeclaredASDA = towardsProperties.getDefaultASDA();
		redeclaredLDA = towardsProperties.getDefaultLDA();
		// Add the calculation section.
		CalculationSection section = new CalculationSection("Calculation of properties.");
		section.addLine(String.format("TORA = %.2f",redeclaredTORA) );
		section.addLine(String.format("TODA = %.2f",redeclaredTODA) );
		section.addLine(String.format("ASDA = %.2f",redeclaredASDA) );
		section.addLine(String.format("LDA = %.2f",redeclaredLDA) );
		calculationHistory.add(section);
	}
	
	/*
	 * Performs the calculations for taking off and landing towards the obstacle.
	 */
	private void calculateTakeoffTowardsLandingTowards(Runway runway, RunwayDesignator designator, float blastProtection, int slopeRatio){
		// Calculate the new TORA, TODA and ASDA.
		calculateTakeOffTowardsObstacle(runway, designator, slopeRatio);
		// Calculate the new LDA.
		calculateLandingtowardsObstacle(runway, designator);
		// The strip is before the obstacle.
		isBeforeObstacle = true;
	}
	
	/*
	 * Performs the calculations for taking off away an landing over the obstacle.
	 */
	private void calculateTakeoffAwayLandingOver(Runway runway, RunwayDesignator designator, float blastProtection, int slopeRatio){
		// Calculate the new TORA, TODA and ASDA.
		calculateTakeOffAwayFromObstacle(runway, designator, blastProtection);
		// Calculate the new LDA.
		calculateLandingOverObstacle(runway, designator, slopeRatio);
		// The strip is after the obstacle.
		isBeforeObstacle = false;
	}

	public boolean requiresNOTAM(){
		return NOTAM;
	}

	public float getRedeclaredTODA(){
		return redeclaredTODA;
	}

	public float getRedeclaredTORA(){
		return redeclaredTORA;
	}

	public float getRedeclaredLDA(){
		return redeclaredLDA;
	}

	public float getRedeclaredASDA(){
		return redeclaredASDA;
	}
	
	/*
	 * Calculates the LDA over an obstacle.
	 */
	private void calculateLandingOverObstacle(Runway runway,
			RunwayDesignator direction, int slopeRatio) {
		RunwayDirectionalProperties closeTo = runway.getDirectionalProperties(direction);
		// Get required independent values.
		float defaultLDA = closeTo.getDefaultLDA();
		float displacementFromEnd = runway.getObstacle().getDisplacementFromThreshold(direction);
		float stripEnd = closeTo.getStripEnd();
		float slopeLength = getSlopeLength();
		// Calculate the result.
		float calc = defaultLDA
				- displacementFromEnd
				- stripEnd
				- slopeLength;
		float result = Math.max(0, calc);
		// Log the calculation history.
		CalculationSection section = new CalculationSection("Calculation of LDA: (Over obstacle)");
			section.addLine("(R)LDA = (Original)LDA - Distance From Threshold - Stripend - Length made by slope");
			section.addLine( 
					String.format("(R)LDA = %.2f - %.2f - %.2f - %.2f",
							defaultLDA,
							displacementFromEnd,
							stripEnd, 
							slopeLength) );
			if( result!=calc)
				section.addLine("LDA cannot be negative!");
			section.addLine(String.format("(R)LDA = %.2f", result));
		calculationHistory.add(section);
		// Return the redeclared LDA.
		redeclaredLDA = result;
	}
	
	/*
	 * Calculates LDA towards the obstacle.
	 * = Distance from Threshold - RESA - Strip End
	 */
	private void calculateLandingtowardsObstacle(Runway runway,
			RunwayDesignator direction) {
		RunwayDirectionalProperties closeTo = runway.getDirectionalProperties(direction);
		RunwayDirectionalProperties awayFrom = runway.getDirectionalProperties(direction.getOpposite());
		// Get required independent values.
		//float defaultLDA = closeTo.getDefaultLDA();
		float displacementFromEnd = runway.getObstacle().getDisplacementFromThreshold(direction);
		float stripEnd = awayFrom.getStripEnd();
		float RESA = awayFrom.getRESA();
		//float displacementThreshold = awayFrom.getDefaultDisplacementThreshold();
		// Calculate the new result
		float calc = 
				displacementFromEnd
				- stripEnd
				- RESA;
		float result = Math.min(calc, closeTo.getDefaultLDA());
		// Log the calculation history.
		CalculationSection section = new CalculationSection("Calculation of LDA: (Towards obstacle)");
			section.addLine("(R)LDA = Distance from Threshold - RESA - Strip End");
			section.addLine(
					String.format(
							"(R)LDA = %.2f - %.2f - %.2f", 
							displacementFromEnd,
							RESA,
							stripEnd));
			section.addLine(String.format("(R)LDA = %.2f", calc));
			if( calc != result ){
				section.addLine("(R)LDA cannot exceed the original LDA!");
				section.addLine(String.format("(R)LDA = %.2f", result));
			}
		calculationHistory.add(section);
		// Set the LDA to the result.
		redeclaredLDA = result;
	}
	
	/*
	 * Calculates the TORA, TODA and ASDA when taking-off away from an obstacle.
	 */
	private void calculateTakeOffAwayFromObstacle(Runway runway,
			RunwayDesignator direction, float blastProtection) {
		RunwayDirectionalProperties closeTo = runway.getDirectionalProperties(direction);
		// Get required independent values.
		float defaultTORA = closeTo.getDefaultTORA();
		float displacementFromEnd = runway.getObstacle().getDisplacementFromThreshold(direction);
		float displacementThreshold = closeTo.getDefaultDisplacementThreshold();
		float clearway = closeTo.getClearway();
		float stopway = closeTo.getStopway();
		// Perform the calculations.
		float resultTORA = defaultTORA - blastProtection - displacementFromEnd
				- displacementThreshold;
		float resultASDA = resultTORA + stopway;
		float resultTODA = resultTORA + clearway;
		// Log the calculation history.
		CalculationSection section = new CalculationSection("Calculation of TORA, TODA and ASDA: (Away from obstacle)");
			// TORA calculation.
			section.addLine("(R)TORA = (Original)TORA - Blast Protection - Dist. from THR - Displaced THR");
			section.addLine(String.format("(R)TORA = %.2f - %.2f - %.2f - %.2f",
					defaultTORA,
					blastProtection,
					displacementFromEnd,
					displacementThreshold));
			section.addLine(String.format("(R)TORA = %.2f", resultTORA));
			// TODA calculation.
			section.addLine("(R)TODA = (R) TORA + CLEARWAY");
			section.addLine(String.format("(R)TODA = %.2f + %.2f", resultTORA,
					clearway));
			section.addLine(String.format("(R)TODA = %.2f", resultTODA));
			// ASDA calculation.
			section.addLine("(R)ASDA = (R) TORA + STOPWAY");
			section.addLine(String.format("(R)ASDA = %.2f + %.2f", resultTORA,
					stopway));
			section.addLine(String.format("(R)ASDA = %.2f", resultASDA));
		calculationHistory.add(section);
		// Set the TORA, ASDA and TODA.
		redeclaredTORA = resultTORA;
		redeclaredASDA = resultASDA;
		redeclaredTODA = resultTODA;
	}

	/*
	 * Calculates the TORA, TODA and ASDA when taking-off towards an obstacle.
	 * TORA = Distance from Threshold - Slope Calculation - Strip End
	 * ASDA = (R)TORA
	 * TODA = (R)TORA 
	 */
	private void calculateTakeOffTowardsObstacle(Runway runway, RunwayDesignator direction, int slopeRatio) {
		RunwayDirectionalProperties closeTo = runway.getDirectionalProperties(direction);
		RunwayDirectionalProperties awayFrom = runway.getDirectionalProperties(direction.getOpposite());
		// Get required independent values.
		float slopeLength = getSlopeLength();
		float displacementFromEnd = runway.getObstacle().getDisplacementFromThreshold(direction);
		float stripEnd = awayFrom.getStripEnd();
		float displacementThreshold = closeTo.getDefaultDisplacementThreshold();
		// Perform the calculations.
		float calcTORA = displacementFromEnd
						+ displacementThreshold
						- slopeLength
						- stripEnd;
		float calcTODA = calcTORA;
		float calcASDA = calcTORA;
		// Check lengths are at most the original values
		float resultTORA = Math.max(0, Math.min(calcTORA, closeTo.getDefaultTORA()));
		float resultTODA = Math.max(0, Math.min(calcTODA, closeTo.getDefaultTODA()));
		float resultASDA = Math.max(0, Math.min(calcASDA, closeTo.getDefaultASDA()));
		// Log the calculation history.
		CalculationSection section = new CalculationSection("Calculation of TORA, TODA and ASDA: (Towards obstacle)");
			// TORA calculation.
			section.addLine("(R)TORA = Distance from Threshold + displacement threshold - Slope Calculation - Strip End");
			section.addLine(String.format("(R)TORA = %.2f + %.2f - %.2f - %.2f",
					displacementFromEnd,
					displacementThreshold,
					slopeLength,
					stripEnd));
			section.addLine(String.format("(R)TORA = %.2f",  calcTORA));
			if( resultTORA == 0 ){
				section.addLine(String.format("(R)TORA cannot be negative!"));
				section.addLine(String.format("(R)TORA = %.2f", resultTORA));
			}
			else if (resultTORA != calcTORA){
				section.addLine(String.format("(R)TORA cannot exceed original TORA!"));
				section.addLine(String.format("(R)TORA = %.2f", resultTORA));
			}
			section.addLine("");
			// TODA calculation.
			section.addLine("(R)TODA = (R)TORA");
			section.addLine(String.format("(R)TODA = %.2f", calcTODA));
			if( resultTODA == 0 ){
				section.addLine(String.format("(R)TODA cannot be negative!"));
				section.addLine(String.format("(R)TODA = %.2f", resultTODA));
			}
			else if (resultTODA != calcTODA){
				section.addLine(String.format("(R)TODA cannot exceed original TODA!"));
				section.addLine(String.format("(R)TODA = %.2f", resultTODA));
			}
			section.addLine("");
			// ASDA calculation.
			section.addLine("(R)ASDA = (R)TORA");
			section.addLine(String.format("(R)ASDA = %.2f", calcASDA));
			if( resultASDA == 0 ){
				section.addLine(String.format("(R)ASDA cannot be negative!"));
				section.addLine(String.format("(R)ASDA = %.2f", resultASDA));
			}
			else if (resultASDA != calcASDA){
				section.addLine(String.format("(R)ASDA cannot exceed original ASDA!"));
				section.addLine(String.format("(R)ASDA = %.2f", resultTODA));
			}
			section.addLine("");
		calculationHistory.add(section);
		// Set the TORA, TODA and ASDA.
		redeclaredTORA = resultTORA;
		redeclaredTODA = resultTODA;
		redeclaredASDA = resultASDA;
	}
	
	/*
	 * Gets the slope length of the obstacle.
	 */
	public float getSlopeLength() {
		return slopeLength;
	}
	
	/*
	 * Sets the slope length of the obstacle
	 */
	public void setSlopeLength(float slopeLength) {
		this.slopeLength = slopeLength;
	}
	
	/*
	 * Sets whether the slope effected the calculations.
	 */
	public void setSlopeRequired(boolean slopRequired) {
		this.sloprRequired = slopRequired;
	}
	


	/*
	 * Gets the result's calculation history.
	 */
	public CalculationHistory getCalculationHistory() {
		return calculationHistory;
	}
	
	/*
	 * Get's whether the strip used is before the obstacle.
	 */
	public boolean isBeforeObstacle() {
		return isBeforeObstacle;
	}


}
