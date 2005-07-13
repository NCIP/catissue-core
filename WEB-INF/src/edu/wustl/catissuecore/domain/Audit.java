/**
 * <p>Title: Audit Class>
 * <p>Description:  Audit keeps a track of events in the application.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Audit keeps a track of events in the application.
 * @hibernate.class table="CATISSUE_AUDIT"
 * @author gautam_shetty
 */
public class Audit extends AbstractDomainObject implements java.io.Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each audit record.
     * */
    protected Long identifier;

    /**
     * Date and time the event occurred.
     */
    protected Date timeStamp;

    /**
     * User who performed the action.
     */
    protected User user;

    
    private Collection eventLogCollection = new HashSet();

    /**
     * Returns the unique identifier assigned to the audit record.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the unique identifier assigned to the audit record.
     * @see #setIdentifier(int)
     * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the unique identifier to the audit record.
     * @param identifier the unique identifier to the audit record.
     * @see #getIdentifier() 
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the date and time the event occured.
     * @hibernate.property name="timeStamp" column="AUDIT_TIMESTAMP" type="date" 
     * @return the date and time the event occured.
     * @see #setTimeStamp(Date)
     */
    public Date getTimeStamp()
    {
        return timeStamp;
    }
    
    /**
     * Sets the date and time the event occured.
     * @param timeStamp the date and time the event occured.
     * @see #getTimeStamp()
     */
    public void setTimeStamp(Date timeStamp)
    {
        this.timeStamp = timeStamp;
    }

    /**
     * Returns the user who performed the action.
	 * @hibernate.many-to-one column="USER_ID" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
     * @return the user who performed the action.
     * @see #setUser(User)
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Sets the user who performed the action.
     * @param user the user who performed the action.
     * @see #getUser()
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
	 * Returns the collection of event logs in this audit.
	 * @return the collection of event logs in this audit.
	 * @hibernate.set name="eventLogCollection" table="CATISSUE_EVENTLOG"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="AUDIT_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.EventLog"
	 * @see #setEventLogCollection(Collection)
	 */
    public Collection getEventLogCollection()
    {

        return eventLogCollection;
    }

    /**
     * Sets the collection of event logs in this audit.
     * @param eventLogCollection the collection of event logs in this audit.
     * @see #getEventLogCollection()
     */
    public void setEventLogCollection(Collection eventLogCollection)
    {
        this.eventLogCollection = eventLogCollection;
    }

    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}