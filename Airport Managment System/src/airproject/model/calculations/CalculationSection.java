package airproject.model.calculations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CalculationSection implements Iterable<String> {
	
	// List of strings within the calculations section.
	private List<String> calculationLines;
	private String tag;
	
	public CalculationSection(String tag) {
		calculationLines = new ArrayList<>();
		this.tag = tag;
	}
	
	/*
	 * Adds a line to the calculation section.
	 */
	public void addLine(String line){
		calculationLines.add(line);
	}
	
	/*
	 * Returns an iterator through the sections strings.
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<String> iterator() {
		return calculationLines.iterator();
	}
	
	/*
	 * gets the section's tag.
	 */
	public String getTag() {
		return tag;
	};
	
}
