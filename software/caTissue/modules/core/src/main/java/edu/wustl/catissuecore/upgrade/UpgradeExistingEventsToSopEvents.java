
package edu.wustl.catissuecore.upgrade;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.cab2b.server.cache.EntityCache;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.sop.ActionApplication;
import edu.wustl.catissuecore.domain.sop.SOP;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;

public class UpgradeExistingEventsToSopEvents
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger
			.getCommonLogger(UpgradeExistingEventsToSopEvents.class);

	/** The hibernate dao. */
	private static LocalHibernateDAO hibernateDAO;

	/** The Constant ENTITY_CACHE. */
	private static final EntityCache ENTITY_CACHE = EntityCache.getInstance();

	/** The Constant CATISSUE_ENTITY_GROUP. */
	public static final EntityGroupInterface CATISSUE_ENTITY_GROUP = ENTITY_CACHE
			.getEntityGroupById(1L);

	/** The Constant MIGRATED_EVENTS. */
	public static final EntityGroupInterface MIGRATED_EVENTS = ENTITY_CACHE
			.getEntityGroupByName("SpecimenEvents");

	/** The Constant ACTION_APPLICATION_RECORD_ENTRY. */
	public static final String ACTION_APPLICATION_RECORD_ENTRY = "edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry";

	/**
	 * The main method.
	 * @param args the arguments
	 * @throws ApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ApplicationException,
			DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		try
		{
			LOGGER.info("Starting the migrationg task - " + Calendar.getInstance().getTime());

			// Initialize Hibernate DAO.
			hibernateDAO = (LocalHibernateDAO) openDAOSessionForMigration(null, "Migration");

			// 1. Migrate Specimen level Events
			// Fetch all the Id's for specimen objects.
			List<Long> allIdentifier = hibernateDAO.retrieve(Specimen.class.getName(),
					new String[]{"id"});
			LOGGER.info("Total number of Specimens are :" + allIdentifier.size());

			// Migrating Specimen level Evetns
			migrateSpecimenEvents(allIdentifier);

			// 2. Migrate SCG level Events
			// Fetch all the Id's for SCG objects.
			allIdentifier = hibernateDAO.retrieve(SpecimenCollectionGroup.class.getName(),
					new String[]{"id"});
			LOGGER.info("Total number of SCG's are :" + allIdentifier.size());

			// Migrating Specimen level Evetns
			migrateSCGEvents(allIdentifier);

			// Create JDBC DAO instance.
			JDBCDAO jdbcDAO = AppUtility.openJDBCSession();

			// 3. Migrate SR level events
			// Fetch all the Id's for SR objects.
			String selectSQL = "select distinct(sr.identifier) from catissue_cp_req_specimen sr, catissue_specimen_event_param sep "
					+ "where sep.specimen_id = sr.identifier ";
			List allSRIdentifier = jdbcDAO.executeQuery(selectSQL, null);
			LOGGER.info("Total number of SR's are :" + allSRIdentifier.size());

			// Migrating Specimen Requirement level Events
			migrateSREvents(allSRIdentifier);
			hibernateDAO.commit();

			System.out.println("Successfully updated the events");
			System.out.println("Migration task completed at : " + Calendar.getInstance().getTime());
		}
		finally
		{
			hibernateDAO.closeSession();
		}
	}

	/**
	 * Open dao session for migration.
	 * @param sessionDataBean the session data bean
	 * @param applicationName the application name
	 * @return the dAO
	 * @throws ApplicationException the application exception
	 */
	private static DAO openDAOSessionForMigration(final SessionDataBean sessionDataBean,
			String applicationName) throws ApplicationException
	{
		LocalHibernateDAO dao = null;
		try
		{
			dao = (LocalHibernateDAO) DAOConfigFactory.getInstance().getDAOFactory(applicationName)
					.getDAO();
			dao.openSession(sessionDataBean);
		}
		catch (final DAOException daoExp)
		{
			LOGGER.error(daoExp.getMessage(), daoExp);
			throw new ApplicationException(ErrorKey.getErrorKey(daoExp.getErrorKeyName()), daoExp,
					daoExp.getMsgValues());
		}
		return dao;
	}

	/**
	 * Migrate specimen events.
	 * @param allIdentifiers the all identifiers
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	private static void migrateSpecimenEvents(List<Long> allIdentifiers)
			throws ApplicationException, DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		int objectCounter = 1;
		int eventsCounter = 1;
		for (Long identifier : allIdentifiers)
		{
			String eventClass = SpecimenEventParameters.class.getName();
			final QueryWhereClause queryWhereClause = new QueryWhereClause(eventClass);
			queryWhereClause.addCondition(new EqualClause("specimen.id", identifier));

			// Get all associated events for each specimen.
			Collection<SpecimenEventParameters> allEvents = hibernateDAO.retrieve(eventClass, null,
					queryWhereClause, null);

			Set<ActionApplication> allMigratedEvents = convertStaticEvents(allEvents, eventsCounter);

			// Fetch one specimen at a time.
			Specimen specimen = (Specimen) hibernateDAO.retrieveById(Specimen.class.getName(),
					identifier);

			// Removing old Static Events association
			specimen.getSpecimenEventCollection().clear();

			// Adding new DE Events association
			specimen.setActionApplicationCollection(allMigratedEvents);

			// Updating specimen object.
			hibernateDAO.update(specimen);
		}

		// Increamenting specimen COunter.
		objectCounter++;

		if (objectCounter >= 1000)
		{
			hibernateDAO.commit();
			LOGGER.info("1000 Specimens updated - " + Calendar.getInstance().getTime());
			LOGGER.info("For " + objectCounter + " specimens, the number of events are "
					+ eventsCounter);
			objectCounter = 1;
			eventsCounter = 1;
		}
	}

	/**
	 * Migrate scg events.
	 * @param allIdentifiers the all identifiers
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	private static void migrateSCGEvents(List<Long> allIdentifiers) throws ApplicationException,
			DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		int objectCounter = 1;
		int eventsCounter = 1;
		for (Long identifier : allIdentifiers)
		{
			// Fetch one SCG at a time.
			SpecimenCollectionGroup scg = (SpecimenCollectionGroup) hibernateDAO.retrieveById(
					SpecimenCollectionGroup.class.getName(), identifier);

			// Get all associated events for each SCG.
			Collection<SpecimenEventParameters> allEvents = scg
					.getSpecimenEventParametersCollection();

			Set<ActionApplication> allMigratedEvents = convertStaticEvents(allEvents, eventsCounter);
			// Removing old Static Events association
			scg.getSpecimenEventParametersCollection().clear();

			// Adding new DE Events association
			scg.setActionApplicationCollection(allMigratedEvents);

			// Updating SCG object.
			hibernateDAO.update(scg);
		}
		// Increamenting SCG COunter.
		objectCounter++;

		if (objectCounter >= 1000)
		{
			hibernateDAO.commit();
			LOGGER.info("1000 SCG updated - " + Calendar.getInstance().getTime());
			LOGGER.info("For " + objectCounter + " SCG's, the number of events are "
					+ eventsCounter);
			objectCounter = 1;
			eventsCounter = 1;
		}
	}

	/**
	 * Migrate sr events.
	 * @param allIdentifier the all identifier
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	@SuppressWarnings("unchecked")
	private static void migrateSREvents(List allIdentifier) throws ApplicationException,
			DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		int objectCounter = 1;
		for (Object srIdentifier : allIdentifier)
		{
			String identifier = ((List<String>) srIdentifier).get(0);

			// b. Fetch one SR at a time.
			SpecimenRequirement specimenRequirement = (SpecimenRequirement) hibernateDAO
					.retrieveById(SpecimenRequirement.class.getName(), Long.valueOf(identifier));

			SPPCreationHelper sppHelper = new SPPCreationHelper(hibernateDAO);

			// c. For each SR find its appropriate SPP and attach that SPP as its processingSPP.
			SOP aptSPP = sppHelper.getMatchingSPP(specimenRequirement.getSpecimenEventCollection());

			specimenRequirement.setProcessingSOP(aptSPP);

			hibernateDAO.update(aptSPP);

			if (objectCounter >= 1000)
			{
				hibernateDAO.commit();
				LOGGER.info("1000 SR updated - " + Calendar.getInstance().getTime());
				objectCounter = 1;
			}
		}
	}

	/**
	 * Convert static events.
	 * @param allEvents the all events
	 * @param eventsCounter the events counter
	 * @return the set <action application>
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	private static Set<ActionApplication> convertStaticEvents(
			Collection<SpecimenEventParameters> allEvents, int eventsCounter)
			throws ApplicationException, DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		//Collection for new DE events.
		Set<ActionApplication> allMigratedEvents = new HashSet<ActionApplication>();

		// For every static event create a DE event object.
		for (SpecimenEventParameters specimenEventParameter : allEvents)
		{
			ActionApplication actionApplication = new ActionApplication();
			actionApplication.setComments(specimenEventParameter.getComment());
			actionApplication.setTimestamp(specimenEventParameter.getTimestamp());
			actionApplication.setPerformedBy(specimenEventParameter.getUser());

			// Create and save DE Event corresponding to static event. Also add the data from static event to DE event
			Long deRecordIdentifier = createDEHierarchy(specimenEventParameter, actionApplication);

			// Insert Record entry object. This is required for hooking.
			Long containerId = actionApplication.getApplicationRecordEntry().getFormContext()
					.getContainerId();

			//hibernateDAO.insert(actionApplication);
			Long staticRecordIdentifier = actionApplication.getApplicationRecordEntry().getId();

			EntityInterface sopStaticEntity = CATISSUE_ENTITY_GROUP
					.getEntityByName(ACTION_APPLICATION_RECORD_ENTRY);

			// This is to make sure that DE gets the static object from database.
			//hibernateDAO.commit();

			// This is to hook DE data with static data (hook Event data with ActionApplicationRecordEntry)
			IntegrateDE integrate = new IntegrateDE();
			integrate.associateRecords(containerId, staticRecordIdentifier, deRecordIdentifier,
					sopStaticEntity.getId(), null);

			allMigratedEvents.add(actionApplication);

			// Increamenting events counter
			eventsCounter++;
		}
		return allMigratedEvents;
	}

	/**
	 * Creates the de hierarchy.
	 *
	 * @param specimenEventParameter the specimen event parameter
	 * @param actionApplication the action application
	 *
	 * @return the long
	 *
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws ApplicationException the application exception
	 */
	private static Long createDEHierarchy(SpecimenEventParameters specimenEventParameter,
			ActionApplication actionApplication) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, ApplicationException
	{
		// Instanciate the class.
		SPPCreationHelper eventConvertor = new SPPCreationHelper(hibernateDAO);

		// It creates a DE events of corresponding static event. It also saves the data for the event.
		ActionApplicationRecordEntry applicationRecordEntry = eventConvertor
				.createAndSaveHookingObject(specimenEventParameter, Boolean.TRUE);

		actionApplication.setApplicationRecordEntry(applicationRecordEntry);

		return eventConvertor.createAndSaveDeEvent(specimenEventParameter);
	}

}