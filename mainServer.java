import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class mainServer {

	
	public static void main(String[] args){
		/**
		Server server = new Server();
		server.acceptDocuments();
		
		for(int i = 0; i < documentsToSend.size(); i++){
			Client client = new Client();
			client.send(documentsToSend.get(i), i);
		}
	
	*/
	
		SAXBuilder builder = new SAXBuilder();
	
		//int numDocsReceived = Server.getNumDocsReceived
		
		int numDocsReceived = 2;
		Vector<Document> documentsReceived = new Vector<Document>();
		
		for(int i = 0 ; i < numDocsReceived; i++){
			try {
				
				documentsReceived.add(builder.build(new File("receive" + String.valueOf(i) + ".xml")));
			} catch (JDOMException | IOException e) {
				e.printStackTrace();
			}
		}
	
		
		Vector<Object> objectListsReceived = new Vector<Object>();
		
		Deserializer deserializer = new Deserializer();
		
		for(int i = 0; i < documentsReceived.size(); i++){
			objectListsReceived.add(deserializer.deserialize(documentsReceived.get(i)));
			
		}
		//Chose a document to inspect, show all the objects in that docs corresponding list of objects
	
		
		/*
		try {
			Asst2TestDriver driver = new Asst2TestDriver("ObjectInspector", true, received);
			
			for(Object obj : objectsReceived){
			
				driver.runTest(obj);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}
	
}
