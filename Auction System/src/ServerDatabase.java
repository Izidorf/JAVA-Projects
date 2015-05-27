import java.util.ArrayList;
import java.util.Random;


public class ServerDatabase {

	ArrayList<User> users = new ArrayList<User>();



	public DataPersistence getDataPersistance() {
		return dataPersistance;
	}


	public void setDataPersistance(DataPersistence dataPersistance) {
		this.dataPersistance = dataPersistance;
	}


	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}


	public void setBids(ArrayList<Bid> bids) {
		this.bids = bids;
	}

	ArrayList<Bid> bids = new ArrayList<Bid>();
	ArrayList<Item> items = new ArrayList<Item>();
	DataPersistence dataPersistance = new DataPersistence();


	public ArrayList<User> getUsers() {
		return users;
	}

	public String getFirstName(String userId){
		for(User u : users){
			if(u.getUsername()==userId)
				return u.getFirstName();
		}
		return null;
	}
	
	public String getLastName(String userId){
		for(User u : users){
			if(u.getUsername()==userId)
				return u.getFamilyName();
		}
		return null;
	}

	public ArrayList<Bid> getUsersBids(String userID){
		ArrayList<Bid> relevant = new ArrayList<Bid>();
		for(Bid b : bids){
			if(b.getBidderID().equals(userID)){
					relevant.add(b);
					
		}
		}
		return relevant;
	}
	
	public ArrayList<Item> getUsersItems(String userID){
		ArrayList<Item> relevant = new ArrayList<Item>();
		for(Item i : items){
			if(i.getVendorID().equals(userID))
					relevant.add(i);
		}
		return relevant;
	}
	
	public synchronized ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}



	public ServerDatabase() {
		//when server re-started load data from the hard disk
		setUsers(dataPersistance.getUsers());
		setItems(dataPersistance.getItems());
		setBids(dataPersistance.getBids());

		if(Main.debug){
			System.out.println("*********PRINT ALL USERS RECOVERED FROM DATA PERSISTANCE");
			for(User ua : users)
				System.out.println("Username "+ua.getUsername()+" Password: "+ua.getPassword());
		}
	}

	public boolean authenticateUser(String name, String password){
		//		users.add(new User("izi","izi", "izi", "123"));		
		for(User u : users){
			System.out.println(u.getUsername()+u.getPassword()+u+"\n");
			if(u.getUsername().equals(name) && u.getPassword().equals(password))
				return true;
		}
		return false;
	}
	public boolean registerUser(User u){

		if(Main.debug){	System.out.println("********List of users BEFORE adding a new one********");
		for(User ua : users)
			System.out.println(u.getUsername()+ua.getPassword());}

		this.users.add(u);

		if(Main.debug){		System.out.println("********List of users AFTER adding a new one********");


		for(User ua : users)
			System.out.println(ua.getUsername()+ua.getPassword());}

		dataPersistance.SaveUser(this.getUsers());
		return true;

	}

	public boolean addItem(Item item){
		item.setItemID(generateItemId());
		this.items.add(item);
		System.out.println("The number of items in the Array List is now: " + items.size());
		dataPersistance.SaveItems(this.getItems());
		return true;
	}

	public void addUser(User user){
		users.add(user);
		dataPersistance.SaveUser(this.getUsers());
	}

	public User getUser(int index){
		return users.get(index);
	}


	public Item getItem(int index){
		return this.items.get(index);
	}

	public void addBid(Bid bid){
		this.bids.add(bid);
		dataPersistance.SaveBids(this.getBids());
	}

	public ArrayList<Bid> getBids() {
		return bids;
	}

	public Item getItemById(int itemId){
		for(Item i : items){
			if(Main.debug) System.out.println("Loking at item IDs: "+i.getItemID());
			if(i.getItemID()==itemId)
				return i;
		}

		return null;
	}

	public ArrayList<Bid> getItemBids(int itemID){
		ArrayList<Bid> relevantBids = new ArrayList<Bid>();
		for(Bid rel : this.bids){
			if(Main.debug) System.out.println("Loking at bids ItemIDs: "+rel.getItemID()+" to see if they match "+ itemID);
			if(rel.getItemID() == itemID){
				relevantBids.add(rel);
			}
		}
		return relevantBids;
	}

	public Bid getBid(int index){
		return this.bids.get(index);
	}

	public ArrayList<Bid> getItemBids(Item item){
		ArrayList<Bid> specificItem = new ArrayList<Bid>();
		for(Bid b : this.bids){
			if(b.getItemID() == item.getItemID()) 
				specificItem.add(b);
		}
		return specificItem;
	}
	
	public boolean placeBid(Bid bid){
			bids.add(bid);
			dataPersistance.SaveBids(this.getBids());
			return true;
	}
	
	//test if your bid is higher then all the current bids
//	public boolean isBidHighest(ArrayList<Bid> relevant, Bid yourBid){
//		int size = relevant.size();
//		for(Bid b : relevant){
//			if(b.getBid() > yourBid.getBid()) //add only if your bid smaller
//			bids.add(yourBid);
//			dataPersistance.SaveBids(bids);
//		}
//		if(size == relevant.size())
//		return true;
//		
//		return false;
//	}
//	
	public int generateItemId(){
		Random rnd = new Random();
		String AB = "0123456789";
		int len = 8;
		{
			StringBuilder sb = new StringBuilder( len );
			for( int i = 0; i < len; i++ ) 
				sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );

			//check that it does not already exist
			for(Item i : items)
				if(i.getItemID() == Integer.parseInt(sb.toString()))
					generateItemId();

			return Integer.parseInt(sb.toString()) ;
		}
	}

	
	public String generateReport(){
		String numUsers = "There are "+users.size()+ " registered within the system. \n Their names are: \n";
		for(User u : users)
		numUsers +=" " +u.getFirstName()+ " "+u.getFamilyName()+" : "+ "no items won \n";
		dataPersistance.makeReport(numUsers);
		return numUsers;
	}

}
