package airproject.model.calculations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CalculationHistory implements Iterable<CalculationSection> {
	
	private String tag;
	private List<CalculationSection> sectionList;

	public CalculationHistory(String tag) {
		this.tag = tag;
		// Initialise the calculation history map.
		sectionList = new ArrayList<>();
	}
	
	/*
	 * Add a section to the calculations history
	 */
	public void add( CalculationSection section){
		sectionList.add(section);
	}
	
	/*
	 * Iterate's through the calculation's sections.
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<CalculationSection> iterator() {
		return sectionList.iterator();
	}
	
	/*
	 * Gets the number of sections in the history.
	 */
	public int numSections(){
		return sectionList.size();
	}
	
	/*
	 * Prints the calculation history to an output stream.
	 */
	public void printTo(PrintStream pw){
		pw.println("--------------------------------------------------------------");
		pw.println("-- Calculation: "+getTag());
		pw.println("--------------------------------------------------------------");
		pw.println();
		// Iterate through all sections.
		for( CalculationSection section : this ){
			pw.println("\t--------------------------------------------------------------");
			pw.println("\t-- "+section.getTag());
			pw.println("\t--------------------------------------------------------------");
			pw.println();
			// Iterate through all steps.
			for( String step : section ){
				pw.println("\t\t"+step);
			}
			pw.println();
		}
	}
	
	/*
	 * Save the calculation text to a file.
	 */
	public void saveToFile(File file) throws FileNotFoundException{
		PrintStream ps = new PrintStream(file);
		printTo(ps);
		ps.flush();
	}
	
	/*
	 * Get the calculation history's tag.
	 */
	public String getTag() {
		return tag;
	}

	public String buildString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--------------------------------------------------------------\n");
		sb.append("-- Calculation: "+getTag()+"\n");
		sb.append("--------------------------------------------------------------\n");
		sb.append("\n");
		// Iterate through all sections.
		for( CalculationSection section : this ){
			sb.append("\t--------------------------------------------------------------\n");
			sb.append("\t-- "+section.getTag()+"\n");
			sb.append("\t--------------------------------------------------------------\n");
			sb.append("\n");
			// Iterate through all steps.
			for( String step : section ){
				sb.append("\t\t"+step+"\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
