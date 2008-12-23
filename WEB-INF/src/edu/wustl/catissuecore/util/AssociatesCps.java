
package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.AbstractMetadata;
import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.domain.integration.EntityMapCondition;
import edu.common.dynamicextensions.domain.integration.FormContext;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class associates Collection Protocol with the Corresponding entities mention in XML file.
 * @author suhas_khot
 *
 */
public class AssociatesCps
{

	/**
	 * Map for storing containers corresponding to entitiesIds
	 */
	public static Map<Long, Long> entityIdsVsContId = new HashMap<Long, Long>();

	/**
	 * @param args get the command line inputs
	 * @throws DynamicExtensionsSystemException fails to validate
	 * @throws DAOException if it fails to do database operation
	 * @throws IOException if it fails to read file IO operation
	 * @throws UserNotAuthorizedException if user is not authorized to perform the operations
	 * @throws BizLogicException fails to get the instance of BizLogic
	 * @throws SAXException fail to parse XML file.
	 * @throws ParserConfigurationException fails to get parser
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException, DAOException,
			ParserConfigurationException, SAXException, IOException, UserNotAuthorizedException,
			BizLogicException
	{
		//Validates arguments
		validate(args);
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

		Long typeId = (Long) xmlParser.getObjectIdentifier(Constants.COLLECTION_PROTOCOL,
				AbstractMetadata.class.getName(), Constants.NAME);
		getAllContainers();

		for (Long cpId : cpIdsVsoverride.keySet())
		{
			if ((cpId != null)
					&& ((Constants.OVERRIDE_TRUE).equalsIgnoreCase(cpIdsVsoverride.get(cpId))))
			{
				disAssociateEntitiesForms(typeId, cpId);
			}
		}

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
	 * @param entityGroupIds entityIds Collection
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException if it fails to do database operation
	 */
	public static void getAllContainers() throws DynamicExtensionsSystemException, DAOException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		String[] colName = {Constants.NAME};
		Collection<NameValueBean> entityGrpBeanColl = entityManager.getAllEntityGroupBeans();
		for (NameValueBean entityGrpBean : entityGrpBeanColl)
		{
			Collection<Long> entityIds = entityManager.getAllEntityIdsForEntityGroup(Long
					.parseLong(entityGrpBean.getValue()));
			if (entityIds != null && !entityIds.isEmpty())
			{
				for (Long entityId : entityIds)
				{
					Long containerId = entityManager.getContainerIdFromEntityId(entityId);
					if (containerId != null)
					{
						entityIdsVsContId.put(entityId, containerId);
					}
				}
			}
		}

		List<String> formNameColl = defaultBizLogic.retrieve(Category.class.getName(), colName);
		for (String formName : formNameColl)
		{
			Long rootCatEntityId = entityManager.getRootCategoryEntityIdByCategoryName(formName);
			if (rootCatEntityId != null)
			{
				Long containerId = entityManager.getContainerIdFromEntityId(rootCatEntityId);
				if (containerId != null)
				{
					entityIdsVsContId.put(rootCatEntityId, containerId);
				}
			}
		}

	}

	/**
	 * @param args get command line user inputs
	 * @throws DynamicExtensionsSystemException if CSV file path has not been mention
	 */
	private static void validate(String[] args) throws DynamicExtensionsSystemException
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
	 * @throws BizLogicException 
	 * @throws UserNotAuthorizedException 
	 */
	private static void disAssociateEntitiesForms(Long typeId, long cpId) throws DAOException,
			DynamicExtensionsSystemException, UserNotAuthorizedException, BizLogicException
	{
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		EntityManagerInterface entityManager = EntityManager.getInstance();
		if (cpId != 0)
		{
			Collection<EntityMapCondition> entMapCondColl = entityManager
					.getAllConditionsByStaticRecordId(cpId);
			for (EntityMapCondition entityMapCond : entMapCondColl)
			{
				entityMapCond.setTypeId(typeId);
				entityMapCond.setStaticRecordId(Long.valueOf(0));
				annotation.update(entityMapCond, Constants.HIBERNATE_DAO);
			}
		}
		else
		{
			for (Long containerId : entityIdsVsContId.values())
			{
				if (containerId != null)
				{
					List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class
							.getName(), Constants.CONTAINERID, containerId);
					if (entityMapList != null && entityMapList.size() > 0)
					{
						EntityMap entityMap = entityMapList.get(0);
						Collection<FormContext> formContextColl = entityMap
								.getFormContextCollection();
						if (formContextColl != null)
						{
							for (FormContext formContext : formContextColl)
							{
								Collection<EntityMapCondition> entityMapCondColl = formContext
										.getEntityMapConditionCollection();
								if (entityMapCondColl.isEmpty() || entityMapCondColl.size() <= 0)
								{
									EntityMapCondition entityMapCond = getEntityMapCondition(
											formContext, Long.valueOf(0), typeId);
									entityMapCondColl.add(entityMapCond);
								}
								formContext.setEntityMapConditionCollection(entityMapCondColl);
							}
						}
						annotation.updateEntityMap(entityMap);
					}
				}
			}
		}
	}

	/**
	 * @param cpId stores Id of Collection Protocol 
	 * @param typeId stores Id of Collection Protocol object
	 * @param entityIds entityIds collection
	 * @throws DAOException if it fails to do database operation
	 * @throws BizLogicException fails to get bizLogic object
	 * @throws UserNotAuthorizedException user is not authorized to perform operation
	 */
	private static void associateEntitiesToCps(Long cpId, Long typeId, List<Long> entityIds)
			throws DAOException, UserNotAuthorizedException, BizLogicException
	{
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		for (Long entityId : entityIds)
		{
			DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
			Long containerId = getContainerId(entityId);
			if (containerId != null)
			{
				List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(),
						Constants.CONTAINERID, containerId);
				if (entityMapList != null && entityMapList.size() > 0)
				{
					EntityMap entityMap = entityMapList.get(0);
					if (cpId != 0)
					{
						editConditions(entityMap, cpId, typeId);
					}
					if (cpId == 0)
					{
						editConditions(entityMap);
					}
					annotation.updateEntityMap(entityMap);
				}
			}

		}
	}

	/**
	 * @param entityId to get corresponding containerId
	 * @return containerId based on particular entityId
	 */
	private static Long getContainerId(Long entityId)
	{
		Long containerId = null;
		if (entityIdsVsContId != null && entityIdsVsContId.size() > 0)
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
	 */
	private static void editConditions(EntityMap entityMap, Long conditionObjectId, Long typeId)
	{
		Collection<FormContext> formContextColl = entityMap.getFormContextCollection();
		if (formContextColl != null)
		{
			for (FormContext formContext : formContextColl)
			{
				Collection<EntityMapCondition> entityMapCondColl = formContext
						.getEntityMapConditionCollection();
				if (entityMapCondColl.isEmpty() || entityMapCondColl.size() <= 0)
				{
					EntityMapCondition entityMapCond = getEntityMapCondition(formContext,
							conditionObjectId, typeId);
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
							EntityMapCondition entityMapCond = getEntityMapCondition(formContext,
									conditionObjectId, typeId);
							entityMapCondColl.add(entityMapCond);
							break;
						}
					}
				}
				formContext.setEntityMapConditionCollection(entityMapCondColl);
			}
		}
	}

	/**
	 * override to associate all CPs for this entity/form
	 * @param entityMap to get formContext object
	 * @throws BizLogicException fails to retrieve bizLogic object
	 * @throws UserNotAuthorizedException user is not authenticated to perform operation
	 */
	private static void editConditions(EntityMap entityMap) throws UserNotAuthorizedException,
			BizLogicException
	{
		Collection<FormContext> formContextColl = entityMap.getFormContextCollection();
		if (formContextColl != null)
		{
			AnnotationBizLogic annotation = new AnnotationBizLogic();
			annotation.updateEntityMap(entityMap);
			for (FormContext formContext : formContextColl)
			{
				Collection<EntityMapCondition> entityMapCondColl = formContext
						.getEntityMapConditionCollection();

				if (!entityMapCondColl.isEmpty() || entityMapCondColl.size() > 0)
				{
					for (EntityMapCondition entityMapCondition : entityMapCondColl)
					{
						annotation.delete(entityMapCondition, Constants.HIBERNATE_DAO);
					}
				}
				formContext.setEntityMapConditionCollection(null);
			}
		}
	}

	/**
	 * @param formContext set the object on which condition has to be set
	 * @param conditionObjectId set the condition
	 * @param typeId set the typeId for entityMapCondition
	 * @return entityMapCondition object
	 */
	private static EntityMapCondition getEntityMapCondition(FormContext formContext,
			Long conditionObjectId, Long typeId)
	{
		EntityMapCondition entityMapCond = new EntityMapCondition();
		entityMapCond.setTypeId(((Long) typeId));
		entityMapCond.setStaticRecordId((conditionObjectId));
		entityMapCond.setFormContext(formContext);
		return entityMapCond;
	}

}
