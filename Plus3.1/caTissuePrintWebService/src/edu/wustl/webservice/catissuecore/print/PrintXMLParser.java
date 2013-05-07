/**
 * <p>Title: PrintXMLParser Class>
 * <p>Description:	PrintXMLParser is a class that is used to parse the 
 * xml data which is given by the client of the print web service. 
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Amit Doshi
 * @version 1.00
 * Created on Nov 1, 2007
 */

package edu.wustl.webservice.catissuecore.print;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PrintXMLParser {
	
	/* this class parse the XML data which is sent by 
	 * the client for print.  
	 */
	private static Document document = null;
	
	
	/*return the LinkedHashMap of the XML data that is send by the client*/
	
	public Map getPrintMap(final String xml) throws SAXException,IOException,ParserConfigurationException
	{
	   
		PrintXMLParser.init(xml);
		Map objMap = PrintXMLParser.getMap();
		return objMap;
		
	}
	public static void init(final String xml) throws SAXException,IOException,ParserConfigurationException
	{

		final DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		final StringBuffer StringBuffer1 = new StringBuffer(xml);
		final ByteArrayInputStream Bis1 = new ByteArrayInputStream(StringBuffer1.toString().getBytes("UTF-8"));
		DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();// throws
		// ParserConfigurationException
		if (xml != null)
		{
			document = dbuilder.parse(Bis1);
			// throws SAXException,IOException,IllegalArgumentException(if path is null
		}
		
	}

	

	public static Map getMap()
	{
		
		Map objMap = new LinkedHashMap();
		
		// it gives the rootNode of the xml file
		Element root = document.getDocumentElement();

		NodeList children = root.getChildNodes();

		
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);
			
			System.out.println("--node name+"+child.getNodeName());
		
			if (child instanceof Element)
			{
				String mapKey = null;
				NamedNodeMap map = child.getAttributes();
							    
			    // Process each attribute
				if(child.getNodeName().equals("object"))
				{
					int numAttrs = map.getLength();	
				    for (int k=0; k<numAttrs; k++) 
				    {
				        Attr attr = (Attr)map.item(k);	
				        String attrValue = attr.getNodeValue();
				        if(mapKey!=null)
				        {
				        	mapKey = mapKey+"_"+attrValue;
				        }
				        else
				        {
				        	mapKey = attrValue;
				        }
				    }
				    
				}
				NodeList subChildNodes = child.getChildNodes();
				LinkedHashMap valueMap = new LinkedHashMap();
				 
				for (int j = 0; j < subChildNodes.getLength(); j++)
				{
					Node subchildNode = subChildNodes.item(j);
					String subNodeName = subchildNode.getNodeName();
					
					if (subchildNode  instanceof Element && subNodeName.equals("property"))
					{
						NodeList propertyChildNodes = subchildNode.getChildNodes();
						ArrayList arr = new ArrayList();
						for(int m=0;m< propertyChildNodes.getLength();m++)
						{
							Node propNode = propertyChildNodes.item(m);
						
							if(propNode  instanceof Element) 
							{
								
								arr.add(propNode.getFirstChild().getNodeValue());							
								
							} 
						
						}
						valueMap.put(arr.get(0),arr.get(1));
						
					}	
					
				}
				objMap.put(mapKey,valueMap);
			}
		}
		return objMap;
	}
}
