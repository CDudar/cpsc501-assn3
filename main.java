import java.util.Vector;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;;

public class main {

	public static void main(String[] args){
		
		ObjectCreator objCreator = new ObjectCreator();
		
		Vector<Object> objectsToSerialize = objCreator.createUserObjects();
		
		Serializer serializer = new Serializer();

		Document doc = serializer.serialize(objectsToSerialize);
		
		
	}
	
}
