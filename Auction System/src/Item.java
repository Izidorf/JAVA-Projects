import java.io.Serializable;
import java.util.*;


@SuppressWarnings("serial")
public class Item implements Serializable {
	
	private int itemID;
	private String vendorID; //username of the seller
	private int reservedPrice;
	private String title;
	private String description;
	private String startTime;
	private String endTime;
	private String categoryChosen;
	private String[] category = {"art", "clothes", "other"};
	private List<Bid> bids; //Stores itemID, userID and bid
	
	public Item(int itemID, String vendorID, String title, int reservedPrice, String category,  String startTime, String endTime, String description) {
		this.itemID=itemID;
		this.vendorID=vendorID;
		this.reservedPrice = reservedPrice;
		this.title=title;
		this.description=description;
		this.startTime=startTime;
		this.endTime=endTime;
		this.categoryChosen=category;
	}
	
	public Item(String vendorID, String title, int reservedPrice, 
			String category, String startTime, String endTime, String description) {
		this.vendorID=vendorID;
		this.title=title;
		this.reservedPrice = reservedPrice;
		this.description=description;
		this.startTime=startTime;
		this.endTime=endTime;
		this.categoryChosen=category;
	}
	
	public String getVendorID() {
		return vendorID;
	}

	public void setVendorID(String vendorID) {
		this.vendorID = vendorID;
	}

	public int getReservedPrice() {
		return reservedPrice;
	}

	public void setReservedPrice(int reservedPrice) {
		this.reservedPrice = reservedPrice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCategoryChosen() {
		return categoryChosen;
	}

	public void setCategoryChosen(String categoryChosen) {
		this.categoryChosen = categoryChosen;
	}

	public String[] getCategory() {
		return category;
	}

	public void setCategory(String[] category) {
		this.category = category;
	}

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}


	
	public int getItemID() {
		return itemID;
	}
	
	public String[] getCategories(){
		return this.category;
	}

}
