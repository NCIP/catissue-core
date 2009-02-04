/**
 * <p>Title: EventParameters Class
 * <p>Description:  Attributes associated with a generic event. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;

import java.util.Calendar;
import java.util.Date;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a generic event.
 * 
 * @author Aniruddha Phadnis
 */
public abstract class EventParameters extends AbstractDomainObject implements java.io.Serializable,Cloneable
{
	
	private static final long serialVersionUID = 1234567890L;
	
	/**
     * System generated unique id.
     */
	protected Long id;
	
//	 Change for API Search   --- Ashwin 04/10/2006
	/**
     * Date and time of the event.
     */
	protected Date timestamp;
	
    // Change for API Search   --- Ashwin 04/10/2006
	/**
     * User who performs the event.
     */
	protected User user;
	
	/**
     * Text comments on event.
     */
	protected String comments;
	
	/**
     * Returns System generated unique id.
     * @return System generated unique id.
     * @see #setId(Integer)
     */
	public Long getId()
	{
		return id;
	}

	/**
     * Sets unique id.
     * @param id Identifier to be set.
     * @see #getId()
     */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
     * Returns date and time of the event. 
     * @return Date and time of the event.
     * @see #setTimestamp(Date)
     * @hibernate.property name="timestamp" type="timestamp" 
     * column="EVENT_TIMESTAMP"
     */
	public Date getTimestamp()
	{
		return timestamp;
	}

	/**
     * Sets date and time of the event.
     * @param timestamp Date and time of the event.
     * @see #getTimestamp()
     */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
     * Returns user who performs the event.
     * @hibernate.many-to-one column="USER_ID" 
     * class="edu.wustl.catissuecore.domain.User" constrained="true"
     * @return The user who performs the event.
     * @see #setUser(User)
     */
	public User getUser()
	{
		return user;
	}

	/**
     * Sets user who performs the event.
     * @param user user who performs the event.
     * @see #getUser()
     */
	public void setUser(User user)
	{
		this.user = user;
	}

	/**
     * Returns text comments on this event. 
     * @return Text comments on this event.
     * @see #setComments(String)
     * @hibernate.property name="comments" type="string" 
     * column="COMMENTS" length="500"
     */
	public String getComments()
	{
		return comments;
	}

	/**
     * Sets text comments on this event.
     * @param comments text comments on this event.
     * @see #getComments()
     */
	public void setComments(String comments)
	{
		this.comments = comments;
	}
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm)throws AssignDataException
	{
		try
		{
			EventParametersForm form = (EventParametersForm)abstractForm ;
	        // Change for API Search   --- Ashwin 04/10/2006
	    	if (SearchUtil.isNullobject(user))
	    	{
	    		user = new User();
	    	}
	        // Change for API Search   --- Ashwin 04/10/2006
	    	if (SearchUtil.isNullobject(timestamp))
	    	{
	    		timestamp = Calendar.getInstance().getTime();
	    	}
	    	
			this.comments = form.getComments();
			
			user.setId(new Long(form.getUserId()));

			if (form.getDateOfEvent() != null && form.getDateOfEvent().trim().length()!=0  )
			{
				Calendar calendar = Calendar.getInstance();
				
				Date date = Utility.parseDate(form.getDateOfEvent(),Utility.datePattern(form.getDateOfEvent()));
				calendar.setTime(date);
				calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(form.getTimeInHours()));
				calendar.set(Calendar.MINUTE,Integer.parseInt(form.getTimeInMinutes()));
			
				this.timestamp = calendar.getTime();  
				

			}
		}
        catch (Exception excp)
        {
	    	// use of logger as per bug 79
	    	Logger.out.error(excp.getMessage(),excp); 
	    	throw new AssignDataException();
        }
	}
	
}