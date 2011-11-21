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
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
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

	/*public static void validateReceivedEvent(ActionErrors errors, Validator validator, long receivedEventUserId, String receivedEventDateOfEvent,
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
		 Bug id: 4179
		 patch id: 4179_2
		if (!validator.isValidTime(receivedTime,  CommonServiceLocator.getInstance().getTimePattern()))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected", ApplicationProperties
							.getValue("receivedTime.invalidTime")));
		}
	}
*/
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

	
	

	public static String[] getEvent(SpecimenEventParameters eventParameters)
	{
		String[] events = new String[2];

//		else if (eventParameters instanceof DisposalEventParameters)
//		{
//			events[0] = "Disposal";
//			events[1] = "pageOfDisposalEventParameters";
//		}
		if (eventParameters instanceof TransferEventParameters)
		{
			events[0] = "Transfer";
			events[1] = "pageOfTransferEventParameters";
		}

		return events;
	}
}
