package edu.wustl.catissuecore.querysuite.metadata;

import java.io.FileInputStream;
import java.util.Collection;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.TaggedValueInterface;
import edu.common.dynamicextensions.entitymanager.EntityGroupManager;
import edu.common.dynamicextensions.entitymanager.EntityGroupManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.daofactory.IDAOFactory;

/**
 * This class adds Tag Values for attributes and Paths 
 * @author falguni_sachde
 */
public class UpdateMetadataTagPath
{
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	//Logger
	private static final org.apache.log4j.Logger LOGGER = LoggerConfig
			.getConfiguredLogger(UpdateMetadataTagPath.class);
	
	public static final String ELEMENT_ENTITY_GROUP = "entity-group";

	public static final String ELEMENT_ENTITY = "entity";

	public static final String ELEMENT_NAME = "name";

	public static final String ELEMENT_ATTRIBUTE = "attribute";

	public static final String ELEMENT_TAG = "tag";

	public static final String ELEMENT_TAG_NAME = "tag-name";

	public static final String ELEMENT_TAG_VALUE = "tag-value";

	public static final String TAGGED_VALUE_NOT_SEARCHABLE = "NOT_SEARCHABLE";

	public static final String TAGGED_VALUE_NOT_VIEWABLE = "NOT_VIEWABLE";

	public static final String TAGGED_VALUE_PRIMARY_KEY = "PRIMARY_KEY";

	public static final String TAGGED_VALUE_PV_FILTER = "PV_FILTER";

	/**
	 * Adds tag values for entities and attributes.
	 * @param args filename
	 */
	public static void main(String[] args)
	{
		try
		{
			LOGGER.info("UpdateMetadataTagPath start");
			if (args.length < 1)
			{
				throw new Exception("Please Specify the xml filename");
			}

			//This will add Tagged values to Participant Object
			String fileName = args[0];
			readTaggedValues(fileName);
			LOGGER.info("UpdateMetadataTagPath finishes");
		}
		catch (Exception e)
		{
			LOGGER.info("error in UpdateMetadataTagPath" + e.getMessage());
		}
	}

	/**
	 * This method reads tagged values from xml file.
	 * @param fileName name of xml file
	 * @throws DynamicExtensionsApplicationException if entity group does not exist
	 * @throws DynamicExtensionsSystemException from EntityGroupManager
	 */
	private static void readTaggedValues(String fileName)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		SAXReader saxReader = new SAXReader();
		try
		{
			FileInputStream inputStream = new FileInputStream(fileName);
			Document document = saxReader.read(inputStream);
			Element rootElement = document.getRootElement();
			Iterator<Element> rootIterator = rootElement.elementIterator(ELEMENT_ENTITY_GROUP);
			//Element entityGrpEle = null;
			EntityGroupManagerInterface entityGroupManager = EntityGroupManager.getInstance();

			while (rootIterator.hasNext())
			{
				Element entityGrpEle = (Element) rootIterator.next();
				Element grpNameElement = entityGrpEle.element(ELEMENT_NAME);
				String entityGroupName = grpNameElement.getText();

				Long entitygroupId = EntityManager.getInstance().getEntityGroupId(entityGroupName);

				EntityGroupInterface entityGroup = entityGroupManager
						.getEntityGroupByName(entityGroupName);
				if (entityGroup == null)
				{
					LOGGER.info("entitygroup.doesNotExist" + entityGroupName);
				}
				else
				{
					readEntities(entityGrpEle, entitygroupId);
				}
			}
			LOGGER.info("=TAGGED VALUES ADDED SUCCESSFULLY!!!");
		}
		catch (Exception e)
		{
			LOGGER.info("=error in readTaggedValues !" + e.getMessage());
		}
	}

	/**
	 * Checks if tag is for NOT SEARCHABLE/NOT_VIEWABLE/PRIMARY KEY/PV_FILTER
	 * @param key
	 * @return
	 */
	private static boolean checkValidTag(String key)
	{
		return key.equals(TAGGED_VALUE_NOT_SEARCHABLE) || key.equals(TAGGED_VALUE_NOT_VIEWABLE)
				|| key.equals(TAGGED_VALUE_PRIMARY_KEY) || key.equals(TAGGED_VALUE_PV_FILTER);
	}

	/**
	 * This method reads entities present in the entity group, from the xml.
	 * @param entityGrpEle xml element for entity group
	 * @param entityGroup that contains the entities
	 * @throws DynamicExtensionsApplicationException if entity does not exist
	 * @throws DynamicExtensionsSystemException 
	 */
	private static void readEntities(Element entityGrpEle, Long entityGroupId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		Element nameElement;
		Iterator elementIterator = entityGrpEle.elementIterator(ELEMENT_ENTITY);//entity element

		while (elementIterator.hasNext())
		{
			Element entityElement = (Element) elementIterator.next();
			nameElement = entityElement.element(ELEMENT_NAME);
			String entityName = nameElement.getText();

			EntityInterface entity = EntityManager.getInstance().getEntityByIdentifier(
					EntityManager.getInstance().getEntityId(entityName, entityGroupId));

			if (entity == null)
			{
				throw new DynamicExtensionsApplicationException(ApplicationProperties.getValue(
						"entity.doesNotExist", entityName));//String errMsg
			}
			readAttributes(entity, entityElement);
			tagValueForEntity(entity, entityElement);
			saveEntity(entity);
		}
	}

	/**
	 * @param entity
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private static void saveEntity(EntityInterface entity) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory("dynamicExtention");
		DAO dao = null;
		try
		{
			dao = daoFactory.getDAO();
			dao.openSession(null);
			dao.update(entity);
			dao.commit();
			dao.closeSession();
		}
		catch (Exception ex)
		{
			throw new RuntimeException("error in saving object!!!!", ex);
		}
	}

	/**
	 * This method reads attributes of an entity.
	 * @param entity that contains the attributes.
	 * @param entityElement xml element for entity.
	 * @throws DynamicExtensionsApplicationException if attribute does not exist.
	 */
	private static void readAttributes(EntityInterface entity, Element entityElement)
			throws DynamicExtensionsApplicationException

	{
		Iterator<Element> attrItr = entityElement.elementIterator(ELEMENT_ATTRIBUTE);
		while (attrItr.hasNext())
		{
			Element attrElement = attrItr.next();
			Element attributeName = attrElement.element(ELEMENT_NAME);
			Iterator<Element> tagItr = attrElement.elementIterator(ELEMENT_TAG);
			String attrName = attributeName.getText();
			while (tagItr.hasNext())
			{
				AttributeInterface attribute = entity.getAttributeByName(attrName);
				LOGGER.info("==" + attrName);
				if (attribute == null)
				{
					throw new DynamicExtensionsApplicationException(ApplicationProperties.getValue(
							"attribute.doesNotExist", attrName));
				}
				Element tag = (Element) tagItr.next();
				String tagName = tag.element(ELEMENT_TAG_NAME).getText();
				String tagValue = tag.element(ELEMENT_TAG_VALUE).getText();
				checkValidTag(tagName);
				TaggedValueInterface taggedValue = getTagValue(attribute, tagName, tagValue);
				if (taggedValue != null)
				{
					System.out.println("adding tag value");
					attribute.addTaggedValue(taggedValue);
				}
			}
		}
	}

	/**
	 * Method creates a Tag Value object.
	 * @param tagName name of tag
	 * @param tagValue tag value.
	 * @return TaggedValueInterface
	 */
	private static TaggedValueInterface getTagValue(AttributeInterface attribute, String tagName,
			String tagValue)
	{
		TaggedValueInterface rettaggedValue = null;
		if (!attribute.getTaggedValueCollection().isEmpty())
		{
			Collection<TaggedValueInterface> taggedValues = attribute.getTaggedValueCollection();
			for (TaggedValueInterface taggedValue : taggedValues)
			{
				if (taggedValue.getKey().equalsIgnoreCase(tagName)
						&& taggedValue.getValue().equalsIgnoreCase(tagValue))
				{
					rettaggedValue = null;
					break;
				}
			}

		}
		else
		{
			rettaggedValue = DomainObjectFactory.getInstance().createTaggedValue();
			rettaggedValue.setKey(tagName);
			rettaggedValue.setValue(tagValue);

		}
		return rettaggedValue;

	}

	/**
	 * @param entity
	 * @param entityElement
	 */
	private static void tagValueForEntity(EntityInterface entity, Element entityElement)
	{

		Iterator<Element> entityTagItr = entityElement.elementIterator(ELEMENT_TAG);
		while (entityTagItr.hasNext())
		{

			Element entityTag = (Element) entityTagItr.next();
			if (entityTag != null)
			{
				String entityTagName = entityTag.element(ELEMENT_TAG_NAME).getText();
				String entityTagValue = entityTag.element(ELEMENT_TAG_VALUE).getText();
				LOGGER.info(entityTagName + "===" + entityTagValue);
				TaggedValueInterface taggedValue = createTagValue(entityTagName, entityTagValue);
				entity.addTaggedValue(taggedValue);
			}
		}
	}

	/**
	 * @param tagName
	 * @param tagValue
	 * @return
	 */
	private static TaggedValueInterface createTagValue(String tagName, String tagValue)
	{
		TaggedValueInterface taggedValue = DomainObjectFactory.getInstance().createTaggedValue();
		taggedValue.setKey(tagName);
		taggedValue.setValue(tagValue);
		return taggedValue;
	}
}