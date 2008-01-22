package edu.wustl.catissuecore.printservicemodule;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ParameterMode;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import edu.wustl.catissuecore.printserviceclient.PropertyHandler;
/**
 * This class used to generate the xml document of given Object and calling print webservice as per configuration. 
 * @author falguni_sachde
 *
 */
public class PrintServiceInputXMLParser implements PrintServiceInputParserInterface 
{
	private Document document;

	String value = null;

	String key = null;

	
	public boolean callPrintService(Object listData) throws Exception 
	{
		try
		{
			Document doc = this.generateXMLDoc((ArrayList)listData);		
			String strXMLData = this.getStringFromDocument(doc);
			String endpointURL = PropertyHandler.getValue("printWebServiceEndPoint");
			//If endpoint URL is mentioned in printservicemodule,it indicates webservice is configured for printing.
			if(endpointURL!=null)
			{
				String method = "print";
	
				// Make the call
				Service service = new Service();
				Call call = (Call) service.createCall();
				call.setTargetEndpointAddress(new java.net.URL(endpointURL));
				call.setOperationName(method);
				call.addParameter("op1", XMLType.XSD_STRING, ParameterMode.IN);
				call.setReturnType(XMLType.XSD_STRING);
				String returnValue = (String) call.invoke(new Object[] {strXMLData});
				System.out.println("Webservice returns...."+returnValue);
				
			}
			return true;
		}
		catch(Exception e)
		{
			return false;	
			
		}
	}
	
	/**
	 * @param doc
	 * @return String
	 */
	public String getStringFromDocument(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			System.out.println(writer.toString());
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * return the documet type object of the Map
	 * @param mapList
	 * @return Document
	 */
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

	
	/**
	 * create object node for the the doucment
	 * @param document
	 * @param classname
	 * @param id
	 * @return Node
	 */
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

	
	/**
	 * create the subchild of the object node
	 * @param document
	 * @param nodename
	 * @param nodevalue
	 * @return Node
	 */
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