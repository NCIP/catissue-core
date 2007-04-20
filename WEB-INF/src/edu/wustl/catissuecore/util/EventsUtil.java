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

import java.util.Collection;
import java.util.Iterator;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import edu.wustl.catissuecore.action.NewSpecimenAction;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;


/**
 * @author ashish_gupta
 *
 */
public class EventsUtil
{
	public static void validateCollectionEvent(ActionErrors errors, Validator validator, long collectionEventUserId, String collectionEventdateOfEvent,String collectionEventCollectionProcedure )
	{
       	if ((collectionEventUserId) == -1L)
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Collection Event's user"));
        }
       	if (!validator.checkDate(collectionEventdateOfEvent) )
       	{
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Collection Event's date"));
       	}

     	// checks the collectionProcedure
      	if (!validator.isValidOption( collectionEventCollectionProcedure ) )
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("collectioneventparameters.collectionprocedure")));
        }

	}
	public static void validateReceivedEvent(ActionErrors errors, Validator validator,long receivedEventUserId,String receivedEventDateOfEvent, String receivedEventReceivedQuality )
	{
       	if ((receivedEventUserId) == -1L)
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Received Event's user"));
        }
       	if (!validator.checkDate(receivedEventDateOfEvent) )
       	{
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required","Received Event's date"));
       	}

     	// checks the collectionProcedure
      	if (!validator.isValidOption( receivedEventReceivedQuality ) )
        {
       		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("receivedeventparameters.receivedquality")));
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
	
}
