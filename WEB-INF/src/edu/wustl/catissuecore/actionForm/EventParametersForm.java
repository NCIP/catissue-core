/**
 * <p>Title: EventParametersForm Class</p>
 * <p>Description:  This Class will be the super class for all Event Parameter classes.
 * <p> It contains the common attributes of the Event Parameter classes.   
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28th, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.EventParameters;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 *  This Class will be the super class for all Event Parameter classes.
 */
public abstract class EventParametersForm extends AbstractActionForm
{

    /**
     * Time in hours for the Event Parameter.
     * */
    protected String timeInHours;

    /**
     * Time in minutes for the Event Parameter.
     * */
    protected String timeInMinutes;

    /**
     * Date of the Event Parameter.
     * */
    protected String dateOfEvent;

    /**
     * Id of the User.   
     */
    protected long userId;

    /**
     * Comments on the event parameter.   
     */
    protected String comments;
    
// ----- variable declaration end
    
    
// ------ GET SET methods
	/**
	 * @return Returns the comments.
	 */
	public String getComments()
	{
		return comments;
	}
	
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}
		
	/**
	 * @return Returns the dateOfEvent.
	 */
	public String getDateOfEvent()
	{
		return dateOfEvent;
	}
	
	/**
	 * @param dateOfEvent The dateOfEvent to set.
	 */
	public void setDateOfEvent(String dateOfEvent)
	{
		this.dateOfEvent = dateOfEvent;
	}
	
	/**
	 * @return Returns the time_InMinutes.
	 */
	public String getTimeInMinutes()
	{
		return timeInMinutes;
	}
	
	/**
	 * @param time_InMinutes The time_InMinutes to set.
	 */
	public void setTimeInMinutes(String time_InMinutes)
	{
		this.timeInMinutes = time_InMinutes;
	}
		
	/**
	 * @return Returns the timeStamp.
	 */
	public String getTimeInHours()
	{
		return timeInHours;
	}
	
	/**
	 * @param timeStamp The timeStamp to set.
	 */
	public void setTimeInHours(String timeStamp)
	{
		this.timeInHours = timeStamp;
	}
	
	/**
	 * @return Returns the userId.
	 */
	public long getUserId()
	{
		return userId;
	}
	
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(long userId)
	{
		this.userId = userId;
	}    
	
	
//--------  Super class Methods
	protected void reset()
	{
//		this.comments = null;
//		this.dateOfEvent =null;
//		this.timeInHours = null;
//		this.timeInMinutes = null;
//		this.userId = -1;
	}

	/**
     * Overrides the validate method of ActionForm.
     * */
     public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
     {
     	
         ActionErrors errors = new ActionErrors();
         Validator validator = new Validator();
         
         try
         {
         	// checks the userid

           	if ((userId) == -1L)
            {
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("eventparameters.user")));
            }
           	if (!validator.checkDate( dateOfEvent ) )
           	{
           		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",ApplicationProperties.getValue("eventparameters.dateofevent")));
           	}
         }
         catch(Exception excp)
         {
             Logger.out.error(excp.getMessage());
         }
         return errors;
      }
	
     /* (non-Javadoc)
 	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setAllValues(edu.wustl.catissuecore.domain.AbstractDomainObject)
 	 */
 	public void setAllValues(AbstractDomainObject abstractDomain)
 	{
 	   EventParameters eventParametersObject = (EventParameters)abstractDomain;
 	   this.comments  = Utility.toString(eventParametersObject.getComments());
 	   this.id = eventParametersObject.getId().longValue() ;
		
 	   Calendar calender = Calendar.getInstance();
 	   calender.setTime(eventParametersObject.getTimestamp());
 	   this.timeInHours = Utility.toString(Integer.toString( calender.get(Calendar.HOUR_OF_DAY)));
 	   this.timeInMinutes = Utility.toString(Integer.toString(calender.get(Calendar.MINUTE)));
 	   this.userId = eventParametersObject.getUser().getId().longValue() ;
 	   this.dateOfEvent = Utility.parseDateToString(eventParametersObject.getTimestamp(),Constants.DATE_PATTERN_MM_DD_YYYY);
 	   //this.dateOfEvent = (calender.get(Calendar.MONTH)+1)+"-"+calender.get(Calendar.DAY_OF_MONTH)+"-"+calender.get(Calendar.YEAR) ;
 	   Logger.out.debug("id:"+id+" timeInHours:"+timeInHours+" timeInMinutes:"+timeInMinutes+" userId:"+userId+" dateOfEvent:"+dateOfEvent);
 	}
 	
}