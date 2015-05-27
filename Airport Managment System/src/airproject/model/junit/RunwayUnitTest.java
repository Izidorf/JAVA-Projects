package airproject.model.junit;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import airproject.model.Airport;
import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;
import airproject.model.RunwaySide;
import static org.junit.Assert.*;

public class RunwayUnitTest {
	
	Runway r1;
	Runway r2;
	
	/*
	 * Set up the test objects.
	 */
	@Before
	public void pre(){
		r1 = new Runway(new RunwayDesignator(9, RunwaySide.RIGHT), 
				new RunwayDirectionalProperties(307.5f, 3660.0f, 3660.0f, 3660.0f, 3353.0f, 240.0f),
				new RunwayDirectionalProperties(0.0f, 3660.0f, 3660.0f, 3660.0f, 3353.0f, 240.0f));
		r2 = new Runway(new RunwayDesignator(9, RunwaySide.LEFT), 
						new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f),
						new RunwayDirectionalProperties(0.0f, 3884.0f, 3962.0f, 3884.0f, 3884.0f, 240.0f));
	}
	
	/*
	 * Make sure test objects are dereferenced.
	 */
	@After
	public void post(){
		r1 = null;
		r2 = null;
	}
	
	/*
	 * Check illegal runway parameters cause exceptions as expected
	 */
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalWidthZero(){
		Runway r3 = new Runway(
				new RunwayDesignator(9, RunwaySide.RIGHT),
				0f,
				105f,
				75f,
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f),
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalGradedZero(){
		Runway r3 = new Runway(
				new RunwayDesignator(9, RunwaySide.RIGHT),
				7f,
				0f,
				75f,
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f),
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalClearedZero(){
		Runway r3 = new Runway(
				new RunwayDesignator(9, RunwaySide.RIGHT),
				7f,
				105f,
				0f,
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f),
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalWidthNegative(){
		Runway r3 = new Runway(
				new RunwayDesignator(9, RunwaySide.RIGHT),
				-1f,
				105f,
				75f,
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f),
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testIllegalGradedNegative(){
		Runway r3 = new Runway(
				new RunwayDesignator(9, RunwaySide.RIGHT),
				7f,
				-1f,
				75f,
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f),
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIllegalShadedNegative(){
		Runway r3 = new Runway(
				new RunwayDesignator(9, RunwaySide.RIGHT),
				7f,
				105f,
				-1f,
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f),
				new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f));
	}
	
	/*
	 * Check designator related operations
	 */
	@Test
	public void testDesignator(){
		assertEquals(new RunwayDesignator(9, RunwaySide.RIGHT), r1.getLowestEquivalentDesignator());
		assertEquals(new RunwayDesignator(27, RunwaySide.LEFT), r1.getHighestEquivalentDesignator());
		System.out.println(r2.getLowestEquivalentDesignator());
		assertEquals(new RunwayDesignator(9, RunwaySide.LEFT), r2.getLowestEquivalentDesignator());
		assertEquals(new RunwayDesignator(27, RunwaySide.RIGHT), r2.getHighestEquivalentDesignator());
		
	}
	
}
