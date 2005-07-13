/**
 * <p>Title: EventLog Class>
 * <p>Description:  EventLog is a collection of event logs.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on May 5, 2005
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * EventLog is a collection event logs.
 * @hibernate.class table="CATISSUE_EVENTLOG"
 * @author gautam_shetty
 */
public class EventLog extends AbstractDomainObject implements Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each change.
     * */
    protected Long identifier;

    /**
     * System number for referenced object.
     */
    protected Long objectID;
    
    /**
     * Type of the object.
     */
    protected String ObjectType;

    /**
     * Name of referenced data element.
     */
    protected String elementName;

    /**
     * What happened to the object (Add,Update, Close or Disable).
     */
    protected String eventType;

    /**
     * The pre-event value of the object property.
     */
    protected String previousValue;

    /**
     * The post-event value of the object property.
     */
    protected String currentValue;

    /**
     * Audit which keeps track of this event log.
     */
    private Audit audit;

    /**
     * Returns the unique identifier assigned to this change.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the unique identifier assigned to this change.
     * @see #setIdentifier(int)
     * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the unique identifier assigned to this change.
     * @param identifier the unique identifier assigned to this change.
     * @see #getIdentifier()
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the system number for referenced object.
     * @hibernate.property name="objectID" type="long"
     * column="OBJECT_ID" length="50"
     * @return the system number for referenced object.
     * @see #setObjectID(Long)
     */
    public Long getObjectID()
    {
        return objectID;
    }

    /**
     * Sets the system number for referenced object.
     * @param objectID the system number for referenced object.
     * @see #getObjectID()
     */
    public void setObjectID(Long objectID)
    {
        this.objectID = objectID;
    }

    /**
     * Returns the type of the object.
     * @hibernate.property name="ObjectType" type="string"
     * column="OBJECT_TYPE" length="50"
     * @return the type of the object.
     * @see #setObjectType(String)
     */
    public String getObjectType()
    {
        return ObjectType;
    }

    /**
     * Sets the type of the object.
     * @param ObjectType the type of the object.
     * @see #getObjectType()
     */
    public void setObjectType(String ObjectType)
    {
        this.ObjectType = ObjectType;
    }

    /**
     * Returns the name of the referenced data element.
     * @hibernate.property name="elementName" type="string"
     * column="ELEMENT_NAME" length="50"
     * @return the name of the referenced data element.
     * @see #setElementName(String)
     */
    public String getElementName()
    {
        return elementName;
    }

    /**
     * Sets the name of the referenced data element.
     * @param elementName the name of the referenced data element.
     * @see #getElementName()
     */
    public void setElementName(String elementName)
    {
        this.elementName = elementName;
    }

    /**
     * Returns what happened to the object (Add,Update, Close or Disable).
     * @hibernate.property name="eventType" type="string"
     * column="EVENT_TYPE" length="50"
     * @return what happened to the object (Add,Update, Close or Disable).
     * @see #setEventType(String)
     */
    public String getEventType()
    {
        return eventType;
    }

    /**
     * Sets what happened to the object (Add,Update, Close or Disable).
     * @param eventType what happened to the object (Add,Update, Close or Disable).
     * @see #getEventType()
     */
    public void setEventType(String eventType)
    {
        this.eventType = eventType;
    }

    /**
     * Returns the pre-event value of the object property.
     * @hibernate.property name="previousValue" type="string"
     * column="PREVIOUS_VALUE" length="50"
     * @return the pre-event value of the object property.
     * @see #setPreviousValue(String)
     */
    public String getPreviousValue()
    {
        return previousValue;
    }

    /**
     * Sets the pre-event value of the object property.
     * @param previousValue the pre-event value of the object property.
     * @see #getPreviousValue()
     */
    public void setPreviousValue(String previousValue)
    {
        this.previousValue = previousValue;
    }

    /**
     * Returns the post-event value of the object property.
     * @hibernate.property name="currentValue" type="string"
     * column="CURRENT_VALUE" length="50"
     * @return the post-event value of the object property.
     * @see #setPreviousValue(String)
     */
    public String getCurrentValue()
    {
        return currentValue;
    }

    /**
     * Sets the post-event value of the object property.
     * @param currentValue the post-event value of the object property.
     * @see #getCurrentValue()
     */
    public void setCurrentValue(String currentValue)
    {
        this.currentValue = currentValue;
    }

    /**
	 * Returns the audit associated with this event log.
	 * @hibernate.many-to-one column="AUDIT_ID"
	 *  class="edu.wustl.catissuecore.domain.Audit"
	 * @return the audit associated with this event log.
	 * @see #setAudit(Audit)
	 */
    public Audit getAudit()
    {
        return audit;
    }

    /**
     * Sets the audit associated with this event log.
     * @param audit the audit associated with this event log.
     * @see #getAudit()
     */
    public void setAudit(Audit audit)
    {
        this.audit = audit;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }

}