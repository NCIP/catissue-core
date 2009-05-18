
package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.AbstractMetadata;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.dao.exception.DAOException;

/**
 * This class associates Collection Protocol with the Corresponding entities mention in XML file.
 * @author suhas_khot
 *
 */
public final class AssociatesCps
{

	/*
	 * create singleton object
	 */
	private static AssociatesCps associateCp = new AssociatesCps();

	/*
	 * private constructor
	 */
	private AssociatesCps()
	{

	}

	/*
	 * returns single object
	 */
	public static AssociatesCps getInstance()
	{
		return associateCp;
	}

	/**
	 * Map for storing containers corresponding to entitiesIds
	 */
	public static Map<Long, Long> entityIdsVsContId = new HashMap<Long, Long>();

	/**
	 * @param args get the command line inputs
	 * @throws DynamicExtensionsSystemException fails to validate
	 * @throws IOException if it fails to read file IO operation
	 * @throws SAXException fail to parse XML file.
	 * @throws ParserConfigurationException fails to get parser
	 * @throws ApplicationException Application Exception
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException,
			ParserConfigurationException, SAXException, IOException, ApplicationException
	{
		//Validates arguments
		validateXML(args);
		//stores filePath
		String filePath = args[0];
		if (Constants.DOUBLE_QUOTES.equals(filePath))
		{
			throw new DynamicExtensionsSystemException("Please enter valid file path");
		}
		XMLParser xmlParser = new XMLParser(filePath);
		//stores mapping of cpIds and corresponding entityIds
		Map<Long, List<Long>> cpIdsVsEntityIds = xmlParser.getCpIdsVsEntityIds();
		//stores mapping of cpIds and corresponding forms/Category
		Map<Long, List<Long>> cpIdsVsFormIds = xmlParser.getCpIdsVsFormIds();
		//stores mapping of cpIds and override option
		Map<Long, String> cpIdsVsoverride = xmlParser.getCpIdVsOverride();

		Long typeId = (Long) AppUtility.getObjectIdentifier(Constants.COLLECTION_PROTOCOL,
				AbstractMetadata.class.getName(), Constants.NAME);
		entityIdsVsContId = AppUtility.getAllContainers();
		dissAssociateEntitiesFormsPerCpId(cpIdsVsoverride, typeId);
		for (Long cpId : cpIdsVsEntityIds.keySet())
		{
			associateEntitiesToCps(cpId, typeId, cpIdsVsEntityIds.get(cpId));
		}
		for (Long cpId : cpIdsVsFormIds.keySet())
		{
			associateEntitiesToCps(cpId, typeId, cpIdsVsFormIds.get(cpId));
		}
	}

	/**
	 * this method dissAssociate Entities per cpId.
	 * @param cpIdsVsoverride
	 * @param typeId
	 * @throws DynamicExtensionsSystemException
	 * @throws ApplicationException Application Exception
	 */
	private static void dissAssociateEntitiesFormsPerCpId(Map<Long, String> cpIdsVsoverride,
			Long typeId) throws DynamicExtensionsSystemException,
			ApplicationException
	{
		for (Long cpId : cpIdsVsoverride.keySet())
		{
			if ((cpId != null)
					&& ((Constants.OVERRIDE_TRUE).equalsIgnoreCase(cpIdsVsoverride.get(cpId))))
			{
				disAssociateEntitiesForms(typeId, cpId);
			}
		}
	}

	/**
	 * @param args get command line user inputs
	 * @throws DynamicExtensionsSystemException if CSV file path has not been mention
	 */
	private static void validateXML(String[] args) throws DynamicExtensionsSystemException
	{
		if (args.length == 0)
		{
			throw new DynamicExtensionsSystemException("PLEASE SPECIFY THE PATH FOR .xml FILE");
		}
	}

	/**
	 * @param typeId stores type of condition
	 * @param cpId stores collection protocol's identifier
	 * @throws DAOException if it fails to do database operation
	 * @throws DynamicExtensionsSystemException 
	 * @throws ApplicationException Application Exception
	 */
	private static void disAssociateEntitiesForms(Long typeId, long cpId)
			throws DynamicExtensionsSystemException, ApplicationException
	{
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		EntityManagerInterface entityManager = EntityManager.getInstance();
		if (cpId != 0)
		{
			Collection<EntityMapCondition> entMapCondColl = entityManager
					.getAllConditionsByStaticRecordId(cpId);
			for (EntityMapCondition entityMapCond : entMapCondColl)
			{
				entityMapCond.setTypeId(typeId);
				entityMapCond.setStaticRecordId(Long.valueOf(0));
				annotation.update(entityMapCond);
			}
		}
		else
		{
			Long cpObjectId = Long.valueOf(-1);
			Collection<EntityMapCondition> entMapCondColl = entityManager
					.getAllConditionsByStaticRecordId(cpObjectId);
			for (EntityMapCondition entityMapCond : entMapCondColl)
			{
				entityMapCond.setTypeId(typeId);
				entityMapCond.setStaticRecordId(Long.valueOf(0));
				annotation.update(entityMapCond);
			}
		}
	}

	/**
	 * @param cpId stores Id of Collection Protocol 
	 * @param typeId stores Id of Collection Protocol object
	 * @param entityIds entityIds collection
	 * @throws DynamicExtensionsSystemException 
	 * @throws ApplicationException Application Exception
	 */
	private static void associateEntitiesToCps(Long cpId, Long typeId, List<Long> entityIds)
			throws ApplicationException
	{
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		for (Long entityId : entityIds)
		{
			Long containerId = getContainerId(entityId);
			if (containerId != null)
			{
				List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(),
						Constants.CONTAINERID, containerId);
				updateEntityMap(cpId, typeId, annotation, entityMapList);
			}

		}
	}

	/**
	 * method updates EntityMap after chaking entityMapList for Null.   
	 * @param cpId
	 * @param typeId
	 * @param annotation
	 * @param entityMapList
	 * @throws DynamicExtensionsSystemException 
	 * @throws ApplicationException Application Exception
	 */
	private static void updateEntityMap(Long cpId, Long typeId, AnnotationBizLogic annotation,
			List<EntityMap> entityMapList) throws ApplicationException
	{
		if (entityMapList != null && !entityMapList.isEmpty())
		{
			EntityMap entityMap = entityMapList.get(0);
			if (cpId != 0)
			{
				editConditions(entityMap, cpId, typeId);
			}
			if (cpId == 0)
			{
				Long conditionObject = Long.valueOf(-1);
				editConditions(entityMap, conditionObject, typeId);
			}
			annotation.updateEntityMap(entityMap);
		}
	}

	/**
	 * @param entityId to get corresponding containerId
	 * @return containerId based on particular entityId
	 */
	private static Long getContainerId(Long entityId)
	{
		Long containerId = null;
		if (entityIdsVsContId != null && !entityIdsVsContId.isEmpty())
		{
			for (Long entityIdFromMap : entityIdsVsContId.keySet())
			{
				if (entityIdFromMap.equals(entityId))
				{
					containerId = entityIdsVsContId.get(entityIdFromMap);
				}
			}
		}
		return containerId;
	}

	/**
	 * associates entity/forms to a CP
	 * @param entityMap to get formContext
	 * @param conditionObjectId to set particular condition
	 * @param typeId stores the identifier value of Collection Protocol object
	 * @throws DynamicExtensionsSystemException 
	 * @throws ApplicationException Application Exception
	 */
	private static void editConditions(EntityMap entityMap, Long conditionObjectId, Long typeId)
			throws ApplicationException
	{
		Collection<FormContext> formContextColl = AppUtility.getFormContexts(entityMap.getId());

		if (formContextColl != null)
		{
			for (FormContext formContext : formContextColl)
			{
				Collection<EntityMapCondition> entityMapCondColl = AppUtility
						.getEntityMapConditions(formContext.getId());

				if (entityMapCondColl.isEmpty() || entityMapCondColl.size() <= 0)
				{
					EntityMapCondition entityMapCond = AppUtility.getEntityMapCondition(
							formContext, conditionObjectId, typeId);
					entityMapCondColl.add(entityMapCond);
				}
				else
				{
					boolean flag = true;
					for (EntityMapCondition entityMapCondition : entityMapCondColl)
					{
						if ((entityMapCondition.getStaticRecordId() == 0) && (flag == true))
						{
							flag = false;
							entityMapCondition.setTypeId(typeId);
							entityMapCondition.setStaticRecordId(conditionObjectId);
							entityMapCondColl.add(entityMapCondition);
							break;
						}
						else if ((entityMapCondition.getStaticRecordId() != 0) && (flag == true))
						{
							flag = false;
							EntityMapCondition entityMapCond = AppUtility.getEntityMapCondition(
									formContext, conditionObjectId, typeId);
							entityMapCondColl.add(entityMapCond);
							break;
						}
					}
				}
				formContext.setEntityMapConditionCollection(entityMapCondColl);
			}
			entityMap.setFormContextCollection(formContextColl);
		}
	}
}
