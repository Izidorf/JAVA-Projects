import java.util.ArrayList;


public class Client {
	
public static String userID=""; //store user id, to identify the client and allow easy access to his identity 
public static boolean isLoggedIn=false;



public static void setUserID(String userId){
	Client.userID=userId;
}

public static String getUserID(){
	return Client.userID;
}



public static void getClientInformation(){
	
}

}
