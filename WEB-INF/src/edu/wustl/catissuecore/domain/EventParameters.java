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
import edu.wustl.catissuecore.domain.ApplicationUser;

/**
 * Attributes associated with a generic event.
 * 
 * @author Aniruddha Phadnis
 */
public abstract class EventParameters implements java.io.Serializable
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
     * ApplicationUser who performs the event.
     */
	protected ApplicationUser user;
	
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
     * class="edu.wustl.catissuecore.domain.ApplicationUser" constrained="true"
     * @return The user who performs the event.
     * @see #setUser(ApplicationUser)
     */
	public ApplicationUser getUser()
	{
		return user;
	}

	/**
     * Sets user who performs the event.
     * @param user user who performs the event.
     * @see #getUser()
     */
	public void setUser(ApplicationUser user)
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
}