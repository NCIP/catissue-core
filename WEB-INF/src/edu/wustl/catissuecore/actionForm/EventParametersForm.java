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

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.EventParameters;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 *  This Class will be the super class for all Event Parameter classes.
 */
public abstract class EventParametersForm extends AbstractActionForm
{
	/**
     * System generated unique identifier.
     * */
    protected long systemIdentifier;
    
	 /**
     * A String containing the operation(Add/Edit) to be performed.
     * */
    protected String operation = null;

    
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getSystemIdentifier()
	 */
	public long getSystemIdentifier()
	{
		return this.systemIdentifier ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setSystemIdentifier(long)
	 */
	public void setSystemIdentifier(long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier ; 
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#getActivityStatus()
	 */
	public String getActivityStatus()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.actionForm.AbstractActionForm#setActivityStatus(java.lang.String)
	 */
	public void setActivityStatus(String activityStatus)
	{
		// TODO Auto-generated method stub
	}

	/**
     * Returns the operation(Add/Edit) to be performed.
     * @return Returns the operation.
     * @see #setOperation(String)
     */
    public String getOperation()
    {
        return operation;
    }
    
    /**
     * Sets the operation to be performed.
     * @param operation The operation to set.
     * @see #getOperation()
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    
    /**
     * Checks the operation to be performed is add operation.
     * @return Returns true if operation is equal to "add", else it returns false
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
    }
    
	protected void reset()
	{
		this.systemIdentifier = -1;
		this.comments = null;
		this.dateOfEvent =null;
		this.timeInHours = null;
		this.timeInMinutes = null;
		this.userId = -1;
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
           	if (dateOfEvent==null || dateOfEvent.trim().length()==0)
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
 		EventParameters eventParametersObject = (EventParameters)abstractDomain ;
  		this.comments  = eventParametersObject.getComments();
 		this.systemIdentifier = eventParametersObject.getSystemIdentifier().longValue() ;
		
 		Calendar calender = Calendar.getInstance();
 		calender.setTime(eventParametersObject.getTimestamp());
 		this.timeInHours = ""+calender.get(Calendar.HOUR_OF_DAY);
 		this.timeInMinutes = "" + calender.get(Calendar.MINUTE);
 		this.userId = eventParametersObject.getUser().getSystemIdentifier().longValue() ;
 		this.dateOfEvent = calender.get(Calendar.MONTH)+"-"+calender.get(Calendar.DAY_OF_MONTH)+"-"+calender.get(Calendar.YEAR) ;
 		Logger.out.debug("systemIdentifier:"+systemIdentifier+" timeInHours:"+timeInHours+" timeInMinutes:"+timeInMinutes+" userId:"+userId+" dateOfEvent:"+dateOfEvent);
 	}
}