package airproject.model.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import airproject.model.Airport;
import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;

public class AirportUnitTest {
	
	Airport a1;
	RunwayDesignator d1;
	RunwayDesignator d2;
	RunwayDirectionalProperties p1;
	RunwayDirectionalProperties p2;
	Runway r1;
	Runway r2;
	
	
	@Before
	public void init(){
		a1 = new Airport("Heathrow", "H");
		d1  = new RunwayDesignator(12);
		d2  = new RunwayDesignator(15);
		p1 = new RunwayDirectionalProperties(306.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f);
		p2 = new RunwayDirectionalProperties(316.0f, 3902.0f, 3902.0f, 3902.0f, 3595.0f, 240.0f);
		r1 = new Runway(d1, p1, p1);
		r2 = new Runway(d1, p1, p1);
	}

	/*
	 * Checking uniqueness of runways added
	 */
	@Test
	public void testHashSetAdd(){
		a1.addRunway(r1);
		assertFalse(a1.addRunway(r2));
	}
	
	/*
	 * Checking runway remove method
	 */
	@Test
	public void testHashSetRemove(){
		a1.addRunway(r1);
		assertTrue(a1.removeRunway(r2));
	}
	
	/*
	 * Checking correctness of size method
	 */
	@Test
	public void testHashSetSize(){
		a1.addRunway(r1);
		assertEquals(1, a1.getRunwayCount());
	}
	
	/*
	 * Checking correctness of get NAME method
	 */
	@Test
	public void testGetName(){
		assertEquals("Heathrow", a1.getName());
	}
	
	/*
	 * Checking correctness of get CODE method
	 */
	@Test
	public void testGetCode(){
		assertEquals("H", a1.getCode());
	}
	
	/*
	 * Testing correctness of iterator
	 */
	@Test
	public void testIterator(){
		r1 = new Runway(d1, p1, p1);
		r2 = new Runway(d2, p2, p2);
		a1.addRunway(r1);
		a1.addRunway(r2);
		
		Iterator<Runway> iter = a1.iterator();
		
		while(iter.hasNext()){
			Runway r = iter.next();
			assertEquals(75, Math.round(r.getWidth()));
		}
	}
	
	/*
	 * Testing MULTI Input
	 */
	@Test
	public void testMultipleEntry(){
		RunwayDesignator d;
		RunwayDirectionalProperties p1;
		RunwayDirectionalProperties p2;
		Runway r;
		
		for(int i = 1; i<100001; i++){
			d = new RunwayDesignator(i%36==0 ? 1 : i%36);
			p1 = new RunwayDirectionalProperties((float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()), (float) (i*Math.random()), (float) (i*Math.random()));
			p2 = new RunwayDirectionalProperties((float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()), (float) (i*Math.random()), (float) (i*Math.random()));
			r = new Runway(d, p1, p2);
			
			a1.addRunway(r);
		}
		assertEquals(100000, a1.getRunwayCount());
	}
	
	/*
	 * Testing MULTI Output
	 */
	@Test
	public void testMultiRemoval(){
		RunwayDesignator d;
		RunwayDirectionalProperties p1;
		RunwayDirectionalProperties p2;
		Runway r;
		ArrayList<Runway> arrList = new ArrayList<Runway>();
		
		for(int i = 1; i<100001; i++){
			d = new RunwayDesignator(i%36==0 ? 1 : i%36);
			p1 = new RunwayDirectionalProperties((float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()), (float) (i*Math.random()), (float) (i*Math.random()));
			p2 = new RunwayDirectionalProperties((float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()),(float) (i*Math.random()), (float) (i*Math.random()), (float) (i*Math.random()));
			r = new Runway(d, p1, p2);
			
			a1.addRunway(r);
			arrList.add(r);
		}
		
		for(int i = 0; i<100000; i++){
			a1.removeRunway(arrList.get(i));
		}
		
		assertEquals(0, a1.getRunwayCount());
	
	}
}
