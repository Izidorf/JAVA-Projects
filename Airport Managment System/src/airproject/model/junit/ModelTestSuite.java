package airproject.model.junit;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({RunwayDesignatorUnitTest.class, RunwayUnitTest.class, RunwayResultsUnitTest.class, AirportUnitTest.class, ImportExportUnitTest.class})
public class ModelTestSuite {
	//Nothing goes here
}
