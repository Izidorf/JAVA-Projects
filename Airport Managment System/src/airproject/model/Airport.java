package airproject.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Represents an airport which is a collection of runways.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Airport implements Iterable<Runway> {
	
	
	private String name;
	private String code;
	
	@XmlElementWrapper
	@XmlElement(name = "runway")
	private Set<Runway> runways; // Set collection used to ensure uniqueness.
	
	/*
	 * Need default no-arg constructors in these classes so JAXB's unmarshalling can work
	 */
	private Airport() {
	}
	
	public Airport(String name, String code) {
		this.name=name;
		this.code=code;
		runways = new HashSet<Runway>();
	}
	
	/*
	 * Adds a runway to the airport.
	 */
	public boolean addRunway(Runway runway){
		boolean b = runways.add(runway);
		update();
		return b;
	}
	
	/*
	 * Removes a runway from the airport.
	 */
	public boolean removeRunway(Runway runway){
		boolean b = runways.remove(runway);
		update();
		return b;
	}
	
	/*
	 * Gets the number of runways in the airport.
	 */
	public int getRunwayCount(){
		return runways.size();
	}
	
	public Set<Runway> getRunways(){
		return runways;
	}
	
	/*
	 * Gets an iterator for the contained runways.
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Runway> iterator(){
		return runways.iterator();
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode() {
		return code;
	}
	
	
	/*
	 * Called whenever the airport is updated
	 */
	private void update(){
		// Alert the listener of an updated airport.
		Model.getInstance().getListener().onAirportUpdate(this);
	}
	
}
