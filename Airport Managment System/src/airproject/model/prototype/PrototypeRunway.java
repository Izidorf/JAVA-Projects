package airproject.model.prototype;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import airproject.model.Obstacle;
import airproject.model.Runway;
import airproject.model.RunwayDesignator;
import airproject.model.RunwayDirectionalProperties;
import airproject.model.RunwayResults;
import airproject.model.calculations.CalculationHistory;

/*
 * Used to demonstrate calculations to the user.
 */
public class PrototypeRunway {
	
	public static void main(String[] args) throws IOException {
		// Create input stream buffered reader.
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// 
		System.out.println("Please input the runway designator: (e.g. 09L)");
		RunwayDesignator runway_desig = RunwayDesignator.parseDesignator(br.readLine());
		if( runway_desig.isHigh() ){
			runway_desig = runway_desig.getOpposite();
		}
		System.out.println("For the side "+runway_desig+", please input landing threshold:");
		float displacementThreshold1 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig+", please input default TORA:");
		float TORA1 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig+", please input default TODA:");
		float TODA1 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig+", please input default ASDA:");
		float ASDA1 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig+", please input default LDA:");
		float LDA1 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig+", please input RESA (default=240m):");
		float RESA1 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig.getOpposite()+", please input landing threshold:");
		float displacementThreshold2 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig.getOpposite()+", please input default TORA:");
		float TORA2 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig.getOpposite()+", please input default TODA:");
		float TODA2 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig.getOpposite()+", please input default ASDA:");
		float ASDA2 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig.getOpposite()+", please input default LDA:");
		float LDA2 = Float.parseFloat(br.readLine());
		System.out.println("For the side "+runway_desig.getOpposite()+", please input RESA (default=240m):");
		float RESA2 = Float.parseFloat(br.readLine());
		System.out.println("For the obstacle, please input height:");
		float height = Float.parseFloat(br.readLine());
		System.out.println("For the obstacle, please input displacement from the lower designator:");
		float displacementAlongLow = Float.parseFloat(br.readLine());
		System.out.println("For the obstacle, please input displacement from the higher designator:");
		float displacementAlongHigh = Float.parseFloat(br.readLine());
		System.out.println("For the obstacle, please input displacement from centerline:");
		float displacementOutwards = Float.parseFloat(br.readLine());
		// Create the runway
		RunwayDirectionalProperties d1 = new RunwayDirectionalProperties(
				displacementThreshold1, TORA1, TODA1, ASDA1, LDA1, RESA1);
		RunwayDirectionalProperties d2 = new RunwayDirectionalProperties(
				displacementThreshold2, TORA2, TODA2, ASDA2, LDA2, RESA2);
		Runway runway = new Runway(runway_desig, d1, d2);
		Obstacle obstacle = new Obstacle(height, displacementAlongLow, displacementAlongHigh, displacementOutwards);
		runway.setObstacle(obstacle);
		// Do the calculations.
		while( true ){
			System.out.println("Which designator would you like to calculate for?");
			RunwayDesignator d_c = RunwayDesignator.parseDesignator(br.readLine());
			try{
			RunwayResults rr = runway.recalculate(d_c, 300f, 50);
			CalculationHistory calculationHistory = rr.getCalculationHistory();
			calculationHistory.printTo(System.out);
			}catch(Exception e ){}
		}
	}

}
