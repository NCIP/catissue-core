
package edu.wustl.catissuecore.caties.util;

/**
 * <p>Title:XMLPropertyHandler Class>
 * <p>Description:This class parses from caTissue_Properties.xml
 * (includes properties name & value pairs) file using DOM parser.</p>.
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
 * @author tapan_sahoo
 */
public class SiteInfoHandler
{
	/**
	 * logger.
	 */
	private static Logger logger = Logger.getCommonLogger(SiteInfoHandler.class);
	/**
	 * document.
	 */
	private static Document document = null;

	/**
	 * Initialization method.
	 * @param path path of file name
	 * @throws Exception generic exception
	 */
	public static void init(String path) throws Exception
	{

		final DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		try
		{
			final DocumentBuilder dbuilder = dbfactory.newDocumentBuilder();// throws
			// ParserConfigurationException
			if (path != null)
			{
				document = dbuilder.parse(path);
				// throws SAXException,IOException,IllegalArgumentException(if path is null
			}
		}
		catch (final SAXException e)
		{
			SiteInfoHandler.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw e;
		}
		catch (final IOException e)
		{
			SiteInfoHandler.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw e;
		}
		catch (final ParserConfigurationException e)
		{
			SiteInfoHandler.logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw e;
		}
	}

	/**
	 *
	 * <p>
	 * Description:This method takes the property siteName as String argument and
	 * returns the abbreviation value as String.
	 * </p>
	 * @return pValue currousponding abbreviated value
	 * @throws Exception generic exception
	 * @param siteName name of the site
	 */
	public static String getSiteAbbriviation(String siteName) throws Exception
	{
		// it gives the rootNode of the xml file
		final Element root = document.getDocumentElement();

		final NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			final Node child = children.item(i);

			if (child instanceof Element)
			{
				// it gives the subchild nodes in the xml file(name & value)
				final NodeList subChildNodes = child.getChildNodes();

				boolean isNameFound = false;
				//Logger.out.debug("subchildNodes : "+subChildNodes.getLength());
				for (int j = 0; j < subChildNodes.getLength(); j++)
				{
					final Node subchildNode = subChildNodes.item(j);
					final String subNodeName = subchildNode.getNodeName();
					//Logger.out.debug("subnodeName : "+subNodeName);
					if (subNodeName.equals(CaTIESConstants.SITE_NAME))
					{
						final String pName = subchildNode.getFirstChild().getNodeValue();
						//Logger.out.debug("pName : "+pName);
						if (siteName.equals(pName))
						{
							//Logger.out.debug("pName : "+pName);
							isNameFound = true;
						}
					}

					if (isNameFound && subNodeName.equals(CaTIESConstants.SITE_ABBRIVIATION))
					{
						return subchildNode.getFirstChild().getNodeValue();
					}
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Description:This method takes the property siteName as String argument and
	 * returns the abbreviation value as String.
	 * </p>
	 * @param abbr abbreviated site name
	 * @return pValue associated site name
	 * @throws Exception generic exception
	 */
	public static String getSiteName(String abbr) throws Exception
	{
		// it gives the rootNode of the xml file
		final Element root = document.getDocumentElement();

		final NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
		{
			final Node child = children.item(i);

			if (child instanceof Element)
			{
				// it gives the subchild nodes in the xml file(name & value)
				final NodeList subChildNodes = child.getChildNodes();

				boolean isNameFound = false;
				//Logger.out.debug("subchildNodes : "+subChildNodes.getLength());
				for (int j = subChildNodes.getLength() - 1; j >= 0; j--)
				{
					final Node subchildNode = subChildNodes.item(j);
					final String subNodeName = subchildNode.getNodeName();
					//Logger.out.debug("subnodeName : "+subNodeName);
					if (subNodeName.equals(CaTIESConstants.SITE_ABBRIVIATION))
					{
						final String pName = subchildNode.getFirstChild().getNodeValue();
						//Logger.out.debug("pName : "+pName);
						if(pName != null && abbr != null)
						{
							if (abbr.equals(pName))
							{
								//Logger.out.debug("pName : "+pName);
								isNameFound = true;
							}
						}
					}
					if (isNameFound && subNodeName.equals(CaTIESConstants.SITE_NAME))
					{
						return subchildNode.getFirstChild().getNodeValue();
					}
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Description:This method returns the default site name value.
	 * </p>
	 * @return pValue default site name
	 */
	public static String getDefaultSiteName()
	{
		try
		{
			// it gives the rootNode of the xml file
			final Element root = document.getDocumentElement();

			final NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++)
			{
				final Node child = children.item(i);

				if (child instanceof Element)
				{
					//Logger.out.debug("subchildNodes : "+subChildNodes.getLength());
					Logger.out.info("subnodeName : " + child.getNodeName());
					if (child.getNodeName().equals(CaTIESConstants.DEFAULT_SITE_NAME))
					{
						final String pName = child.getFirstChild().getNodeValue();
						Logger.out.info("sitename:" + pName);
						return pName;
					}
				}
			}
		}
		catch (final Exception ex)
		{
			SiteInfoHandler.logger.error(ex.getMessage(), ex);
			ex.printStackTrace() ;
		}
		return null;
	}
}