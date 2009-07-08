
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
import edu.wustl.common.util.logger.Logger;
import edu.wustl.webservice.catissuecore.print.PrintService;
import edu.wustl.webservice.catissuecore.print.PrintWebService;

/**
 * This class used to generate the XML document of given
 * Object and calling print web service as per configuration.
 * @author falguni_sachde
 *
 */
public class PrintServiceInputXMLParser implements PrintServiceInputParserInterface
{

	/**
	 * Generic logger.
	 */
	private transient Logger logger = Logger
			.getCommonLogger(PrintServiceInputXMLParser.class);
	/**
	 * Document object.
	 */
	private Document document;

	/**
	 * specify value.
	 */
	String value = null;

	/**
	 * specify key.
	 */
	String key = null;
	/**
	 * specify Printing Failed.
	 */
	public static final String PRINT_FAILED = "Printing Failed";

	/**
	 * This method call Print Service.
	 * @param listData Object.
	 * @return true or false.
	 */
	public boolean callPrintService(Object listData)
	{
		try
		{
			System.setProperty("org.apache.xerces.xni.parser.XMLParserConfiguration",
					"org.apache.xerces.parsers.XIncludeAwareParserConfiguration");
			final Document doc = this.generateXMLDoc((List) listData);
			final String strXMLData = this.getStringFromDocument(doc);
			final String endpointURL = PropertyHandler.getValue("printWebServiceEndPoint");
			final PrintWebService p = new PrintWebService(new URL(endpointURL), new QName(
					"http://print.catissuecore.webservice.wustl.edu/", "PrintWebService"));
			final PrintService pservice = p.getPrintServicePort();
			final String msg = pservice.print(strXMLData);
			if (msg.equals(PRINT_FAILED))
			{
				return false;
			}
			return true;
		}
		catch (final Exception e)
		{
			this.logger.debug(e.getMessage(), e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param doc Document object.
	 * @return String
	 */
	public String getStringFromDocument(Document doc)
	{
		try
		{
			final DOMSource domSource = new DOMSource(doc);
			final StringWriter writer = new StringWriter();
			final StreamResult result = new StreamResult(writer);
			final TransformerFactory tf = TransformerFactory.newInstance();
			final Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			System.out.println(writer.toString());
			return writer.toString();
		}
		catch (final TransformerException ex)
		{
			this.logger.debug(ex.getMessage(), ex);
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * return the documet type object of the Map.
	 * @param mapList map list
	 * @return Document
	 */
	public Document generateXMLDoc(List mapList)
	{

		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			final DocumentBuilder builder = factory.newDocumentBuilder();
			this.document = builder.newDocument();
		}
		catch (final ParserConfigurationException parserException)
		{
			this.logger.debug(parserException.getMessage(), parserException);
			parserException.printStackTrace();
		}
		// create root element for Document
		final Element root = this.document.createElement("Properties");
		this.document.appendChild(root);
		// add comment to XML document
		final Comment simpleComment = this.document
				.createComment("This is a simple File that contain specimens object data");
		root.appendChild(simpleComment);
		for (int cnt = 0; cnt < mapList.size(); cnt++)
		{
			final LinkedHashMap objMap = (LinkedHashMap) mapList.get(cnt);

			if (objMap != null)
			{
				final Iterator it = objMap.keySet().iterator();

				if (it.hasNext())
				{
					final String classname = (String) objMap.get("class");
					final String id = (String) objMap.get("id");
					// add child element
					final Node objectNode =
						this.createObjectNode(this.document, classname, id);
					root.appendChild(objectNode);
					while (it.hasNext())
					{
						this.key = (String) it.next();
						this.value = (String) objMap.get(this.key);
						final Node property =
							this.createPropertyNode(this.document, this.key,
								this.value);
						objectNode.appendChild(property);
					}
				}
			}
		}
		return this.document;
	}

	/**
	 * create object node for the the document.
	 * @param document Document object.
	 * @param classname class name.
	 * @param id identifier.
	 * @return Node
	 */
	public Node createObjectNode(Document document, String classname, String id)
	{
		// create object element
		final Element object = document.createElement("object");

		// create attribute
		final Attr objectAttribute = document.createAttribute("class");
		objectAttribute.setValue(classname);

		final Attr idAttribute = document.createAttribute("id");
		idAttribute.setValue(id);

		// append attribute to object element
		object.setAttributeNode(objectAttribute);
		object.setAttributeNode(idAttribute);

		return object;
	}

	/**
	 * create the sub child of the object node.
	 * @param document Document object
	 * @param nodename node name
	 * @param nodevalue node value.
	 * @return Node
	 */
	public Node createPropertyNode(Document document, String nodename, String nodevalue)
	{

		final Element name = document.createElement("name");
		final Element value = document.createElement("value");

		name.appendChild(document.createTextNode(nodename));
		value.appendChild(document.createTextNode(nodevalue));

		final Element property = document.createElement("property");

		property.appendChild(name);
		property.appendChild(value);

		return property;
	}

	/**
	 * Main method.
	 * @param args arguments.
	 * @throws Exception Exception.
	 */
	public static void main(String[] args) throws Exception
	{
		final PrintServiceInputXMLParser print = new PrintServiceInputXMLParser();
		final LinkedHashMap map = new LinkedHashMap();
		map.put("class", "Specimen");
		map.put("id", "12");
		final ArrayList l = new ArrayList();
		l.add(map);
		print.callPrintService(l);

	}
}