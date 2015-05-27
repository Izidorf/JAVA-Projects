import java.io.Serializable;
import java.util.ArrayList;


public abstract class Message implements Serializable {


protected Object message;


public Message(Object message){
	this.message=message;
	
}

public Object getObjectContent() {
	return message;
}

}


@SuppressWarnings("serial")
class RegisterMessage extends Message implements Serializable{

	User user;
	
	public RegisterMessage(User usr) {
		super("Register Message");
		this.user=usr;
	}
	
	public User getUser(){
		return user;
		
	}

}

class BooleanMessage extends Message implements Serializable{
	
	boolean bool;
	
	public BooleanMessage(boolean bool) {
		super("boolean message");
		this.bool=bool;
		
	}
	
	public boolean getBoolean(){
		return this.bool;
	}	
}

class LoginMessage extends Message implements Serializable{

	String username, pass;
	public LoginMessage(String username, String pass) {
		super("Login Message");
		this.username=username;
		this.pass=pass;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return pass;
	}
}



class AddItemMessage extends Message implements Serializable{
	
	Item item;
	
	public AddItemMessage(Item item){
		super("Add Item Message");
		this.item=item;
		
	}
	
	public Item getItem(){
		return this.item;
	}	
	}


class RequestAllAuctions extends Message implements Serializable{

	public RequestAllAuctions() {
		super("Request all auctions");
	}
}	
	
class GetAllAuctions extends Message implements Serializable{

	ArrayList<Item> item;
	
	public GetAllAuctions(ArrayList<Item> item) {
		super("Get All Auctions");
		this.item=item;	
	}
	
	public ArrayList<Item> getItemsList(){
		return item;
	}

	
	
	
}

class RequestItemInfo extends Message implements Serializable{

	int ItemID;
	
	public int getItemID() {
		return ItemID;
	}

	public RequestItemInfo(int ItemID) {
		super("Request Item info");
		this.ItemID=ItemID;
		
	}
	

	
}

class SendItemInfo extends Message implements Serializable{

	Item Item;
	public Item getItem() {
		return Item;
	}

	public ArrayList<Bid> getRelevantBids() {
		return relevantBids;
	}

	ArrayList<Bid> relevantBids;
	
	public SendItemInfo(Item item,ArrayList<Bid> relevantBids ) {
		super("Request Item info");
		this.Item=item;
		this.relevantBids=relevantBids;
				
	}
	

	
}


class PlaceBidMessage extends Message implements Serializable{
	
	Bid bid;
	
	public Bid getBid() {
		return bid;
	}

	public PlaceBidMessage(Bid bid) {
		super("Place bid");
		this.bid=bid;
	}
	
	
	
}


class RequestUserInfo extends Message implements Serializable{

	String userId;
	
	public RequestUserInfo(String userId) {
		super("Request user Info");
		this.userId=userId;
	}

	public String getUserId() {
		return userId;
	}
}

class SendUserInfo extends Message implements Serializable{
	
	String name;
	String lastname;
	ArrayList<Bid> relevantBids;
	ArrayList<Item> usersItems;
	
	public  SendUserInfo(String name, String lastName, ArrayList<Bid> relevantBids, ArrayList<Item> usersItems) {
		super("Place bid");
		this.name=name;
		this.lastname=lastName;
		this.relevantBids=relevantBids;
		this.usersItems=usersItems;

	}
	
	public String getName() {
		return name;
	}

	public String getLastname() {
		return lastname;
	}

	public ArrayList<Bid> getRelevantBids() {
		return relevantBids;
	}
	
}



	
	
