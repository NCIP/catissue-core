/**
 * <p>Title: Box Class>
 * <p>Description:  Models the Box information.</p>
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
 * Models the Box information.
 * @hibernate.class table="CATISSUE_BOX"
 * @author gautam_shetty
 */
public class Box extends AbstractDomainObject implements Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each box.
     * */
    protected Long identifier;

    /**
     * Type of box.
     */
    protected String type;

    /**
     * The status of the collection ("Full", "Open", "Inactive").
     */
    protected ActivityStatus activityStatus;

    /**
     * Barcode assigned to the box.
     */
    protected String barCode;

    /**
     * The capacity of the box.
     */
    private Capacity capacity;

    /**
     * The collection to which this box belongs.
     */
    private Collection collection;

    /**
	 * Returns the unique identifier assigned to this box.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the unique identifier assigned to this box.
     * @see #setIdentifier(Long)
	 * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
	 * Sets an identifier to this box.
	 * @param identifier identifier to this box.
	 * @see #getIdentifier()
	 * */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the type of the box.
     * @hibernate.property name="type" type="string" 
     * column="TYPE" length="50"
     * @return the type of the box.
     * @see #setType(String)
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type of the box.
     * @param type the type of the box.
     * @see #getType()
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Returns the activity status of the box.
     * @hibernate.many-to-one column="ACTIVITY_STATUS_ID" 
     * class="edu.wustl.catissuecore.domain.ActivityStatus" constrained="true"
     * @return Returns the activity status of the box.
     * @see #setActivityStatus(ActivityStatus)
     */
    public ActivityStatus getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the box.
     * @param activityStatus the activity status of the box.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(ActivityStatus activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Returns the barcode assigned to the box.
     * @hibernate.property name="barCode" type="string" 
     * column="BAR_CODE" length="100"
     * @return the barcode assigned to the box.
     * @see #setBarCode(String)
     */
    public String getBarCode()
    {
        return barCode;
    }

    /**
     * Sets the barcode assigned to the box.
     * @param barCode the barcode assigned to the box.
     * @see #getBarCode() 
     */
    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
    }

    /**
     * Returns the capacity of the box.
	 * @hibernate.many-to-one column="CAPACITY_ID" 
	 * class="edu.wustl.catissuecore.domain.Capacity" constrained="true"
     * @return the capacity of the box.
     * @see #setCapacity(Capacity)
     */
    public Capacity getCapacity()
    {
        return capacity;
    }

    /**
     * Sets the capacity of the box.
     * @param capacity the capacity of the box.
     * @see #getCapacity()
     */
    public void setCapacity(Capacity capacity)
    {
        this.capacity = capacity;
    }

    /**
     * Returns the Collection to which this box belongs.
	 * @hibernate.many-to-one column="COLLECTION_ID" 
	 * class="edu.wustl.catissuecore.domain.Collection" constrained="true"
     * @return the Collection to which this box belongs.
     * @see #setCapacity(Capacity)
     */
    public Collection getCollection()
    {
        return collection;
    }

    /**
     * Sets the collection to which this box belongs.
     * @param collection the collection to which this box belongs.
     * @see #getCollection()
     */
    public void setCollection(Collection collection)
    {
        this.collection = collection;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}