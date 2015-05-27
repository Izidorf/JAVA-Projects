package airproject.model.junit;
import org.junit.Test;
import static org.junit.Assert.*;

import airproject.model.RunwayDesignator;
import airproject.model.RunwaySide;
public class RunwayDesignatorUnitTest {
	
	/*
	 * Tests construction of a designator object with an angle above the valid range.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateTooHighAngle(){
		RunwayDesignator desig = new RunwayDesignator(37, RunwaySide.NONE);
	}
	
	/*
	 * Tests construction of a designator object with an angle above the valid range.
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateTooLowAngle(){
		RunwayDesignator desig = new RunwayDesignator(0, RunwaySide.NONE);
	}
	
	/*
	 * Tests the equals() method of a designator object.
	 */
	@Test
	public void testEquals(){
		// Create some test designators.
		RunwayDesignator d1 = new RunwayDesignator(30, RunwaySide.LEFT);
		RunwayDesignator d2 = new RunwayDesignator(30, RunwaySide.NONE);
		RunwayDesignator d3 = new RunwayDesignator(30, RunwaySide.CENTER);
		RunwayDesignator d4 = new RunwayDesignator(30, RunwaySide.RIGHT);
		RunwayDesignator d5 = new RunwayDesignator(12, RunwaySide.LEFT);
		RunwayDesignator d6 = new RunwayDesignator(12, RunwaySide.RIGHT);
		RunwayDesignator d7 = new RunwayDesignator(30, RunwaySide.LEFT);
		
		// Perform assertions.
		
		assertTrue(d1.equals(d7));
		assertTrue(d1.equals(d1));
		assertFalse(d1.equals(d2));
		assertFalse(d1.equals(d3));
		assertFalse(d1.equals(d4));
		assertFalse(d1.equals(d5));
		assertFalse(d1.equals(d6));
	}
	
	/*
	 * Tests the getOpposite() method of a designator object.
	 */
	@Test
	public void testOpposites(){
		// Create some test designators.
		RunwayDesignator d1 = new RunwayDesignator(30, RunwaySide.LEFT);
		RunwayDesignator d2 = new RunwayDesignator(5, RunwaySide.NONE);
		RunwayDesignator d3 = new RunwayDesignator(27, RunwaySide.CENTER);
		RunwayDesignator d4 = new RunwayDesignator(1, RunwaySide.RIGHT);
		// Calculate their opposites.
		RunwayDesignator o1 = d1.getOpposite();
		RunwayDesignator o2 = d2.getOpposite();
		RunwayDesignator o3 = d3.getOpposite();
		RunwayDesignator o4 = d4.getOpposite();
		// Perform assertions.
		assertEquals(RunwaySide.RIGHT, o1.getSide());
		assertEquals(RunwaySide.NONE, o2.getSide());
		assertEquals(RunwaySide.CENTER, o3.getSide());
		assertEquals(RunwaySide.LEFT, o4.getSide());
		assertEquals(12, o1.getAngle());
		assertEquals(23, o2.getAngle());
		assertEquals(9, o3.getAngle());
		assertEquals(19, o4.getAngle());
	}
	
	/*
	 * Test the isHigh method of the runway designator.
	 */
	@Test
	public void testIsHigh(){
		//Test high is working
		RunwayDesignator d1 = new RunwayDesignator(20, RunwaySide.NONE);
		assertEquals(true, d1.isHigh());
		//Test low is working
		RunwayDesignator d2 = new RunwayDesignator(5, RunwaySide.NONE);
		assertEquals(false, d2.isHigh());
		//Border cases check: 1 - low
		RunwayDesignator d3 = new RunwayDesignator(1, RunwaySide.NONE);
		assertEquals(false, d3.isHigh());
		//Border cases check: 18 - low
		RunwayDesignator d4 = new RunwayDesignator(18, RunwaySide.NONE);
		assertEquals(false, d4.isHigh());
		//Border cases check: 19 - high
		RunwayDesignator d5 = new RunwayDesignator(19, RunwaySide.NONE);
		assertEquals(true, d5.isHigh());
		//Border cases check: 36 - high
		RunwayDesignator d6 = new RunwayDesignator(36, RunwaySide.NONE);
		assertEquals(true, d6.isHigh());
	}
	
}
