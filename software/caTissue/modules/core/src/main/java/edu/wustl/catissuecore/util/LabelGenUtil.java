package edu.wustl.catissuecore.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author nitesh_marwaha
 *
 */
public class LabelGenUtil
{
	/**
	 * logger.
	 */
	private static Logger logger = Logger.getCommonLogger(LabelGenUtil.class);
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
			logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw e;
		}
		catch (final IOException e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace() ;
			throw e;
		}
		catch (final ParserConfigurationException e)
		{
			logger.error(e.getMessage(), e);
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
	public static String getTypeAbbriviation(String typeName) throws Exception
	{
		if(document == null)
		{
			String path=CommonServiceLocator.getInstance().getPropDirPath()
			+ File.separator + Constants.ABBREVIATION_XAML_FILE_NAME;
			init(path);
		}
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
					if (subNodeName.equals(Constants.SPECIMEN_TYPE_NAME))
					{
						final String pName = subchildNode.getFirstChild().getNodeValue();
						//Logger.out.debug("pName : "+pName);
						if (typeName.equals(pName))
						{
							//Logger.out.debug("pName : "+pName);
							isNameFound = true;
						}
					}

					if (isNameFound && subNodeName.equals(Constants.SPECIMEN_TYPE_ABBRIVIATION))
					{
						return subchildNode.getFirstChild().getNodeValue();
					}
				}
			}
		}
		return document.getElementsByTagName("DEFAULT_NAME").item(0).getChildNodes().item(0).getNodeValue();
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
					if (subNodeName.equals(Constants.SPECIMEN_TYPE_ABBRIVIATION))
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
					if (isNameFound && subNodeName.equals(Constants.SPECIMEN_TYPE_NAME))
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
					if (child.getNodeName().equals(Constants.DEFAULT_TYPE_NAME))
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
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace() ;
		}
		return null;
	}

	/**
	 * Returns message label to display on success of add or edit.
	 * @return message.
	 */
	public static String getMessageLabel(AbstractDomainObject abstractDomainObject)
	{
		return abstractDomainObject.getId().toString();
	}

	public static String getMessageLabel(User user)
	{
		return user.getLastName() + "," + user.getFirstName();
	}
}
