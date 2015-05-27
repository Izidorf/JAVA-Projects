package airproject.model.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import airproject.model.Airport;
import airproject.model.Obstacle;
import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;
import airproject.model.RunwayResults;
import airproject.model.RunwaySide;

public class RunwayResultsUnitTest {
	
	static final float BLAST_PROTECTION = 300f;
	
	Airport airport;
	Runway runway1, runway2;
	
	@Before
	public void pre(){
		//First Create an Airport
		airport = new Airport("Heathrow", "HRW");
		//Create runway 09R/27L
		runway1 = new Runway(
				new RunwayDesignator(9, RunwaySide.RIGHT),
				new RunwayDirectionalProperties(307f, 3660.0f, 3660.0f, 3660.0f, 3353.0f, 240.0f), //Logical representation of 09R
				new RunwayDirectionalProperties(0.0f, 3660.0f, 3660.0f, 3660.0f, 3660.0f, 240.0f)); //Logical representation of  27L
		//Create runway 09L/27R
		runway2 = new Runway(
				new RunwayDesignator(9, RunwaySide.LEFT),
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f), //Logical representation of 09L
				new RunwayDirectionalProperties(0.0f, 3884.0f, 3962.0f, 3884.0f, 3884.0f, 240.0f)); //Logical representation of 27L
		//Add both runways to the airport
		airport.addRunway(runway1);
		airport.addRunway(runway2);
	}
	
	@After
	public void post(){
		airport = null;
		runway1 = null;
		runway2 = null;
	}
	
	@Test
	public void ScenarioOne(){
		// Add the obstacle.
		Obstacle obstacle = new Obstacle(12.0f, -50.0f, 3646f, 0.0f);
		runway2.setObstacle(obstacle);
		// Get the two result sets.
		RunwayResults resultsHigh = runway2.recalculate(runway2.getHighestEquivalentDesignator(), BLAST_PROTECTION, 50);
		RunwayResults resultsLow = runway2.recalculate(runway2.getLowestEquivalentDesignator(), BLAST_PROTECTION, 50);
		// Print the results.
		resultsLow.getCalculationHistory().printTo(System.out);
		resultsHigh.getCalculationHistory().printTo(System.out);
		// Check the low results (9R)
		assertTrue(resultsLow.getRedeclaredTORA() == 3346f);
		assertTrue(resultsLow.getRedeclaredTODA() == 3346f);
		assertTrue(resultsLow.getRedeclaredASDA() == 3346f);
		assertTrue(resultsLow.getRedeclaredLDA()  == 2985f);
		// Check the high results (27L)
		assertTrue(resultsHigh.getRedeclaredTORA() == 2986f);
		assertTrue(resultsHigh.getRedeclaredTODA() == 2986f);
		assertTrue(resultsHigh.getRedeclaredASDA() == 2986f);
		assertTrue(resultsHigh.getRedeclaredLDA()  == 3346f);
	}
	
	@Test
	public void ScenarioTwo(){
		// Add the obstacle.
		Obstacle obstacle = new Obstacle(25.0f, 2853.0f, 500.0f, 20.0f);
		runway1.setObstacle(obstacle);
		// Get the two result sets.
		RunwayResults resultsHigh = runway1.recalculate(runway1.getHighestEquivalentDesignator(), BLAST_PROTECTION, 50);
		RunwayResults resultsLow = runway1.recalculate(runway1.getLowestEquivalentDesignator(), BLAST_PROTECTION, 50);
		// Print the results.
		resultsLow.getCalculationHistory().printTo(System.out);
		resultsHigh.getCalculationHistory().printTo(System.out);
		// Check the low results (9L)
		assertTrue(resultsLow.getRedeclaredTORA() == 1850f);
		assertTrue(resultsLow.getRedeclaredTODA() == 1850f);
		assertTrue(resultsLow.getRedeclaredASDA() == 1850f);
		assertTrue(resultsLow.getRedeclaredLDA()  == 2553f);
		// Check the high results (27R)
		assertTrue(resultsHigh.getRedeclaredTORA() == 2860f);
		assertTrue(resultsHigh.getRedeclaredTODA() == 2860f);
		assertTrue(resultsHigh.getRedeclaredASDA() == 2860f);
		assertTrue(resultsHigh.getRedeclaredLDA()  == 1850f);
	}
	
	@Test
	public void ScenarioThree(){
		// Add the obstacle
		Obstacle obstacle = new Obstacle(15f, 150f, 3203f, -60f);
		runway1.setObstacle(obstacle);
		// Get the two result sets
		RunwayResults resultsHigh = runway1.recalculate(runway1.getHighestEquivalentDesignator(), BLAST_PROTECTION, 50);
		RunwayResults resultsLow = runway1.recalculate(runway1.getLowestEquivalentDesignator(), BLAST_PROTECTION, 50);
		// Print the results.
		resultsLow.getCalculationHistory().printTo(System.out);
		resultsHigh.getCalculationHistory().printTo(System.out);
		// Check the low results (9L)
		assertTrue(resultsLow.getRedeclaredTORA() == 2903f);
		assertTrue(resultsLow.getRedeclaredTODA() == 2903f);
		assertTrue(resultsLow.getRedeclaredASDA() == 2903f);
		assertTrue(resultsLow.getRedeclaredLDA()  == 2393f);
		// Check the high results (27R)
		assertTrue(resultsHigh.getRedeclaredTORA() == 2393f);
		assertTrue(resultsHigh.getRedeclaredTODA() == 2393f);
		assertTrue(resultsHigh.getRedeclaredASDA() == 2393f);
		assertTrue(resultsHigh.getRedeclaredLDA()  == 2903f);
	}

	
	@Test
	public void ScenarioFour(){
		// Add the obstacle.
		Obstacle obstacle = new Obstacle(20.0f, 3546f, 50.0f, 20.0f);
		runway2.setObstacle(obstacle);
		// Get the two result sets.
		RunwayResults resultsHigh = runway2.recalculate(runway2.getHighestEquivalentDesignator(), BLAST_PROTECTION, 50);
		RunwayResults resultsLow = runway2.recalculate(runway2.getLowestEquivalentDesignator(), BLAST_PROTECTION, 50);
		// Print the results.
		resultsLow.getCalculationHistory().printTo(System.out);
		resultsHigh.getCalculationHistory().printTo(System.out);
		// Check the low results (9R)
		assertTrue(resultsLow.getRedeclaredTORA() == 2792f);
		assertTrue(resultsLow.getRedeclaredTODA() == 2792f);
		assertTrue(resultsLow.getRedeclaredASDA() == 2792f);
		assertTrue(resultsLow.getRedeclaredLDA()  == 3246f);
		// Check the high results (27L)
		assertTrue(resultsHigh.getRedeclaredTORA() == 3534f);
		assertTrue(resultsHigh.getRedeclaredTODA() == 3534f);
		assertTrue(resultsHigh.getRedeclaredASDA() == 3534f);
		assertTrue(resultsHigh.getRedeclaredLDA()  == 2774f);
	}
	
}
