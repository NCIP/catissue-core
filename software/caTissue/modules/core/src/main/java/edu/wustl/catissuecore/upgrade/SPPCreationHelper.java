
package edu.wustl.catissuecore.upgrade;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.FileQueryBean;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry;
import edu.wustl.catissuecore.domain.processingprocedure.Action;
import edu.wustl.catissuecore.domain.processingprocedure.DefaultAction;
import edu.wustl.catissuecore.domain.processingprocedure.SpecimenProcessingProcedure;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.exception.DAOException;

public class SPPCreationHelper
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(SPPCreationHelper.class);

	/** The hibernate dao. */
	private static LocalHibernateDAO hibernateDAO = null;

	/** The unique spp map. */
	private static Map<String, SpecimenProcessingProcedure> uniqueSPPMap = new HashMap<String, SpecimenProcessingProcedure>();

	/**
	 * Instantiates a new sPP creation helper.
	 * @param specimenEvents the specimen events
	 * @param dao the dao
	 */
	public SPPCreationHelper(LocalHibernateDAO dao)
	{
		hibernateDAO = dao;
	}

	/**
	 * Gets the matching spp.
	 * @param specimenEventsCollection the specimen events collection
	 * @return the matching spp
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	public SpecimenProcessingProcedure getMatchingSPP(Collection<SpecimenEventParameters> specimenEventsCollection)
			throws ApplicationException, DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		String uniqueKeyForSPP = getUniqueKeyForSR(specimenEventsCollection);

		if (uniqueSPPMap.get(uniqueKeyForSPP) == null)
		{
			/* a. Create unisque SPP which will be used for all SR's depending on the
			 *    values of Collection Event and Received Event */
			SpecimenProcessingProcedure uniqueSPPs = createUniqueSPPs(specimenEventsCollection, uniqueKeyForSPP);

			uniqueSPPMap.put(uniqueKeyForSPP, uniqueSPPs);
		}
		return uniqueSPPMap.get(uniqueKeyForSPP);
	}

	/**
	 * Gets the unique key for sr.
	 * @param eventsCollection the events collection
	 * @return the unique key for sr
	 */
	private String getUniqueKeyForSR(Collection<SpecimenEventParameters> eventsCollection)
	{
		StringBuilder uniqueKey = new StringBuilder();

		String receivedQuality = null;
		String collecectionEventKey = null;
		for (SpecimenEventParameters specimenEvent : eventsCollection)
		{
			if (specimenEvent instanceof CollectionEventParameters)
			{
				CollectionEventParameters event = (CollectionEventParameters) specimenEvent;
				collecectionEventKey = event.getCollectionProcedure() + '#' + event.getContainer()
						+ '#';
			}
			else
			{
				receivedQuality = ((ReceivedEventParameters) specimenEvent).getReceivedQuality();
			}
		}
		uniqueKey.append(collecectionEventKey).append(receivedQuality);
		return uniqueKey.toString();
	}

	/**
	 * Creates the unique sp ps.
	 * @param eventsCollection the events collection
	 * @param uniqueSPPName
	 * @return the SPP
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private SpecimenProcessingProcedure createUniqueSPPs(Collection<SpecimenEventParameters> eventsCollection,
			String uniqueSPPName) throws ApplicationException,
			DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		SpecimenProcessingProcedure defaultSpp = new SpecimenProcessingProcedure();
		defaultSpp.setName(uniqueSPPName.replaceAll("#", "-"));
		Collection<Action> actionCollection = new HashSet<Action>();
		int uniqueOrderId = 0;
		for (SpecimenEventParameters specimenEvent : eventsCollection)
		{

			ActionApplicationRecordEntry recordEntry = createAndSaveHookingObject(specimenEvent,
					Boolean.FALSE);

			Long deRecordIdentifier = createAndSaveDeEvent(specimenEvent);

			// Fetch the DE Embedded Event Object.
			EntityInterface deEventParameters = getDeEventForStaticEvent(specimenEvent);

			Long containerIdentifier = ((ContainerInterface) deEventParameters
					.getContainerCollection().iterator().next()).getId();

			EntityInterface sppStaticEntity = UpgradeExistingEventsToSppEvents.CATISSUE_ENTITY_GROUP
					.getEntityByName(UpgradeExistingEventsToSppEvents.ACTION_APPLICATION_RECORD_ENTRY);

			// This is to hook DE data with static data (hook Event data with ActionApplicationRecordEntry)
			IntegrateDE integrate = new IntegrateDE();
			integrate.associateRecords(containerIdentifier, recordEntry.getId(),
					deRecordIdentifier, sppStaticEntity.getId(), null);

			Action action = new Action();
			action.setApplicationDefaultValue(recordEntry);
			action.setContainerId(containerIdentifier);
			action.setActionOrder(Long.valueOf(uniqueOrderId));
			action.setUniqueId(String.valueOf(uniqueOrderId));

			actionCollection.add(action);
			uniqueOrderId++;
		}
		defaultSpp.setActionCollection(actionCollection);

		hibernateDAO.insert(defaultSpp);
		return defaultSpp;
	}

	/**
	 * Convert to de event.
	 * @param eventParameters the event parameters
	 * @param actionApplication the action application
	 * @throws ApplicationException the application exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public ActionApplicationRecordEntry createAndSaveHookingObject(
			SpecimenEventParameters eventParameters, Boolean dataEntryFlow)
			throws ApplicationException
	{
		// Create Record Entry Object.
		ActionApplicationRecordEntry applicationRecordEntry = new ActionApplicationRecordEntry();
		applicationRecordEntry.setActivityStatus("Active");
		applicationRecordEntry.setAdminuser(eventParameters.getAdminuser());
		applicationRecordEntry.setModifiedBy(eventParameters.getUser().getFirstName());
		applicationRecordEntry.setModifiedDate(new Date());

		if (dataEntryFlow)
		{
			// Fetch the DE Embedded Event Object.
			EntityInterface deEventParameters = getDeEventForStaticEvent(eventParameters);

			Long containerIdentifier = ((ContainerInterface) deEventParameters
					.getContainerCollection().iterator().next()).getId();

			DefaultAction action = getDefaultAction(containerIdentifier);
			applicationRecordEntry.setFormContext(action);
		}

		/* This is required because ActionApplication->ActionApplicationRecordEntry cascade is none.
		 * Also becuase ActionApplicationRecordEntry id is required for hooking DE data and static data
		 */
		hibernateDAO.insert(applicationRecordEntry);

		return applicationRecordEntry;
	}

	/**
	 * Creates the and save de event.
	 * @param eventParameters the event parameters
	 * @return the long
	 * @throws ApplicationException the application exception
	 */
	public Long createAndSaveDeEvent(SpecimenEventParameters eventParameters)
			throws ApplicationException
	{
		// Fetch the DE Embedded Event Object.
		EntityInterface deEventParameters = getDeEventForStaticEvent(eventParameters);

		try
		{
			// This is the data value map required for doing DE data entry.
			Map<AbstractAttributeInterface, Object> dataValueMap = new HashMap<AbstractAttributeInterface, Object>();

			if (eventParameters instanceof TransferEventParameters)
			{
				// Iterate over all the attributes of the DE event object.
				for (AttributeInterface attribute : deEventParameters.getAttributeCollection())
				{
					// Ignore the Id attribute as data won't be inserted for it.
					if (!attribute.getName().equalsIgnoreCase("id")
							&& (!attribute.getName().equalsIgnoreCase("fromStorageContainer") && !attribute
									.getName().equalsIgnoreCase("toStorageContainer")))
					{
						// Populate datavalue map with attribute and its value from static event.
						dataValueMap.put(attribute, getAttributeValue(eventParameters, attribute));
					}
					else if (attribute.getName().equalsIgnoreCase("fromStorageContainer"))
					{
						dataValueMap.put(attribute, getFromStorageContainerValue(eventParameters));
					}
					else if (attribute.getName().equalsIgnoreCase("toStorageContainer"))
					{
						dataValueMap.put(attribute, getToStorageContainerValue(eventParameters));
					}
				}
			}
			else
			{
				if (deEventParameters.getAttributeCollection() != null)
				{
					// Iterate over all the attributes of the DE event object.
					for (AttributeInterface attribute : deEventParameters.getAttributeCollection())
					{
						// Ignore the Id attribute as data won't be inserted for it.
						if (!attribute.getName().equalsIgnoreCase("id"))
						{
							// Populate datavalue map with attribute and its value from static event.
							dataValueMap.put(attribute, getAttributeValue(eventParameters,
									attribute));
						}
					}
				}
			}
			return EntityManager.getInstance().insertData(deEventParameters, dataValueMap, null,
					new ArrayList<FileQueryBean>());
		}
		catch (InvocationTargetException e)
		{
			LOGGER.error(e.getTargetException().getLocalizedMessage(), e);
			throw new ApplicationException(null, e, e.getTargetException().getMessage());
		}
		catch (Exception e)
		{
			LOGGER.error(e.getLocalizedMessage(), e);
			throw new ApplicationException(null, e, e.getMessage());
		}
	}

	/**
	 * @param eventParameters
	 * @param attribute
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private Object getAttributeValue(SpecimenEventParameters eventParameters,
			AttributeInterface attribute) throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException
	{
		// Using reflection fetch the data for each attribute of static Embedded Event.
		Class hostClass = Class.forName(eventParameters.getClass().getName());
		String methodName = "get" + StringUtils.capitalize(attribute.getName());
		java.lang.reflect.Method callableMethod = hostClass.getMethod(methodName);

		// Object passed is of static Event.
		Object methodValue = callableMethod.invoke(eventParameters);
		return methodValue;
	}

	/**
	 * Gets the de event for static event.
	 * @param staticEvent the static event
	 * @return the de event for static event
	 */
	private EntityInterface getDeEventForStaticEvent(SpecimenEventParameters staticEvent)
	{
		// Static Class name. Required for reflection.
		String hostClassName = staticEvent.getClass().getName();

		String deClassName = hostClassName.substring(hostClassName.lastIndexOf('.') + 1,
				hostClassName.length());

		// Fetch the DE Embedded Event Object.
		return UpgradeExistingEventsToSppEvents.MIGRATED_EVENTS.getEntityByName(deClassName);
	}

	/**
	 * Gets the default action.
	 * @param containerIdentifier the container identifier
	 * @return the default action
	 * @throws DAOException the DAO exception
	 */
	@SuppressWarnings("unchecked")
	private DefaultAction getDefaultAction(Long containerIdentifier) throws DAOException
	{
		DefaultAction action = null;
		List<DefaultAction> defaultActionList = hibernateDAO
				.retrieve(DefaultAction.class.getName());
		Boolean defaultActionPresent = Boolean.FALSE;
		if (defaultActionList == null || defaultActionList.isEmpty())
		{
			defaultActionPresent = Boolean.FALSE;
		}
		else
		{
			for (DefaultAction defaultAction : defaultActionList)
			{
				if (defaultAction.getContainerId().equals(containerIdentifier))
				{
					action = defaultAction;
					defaultActionPresent = Boolean.TRUE;
					break;
				}
			}
		}

		if (!defaultActionPresent)
		{
			action = new DefaultAction();
			action.setContainerId(containerIdentifier);
			hibernateDAO.insert(action);
		}
		return action;
	}

	/**
	 * Gets the from storage container value.
	 * @param transferEvent the transfer event
	 * @return the from storage container value
	 */
	private String getFromStorageContainerValue(SpecimenEventParameters transferEvent)
	{
		Specimen eventSpecimen = ((Specimen) transferEvent.getSpecimen());
		StringBuilder fromStorageContainer;
		if (eventSpecimen.getSpecimenPosition() == null)
		{
			fromStorageContainer = new StringBuilder("Virtual");
		}
		else
		{
			fromStorageContainer = new StringBuilder(eventSpecimen.getSpecimenPosition()
					.getStorageContainer().getName());
			Integer posDimenOne = ((Specimen) transferEvent.getSpecimen()).getSpecimenPosition()
					.getPositionDimensionOne();
			Integer posDimenTwo = ((Specimen) transferEvent.getSpecimen()).getSpecimenPosition()
					.getPositionDimensionTwo();
			fromStorageContainer.append(':').append("pos(").append(posDimenOne).append(',').append(
					posDimenTwo).append(')');
		}
		return fromStorageContainer.toString();
	}

	/**
	 * Gets the to storage container value.
	 * @param transferEvent the transfer event
	 * @return the to storage container value
	 */
	private String getToStorageContainerValue(SpecimenEventParameters transferEvent)
	{
		TransferEventParameters transferEventParameters = (TransferEventParameters) transferEvent;
		StringBuilder fromStorageContainer = new StringBuilder(transferEventParameters
				.getToStorageContainer().getName());
		fromStorageContainer.append(':').append("pos(").append(
				transferEventParameters.getToPositionDimensionOne()).append(',').append(
				transferEventParameters.getToPositionDimensionTwo()).append(')');
		return fromStorageContainer.toString();
	}
}
