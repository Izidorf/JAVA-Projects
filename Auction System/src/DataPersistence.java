import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;


public class DataPersistence {

	BufferedReader reader;
	String usersFile = "users.csv";
	String itemsFile = "items.csv";
	String bidsFile = "bids.csv";
	String logsFile = "logs.txt";
	String reportFile = "report.txt";

	public void SaveUser(ArrayList<User> users){
		createFile(usersFile);

		try {
			FileOutputStream out = new FileOutputStream(usersFile); //this method does not append text...add true if you want it to
			PrintStream ps = new PrintStream(out);
			for(User u : users){
				ps.println(u.getFirstName()+","+u.getFamilyName()+","+u.getUsername()+","+u.getPassword());

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void SaveItems(ArrayList<Item> items){
		createFile(itemsFile);

		try {
			FileOutputStream out = new FileOutputStream(itemsFile); //this method does not append text...add true if you want it to
			PrintStream ps = new PrintStream(out);
			for(Item i : items){
				ps.println(i.getItemID()+","+i.getVendorID()+","+i.getTitle()+","+i.getReservedPrice()+","+i.getCategoryChosen()+","+i.getStartTime()+","+i.getEndTime()+","+i.getDescription());

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	public void SaveBids(ArrayList<Bid>  bids){
		createFile(bidsFile);

		try {
			FileOutputStream out = new FileOutputStream(bidsFile); //this method does not append text...add true if you want it to
			PrintStream ps = new PrintStream(out);
			for(Bid b : bids){
				ps.println(b.getBidderID()+","+b.getItemID()+","+b.getBid());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	 public void SaveLog(String log){
		createFile(logsFile);
		
		try {
			FileOutputStream out = new FileOutputStream(logsFile); //this method does not append text...add true if you want it to
			PrintStream ps = new PrintStream(out);
				ps.println(log);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	 
	 public void makeReport(String report){
			createFile(reportFile);
			
			try {
				FileOutputStream out = new FileOutputStream(reportFile); //this method does not append text...add true if you want it to
				PrintStream ps = new PrintStream(out);
					ps.println(report);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}

	public ArrayList<User> getUsers(){
		ArrayList<User> users = new ArrayList<User>();
		createFile(usersFile);
		try {
			String[] data;
			reader = new BufferedReader(new FileReader(usersFile));

			String strLine;
			while((strLine = getLine()) != null){
				data = strLine.split(",");

				User user = new User(data[0], data[1],data[2],data[3]);
				users.add(user);

			}
		}catch (IOException e){
			if(Main.debug)System.err.println(e);
		}

		return users;
	}

	//reads items from the csv file, stores them in array list and returns them
	public ArrayList<Item> getItems(){
		ArrayList<Item> items = new ArrayList<Item>();
		createFile(itemsFile);
		try {
			String[] data;
			reader = new BufferedReader(new FileReader(itemsFile));

			String strLine;
			while((strLine = getLine()) != null){
				data = strLine.split(",");


				Item item = new Item(Integer.parseInt(data[0]), data[1],data[2],Integer.parseInt(data[3]), data[4], data[5], data[6],data[7]);
				items.add(item);

			}
		}catch (IOException e){
			if(Main.debug)System.err.println(e);
		}

		return items;
	}

	//reads bids from the csv file, stores them in array list and returns them
	public ArrayList<Bid> getBids(){
		ArrayList<Bid> bids = new ArrayList<Bid>();
		createFile(bidsFile);
		try {
			String[] data;
			reader = new BufferedReader(new FileReader(bidsFile));
			
			String strLine;
			while((strLine = getLine()) != null){
				data = strLine.split(",");

				Bid bid = new Bid(data[0], Integer.parseInt(data[1]),Integer.parseInt(data[2]));
				bids.add(bid);

			}
		}catch (IOException e){
			if(Main.debug)System.err.println(e);
		}
		
		return bids;
	}


	public File createFile(String name){
		File file = new File(name);
			if(!file.exists()){ //create a new file only if it does not exist already
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
			}
		return file;
	}



	// read the first line of the file
	public String getLine() { // 
		try {
			return reader.readLine();
		} catch (IOException e) {
			System.err.println("Can not get a line!");
			return "false";
		}
	}

	//		public void readData(String filename) throws IOException{
	//			try {
	//				String[] data;
	//				reader = new BufferedReader(new FileReader(filename));
	//				while(getLine()!= null){
	//					data = reader.readLine().split(",");
	//
	//					Point point = new Complex(Double.parseDouble(data[1]), Double.parseDouble(data[2]));
	//
	//					int count=1;
	//					favouriteSets.add("JuliaSet"+count, point);
	//					count++;
	//				}



}
