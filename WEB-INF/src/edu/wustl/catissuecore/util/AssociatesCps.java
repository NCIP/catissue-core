
package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;

/**
 * This class associates Collection Protocol with the Corresponding entities
 * mentioned in XML file.
 * @author suhas_khot
 *
 */
public final class AssociatesCps
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(AssociatesCps.class);

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

	/**
	 * @return returns single object
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

		entityIdsVsContId = AppUtility.getAllContainers();

		Map<Long, List<Long>> mergedCpIdMap = mergeEntityAndFormIdForCP(cpIdsVsEntityIds,
				cpIdsVsFormIds);

		for (Long cpId : mergedCpIdMap.keySet())
		{
			associateEntitiesToCps(cpId, mergedCpIdMap.get(cpId), cpIdsVsoverride);
		}

		logger.info("DONE----------------------");
	}

	/**
	 *
	 * @param cpIdsVsEntityIds
	 * @param cpIdsVsFormIds
	 * @return
	 */
	private static Map<Long, List<Long>> mergeEntityAndFormIdForCP(
			Map<Long, List<Long>> cpIdsVsEntityIds, Map<Long, List<Long>> cpIdsVsFormIds)
	{
		Iterator<Long> iterator = cpIdsVsFormIds.keySet().iterator();
		while (iterator.hasNext())
		{
			Long cpId = iterator.next();
			if (cpIdsVsEntityIds.containsKey(cpId))
			{
				List<Long> formIds = cpIdsVsFormIds.get(cpId);
				List<Long> entityIds = cpIdsVsEntityIds.get(cpId);
				entityIds.addAll(formIds);
			}
			else
			{
				cpIdsVsEntityIds.put(cpId, cpIdsVsFormIds.get(cpId));
			}
		}

		return cpIdsVsEntityIds;

	}

	/**
	 * this method dissAssociate Entities per cpId.
	 * @param cpIdsVsoverride
	 * @param collectionProtocol
	 * @param sfcSet
	 * @param hibernateDao
	 * @throws DAOException
	 */
	private static void dissAssociateEntitiesFormsPerCpId(Map<Long, String> cpIdsVsoverride,
			Long collectionProtocolId, HibernateDAO hibernateDao) throws DAOException
	{
		if (collectionProtocolId == null)
		{
			// ALL CPs
			if (cpIdsVsoverride.keySet().contains(Long.valueOf(0))
					&& Constants.OVERRIDE_TRUE.equalsIgnoreCase(cpIdsVsoverride
							.get(Long.valueOf(0))))
			{
				logger.info("Dissassociate Entities for ALL CPs ----");
				for (Long containerId : entityIdsVsContId.values())
				{
					if (containerId != null)
					{
						List<StudyFormContext> formContextList = hibernateDao.retrieve(
								StudyFormContext.class.getName(), Constants.CONTAINERID,
								containerId);

						if (formContextList == null || formContextList.isEmpty())
						{
							logger.info("Creating new form context for container : -----"
									+ containerId);
							StudyFormContext studyFormContext = new StudyFormContext();
							studyFormContext.setContainerId(containerId);
							studyFormContext.setHideForm(true);
							hibernateDao.insert(studyFormContext);
						}
						else
						{
							logger.info("Got form context for container : -----" + containerId);
							StudyFormContext studyFormContext = formContextList.get(0);
							Collection cpColl = studyFormContext.getCollectionProtocolCollection();
							cpColl.clear();
							studyFormContext.setCollectionProtocolCollection(cpColl);
							studyFormContext.setHideForm(true);
							hibernateDao.update(studyFormContext);
						}

					}
				}
			}
		}
		else
		{
			List<CollectionProtocol> cpList = hibernateDao.retrieve(CollectionProtocol.class
					.getName(), Constants.ID, collectionProtocolId);
			CollectionProtocol collectionProtocol = cpList.get(0);
			Collection<StudyFormContext> sfcSet = collectionProtocol.getStudyFormContextCollection();

			if (cpIdsVsoverride.keySet().contains(collectionProtocolId)
					&& Constants.OVERRIDE_TRUE.equalsIgnoreCase(cpIdsVsoverride
							.get(collectionProtocolId)))
			{
				logger.info("Dissassociate Entities for CP id : " + collectionProtocolId);
				if (!sfcSet.isEmpty())
				{
					// remove the CP from all StudyFormContext objects
					for (StudyFormContext sfc : sfcSet)
					{
						Collection<CollectionProtocol> coll = sfc.getCollectionProtocolCollection();
						coll.remove(collectionProtocol);
						sfc.setCollectionProtocolCollection(coll);
						if (coll.isEmpty())
						{
							sfc.setHideForm(true);
						}
						hibernateDao.update(sfc);
					}
					sfcSet.clear();
				}
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
	 * @param cpId stores Id of Collection Protocol
	 * @param entityIds entityIds collection
	 * @param cpIdsVsoverride
	 * @throws DAOException
	 */
	private static void associateEntitiesToCps(Long cpId, List<Long> entityIds,
			Map<Long, String> cpIdsVsoverride) throws DAOException
	{
		String appName = CommonServiceLocator.getInstance().getAppName();
		HibernateDAO hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance().getDAOFactory(
				appName).getDAO();
		if (cpId == 0)
		{
			hibernateDao.openSession(null);
			dissAssociateEntitiesFormsPerCpId(cpIdsVsoverride, null, hibernateDao);
			hibernateDao.commit();
			hibernateDao.closeSession();
			hibernateDao.openSession(null);

			for (Long entityId : entityIds)
			{
				Long containerId = getContainerId(entityId);
				if (containerId != null)
				{
					List<StudyFormContext> formContextList = hibernateDao.retrieve(
							StudyFormContext.class.getName(), Constants.CONTAINERID, containerId);

					if (formContextList == null || formContextList.isEmpty())
					{
						logger
								.info("Creating new form context for container : -----"
										+ containerId);
						StudyFormContext studyFormContext = new StudyFormContext();
						updateStudyFormContext(null, studyFormContext, containerId);
						hibernateDao.insert(studyFormContext);
					}
					else
					{
						logger.info("Got form context for container : -----" + containerId);
						StudyFormContext studyFormContext = formContextList.get(0);
						updateStudyFormContext(null, studyFormContext, containerId);
						hibernateDao.update(studyFormContext);
					}

				}
			}
			hibernateDao.commit();
			hibernateDao.closeSession();
		}
		else
		{
			hibernateDao.openSession(null);
			dissAssociateEntitiesFormsPerCpId(cpIdsVsoverride, cpId, hibernateDao);
			hibernateDao.commit();
			hibernateDao.closeSession();
			hibernateDao.openSession(null);

			List<CollectionProtocol> cpList = hibernateDao.retrieve(CollectionProtocol.class
					.getName(), Constants.ID, cpId);
			CollectionProtocol collectionProtocol = cpList.get(0);
			Collection<StudyFormContext> sfcSet = collectionProtocol.getStudyFormContextCollection();

			for (Long entityId : entityIds)
			{
				Long containerId = getContainerId(entityId);
				if (containerId != null)
				{
					List<StudyFormContext> formContextList = hibernateDao.retrieve(
							StudyFormContext.class.getName(), Constants.CONTAINERID, containerId);

					logger.info("Associating Container : " + containerId + " with CP : " + cpId);

					if (formContextList == null || formContextList.isEmpty())
					{
						logger
								.info("Creating new form context for container : -----"
										+ containerId);
						StudyFormContext studyFormContext = new StudyFormContext();
						updateStudyFormContext(collectionProtocol, studyFormContext, containerId);
						sfcSet.add(studyFormContext);
						hibernateDao.insert(studyFormContext);
					}
					else
					{
						logger.info("Got form context for container : -----" + containerId);
						StudyFormContext studyFormContext = formContextList.get(0);
						updateStudyFormContext(collectionProtocol, studyFormContext, containerId);
						sfcSet.add(studyFormContext);
						hibernateDao.update(studyFormContext);
					}
				}
			}
			hibernateDao.commit();
			hibernateDao.closeSession();
		}
	}

	/**
	 *
	 * @param collectionProtocol
	 * @param studyFormContext
	 * @param containerId
	 */
	private static void updateStudyFormContext(CollectionProtocol collectionProtocol,
			StudyFormContext studyFormContext, Long containerId)
	{
		if (collectionProtocol != null)
		{
			Collection cpColl = studyFormContext.getCollectionProtocolCollection();
			cpColl.add(collectionProtocol);
			studyFormContext.setCollectionProtocolCollection(cpColl);
		}

		studyFormContext.setHideForm(false);
		studyFormContext.setContainerId(containerId);
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

}
