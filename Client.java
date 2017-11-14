import java.io.IOException;
import java.net.Socket;

public class Client {

	String hostName = "localhost";
	int portNumber = 3500;
	
	public void send(String xmlFile){
		
		try {
			Socket socket = new Socket("localhost", portNumber);
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
}
