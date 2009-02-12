
package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class parses the XML file.
 * @author suhas_khot
 *
 */
public class XMLParser extends DefaultHandler
{

	//stores names of Collection Protocol
	private String[] collProtNamecoll = null;
	//store the form name
	private String formName = null;
	//store the override option for CP
	private String override = null;
	//stores the name of entity Group
	private String entityGroupName = null;
	//stores the entity names in list
	private List<String> entitiesName = null;
	//store the form name n a list
	private List<String> formNames = null;
	//stores the collection protocol mapping with entity group and entities
	private Map<String, Map<String, List<String>>> cpVsEGWithEntities = new HashMap<String, Map<String, List<String>>>();
	//stores mapping of collection protocol's id and corresponding entities
	private Map<Long, List<Long>> cpIdsVsEntityIds = new HashMap<Long, List<Long>>();
	//stores mapping of cpIds and its corresponding forms/category
	private Map<Long, List<Long>> cpIdsVsFormIds = new HashMap<Long, List<Long>>();
	//stores the mapping of form names and collection protocol
	private Map<String, List<String>> cpsVsForms = new HashMap<String, List<String>>();
	//store the mapping of entityGroup and entities
	private Map<String, List<String>> entGrpVsEntities = null;
	//check the whether EntityGroup is inside Collection Protocol
	private boolean collProtFlag = false;
	//check the whether entity is present inside EntityGroup
	private boolean entityGrpFlag = false;
	//stores mapping of collection protocol's id and its override option
	private Map<Long, String> cpIdVsOverride = new HashMap<Long, String>();
	//stores the mapping of form names and collection protocol
	private Map<String, String> cpsVsOverride = new HashMap<String, String>();

	/**
	 * @param filePath path of XML file
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException fails to perform database operation
	 * @throws ParserConfigurationException fail to get parser
	 * @throws SAXException  fails to parse the XML file
	 * @throws IOException fails to perform IO operation
	 */
	public XMLParser(String filePath) throws DynamicExtensionsSystemException, DAOException,
			ParserConfigurationException, SAXException, IOException
	{
		parseDocument(filePath);
	}

	/**
	 * Parses the XML file.
	 * @param filePath path of XML file
	 * @throws DAOException fails to connect database
	 * @throws DynamicExtensionsSystemException
	 * @throws SAXException fails to parse the XML file
	 * @throws ParserConfigurationException fail to get parser object
	 * @throws IOException fails to perform IO operation
	 */
	private void parseDocument(String filePath) throws DynamicExtensionsSystemException,
			DAOException, ParserConfigurationException, SAXException, IOException
	{
		//get a instance of factory
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setValidating(true);
		//get a new instance of parser
		SAXParser saxParser = saxParserFactory.newSAXParser();
		//parse the file
		saxParser.parse(filePath, this);
		processIds();
	}

	/**
	 * Event handler that checks the start of tag.
	 * @param uri the location of the content to be parsed.
	 * @param localName the local name, if NameSpace processing has not been performed
	 * @param qName the qualified name or the empty string if qualified names are not available.
	 * @param attributes specified or default attributes
	 * @throws SAXException if it fails to parse the XML file
	 */
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException
	{
		if (qName.equalsIgnoreCase(Constants.CP))
		{
			collProtFlag = true;
			formNames = new ArrayList<String>();
			entGrpVsEntities = new HashMap<String, List<String>>();
			if (attributes.getLength() != 2)
			{
				try
				{
					throw new DynamicExtensionsSystemException(
							"Name and override attributes are mandatory for the collection protocol.");
				}
				catch (DynamicExtensionsSystemException e)
				{
					throw new SAXException(e);
				}
			}
			else
			{
				String collProtName = attributes.getValue(Constants.NAME);
				override = attributes.getValue(Constants.OVERRIDE);

				if (collProtName == null
						|| collProtName.trim().length() <= 0
						|| override == null
						|| override.trim().length() <= 0
						|| (!Constants.OVERRIDE_FALSE.equalsIgnoreCase(override) && !Constants.OVERRIDE_TRUE
								.equalsIgnoreCase(override)))
				{
					try
					{
						throw new DynamicExtensionsSystemException(
								"Please enter valid Collection Protocol's attributes value.");
					}
					catch (DynamicExtensionsSystemException e)
					{
						throw new SAXException(e);
					}
				}
				collProtNamecoll = collProtName.split(",");
			}
		}
		if (qName.equalsIgnoreCase(Constants.ENTITY_GROUP))
		{
			entityGrpFlag = true;
			entitiesName = new ArrayList<String>();
			entityGroupName = attributes.getValue(Constants.NAME);
			if (entityGroupName == null)
			{
				try
				{
					throw new DynamicExtensionsSystemException(
							"Entity Group cannot be null. Please enter entityGroup name.");
				}
				catch (DynamicExtensionsSystemException e)
				{
					throw new SAXException(e);
				}
			}
		}
		if (qName.equalsIgnoreCase(Constants.ENTITY))
		{
			if (attributes.getValue(Constants.NAME) == null)
			{
				try
				{
					throw new DynamicExtensionsSystemException(
							"Entity cannot be null. Please enter Entity name.");
				}
				catch (DynamicExtensionsSystemException e)
				{
					throw new SAXException(e);
				}

			}
			if (entityGroupName != null && entityGrpFlag == true)
			{
				entitiesName.add(attributes.getValue(Constants.NAME));
			}
			else
			{
				try
				{
					throw new DynamicExtensionsSystemException(
							"Node Entity must be specified inside the EntityGroup node or check the EntityGroup's tag name.");
				}
				catch (DynamicExtensionsSystemException e)
				{
					throw new SAXException(e);
				}
			}
		}
		if (qName.equalsIgnoreCase(Constants.FORM))
		{
			formName = (attributes.getValue(Constants.NAME));
			if (formName == null)
			{
				try
				{
					throw new DynamicExtensionsSystemException(
							"Category cannot be null. Please enter Category name.");
				}
				catch (DynamicExtensionsSystemException e)
				{
					throw new SAXException(e);
				}
			}
		}
	}

	/**
	 * Event handler that checks the end of tag.
	 * @param uri the location of the content to be parsed.
	 * @param localName the local name, if NameSpace processing has not been performed
	 * @param qName the qualified name or the empty string if qualified names are not available.
	 * @throws SAXException if it fails to parse the XML file
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.equalsIgnoreCase(Constants.CP))
		{
			for (String cpName : collProtNamecoll)
			{
				cpsVsForms.put(cpName, formNames);
				cpVsEGWithEntities.put(cpName, entGrpVsEntities);
				cpsVsOverride.put(cpName, override);
			}
			collProtFlag = false;
		}
		if (qName.equalsIgnoreCase(Constants.ENTITY_GROUP))
		{
			if (collProtFlag == true)
			{
				entGrpVsEntities.put(entityGroupName, entitiesName);
			}
			else
			{
				try
				{
					throw new DynamicExtensionsSystemException(
							"Node EntityGroup must be specified inside the collection protocol node or check the collection protocol's tag name.");
				}
				catch (DynamicExtensionsSystemException e)
				{
					throw new SAXException(e);
				}
			}
			entityGrpFlag = false;
		}
//		if (qName.equalsIgnoreCase(Constants.ENTITY))
//		{
//		}
		if (qName.equalsIgnoreCase(Constants.FORM))
		{
			if (collProtFlag == true)
			{
				formNames.add(formName);
			}
			else
			{
				try
				{
					throw new DynamicExtensionsSystemException(
							"Node Form must be specified inside the collection protocol node or check the collection protocol's tag name.");
				}
				catch (DynamicExtensionsSystemException e)
				{
					throw new SAXException(e);
				}
			}
		}

	}

	/**
	 * @throws DAOException fails to do database operation
	 * @throws DynamicExtensionsSystemException fails to get valid EntityGroup's or entity Name or form name
	 */
	private void processIds() throws DAOException, DynamicExtensionsSystemException

	{
		Long entityGroupId = null;
		Long cpId = null;
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		String[] colName = {Constants.NAME};
		for (String cpName : cpVsEGWithEntities.keySet())
		{
			List<Long> entityIds = new ArrayList<Long>();
			List<Long> formIds = new ArrayList<Long>();
			if (Constants.ALL.equalsIgnoreCase(cpName) && cpName != null)
			{
				cpId = Long.valueOf(0);
			}
			else if ((cpName != null) && !(Constants.ALL.equalsIgnoreCase(cpName)))
			{
				cpId = (Long) Utility.getObjectIdentifier(cpName, CollectionProtocol.class.getName(),
						Constants.TITLE);
				if (cpId == null)
				{
					throw new DynamicExtensionsSystemException(
							"Please enter valid Collection Protocol's name or check whether Collection Protocol is created or not");
				}
			}
			entGrpVsEntities = cpVsEGWithEntities.get(cpName);
			for (String entityGrpName : entGrpVsEntities.keySet())
			{
				if (Constants.ALL.equalsIgnoreCase(entityGrpName) && entityGrpName != null)
				{
					Collection<NameValueBean> entityGrpBeanColl = entityManager
							.getAllEntityGroupBeans();
					for (NameValueBean entityGrpBean : entityGrpBeanColl)
					{
						Collection<Long> entityIdColl = entityManager
								.getAllEntityIdsForEntityGroup(Long.parseLong(entityGrpBean
										.getValue()));
						if (entityIdColl != null && !entityIdColl.isEmpty())
						{
							for (Long entityId : entityIdColl)
							{
								if (entityId != null)
								{
									entityIds.add(entityId);
								}
							}
						}
					}
				}
				else if (entityGrpName != null && !Constants.ALL.equalsIgnoreCase(entityGrpName))
				{
					entityGroupId = entityManager.getEntityGroupId(entityGrpName);
					if (entityGroupId == null)
					{
						throw new DynamicExtensionsSystemException(
								"Please enter valid EntityGroup name.");
					}
					entitiesName = entGrpVsEntities.get(entityGrpName);
					for (String entityName : entitiesName)
					{
						if (Constants.ALL.equalsIgnoreCase(entityName) && entityName != null)
						{
							Collection<Long> entityIdColl = entityManager
									.getAllEntityIdsForEntityGroup(entityGroupId);
							if (entityIdColl != null && !entityIdColl.isEmpty())
							{
								for (Long entityId : entityIdColl)
								{
									if (entityId != null)
									{
										entityIds.add(entityId);
									}
								}
							}
						}
						else if (entityName != null && !Constants.ALL.equalsIgnoreCase(entityName))
						{
							Long entityId = entityManager.getEntityId(entityName, entityGroupId);
							if (entityId == null)
							{
								throw new DynamicExtensionsSystemException(
										"Please enter valid Entity name.");
							}
							entityIds.add(entityId);
						}
					}
				}
			}

			for (String formName : cpsVsForms.get(cpName))
			{
				if (Constants.ALL.equalsIgnoreCase(formName) && formName != null)
				{
					List<String> formNameColl = defaultBizLogic.retrieve(Category.class.getName(),
							colName);
					for (String frmName : formNameColl)
					{
						Long rootCatEntityId = entityManager
								.getRootCategoryEntityIdByCategoryName(frmName);
						if (rootCatEntityId != null)
						{
							formIds.add(rootCatEntityId);
						}

					}
				}
				else if (formName != null && !Constants.ALL.equalsIgnoreCase(formName))
				{
					Long formId = entityManager.getRootCategoryEntityIdByCategoryName(formName);
					if (formId == null)
					{
						throw new DynamicExtensionsSystemException(
								"Please enter valid Category name.");
					}
					formIds.add(formId);
				}

			}
			cpIdsVsEntityIds.put(cpId, entityIds);
			cpIdsVsFormIds.put(cpId, formIds);
			cpIdVsOverride.put(cpId, cpsVsOverride.get(cpName));
		}
		setCpIdsVsEntityIds(cpIdsVsEntityIds);
		setCpIdsVsFormIds(cpIdsVsFormIds);
		setCpIdVsOverride(cpIdVsOverride);
	}

	/**
	 * @return the cpIdsVsEntityIds
	 */
	public Map<Long, List<Long>> getCpIdsVsEntityIds()
	{
		return cpIdsVsEntityIds;
	}

	/**
	 * @param cpIdsVsEntityIds the cpIdsVsEntityIds to set
	 */
	public void setCpIdsVsEntityIds(Map<Long, List<Long>> cpIdsVsEntityIds)
	{
		this.cpIdsVsEntityIds = cpIdsVsEntityIds;
	}

	/**
	 * @return the cpIdsVsFormIds
	 */
	public Map<Long, List<Long>> getCpIdsVsFormIds()
	{
		return cpIdsVsFormIds;
	}

	/**
	 * @param cpIdsVsFormIds the cpIdsVsFormIds to set
	 */
	public void setCpIdsVsFormIds(Map<Long, List<Long>> cpIdsVsFormIds)
	{
		this.cpIdsVsFormIds = cpIdsVsFormIds;
	}

	/**
	 * @return the cpIdVsOverride
	 */
	public Map<Long, String> getCpIdVsOverride()
	{
		return cpIdVsOverride;
	}

	/**
	 * @param cpIdVsOverride the cpIdVsOverride to set
	 */
	public void setCpIdVsOverride(Map<Long, String> cpIdVsOverride)
	{
		this.cpIdVsOverride = cpIdVsOverride;
	}

}
