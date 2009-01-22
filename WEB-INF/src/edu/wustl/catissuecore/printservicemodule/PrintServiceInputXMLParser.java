package edu.wustl.catissuecore.printservicemodule;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import edu.wustl.catissuecore.printserviceclient.PropertyHandler;
import edu.wustl.webservice.catissuecore.print.PrintService;
import edu.wustl.webservice.catissuecore.print.PrintWebService;

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
	public static final String PRINT_FAILED = "Printing Failed";
	

	public boolean callPrintService(Object listData) 
	{
		try
		{
			System.setProperty("org.apache.xerces.xni.parser.XMLParserConfiguration", "org.apache.xerces.parsers.XIncludeAwareParserConfiguration");
			Document doc = this.generateXMLDoc((List)listData);		
			String strXMLData = this.getStringFromDocument(doc);
			String endpointURL = PropertyHandler.getValue("printWebServiceEndPoint");
			PrintWebService p = new PrintWebService(new URL(endpointURL),new QName("http://print.catissuecore.webservice.wustl.edu/", "PrintWebService"));
			PrintService pservice = p.getPrintServicePort();
			String msg = pservice.print(strXMLData);
			if(msg.equals(PRINT_FAILED))
			{
				return false;
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
	public Document generateXMLDoc(List mapList) {
		
		
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
	public static void main(String[] args) throws Exception 
	{
		 PrintServiceInputXMLParser print = new PrintServiceInputXMLParser();
		 LinkedHashMap map = new LinkedHashMap();
		map.put("class", "Specimen");
		map.put("id", "12");
		ArrayList l = new ArrayList();
		l.add(map);
		print.callPrintService(l);		
		
	}
}