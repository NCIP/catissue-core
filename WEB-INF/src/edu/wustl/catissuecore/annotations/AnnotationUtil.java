/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@version 1.0
 */

package edu.wustl.catissuecore.annotations;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.common.dynamicextensions.domain.integration.AbstractRecordEntry;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry;
import edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;

/**
 *
 * @author janhavi_hasabnis
 *
 */
public class AnnotationUtil

{

	private transient final Logger logger = Logger.getCommonLogger(AnnotationUtil.class);
	/**
	 * This method updates module map by parsing xml file
	 * @param xmlFileName file to be parsed
	 * @return dataType Map
	 * @throws DataTypeFactoryInitializationException on Exception
	 */
	public static Map map = new HashMap();

	public final List<NameValueBean> populateStaticEntityList(String xmlFileName, String displayNam)
			throws DataTypeFactoryInitializationException
	{
		final List list = new ArrayList();

		final SAXReader saxReader = new SAXReader();
		final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(
				xmlFileName);

		Document document = null;

		try
		{
			document = saxReader.read(inputStream);
			Element className = null;
			Element displayName = null;
			Element conditionInvoker = null;

			final Element primitiveAttributesElement = document.getRootElement();
			final Iterator primitiveAttributeElementIterator = primitiveAttributesElement
					.elementIterator("static-entity");

			Element primitiveAttributeElement = null;

			while (primitiveAttributeElementIterator.hasNext())
			{
				primitiveAttributeElement = (Element) primitiveAttributeElementIterator.next();

				className = primitiveAttributeElement.element("name");
				displayName = primitiveAttributeElement.element("displayName");
				conditionInvoker = primitiveAttributeElement.element("conditionInvoker");
				list
						.add(new NameValueBean(displayName.getStringValue(), className
								.getStringValue()));

				if (displayNam != null && className.getText().equals(displayNam))
				{
					map.put("name", className.getText());
					map.put("displayName", displayName.getText());
					map.put("conditionInvoker", conditionInvoker.getText());
				}

			}
		}
		catch (final DocumentException documentException)
		{
			this.logger.error(documentException.getMessage(), documentException);
			throw new DataTypeFactoryInitializationException(documentException);
		}

		return list;
	}

	public boolean checkForAll(String[] conditions)
	{
		if (conditions != null)
		{
			for (final String condition : conditions)
			{
				if (condition.equals(Constants.ALL))
				{
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @param annotationForm - annotationForm
	 * @throws DynamicExtensionsApplicationException - DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException - DynamicExtensionsSystemException
	 * @throws CacheException - CacheException
	 * @return - List
	 */
	public static List getSystemEntityList() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, CacheException
	{
		final List<NameValueBean> systemEntityList = new ArrayList<NameValueBean>();
		final AnnotationUtil util = new AnnotationUtil();
		final List<NameValueBean> staticEntityInformationList = util.populateStaticEntityList(
				"StaticEntityInformation.xml", null);

		systemEntityList.add(new NameValueBean(Constants.SELECT_OPTION,
				Constants.SELECT_OPTION_VALUE));
		String key = null;
		if (staticEntityInformationList != null && !staticEntityInformationList.isEmpty())
		{
			final CatissueCoreCacheManager cache = CatissueCoreCacheManager.getInstance();
			final Iterator listIterator = staticEntityInformationList.iterator();
			while (listIterator.hasNext())
			{
				final NameValueBean nameValueBean = (NameValueBean) listIterator.next();
				key = getHookEntityIdByName(nameValueBean.getValue());
				if (key != null && !key.equals(""))
				{
					systemEntityList.add(new NameValueBean(nameValueBean.getName(), cache
							.getObjectFromCache(key)));
				}
			}
		}
		return systemEntityList;
	}

	/**
	 *
	 * @param entityName - entityName
	 * @return - String
	 */
/*	private static String getKeyFromEntityName(String entityName)
	{
		String key = "";
		if (entityName != null)
		{
			if (entityName.equals(AnnotationConstants.ENTITY_NAME_PARTICIPANT))
			{
				key = AnnotationConstants.PARTICIPANT_ENTITY_ID;
			}
			else if (entityName.equals(AnnotationConstants.ENTITY_NAME_SPECIMEN_COLLN_GROUP))
			{
				key = AnnotationConstants.SCG_ENTITY_ID;
			}
			else if (entityName.equals(AnnotationConstants.ENTITY_NAME_SPECIMEN))
			{
				key = AnnotationConstants.SPECIMEN_ENTITY_ID;
			}
		}
		return key;
	}*/
	/**
	 *
	 * @param abstractRecordEntry
	 * @return
	 */
	public static String getHookEntityIdByName(String  abstractRecordEntryName)
	{
		String key = null;
		final CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
				.getInstance();
		if (abstractRecordEntryName.equals(AnnotationConstants.ENTITY_NAME_PARTICIPANT_REC_ENTRY))
		{
			key = AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID;
		}
		else if (abstractRecordEntryName.equals(AnnotationConstants.ENTITY_NAME_SPECIMEN_REC_ENTRY))
		{
			key = AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID;
		}
		else
		{
			key = AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID;
		}

		return key;
	}

	/**
	 *
	 * @param abstractRecordEntry
	 * @return
	 */
	public static Long getHookEntityId(AbstractRecordEntry abstractRecordEntry)
	{
		String key = null;
		final CatissueCoreCacheManager catissueCoreCacheManager = CatissueCoreCacheManager
				.getInstance();
		if (abstractRecordEntry instanceof ParticipantRecordEntry)
		{
			key = AnnotationConstants.PARTICIPANT_REC_ENTRY_ENTITY_ID;
		}
		else if (abstractRecordEntry instanceof SpecimenRecordEntry)
		{
			key = AnnotationConstants.SPECIMEN_REC_ENTRY_ENTITY_ID;
		}
		else
		{
			key = AnnotationConstants.SCG_REC_ENTRY_ENTITY_ID;
		}

		return (Long) catissueCoreCacheManager.getObjectFromCache(key);
	}

	/**
	 *
	 * @param abstractRecordEntry
	 * @return
	 */
	public static Long getStaticEntityRecordId(AbstractRecordEntry abstractRecordEntry)
	{
		String hqlQuery = "";

		if (abstractRecordEntry instanceof ParticipantRecordEntry)
		{
			hqlQuery = "select partRE.participant.id from "
					+ "edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry partRE where "
					+ "partRE.id=" + abstractRecordEntry.getId();
		}
		else if (abstractRecordEntry instanceof SpecimenRecordEntry)
		{
			hqlQuery = "select sp.specimen.id from "
					+ "edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry sp where "
					+ "id=" + abstractRecordEntry.getId();
		}
		else
		{
			hqlQuery = "select scg.specimenCollectionGroup.id from "
					+ "edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry scg where "
					+ "id=" + abstractRecordEntry.getId();
		}
		Object obj = null;
		try
		{
			List<Object[]> idList = AppUtility.executeQuery(hqlQuery);
			obj = idList.get(0);
		}
		catch (ApplicationException e)
		{
			e.printStackTrace();
		}

		return Long.valueOf(obj.toString());
	}


	/**
	 *
	 * @param recordEntryEntityName
	 * @return
	 */
	public static String getAssociatedStaticEntityForRecordEntry(String recordEntryEntityName)
	{
		String associatedStaticEntity = null;
		if(ParticipantRecordEntry.class.getName().equals(recordEntryEntityName))
		{
			associatedStaticEntity = Participant.class.getName();
		}
		else if(SpecimenRecordEntry.class.getName().equals(recordEntryEntityName))
		{
			associatedStaticEntity = Specimen.class.getName();
		}
		else if(SCGRecordEntry.class.getName().equals(recordEntryEntityName))
		{
			associatedStaticEntity = SpecimenCollectionGroup.class.getName();
		}
		return associatedStaticEntity;
	}
}
