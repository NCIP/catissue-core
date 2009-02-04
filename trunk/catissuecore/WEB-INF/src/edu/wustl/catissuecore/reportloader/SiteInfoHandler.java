package edu.wustl.catissuecore.reportloader;

/**
 * <p>Title:XMLPropertyHandler Class>
 * <p>Description:This class parses from caTissue_Properties.xml(includes properties name & value pairs) file using DOM parser.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Tapan Sahoo
 * @version 1.00
 * Created on May 15, 2006
 */



import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.common.util.logger.Logger;

/**
 * This class gives the properties value by giving properties name.
 * 
 * @author tapan_sahoo
 */
public class SiteInfoHandler
{

	private static Document document = null;
	
	/**
	 * Initialization method
	 * @param path path of file name
	 * @throws Exception generic exception
	 */
	public static void init(String path) throws Exception
	{
	//	Logger.out.debug("path.........................."+path);
		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();// throws
			// ParserConfigurationException
			if (path != null)
			{
				document = dbuilder.parse(path);
				// throws SAXException,IOException,IllegalArgumentException(if path is null
			}
		}
		catch (SAXException e)
		{
			Logger.out.error(e.getMessage(),e);
			throw e;
		}
		catch (IOException e)
		{
			Logger.out.error(e.getMessage(),e);
			throw e;
		}
		catch (ParserConfigurationException e)
		{
			Logger.out.error("Could not locate a JAXP parser: "+e.getMessage(),e);
			throw e;
		}
	}

	/**
	 *
	 * <p>
	 * Description:This method takes the property siteName as String argument and
	 * returns the abbreviation value as String. 
	 * </p>
	 * @param siteName name of the site
	 * @return pValue currousponding abbreviated value
	 * @throws Exception generic exception
	 */
	public static String getSiteAbbriviation(String siteName) throws Exception
	{
		// it gives the rootNode of the xml file
		Element root = document.getDocumentElement();

		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);

			if (child instanceof Element)
			{
				// it gives the subchild nodes in the xml file(name & value)
				NodeList subChildNodes = child.getChildNodes();

				boolean isNameFound = false;
				//Logger.out.debug("subchildNodes : "+subChildNodes.getLength()); 
				for (int j = 0; j < subChildNodes.getLength(); j++)
				{
					Node subchildNode = subChildNodes.item(j);
					String subNodeName = subchildNode.getNodeName();
					//Logger.out.debug("subnodeName : "+subNodeName);
					if (subNodeName.equals(Parser.SITE_NAME))
					{
						String pName = (String) subchildNode.getFirstChild().getNodeValue();
						//Logger.out.debug("pName : "+pName);
						if (siteName.equals(pName))
						{
							//Logger.out.debug("pName : "+pName);
							isNameFound = true;
						}
					}
					
					if(isNameFound && subNodeName.equals(Parser.SITE_ABBRIVIATION))
					{
						String pValue = (String) subchildNode.getFirstChild()
								.getNodeValue();
						return pValue;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * <p>
	 * Description:This method takes the property siteName as String argument and
	 * returns the abbreviation value as String. 
	 * </p>
	 * @param abbr abbriviated site name
	 * @return pValue associated site name
	 * @throws Exception generic exception
	 */
	public static String getSiteName(String abbr) throws Exception
	{
		// it gives the rootNode of the xml file
		Element root = document.getDocumentElement();

		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			Node child = children.item(i);

			if (child instanceof Element)
			{
				// it gives the subchild nodes in the xml file(name & value)
				NodeList subChildNodes = child.getChildNodes();

				boolean isNameFound = false;
				//Logger.out.debug("subchildNodes : "+subChildNodes.getLength()); 
				for (int j=subChildNodes.getLength()-1;j>=0; j--)
				{
					Node subchildNode = subChildNodes.item(j);
					String subNodeName = subchildNode.getNodeName();
					//Logger.out.debug("subnodeName : "+subNodeName);
					if (subNodeName.equals(Parser.SITE_ABBRIVIATION))
					{
						String pName = (String) subchildNode.getFirstChild().getNodeValue();
						//Logger.out.debug("pName : "+pName);
						if (abbr.equals(pName))
						{
							//Logger.out.debug("pName : "+pName);
							isNameFound = true;
						}
					}
					
					if(isNameFound && subNodeName.equals(Parser.SITE_NAME))
					{
						String pValue = (String) subchildNode.getFirstChild()
								.getNodeValue();
						return pValue;
					}
				}
			}
		}
		return null;
	}
	
}