
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
 * This class adds Tag Values for attributes and Paths.
 * @author falguni_sachde
 */
public class UpdateMetadataTagPath
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	/**
	 * Logger.
	 */
	private static final org.apache.log4j.Logger LOGGER = LoggerConfig
			.getConfiguredLogger(UpdateMetadataTagPath.class);

	/**
	 * Specify ELEMENT_ENTITY_GROUP.
	 */
	public static final String ELEMENT_ENTITY_GROUP = "entity-group";

	/**
	 * Specify ELEMENT_ENTITY.
	 */
	public static final String ELEMENT_ENTITY = "entity";

	/**
	 * Specify ELEMENT_NAME.
	 */
	public static final String ELEMENT_NAME = "name";

	/**
	 * Specify ELEMENT_ATTRIBUTE.
	 */
	public static final String ELEMENT_ATTRIBUTE = "attribute";

	/**
	 * Specify ELEMENT_TAG.
	 */
	public static final String ELEMENT_TAG = "tag";

	/**
	 * Specify ELEMENT_TAG_NAME.
	 */
	public static final String ELEMENT_TAG_NAME = "tag-name";

	/**
	 * Specify ELEMENT_TAG_VALUE.
	 */
	public static final String ELEMENT_TAG_VALUE = "tag-value";

	/**
	 * Specify TAGGED_VALUE_NOT_SEARCHABLE.
	 */
	public static final String TAGGED_VALUE_NOT_SEARCHABLE = "NOT_SEARCHABLE";

	/**
	 * Specify TAGGED_VALUE_NOT_VIEWABLE.
	 */
	public static final String TAGGED_VALUE_NOT_VIEWABLE = "NOT_VIEWABLE";

	/**
	 * Specify TAGGED_VALUE_PRIMARY_KEY.
	 */
	public static final String TAGGED_VALUE_PRIMARY_KEY = "PRIMARY_KEY";

	/**
	 * Specify TAGGED_VALUE_PV_FILTER.
	 */
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
			final String fileName = args[0];
			readTaggedValues(fileName);
			LOGGER.info("UpdateMetadataTagPath finishes");
		}
		catch (final Exception e)
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
		final SAXReader saxReader = new SAXReader();
		try
		{
			final FileInputStream inputStream = new FileInputStream(fileName);
			final Document document = saxReader.read(inputStream);
			final Element rootElement = document.getRootElement();
			final Iterator<Element> rootIterator = rootElement
					.elementIterator(ELEMENT_ENTITY_GROUP);
			//Element entityGrpEle = null;
			final EntityGroupManagerInterface entityGroupManager = EntityGroupManager.getInstance();

			while (rootIterator.hasNext())
			{
				final Element entityGrpEle = rootIterator.next();
				final Element grpNameElement = entityGrpEle.element(ELEMENT_NAME);
				final String entityGroupName = grpNameElement.getText();

				final Long entitygroupId = EntityManager.getInstance().getEntityGroupId(
						entityGroupName);

				final EntityGroupInterface entityGroup = entityGroupManager
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
		catch (final Exception e)
		{
			LOGGER.info("=error in readTaggedValues !" + e.getMessage());
		}
	}

	/**
	 * Checks if tag is for NOT SEARCHABLE/NOT_VIEWABLE/PRIMARY KEY/PV_FILTER.
	 * @param key key
	 * @return true or false.
	 */
	private static boolean checkValidTag(String key)
	{
		return key.equals(TAGGED_VALUE_NOT_SEARCHABLE) || key.equals(TAGGED_VALUE_NOT_VIEWABLE)
				|| key.equals(TAGGED_VALUE_PRIMARY_KEY) || key.equals(TAGGED_VALUE_PV_FILTER);
	}

	/**
	 * This method reads entities present in the entity group, from the xml.
	 * @param entityGrpEle xml element for entity group
	 * @param entityGroupId that contains the entities
	 * @throws DynamicExtensionsApplicationException if entity does not exist
	 * @throws DynamicExtensionsSystemException DynamicExtensions System Exception
	 */
	private static void readEntities(Element entityGrpEle, Long entityGroupId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		Element nameElement;
		final Iterator elementIterator = entityGrpEle.elementIterator(ELEMENT_ENTITY);//entity element

		while (elementIterator.hasNext())
		{
			final Element entityElement = (Element) elementIterator.next();
			nameElement = entityElement.element(ELEMENT_NAME);
			final String entityName = nameElement.getText();

			final EntityInterface entity = EntityManager.getInstance().getEntityByIdentifier(
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
	 * @param entity entity
	 * @throws DynamicExtensionsSystemException Dynamic Extensions System Exception
	 * @throws DynamicExtensionsApplicationException Dynamic Extensions Application Exception
	 */
	private static void saveEntity(EntityInterface entity) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		final IDAOFactory daoFactory = DAOConfigFactory.getInstance().getDAOFactory(
				"dynamicExtention");
		DAO dao = null;
		try
		{
			dao = daoFactory.getDAO();
			dao.openSession(null);
			dao.update(entity);
			dao.commit();
			dao.closeSession();
		}
		catch (final Exception ex)
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
		final Iterator<Element> attrItr = entityElement.elementIterator(ELEMENT_ATTRIBUTE);
		while (attrItr.hasNext())
		{
			final Element attrElement = attrItr.next();
			final Element attributeName = attrElement.element(ELEMENT_NAME);
			final Iterator<Element> tagItr = attrElement.elementIterator(ELEMENT_TAG);
			final String attrName = attributeName.getText();
			while (tagItr.hasNext())
			{
				final AttributeInterface attribute = entity.getAttributeByName(attrName);
				LOGGER.info("==" + attrName);
				if (attribute == null)
				{
					throw new DynamicExtensionsApplicationException(ApplicationProperties.getValue(
							"attribute.doesNotExist", attrName));
				}
				final Element tag = tagItr.next();
				final String tagName = tag.element(ELEMENT_TAG_NAME).getText();
				final String tagValue = tag.element(ELEMENT_TAG_VALUE).getText();
				checkValidTag(tagName);
				final TaggedValueInterface taggedValue = getTagValue(attribute, tagName, tagValue);
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
	 * @param attribute attribute
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
			final Collection<TaggedValueInterface> taggedValues = attribute
					.getTaggedValueCollection();
			for (final TaggedValueInterface taggedValue : taggedValues)
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
	 * @param entity entity
	 * @param entityElement entity Element
	 */
	private static void tagValueForEntity(EntityInterface entity, Element entityElement)
	{

		final Iterator<Element> entityTagItr = entityElement.elementIterator(ELEMENT_TAG);
		while (entityTagItr.hasNext())
		{

			final Element entityTag = entityTagItr.next();
			if (entityTag != null)
			{
				final String entityTagName = entityTag.element(ELEMENT_TAG_NAME).getText();
				final String entityTagValue = entityTag.element(ELEMENT_TAG_VALUE).getText();
				LOGGER.info(entityTagName + "===" + entityTagValue);
				final TaggedValueInterface taggedValue = createTagValue(entityTagName,
						entityTagValue);
				entity.addTaggedValue(taggedValue);
			}
		}
	}

	/**
	 * @param tagName tag Name
	 * @param tagValue tag Value
	 * @return taggedValue
	 */
	private static TaggedValueInterface createTagValue(String tagName, String tagValue)
	{
		final TaggedValueInterface taggedValue = DomainObjectFactory.getInstance()
				.createTaggedValue();
		taggedValue.setKey(tagName);
		taggedValue.setValue(tagValue);
		return taggedValue;
	}
}