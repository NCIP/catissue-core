
package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class show/hides entities-forms mention in CSV file.
 * @author suhas_khot
 *
 */
public class AssociatesForms
{

	/**
	 * Map for storing containers corresponding to entitiesIds
	 */
	private static Map<Long, Long> entityIdsVsContainersId = new HashMap<Long, Long>();

	/**
	 * @param args stores command line user inputs
	 * @throws DynamicExtensionsSystemException fails to validate
	 * @throws DAOException if it fails to do database operation
	 * @throws IOException if it fails to read file IO operation
	 * @throws BizLogicException fails to get the instance of BizLogic
	 * @throws UserNotAuthorizedException if user is not authorized to perform the operations
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException, IOException,
			DAOException, UserNotAuthorizedException, BizLogicException
	{
		//Validates arguments
		validate(args);
		String filePath = args[0];
		CSVFileParser csvFileParser = new CSVFileParser(filePath);
		//get EntityGroup with Collection Protocol along with corresponding entities
		csvFileParser.processCSV();

		Long typeId = (Long) getObjectIdentifier(Constants.COLLECTION_PROTOCOL,
				AbstractMetadata.class.getName(), Constants.NAME);

		getAllContainers(csvFileParser.getEntityGroupIds());

		disAssociateEntitiesAndForms(typeId);

		associateEntitiesAndForms(csvFileParser.getEntityIds());
		associateEntitiesAndForms(csvFileParser.getFormIds());
	}

	/**
	 * @param entityGroupIds entityGroupIds Collection
	 * @throws DynamicExtensionsSystemException fails to get forms,entity
	 * @throws DAOException fail to do database operation
	 */
	private static void getAllContainers(Set<Long> entityGroupIds)
			throws DynamicExtensionsSystemException, DAOException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		for (Long entityGroupId : entityGroupIds)
		{
			Collection<Long> entityIds = entityManager.getAllEntityIdsForEntityGroup(entityGroupId);
			if (entityIds != null && !entityIds.isEmpty())
			{
				for (Long entityId : entityIds)
				{
					Long containerId = entityManager.getContainerIdFromEntityId(entityId);
					if (containerId != null)
					{
						entityIdsVsContainersId.put(entityId, containerId);
					}
				}
			}
		}
		String[] colName = {Constants.NAME};
		List<String> formNameColl = defaultBizLogic.retrieve(Category.class.getName(), colName);
		for (String formName : formNameColl)
		{
			Long rootCategoryEntityId = entityManager.getRootCategoryEntityIdByCategoryName(formName);
			if(rootCategoryEntityId!=null)
			{
				Long containerId = entityManager.getContainerIdFromEntityId(rootCategoryEntityId);
				if (containerId != null)
				{
					entityIdsVsContainersId.put(rootCategoryEntityId, containerId);
				}
			}
		}
	}

	/**
	 * @throws DynamicExtensionsSystemException fails to validate arguments
	 * @param args filename
	 */
	private static void validate(String[] args) throws DynamicExtensionsSystemException
	{
		if (args.length == 0)
		{
			throw new DynamicExtensionsSystemException("PLEASE SPECIFY THE PATH FOR .csv FILE");
		}
	}

	/**
	 * @param typeId type id of collection protocol
	 * @throws DAOException if it fails to do database operation
	 */
	private static void disAssociateEntitiesAndForms(Long typeId) throws DAOException
	{
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		Long cpId = Long.valueOf(0);
		for (Long containerId : entityIdsVsContainersId.values())
		{
			if (containerId != null)
			{
				List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(),
						Constants.CONTAINERID, containerId);
				if (entityMapList != null && entityMapList.size() > 0)
				{
					EntityMap entityMap = entityMapList.get(0);
					editConditions(entityMap, cpId, typeId);
					annotation.updateEntityMap(entityMap);
				}
			}
		}
	}

	/**
	 * @param entityIds entityIds collection
	 * @throws DAOException if it fails to do database operation
	 * @throws DynamicExtensionsSystemException
	 * @throws UserNotAuthorizedException if user is not authorized to perform the operations
	 * @throws BizLogicException fails to get the instance of BizLogic
	 */
	private static void associateEntitiesAndForms(List<Long> entityIds) throws DAOException,
			DynamicExtensionsSystemException, UserNotAuthorizedException, BizLogicException
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
					editConditions(entityMap);
					annotation.updateEntityMap(entityMap);
				}
			}
			if (containerId == null)
			{
				throw new DynamicExtensionsSystemException(
						"This entity is not directly associated with the hook entity. Please enter proper entity in Show_Hide_Forms.csv file");
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
		if (entityIdsVsContainersId != null && entityIdsVsContainersId.size() > 0)
		{
			for (Long entityIdFromMap : entityIdsVsContainersId.keySet())
			{
				if (entityIdFromMap.equals(entityId))
				{
					containerId = entityIdsVsContainersId.get(entityIdFromMap);
				}
			}
		}
		return containerId;
	}

	/**
	 * @param entityMap to get formContext
	 * @param conditionObjectId condition on forms
	 * @param typeId collection protocol id
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
					EntityMapCondition entityMapCondition = getEntityMapCondition(formContext,
							conditionObjectId, typeId);
					entityMapCondColl.add(entityMapCondition);
				}
				else
				{
					for (EntityMapCondition entityMapCondition : entityMapCondColl)
					{
						entityMapCondition.setTypeId(typeId);
						entityMapCondition.setStaticRecordId(conditionObjectId);
						entityMapCondColl.add(entityMapCondition);
					}
				}
				formContext.setEntityMapConditionCollection(entityMapCondColl);
			}
		}
	}

	/**
	 * @param entityMap to get formContext Object
	 * @throws BizLogicException fails to retrieve bizLogic object
	 * @throws UserNotAuthorizedException user is not authenticated to perform operation
	 */
	private static void editConditions(EntityMap entityMap) throws BizLogicException,
			UserNotAuthorizedException
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
	 * @param formContext object on which condition has to be set
	 * @param conditionObjectId set the condition
	 * @param typeId set the typeId for entityMapCondition
	 * @return entityMapCondition
	 */
	private static EntityMapCondition getEntityMapCondition(FormContext formContext,
			Long conditionObjectId, Long typeId)
	{
		EntityMapCondition entityMapCondition = new EntityMapCondition();
		entityMapCondition.setStaticRecordId((conditionObjectId));
		entityMapCondition.setTypeId(((Long) typeId));
		entityMapCondition.setFormContext(formContext);
		return entityMapCondition;
	}

	/**
	 * @param whereColumnValue value of condition column
	 * @param selectObjName Object of select query to fire on
	 * @param whereColumnName name of condition column
	 * @return identifier
	 * @throws DAOException fails to do database operation
	 */
	private static Object getObjectIdentifier(String whereColumnValue, String selectObjName,
			String whereColumnName) throws DAOException
	{
		Object identifier = null;
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		String[] selectColName = {Constants.SYSTEM_IDENTIFIER};
		String[] whereColName = {whereColumnName};
		Object[] whereColValue = {whereColumnValue};
		String[] whereColCondition = {Constants.EQUALS};
		String joinCondition = Constants.AND_JOIN_CONDITION;
		List identifierList = defaultBizLogic.retrieve(selectObjName, selectColName, whereColName,
				whereColCondition, whereColValue, joinCondition);
		if (identifierList != null && identifierList.size() > 0)
		{
			identifier = identifierList.get(0);
		}
		return identifier;
	}
}
