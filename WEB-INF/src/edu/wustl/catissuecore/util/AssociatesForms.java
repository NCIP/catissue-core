
package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.bizlogic.BizLogicFactory;
import edu.common.dynamicextensions.dao.impl.DynamicExtensionDAO;
import edu.common.dynamicextensions.domain.AbstractMetadata;
import edu.common.dynamicextensions.domain.integration.EntityMap;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.bizlogic.AnnotationBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * This class show/hides entities-forms mention in CSV file.
 * @author suhas_khot
 *
 */
public final class AssociatesForms
{
	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(AssociatesForms.class);
	/*
	 * create singleton object
	 */
	private static AssociatesForms associatForms= new AssociatesForms();
	/*
	 * private constructor
	 */
	private AssociatesForms()
	{
		
	}
	/*
	 * returns single object
	 */
	public static AssociatesForms getInstance()
	{
		return associatForms;
	}
	
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
	ApplicationException
	{
		//Validates arguments
		validateCSV(args);
		String filePath = args[0];
		CSVFileParser csvFileParser = new CSVFileParser(filePath);
		//get EntityGroup with Collection Protocol along with corresponding entities
		csvFileParser.processCSV();

		Long typeId = (Long) AppUtility.getObjectIdentifier(Constants.COLLECTION_PROTOCOL,
				AbstractMetadata.class.getName(), Constants.NAME);

		entityIdsVsContainersId = AppUtility.getAllContainers(csvFileParser.getEntityGroupIds());

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
	 * @throws DynamicExtensionsSystemException 
	 */
	private static void disAssociateEntitiesAndForms(Long typeId) throws ApplicationException, DynamicExtensionsSystemException
	{
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		annotation.setAppName(DynamicExtensionDAO.getInstance().getAppName());
		Long cpId = Long.valueOf(0);
		for (Long containerId : entityIdsVsContainersId.values())
		{
			if (containerId != null)
			{
				List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(),
						Constants.CONTAINERID, containerId);
				if (entityMapList != null && !entityMapList.isEmpty())
				{
					EntityMap entityMap = entityMapList.get(0);
					AppUtility.editConditions(entityMap, cpId, typeId, true);
					annotation.updateEntityMap(entityMap);
				}
			}
		}
	}

	/**
	 * @param entityIds entityIds collection
	 * @throws DynamicExtensionsSystemException
	 * @throws ApplicationException Application Exception
	 */
	private static void associateEntitiesForms(List<Long> entityIds, Long typeId) throws DynamicExtensionsSystemException, ApplicationException
	{
		AnnotationBizLogic annotation = new AnnotationBizLogic();
		annotation.setAppName(DynamicExtensionDAO.getInstance().getAppName());
		DefaultBizLogic defaultBizLogic = BizLogicFactory.getDefaultBizLogic();
		Long conditionObject = Long.valueOf(-1);
		for (Long entityId : entityIds)
		{
			Long containerId = getContainerId(entityId);
			if (containerId == null)
			{
				throw new DynamicExtensionsSystemException(
				"This entity is not directly associated with the hook entity. Please enter proper entity in Show_Hide_Forms.csv file");
			}
			else
			{
				List<EntityMap> entityMapList = defaultBizLogic.retrieve(EntityMap.class.getName(),
						Constants.CONTAINERID, containerId);
				if (entityMapList != null && !entityMapList.isEmpty())
				{
					EntityMap entityMap = entityMapList.get(0);
					AppUtility.editConditions(entityMap, conditionObject, typeId, true);
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
		if (entityIdsVsContainersId != null && !entityIdsVsContainersId.isEmpty())
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
