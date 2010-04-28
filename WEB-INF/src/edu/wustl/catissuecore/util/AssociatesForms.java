
package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;

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
	private static AssociatesForms associatForms = new AssociatesForms();

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

	private static List<Long> entityAndFormContainerIds = new ArrayList<Long>();

	/**
	 * @param args stores command line user inputs
	 * @throws DynamicExtensionsSystemException fails to validate
	 * @throws IOException if it fails to read file IO operation
	 * @throws ApplicationException
	 */
	public static void main(String[] args) throws DynamicExtensionsSystemException, IOException,
			ApplicationException
	{
		//Validates arguments
		//String filePath = "E:/Program Files/Eclipse-Galileo/eclipse/workspace/pcatissue_cp_integration_branch_de_restructure/Show_Hide_Forms.csv";
		validateCSV(args);
		String filePath = args[0];
		CSVFileParser csvFileParser = new CSVFileParser(filePath);
		//get EntityGroup with Collection Protocol along with corresponding entities
		csvFileParser.processCSV();

		// Get all containerIDs for all entities in the entity groups
		entityIdsVsContainersId = AppUtility.getAllContainers(csvFileParser.getEntityGroupIds());

		// Get all container IDs which need to be associated with ALL CPs
		entityAndFormContainerIds = getContainerIdsForMapping(csvFileParser);

		associateAllEntitiesForms();
		logger.info("Associated the entities and forms with All CPs");
	}

	/**
	 *
	 * @param csvFileParser
	 * @return
	 */
	private static List<Long> getContainerIdsForMapping(CSVFileParser csvFileParser)
	{
		List<Long> containerIds = new ArrayList<Long>();
		List<Long> entityAndFormIds = new ArrayList<Long>();
		entityAndFormIds.addAll(csvFileParser.getEntityIds());
		entityAndFormIds.addAll(csvFileParser.getFormIds());

		for (Long entityId : entityAndFormIds)
		{
			containerIds.add(getContainerId(entityId));
		}

		return containerIds;
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
	 * @throws BizLogicException
	 * @throws DAOException
	 */
	private static void associateAllEntitiesForms() throws DAOException
	{
		String appName = CommonServiceLocator.getInstance().getAppName();
		HibernateDAO hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance().getDAOFactory(
				appName).getDAO();
		hibernateDao.openSession(null);

		for (Long containerId : entityIdsVsContainersId.values())
		{
			if (containerId != null)
			{
				List<StudyFormContext> studyFormList = hibernateDao.executeQuery("from "
						+ StudyFormContext.class.getName() + " sfc where sfc.containerId="
						+ containerId);

				StudyFormContext studyFormContext = null;
				if (studyFormList != null && !studyFormList.isEmpty())
				{
					studyFormContext = studyFormList.get(0);
					Collection<CollectionProtocol> coll = studyFormContext
							.getCollectionProtocolCollection();
					coll.clear();
					updateStudyFormContext(studyFormContext, containerId);
					hibernateDao.update(studyFormContext);
				}
				else
				{
					studyFormContext = new StudyFormContext();
					updateStudyFormContext(studyFormContext, containerId);
					hibernateDao.insert(studyFormContext);
				}
			}
		}

		hibernateDao.commit();
		hibernateDao.closeSession();
	}

	/**
	 *
	 * @param studyFormContext
	 * @param containerId
	 */
	private static void updateStudyFormContext(StudyFormContext studyFormContext, Long containerId)
	{
		studyFormContext.setContainerId(containerId);
		if (entityAndFormContainerIds.contains(containerId))
		{
			studyFormContext.setHideForm(false);
		}
		else
		{
			studyFormContext.setHideForm(true);
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
