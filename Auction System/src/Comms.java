import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;




public class Comms {


	static private ObjectOutputStream output; // output stream to server
	static private ObjectInputStream input; // input stream from server
	static private Socket client; // socket to communicate with server

	
	public void runClient(Message message){
		try // connect to server, get streams, process connection
	      {
	         connectToServer(); // create a Socket to make connection
	         AcessServer(); // get the input and output streams
	         sendMessage(message);
	         receiveMessage(); // process connection
	      } // end try
	      catch ( EOFException eofException ) 
	      {
	       
	      } // end catch
	      catch ( Exception Exception ) 
	      {
	        
	      } // end catch
	      finally 
	      {
	         closeConnection(); // close connection
	      } // end finally
		
	}
	
	public static void connectToServer(){
		if(Main.debug)
			System.err.println("Connecting to server..................");
		
		try {
			client = new Socket("localhost", 4444);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: localhost");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: localhost");
		}

	}
	
	public static void AcessServer() throws IOException{
		if(Main.debug)
			System.err.println("Openign OOS and OIS!!!!!!!!!!!");
		
	    // set up output stream for objects
	      output = new ObjectOutputStream( client.getOutputStream());      
	      output.flush(); // flush output buffer to send header information

	      // set up input stream for objects
	      input = new ObjectInputStream( client.getInputStream() );
	}


	//stores message on the server in the receivers repository
	public static void sendMessage(Message message){
		 try {
			 output.reset();
			output.writeObject(message);   
			output.flush(); // flush data to output
		} catch (IOException e) 
			{	e.printStackTrace();}
        

		 if(Main.debug) System.out.println("CLIENT-> sent a message "+message);

	}


	//displays message to the user
	public static Message receiveMessage(){
		Message message;
		try {
			message = (Message) input.readObject();
			// read new message
			 if(Main.debug) System.out.println("SERVER-> message received from server "+message);
			 if(message instanceof RegisterMessage)
				 ((RegisterMessage) message).getUser();
			 return message;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	 // close streams and socket
	   private void closeConnection() 
	   {

	      try 
	      {
	         output.close(); // close output stream
	         input.close(); // close input stream
	         client.close(); // close socket
	      } // end try
	      catch ( IOException ioException ) 
	      {
	         ioException.printStackTrace();
	      } // end catch
	   } // end method closeConnection

	
	



}
