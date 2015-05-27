import java.io.Serializable;


public class Bid implements Serializable {

	private String bidderID;
	private int ItemID;
	private int bid;
	
	public Bid(String bidderID, int ItemID, int bid) {
		this.bidderID=bidderID;
		this.ItemID=ItemID;
		this.bid=bid;
	}
	
	public void placeABid(int BidderID, int ItemID, int bid){
		this.ItemID=ItemID;
		this.bid=bid;
	}
	
	public int getItemID(){
		return this.ItemID;
	}

	public String getBidderID() {
		return bidderID;
	}

	public void setBidderID(String bidderID) {
		this.bidderID = bidderID;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public void setItemID(int itemID) {
		ItemID = itemID;
	}
	
	
}
