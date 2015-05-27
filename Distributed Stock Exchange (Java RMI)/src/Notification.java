

import java.io.Serializable;

public class Notification implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String source;
	private Object info;
	
	public Notification(String source, Object info) {
		this.source=source;
		this.info=info;	
	}
	
	public String getSource(){
		return this.source;
	}
	
	public Object getInfo(){
		return this.info;
	}
	
	

}
