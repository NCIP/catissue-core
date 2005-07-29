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

import java.util.Date;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * Attributes associated with a generic event.
 * 
 * @author Aniruddha Phadnis
 */
public abstract class EventParameters extends AbstractDomainObject implements java.io.Serializable
{
	
	private static final long serialVersionUID = 1234567890L;

	/**
     * System generated unique systemIdentifier.
     */
	protected Long systemIdentifier;
	
	/**
     * Date and time of the event.
     */
	protected Date timestamp;
	
	/**
     * User who performs the event.
     */
	protected User user;
	
	/**
     * Text comments on event.
     */
	protected String comments;

	/**
     * Returns System generated unique systemIdentifier.
     * @return System generated unique systemIdentifier.
     * @see #setSystemIdentifier(Integer)
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets unique systemIdentifier.
     * @param systemIdentifier Identifier to be set.
     * @see #getSystemIdentifier()
     */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
     * Returns date and time of the event. 
     * @return Date and time of the event.
     * @see #setTimestamp(Date)
     * @hibernate.property name="timestamp" type="date" 
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
	public void setAllValues(AbstractActionForm abstractForm)
	{
		try
		{
			EventParametersForm form = (EventParametersForm)abstractForm ;
			this.systemIdentifier = new Long(form.getSystemIdentifier());
			this.comments = form.getComments();
			User u = new User();
			u.setSystemIdentifier(new Long(form.getUserId()));
			this.user = u;
//System.out.println("Done");
			if (form.getDateOfEvent().trim().length()!=0  )
			{
				this.timestamp = Utility.parseDate(form.getDateOfEvent(),Constants.DATE_PATTERN_MM_DD_YYYY);
				this.timestamp.setHours(Integer.parseInt(form.getTimeInHours() ));
				this.timestamp.setMinutes(Integer.parseInt(form.getTimeInMinutes()) );
			}
		}
        catch (Exception excp)
        {
        	excp.printStackTrace();
            Logger.out.error(excp.getMessage());
        }
	}
	
}