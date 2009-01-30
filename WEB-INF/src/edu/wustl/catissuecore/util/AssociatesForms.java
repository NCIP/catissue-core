
package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.domain.AbstractMetadata;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
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
		validateCSV(args);
		String filePath = args[0];
		CSVFileParser csvFileParser = new CSVFileParser(filePath);
		//get EntityGroup with Collection Protocol along with corresponding entities
		csvFileParser.processCSV();

		Long typeId = (Long) Utility.getObjectIdentifier(Constants.COLLECTION_PROTOCOL,
				AbstractMetadata.class.getName(), Constants.NAME);

		entityIdsVsContainersId = Utility.getAllContainers(csvFileParser.getEntityGroupIds());

		disAssociateEntitiesAndForms(typeId);

		associateEntitiesForms(csvFileParser.getEntityIds(),typeId);
		associateEntitiesForms(csvFileParser.getFormIds(),typeId);
	}

	/**
	 * @throws DynamicExtensionsSystemException fails to validate arguments
	 * @param args filename
	 */
	private static void validateCSV(String[] args) throws DynamicExtensionsSystemException
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
					Utility.editConditions(entityMap, cpId, typeId);
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
	private static void associateEntitiesForms(List<Long> entityIds, Long typeId) throws DAOException,
			DynamicExtensionsSystemException, UserNotAuthorizedException, BizLogicException
	{
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		Long conditionObject = Long.valueOf(-1);
		for (Long entityId : entityIds)
		{
			Long containerId = getContainerId(entityId);
			if (containerId != null)
			{
				List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(),
						Constants.CONTAINERID, containerId);
				if (entityMapList != null && entityMapList.size() > 0)
				{
					EntityMap entityMap = entityMapList.get(0);
					Utility.editConditions(entityMap, conditionObject, typeId);
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

}
