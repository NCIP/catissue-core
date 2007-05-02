/**
 * <p>Title: EventsUtil Class>
 * <p>Description: Utility methods related to events
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

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author ashish_gupta
 *
 */
public class EventsUtil
{
	public static void validateCollectionEvent(ActionErrors errors, Validator validator, long collectionEventUserId, String collectionEventdateOfEvent,String collectionEventCollectionProcedure,String collectionTime )
	{
       	if ((collectionEventUserId) == -1L)
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Collection Event's user"));
        }
       	if (!validator.checkDate(collectionEventdateOfEvent) )
       	{
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid","Collection Event's date"));
       	}

     	// checks the collectionProcedure
      	if (!validator.isValidOption( collectionEventCollectionProcedure ) )
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectioneventparameters.collectionprocedure")));
        }
        /* Bug id: 4179
			patch id: 4179_1*/    
      	if(!validator.isValidTime(collectionTime, Constants.TIME_PATTERN_HH_MM_SS))
      	{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
					ApplicationProperties.getValue("collectionEvent.invalidTime")));
      	}
		

	}
	public static void validateReceivedEvent(ActionErrors errors, Validator validator,long receivedEventUserId,String receivedEventDateOfEvent, String receivedEventReceivedQuality,String receivedTime )
	{
       	if ((receivedEventUserId) == -1L)
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Received Event's user"));
        }
       	if (!validator.checkDate(receivedEventDateOfEvent) )
       	{
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid","Received Event's date"));
       	}

     	// checks the collectionProcedure
      	if (!validator.isValidOption( receivedEventReceivedQuality ) )
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("receivedeventparameters.receivedquality")));
        }
        /* Bug id: 4179
			patch id: 4179_2*/  
      	if(!validator.isValidTime(receivedTime, Constants.TIME_PATTERN_HH_MM_SS))
      	{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.selected",
					ApplicationProperties.getValue("receivedTime.invalidTime")));
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
		Iterator itr = userList.iterator();
		for (int i = 0; itr.hasNext(); i++)
		{
			NameValueBean nameValueBean = (NameValueBean) itr.next();
			if (nameValueBean.getName() != null && nameValueBean.getName().trim().equals(userName))
			{
				String id = nameValueBean.getValue();
				return Long.valueOf(id).longValue();
			}
		}
		return -1;
	}
	/**
	 * @param eventObject
	 * @param validator
	 * @throws DAOException
	 * validating events from bizlogic
	 */
	public static void validateEventsObject(Object eventObject,Validator validator)throws DAOException
	{
		if (eventObject instanceof CollectionEventParameters)
		{
			CollectionEventParameters collectionEventParameters = (CollectionEventParameters) eventObject;
			collectionEventParameters.getUser();
			/* Bug id: 4179
			patch id: 4179_3*/  
			//Collector validation
			if (collectionEventParameters.getUser() == null || collectionEventParameters.getUser().getId() == null || collectionEventParameters.getUser().getId() == 0)
			{
				String message = ApplicationProperties.getValue("specimen.collection.event.user");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
			}
			//Date validation
			if (!validator.checkDate(Utility.parseDateToString(collectionEventParameters.getTimestamp(), Constants.DATE_PATTERN_MM_DD_YYYY)))
			{

				String message = ApplicationProperties.getValue("specimen.collection.event.date");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
			}
			// checks the collectionProcedure
			if (!validator.isValidOption(collectionEventParameters.getCollectionProcedure()))
			{
				String message = ApplicationProperties.getValue("collectioneventparameters.collectionprocedure");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
			}			
			List procedureList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COLLECTION_PROCEDURE, null);
			if (!Validator.isEnumeratedValue(procedureList, collectionEventParameters.getCollectionProcedure()))
			{
				throw new DAOException(ApplicationProperties.getValue("events.collectionProcedure.errMsg"));
			}
			//Container validation
			if (!validator.isValidOption(collectionEventParameters.getContainer()))
			{
				String message = ApplicationProperties.getValue("collectioneventparameters.container");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
			}
			List containerList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_CONTAINER, null);
			if (!Validator.isEnumeratedValue(containerList, collectionEventParameters.getContainer()))
			{
				throw new DAOException(ApplicationProperties.getValue("events.container.errMsg"));
			}

		}
		//ReceivedEvent validation
		else if (eventObject instanceof ReceivedEventParameters)
		{
			ReceivedEventParameters receivedEventParameters = (ReceivedEventParameters) eventObject;
			/* Bug id: 4179
			patch id: 4179_4*/  
			if (receivedEventParameters.getUser() == null || receivedEventParameters.getUser().getId() == null || receivedEventParameters.getUser().getId() == 0)
			{
				String message = ApplicationProperties.getValue("specimen.recieved.event.user");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
			}
			if (!validator.checkDate(Utility.parseDateToString(receivedEventParameters.getTimestamp(), Constants.DATE_PATTERN_MM_DD_YYYY)))
			{
				String message = ApplicationProperties.getValue("specimen.recieved.event.date");
				throw new DAOException(ApplicationProperties.getValue("errors.item.required", message));
			}
	
			List qualityList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RECEIVED_QUALITY, null);
			if (!Validator.isEnumeratedValue(qualityList, receivedEventParameters.getReceivedQuality()))
			{
				throw new DAOException(ApplicationProperties.getValue("events.receivedQuality.errMsg"));
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
	public static Date setTimeStamp(String dateOfEvent,String timeInHrs,String timeInMinutes)
	{
		Date timestamp = null;
		if (dateOfEvent != null && dateOfEvent.trim().length()!=0  )
		{
			Calendar calendar = Calendar.getInstance();			
			try
			{
				Date date = Utility.parseDate(dateOfEvent,Utility.datePattern(dateOfEvent));
				calendar.setTime(date);
				timestamp = calendar.getTime();  
				calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeInHrs));
				calendar.set(Calendar.MINUTE,Integer.parseInt(timeInMinutes));
				timestamp = calendar.getTime();		
			}
			catch (ParseException e)
			{
				Logger.out.debug("Exception in Parsing Date" + e);
			}			
		}
		else
		{
			timestamp = Calendar.getInstance().getTime();
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
		collectionEventParameters.setComments("");
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
		receivedEventParameters.setComments("");
		receivedEventParameters.setReceivedQuality(Constants.NOT_SPECIFIED);
		
		receivedEventParameters.setTimestamp(new Date(System.currentTimeMillis()));
		receivedEventParameters.setUser(user);
		return receivedEventParameters;
	}
}
