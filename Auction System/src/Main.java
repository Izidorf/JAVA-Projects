import java.io.IOException;


public class Main {
	
public static boolean debug=true;
//private static String auctionServer="127.0.0.1"; // host server for this application
//static Socket kkSocket ; // socket to communicate with server

	public static void main(String[] args){
		MainFrame rcf = new MainFrame("Lightweight Auction System");
		
		//Connect to server asdfa
		try {
			Comms.connectToServer();
			Comms.AcessServer();
		} catch (IOException e) { e.printStackTrace();}
		rcf.init();
	}
	
	

}
