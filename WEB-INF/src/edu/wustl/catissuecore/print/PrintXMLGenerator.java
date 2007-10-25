package edu.wustl.catissuecore.print;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/*
 * this class used to generate the xml document for the client to send to
 * the web service method
 */

public class PrintXMLGenerator {

	
	private Document document;

	String value = null;

	String key = null;

	// return the documet type object of the Map
	public Document generateXMLDoc(ArrayList mapList) {
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException parserException) {
			parserException.printStackTrace();
		}
		// create root element for Document
		Element root = document.createElement("Properties");
		document.appendChild(root);

		// add comment to XML document
		Comment simpleComment = document
				.createComment("This is a simple File that contain specimens object data");
		root.appendChild(simpleComment);
		for(int cnt=0;cnt< mapList.size();cnt++)
		{
			LinkedHashMap objMap = (LinkedHashMap)mapList.get(cnt); 

			if (objMap != null) 
			{
				Iterator it = objMap.keySet().iterator();

				if (it.hasNext()) 
				{
					it.next();
					String classname = (String) objMap.get("class");
					key = (String) it.next();
					String id = (String) objMap.get("id");
					// add child element
					Node objectNode = createObjectNode(document, classname, id);
					root.appendChild(objectNode);
					while (it.hasNext()) 
					{
						key = (String) it.next();
						value = (String)objMap.get(key);
						Node property = createPropertyNode(document, key, value);
						objectNode.appendChild(property);

					}

				}
			}
		}

		return document;
	}

	// create object node for the the doucment
	public Node createObjectNode(Document document, String classname, String id) {

		// create object element
		Element object = document.createElement("object");

		// create attribute
		Attr objectAttribute = document.createAttribute("class");
		objectAttribute.setValue(classname);

		Attr idAttribute = document.createAttribute("id");
		idAttribute.setValue(id);

		// append attribute to object element
		object.setAttributeNode(objectAttribute);
		object.setAttributeNode(idAttribute);

		return object;
	}

	// create the subchild of the object node
	public Node createPropertyNode(Document document, String nodename,
			String nodevalue) {

		Element name = document.createElement("name");
		Element value = document.createElement("value");

		name.appendChild(document.createTextNode(nodename));
		value.appendChild(document.createTextNode(nodevalue));

		Element property = document.createElement("property");

		property.appendChild(name);
		property.appendChild(value);

		return property;
	}

}