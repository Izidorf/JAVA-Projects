import java.io.Serializable;


@SuppressWarnings("serial")
public class User implements Serializable{
	
	private String username;
	private String givenName;
	private String familyName;
	private String password;
	
	
	public User( String givenName, String familyName, String username, String password) {
		this.givenName=givenName;
		this.familyName=familyName;
		this.username=username;
		this.password=password;
	}
	
	public String getUsername() {
		return username;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	public String getFullName(){
		return this.givenName+" "+this.familyName;
	}
	
	public void changePassword(String password){
		//first ask for the old password
		
		//then change it
		this.password=password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
