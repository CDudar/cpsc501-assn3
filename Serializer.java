import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.Vector;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;;





public class Serializer {
	
		private int IDNumber = 0;
	
		private IdentityHashMap<Object, Integer> objectsSerialized = new IdentityHashMap<Object, Integer>();
		
		Document doc = new Document();
		
		Element theRoot = new Element("serialized");
		
		public Document serialize(Object obj){
			
		Vector<Object> objToSerialize = (Vector<Object>)obj;
		
		System.out.println(objToSerialize.size());
		
		for(int i = 0; i < objToSerialize.size(); i++){
			System.out.println(("serializing " + objToSerialize.get(i)));
			serializeCurrentObject(objToSerialize.get(i), IDNumber++);
			
		}

		doc.setRootElement(theRoot);
		
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutput.output(doc, new FileOutputStream(new File("test.xml")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		
		return doc;
	}
		




public void serializeCurrentObject(Object obj, int ID){
	
	if(objectsSerialized.containsKey(obj)){
		System.out.println("Object already serialized");
		return;
	}
	
	Vector<Pair<Object, Integer>> referencesToSerialize = new Vector<Pair<Object, Integer>>();
	
	Element objElement = new Element("object");
	objElement.setAttribute("class", obj.getClass().toString());
	
	objElement.setAttribute("id", String.valueOf(ID));
	objectsSerialized.put(obj, ID);
	
	Class objClass = obj.getClass();
	
	
	Field[] fields = objClass.getDeclaredFields();
	
	System.out.println("accessing fields");
	
	for(int i = 0; i < fields.length; i++){
		
		Field f = fields[i];
		Object fieldObject = null;
		f.setAccessible(true);
		try {
			fieldObject = f.get(obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		System.out.println(f.getType());
		System.out.println(f.getName());
		
		if(f.getType().isArray()){
			
			Class<?> componentType = (f.getType().getComponentType());
			
			
			Element objectTag = new Element("field");

			int arrayLength = Array.getLength(fieldObject);
			
			objectTag.setAttribute("name", f.getName());
			objectTag.setAttribute("declaringclass", f.getDeclaringClass().toString());
			objectTag.setAttribute("length", String.valueOf(arrayLength));
			
			
			if(componentType.isPrimitive()){
				System.out.println("Serializing primitive array");
				
				for(int j = 0; j < arrayLength; j++){
					
					Element value = new Element("value");
					
					value.addContent(Array.get(fieldObject, j).toString());
					
					objectTag.addContent(value);
				}

			}
			
			else{
				System.out.println("Serializing object reference array");
				
				
				for(int j = 0; j < arrayLength; j++){
					
					Element reference = new Element("reference");
					
					reference.addContent(String.valueOf(IDNumber));
					
					referencesToSerialize.add(new Pair(Array.get(fieldObject, j), IDNumber++));
					
					objectTag.addContent(reference);
				}
				
				
				
			}
			
			
			objElement.addContent(objectTag);
			
		}
		else{
			System.out.println("Not an array");
			
			if(f.getType().isPrimitive()){
				
				System.out.println("SERIALIZING PRIMITIVE FIELD");
				
				Element primitiveField = new Element("field");
				primitiveField.setAttribute("name", f.getName());
				primitiveField.setAttribute("declaringclass", f.getDeclaringClass().toString());
				
				
				Element value = new Element("value");
				try {
					value.addContent(f.get(obj).toString());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				
				primitiveField.addContent(value);
				
				objElement.addContent(primitiveField);
				

			}
			
			else{
				System.out.println("SERIALIZING OBJECT FIELD");
				
				Element objectTag = new Element("field");
				
				objectTag.setAttribute("name", f.getName());
				objectTag.setAttribute("declaringclass", f.getDeclaringClass().toString());
				
				Element reference = new Element("reference");
				
				reference.addContent(String.valueOf(IDNumber));
				
				referencesToSerialize.add(new Pair(fieldObject, IDNumber++));
				
				
				objectTag.addContent(reference);
				
				objElement.addContent(objectTag);
				
				
				
			}
			
		}
		
	}
	
		theRoot.addContent(objElement);
	
		for(int i = 0; i < referencesToSerialize.size(); i++)
			serializeCurrentObject(referencesToSerialize.get(i).getL(), referencesToSerialize.get(i).getR());
			
	
	
	}

}
