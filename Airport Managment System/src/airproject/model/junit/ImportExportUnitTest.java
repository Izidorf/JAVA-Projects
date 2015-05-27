package airproject.model.junit;

import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.*;

import airproject.model.Airport;
import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;
import airproject.model.RunwaySide;
import airproject.model.io.XMLIO;

public class ImportExportUnitTest {

	@Test
	public void testXMLImport() {
		// First Create an Airport
		Airport airport = new Airport("Heathrow", "HRW");
		// Create runway 09R/27L
		Runway runway1 = new Runway(new RunwayDesignator(9, RunwaySide.RIGHT),
				new RunwayDirectionalProperties(307f, 3660.0f, 3660.0f,
						3660.0f, 3353.0f, 240.0f), // Logical representation of
													// 09R
				new RunwayDirectionalProperties(0.0f, 3660.0f, 3660.0f,
						3660.0f, 3660.0f, 240.0f)); // Logical representation of
		// Add both runways to the airport
		airport.addRunway(runway1);
		XMLIO.writeXml(Airport.class, airport);
		Airport airport2 = XMLIO.readXml(Airport.class);
		assertEquals(airport.getCode(), airport2.getCode());
		assertEquals(airport.getName(), airport2.getName());
		assertEquals(airport.getRunwayCount(), airport.getRunwayCount());
		Iterator<Runway> it1 = airport.iterator(), it2 = airport2.iterator();
			Runway r1 = it1.next();
			Runway r2 = it2.next();
			assertTrue( r1.getDistCleared() == r2.getDistCleared() );
			assertTrue( r1.getDistGraded() == r2.getDistGraded() );
			assertTrue( r1.getWidth() == r2.getWidth() );
			assertTrue( r1.getLowestEquivalentDesignator().equals(r2.getLowestEquivalentDesignator()) );
	}

}
