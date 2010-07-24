/**
 * <p>Title: EventsUtil Class>
 * <p>Description: AppUtility methods related to events
 * of one or more specimen from a participant.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * 20th April, 2007
 */

package edu.wustl.catissuecore.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimenReviewParameters;
import edu.wustl.catissuecore.domain.ProcedureEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpunEventParameters;
import edu.wustl.catissuecore.domain.ThawEventParameters;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author ashish_gupta
 *
 */
public final class EventsUtil
{
	private static Logger logger = Logger.getCommonLogger(EventsUtil.class);
	/*
	 * create singleton object
	 */
	private static EventsUtil eventUtil = new EventsUtil();
	/*
	 * private constructor
	 */
	private EventsUtil()
	{
		
	}
	/*
	 * return the single object
	 */
	public static EventsUtil getInstance()
	{
		return eventUtil;
	}
	
	public static void validateCollectionEvent(ActionErrors errors, Validator validator, long collectionEventUserId,
			String collectionEventdateOfEvent, String collectionEventCollectionProcedure, String collectionTime)
	{
		if ((collectionEventUserId) == -1L)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Collection Event's user"));
		}
		if (!validator.checkDate(collectionEventdateOfEvent))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid", "Collection Event's date"));
		}

		// checks the collectionProcedure
		if (!validator.isValidOption( collectionEventCollectionProcedure ) )
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectioneventparameters.collectionprocedure")));
		}
		/* Bug id: 4179
		 patch id: 4179_1*/
		if (!validator.isValidTime(collectionTime, CommonServiceLocator.getInstance().getTimePattern()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected", ApplicationProperties
					.getValue("collectionEvent.invalidTime")));
		}

	}

	public static void validateReceivedEvent(ActionErrors errors, Validator validator, long receivedEventUserId, String receivedEventDateOfEvent,
			String receivedEventReceivedQuality, String receivedTime)
	{
		if ((receivedEventUserId) == -1L)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Received Event's user"));
		}
		if (!validator.checkDate(receivedEventDateOfEvent))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid", "Received Event's date"));
		}

		// checks the collectionProcedure
		if (!validator.isValidOption( receivedEventReceivedQuality ) )
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("receivedeventparameters.receivedquality")));
		}
		/* Bug id: 4179
		 patch id: 4179_2*/
		if (!validator.isValidTime(receivedTime,  CommonServiceLocator.getInstance().getTimePattern()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected", ApplicationProperties
							.getValue("receivedTime.invalidTime")));
		}
	}

	/**
	 * 
	 * @param userList Collection
	 * @param userName userName
	 * @return long
	 */
	public static long getIdFromCollection(Collection userList, String userName)
	{
		Iterator<NameValueBean> itr = userList.iterator();
		long returnId = Long.valueOf(-1);
		while(itr.hasNext())
		{
			NameValueBean nameValueBean = (NameValueBean) itr.next();
			if (nameValueBean.getName() != null && nameValueBean.getName().trim().equals(userName))
			{
				String identifier = nameValueBean.getValue();
				returnId = Long.valueOf(identifier);
				break;
			}
		}
		return returnId;
	}

	/**
	 * @param eventObject
	 * @param validator
	 * @throws DAOException
	 * validating events from bizlogic
	 */
	public static void validateEventsObject(Object eventObject, Validator validator) throws ApplicationException
	{
		String datePattern = CommonServiceLocator.getInstance().getDatePattern();
		if (eventObject instanceof CollectionEventParameters)
		{
			CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObject;
			AbstractSpecimen specimen = collectionEventParameters.getSpecimen();
			collectionEventParameters.getUser();
			/* Bug id: 4179
			 patch id: 4179_3*/
			//Collector validation
			if (collectionEventParameters.getUser() != null &&
					((collectionEventParameters.getUser().getId() == null
						|| collectionEventParameters.getUser().getId() == 0)
							&& (collectionEventParameters.getUser().getLoginName() == null
								|| collectionEventParameters.getUser().getLoginName().length() == 0)))
			{
				String message = ApplicationProperties.getValue("specimen.collection.event.user");
				throw AppUtility.getApplicationException(null,"errors.item.required",  message);
			}
			if (specimen != null)
			{
				//Date validation
				if (!validator.checkDate(Utility.parseDateToString(collectionEventParameters.getTimestamp(), datePattern)))
				{
					String message = ApplicationProperties.getValue("specimen.collection.event.date");
					throw AppUtility.getApplicationException(null,"errors.item.required",  message);
				}
			}
			// checks the collectionProcedure
			if (collectionEventParameters.getCollectionProcedure() == null || !collectionEventParameters.getCollectionProcedure().equals(""))
			{
				if (!validator.isValidOption(collectionEventParameters.getCollectionProcedure()))
				{
					String message = ApplicationProperties.getValue("collectioneventparameters.collectionprocedure");
					throw AppUtility.getApplicationException( null,"errors.item.required", message);
				}

				List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);

				if (!Validator.isEnumeratedValue(procedureList, collectionEventParameters.getCollectionProcedure()))
				{
					if(!collectionEventParameters.getCollectionProcedure().equals(Constants.CP_DEFAULT))
					{
						throw AppUtility.getApplicationException( null,"events.collectionProcedure.errMsg", "");
					}
				}
			}

			//Container validation
			if (collectionEventParameters.getContainer() == null || !collectionEventParameters.getContainer().equals(""))
			{
				if (!validator.isValidOption(collectionEventParameters.getContainer()))
				{
					String message = ApplicationProperties.getValue("collectioneventparameters.container");
					throw AppUtility.getApplicationException(null,"errors.item.required",  message);
				}
				List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
				if (!Validator.isEnumeratedValue(containerList, collectionEventParameters.getContainer()))
				{
					if(!collectionEventParameters.getContainer().equals(Constants.CP_DEFAULT))
					{
						throw AppUtility.getApplicationException(null,"events.container.errMsg",  "");
					}
				}
			}
		}
		//ReceivedEvent validation
		else if (eventObject instanceof ReceivedEventParameters)
		{
			ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObject;
			AbstractSpecimen specimen = receivedEventParameters.getSpecimen();
			/* Bug id: 4179
			 patch id: 4179_4*/
			if (receivedEventParameters.getUser() != null &&
					((receivedEventParameters.getUser().getId() == null
						|| receivedEventParameters.getUser().getId() == 0)
							&& (receivedEventParameters.getUser().getLoginName() == null
								|| receivedEventParameters.getUser().getLoginName().length() == 0)))
			{
				String message = ApplicationProperties.getValue("specimen.recieved.event.user");
				throw AppUtility.getApplicationException( null,"errors.item.required", message);
			}
			if (specimen != null)
			{
				if (!validator.checkDate(Utility.parseDateToString(receivedEventParameters.getTimestamp(), datePattern)))
				{
					String message = ApplicationProperties.getValue("specimen.recieved.event.date");
					throw AppUtility.getApplicationException(null,"errors.item.required",  message);
				}
			}
			if (receivedEventParameters.getReceivedQuality() == null || !receivedEventParameters.getReceivedQuality().equals(""))
			{
				if (!validator.isValidOption(receivedEventParameters.getReceivedQuality()))
				{
					String message = ApplicationProperties.getValue("collectioneventparameters.receivedquality");
					throw AppUtility.getApplicationException( null, "errors.item.required",message);
				}
				List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
				if (!Validator.isEnumeratedValue(qualityList, receivedEventParameters.getReceivedQuality()))
				{
					if(!receivedEventParameters.getReceivedQuality().equals(Constants.CP_DEFAULT))
					{
						throw AppUtility.getApplicationException(null,"events.receivedQuality.errMsg",  "");
					}
				}
			}
		}
	}

	/**
	 * @param dateOfEvent
	 * @param timeInHrs
	 * @param timeInMinutes
	 * @return
	 * Setting the timestamp
	 */
	public static Date setTimeStamp(String dateOfEvent, String timeInHrs, String timeInMinutes)
	{
		Date timestamp = null;
		if (Validator.isEmpty(dateOfEvent))
		{
			timestamp = Calendar.getInstance().getTime();
		}
		else
		{
			Calendar calendar = Calendar.getInstance();
			try
			{
				Date date = Utility.parseDate(dateOfEvent, Utility.datePattern(dateOfEvent));
				calendar.setTime(date);
				timestamp = calendar.getTime();
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeInHrs));
				calendar.set(Calendar.MINUTE, Integer.parseInt(timeInMinutes));
				timestamp = calendar.getTime();
			}
			catch (ParseException e)
			{
				EventsUtil.logger.error("Exception in Parsing Date" + e.getMessage(),e);
				e.printStackTrace();
			}
		}
		return timestamp;
	}

	/**
	 * @param specimen
	 * @param user
	 * @return
	 * Populating default collection event parameters
	 */
	public static CollectionEventParameters populateCollectionEventParameters(User user)
	{
		//		Collection Events
		CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
		collectionEventParameters.setUser(user);
		collectionEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
		collectionEventParameters.setCollectionProcedure(Constants.NOT_SPECIFIED);
		collectionEventParameters.setComment("");
		collectionEventParameters.setContainer(Constants.NOT_SPECIFIED);
		return collectionEventParameters;
	}

	/**
	 * @param specimen
	 * @param user
	 * @return
	 * Populating default received event parameters
	 */
	public static ReceivedEventParameters populateReceivedEventParameters(User user)
	{
		//		Received Events
		ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
		receivedEventParameters.setComment("");
		receivedEventParameters.setReceivedQuality(Constants.NOT_SPECIFIED);

		receivedEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
		receivedEventParameters.setUser(user);
		return receivedEventParameters;
	}

	public static String[] getEvent(SpecimenEventParameters eventParameters)
	{
		String[] events = new String[2];

		if (eventParameters instanceof CellSpecimenReviewParameters)
		{
			events[0] = "Cell Specimen Review";
			events[1] = "pageOfCellSpecimenReviewParameters";
		}
		else if (eventParameters instanceof CheckInCheckOutEventParameter)
		{
			events[0] = "Check In Check Out";
			events[1] = "pageOfCheckInCheckOutEventParameters";
		}
		else if (eventParameters instanceof CollectionEventParameters)
		{
			events[0] = "Collection";
			events[1] = "pageOfCollectionEventParameters";
		}
		else if (eventParameters instanceof DisposalEventParameters)
		{
			events[0] = "Disposal";
			events[1] = "pageOfDisposalEventParameters";
		}
		else if (eventParameters instanceof EmbeddedEventParameters)
		{
			events[0] = "Embedded";
			events[1] = "pageOfEmbeddedEventParameters";
		}
		else if (eventParameters instanceof FixedEventParameters)
		{
			events[0] = "Fixed";
			events[1] = "pageOfFixedEventParameters";
		}
		else if (eventParameters instanceof FluidSpecimenReviewEventParameters)
		{
			events[0] = "Fluid Specimen Review";
			events[1] = "pageOfFluidSpecimenReviewParameters";
		}
		else if (eventParameters instanceof FrozenEventParameters)
		{
			events[0] = "Frozen";
			events[1] = "pageOfFrozenEventParameters";
		}
		else if (eventParameters instanceof MolecularSpecimenReviewParameters)
		{
			events[0] = "Molecular Specimen Review";
			events[1] = "pageOfMolecularSpecimenReviewParameters";
		}
		else if (eventParameters instanceof ProcedureEventParameters)
		{
			events[0] = "Procedure Event";
			events[1] = "pageOfProcedureEventParameters";
		}
		else if (eventParameters instanceof ReceivedEventParameters)
		{
			events[0] = "Received Event";
			events[1] = "pageOfReceivedEventParameters";
		}
		else if (eventParameters instanceof SpunEventParameters)
		{
			events[0] = "Spun";
			events[1] = "pageOfSpunEventParameters";
		}
		else if (eventParameters instanceof ThawEventParameters)
		{
			events[0] = "Thaw";
			events[1] = "pageOfThawEventParameters";
		}
		else if (eventParameters instanceof TissueSpecimenReviewEventParameters)
		{
			events[0] = "Tissue Specimen Review";
			events[1] = "pageOfTissueSpecimenReviewParameters";
		}
		else if (eventParameters instanceof TransferEventParameters)
		{
			events[0] = "Transfer";
			events[1] = "pageOfTransferEventParameters";
		}

		return events;
	}
}
