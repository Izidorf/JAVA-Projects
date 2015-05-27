package airproject.model.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import airproject.model.Obstacle;
import airproject.model.RunwayDesignator;
import airproject.model.RunwaySide;

public class ObstacleUnitTest {

	private Obstacle obstacle;
	private Obstacle obstacle1;
	private Obstacle obstacle2;
	private RunwayDesignator desig;

	@Before
	public void test() {
		desig = new RunwayDesignator(23, RunwaySide.CENTER);
		obstacle = new Obstacle(150, 10f, 190f, 20);
		obstacle1 = new Obstacle(150, 10f, 190f, 20);
		obstacle2 =  new Obstacle(120, 190f, 11f, 20);
	}
	
	@Test
	public void testDesignatorDistances(){
		System.out.println(obstacle1.getDisplacementFromThreshold(new RunwayDesignator(5)));
		assertTrue(10f == obstacle1.getDisplacementFromThreshold(new RunwayDesignator(5)));
		assertTrue(190f == obstacle1.getDisplacementFromThreshold(new RunwayDesignator(25)));
		assertTrue(190f == obstacle2.getDisplacementFromThreshold(new RunwayDesignator(5)));
		assertTrue(11f == obstacle2.getDisplacementFromThreshold(new RunwayDesignator(25)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testBadHeight() {
		obstacle.setHeight(-20);
	}
	
	@Test
	public void testEquals() {
		assertTrue(obstacle.equals(obstacle1));
	}
	
	@Test
	public void testNotEquals() {
		assertFalse(obstacle.equals(obstacle2));
	}
	
	@Test
	public void testHashCode() {
		assertTrue(obstacle.hashCode() == obstacle1.hashCode());
		assertTrue(obstacle.hashCode() != obstacle2.hashCode());
	}
	
	@Test
	public void testGetHeight() {
		assertTrue(obstacle.getHeight() == obstacle1.getHeight());
	}

}
