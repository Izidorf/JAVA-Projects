
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Database {

	Fractal fractal;
	BufferedReader reader;
	HashMap<String, Complex> favouriteSets; //Data from the file stored the hashmap
	ArrayList<String> keys; //Stores keys from the above hash set
	
	public Database(){
		try {
			this.favouriteSets = readData();
			this.keys =fillNames();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //populate the hashmap with data
		
	}
	
	public Database(Fractal fractal) {
		this.fractal = fractal;
			try {
			this.favouriteSets = readData(); //populate the hashmap with data
			this.keys =fillNames();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void writeInFile(){
		try {
			FileOutputStream out = new FileOutputStream("saveFractals.txt", true); //this method appends text
			PrintStream ps = new PrintStream(out);
			ps.println(fractal.getClass()+":"+fractal.clickedX+":"+fractal.clickedY);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getLine(){
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "false";
		}
	}

	//This method reads data from the file and returns the hasmap with it
	public HashMap<String, Complex> readData() throws IOException{

		String[] data;
		HashMap<String, Complex> favourite = new HashMap<String, Complex>();
		try {
			reader = new BufferedReader(new FileReader("saveFractals.txt"));
			String line;
			int count=1;
		      while ((line = getLine()) != null) { //read lines until the line is empty
		    	data = line.split(":"); //Split into array where :
		  		
				Complex complex = new Complex(Double.parseDouble(data[1]), Double.parseDouble(data[2]));

				
				favourite.put("JuliaSet"+count, complex);
				count++;
			}	
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		return favourite;

	}

	public HashMap<String, Complex> getfavourites(){
		return this.favouriteSets;
	}

	public ArrayList<String> fillNames(){
		ArrayList<String> arr = new ArrayList<String>();
		Iterator<String> it1 = this.favouriteSets.keySet().iterator();
		//now loop throug hashMap and print out keys and the number of times the word appears
		int count =0;
		while(it1.hasNext()) {			
			String f = it1.next();
			
			arr.add(f);
			count++;
		}
		return arr;
	}
	
	public ArrayList<String> getNames(){
		return this.keys;
	}


}
