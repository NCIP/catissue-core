/**
 * <p>Title: Collection Class>
 * <p>Description:  Models the information of Collection storage unit.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the information of Collection storage unit.
 * @hibernate.class table="CATISSUE_COLLECTION"
 * @author gautam_shetty
 */
public class Collection extends AbstractDomainObject implements Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
	 * identifier is a unique id assigned to this collection storage unit.
	 */
    protected Long identifier;

    /**
     * Type of collection.
     */
    protected String type;

    /**
     * The status of the Collection ("Full", "Open", "Inactive").
     */
    protected ActivityStatus activityStatus;

    /**
     * Barcode of the collection.
     */
    protected String barCode;

    /**
     * Capacity of the collection.
     */
    private Capacity capacity;

    /**
     * Collection of boxes in this collection.
     */
    private java.util.Collection boxCollection = new HashSet();

    /**
     * The unit in which this collection resides.
     */
    private Unit unit;

    /**
	 * Returns the identifier assigned to this collection.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @return the identifier assigned to this collection.
	 * @see #setIdentifier(Long)
	 */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the identifier assigned to this collection.
     * @param identifier the identifier assigned to this collection.
     * @see #getIdentifier()
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the type of the collection.
     * @hibernate.property name="type" type="string" 
     * column="TYPE" length="50"
     * @return the type of the collection.
     * @see #setType(String)
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type of the collection.
     * @param type the type of the collection.
     * @see #getType()
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Returns the activity status of the collection.
     * @hibernate.many-to-one column="ACTIVITY_STATUS_ID" 
     * class="edu.wustl.catissuecore.domain.ActivityStatus" constrained="true"
     * @return Returns the activity status of the collection.
     * @see #setActivityStatus(ActivityStatus)
     */
    public ActivityStatus getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the collection.
     * @param activityStatus the activity status of the collection.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(ActivityStatus activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Returns the barcode assigned to the collection.
     * @hibernate.property name="barCode" type="string" 
     * column="BAR_CODE" length="100"
     * @return the barcode assigned to the collection.
     * @see #setBarCode(String)
     */
    public String getBarCode()
    {
        return barCode;
    }

    /**
     * Sets the barcode assigned to the collection.
     * @param barCode the barcode assigned to the collection.
     * @see #getBarCode() 
     */
    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
    }

    /**
     * Returns the capacity of the collection.
	 * @hibernate.many-to-one column="CAPACITY_ID" 
	 * class="edu.wustl.catissuecore.domain.Capacity" constrained="true"
     * @return the capacity of the collection.
     * @see #setCapacity(Capacity)
     */
    public Capacity getCapacity()
    {

        return capacity;
    }

    /**
     * Sets the capacity of the collection.
     * @param capacity the capacity of the collection.
     * @see #getCapacity()
     */
    public void setCapacity(Capacity capacity)
    {
        this.capacity = capacity;
    }

    /**
	 * Returns the collection of boxes present in this Collection.
	 * @return the collection of boxes present in this Collection.
	 * @hibernate.set name="boxCollection" table="CATISSUE_BOX"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Box"
	 * @see #setSampleCollection(Collection)
	 */
    public java.util.Collection getBoxCollection()
    {
        return boxCollection;
    }

    /**
     * Sets the collection of boxes present in this Collection.
     * @param boxCollection the collection of boxes present in this Collection.
     * @see #getBoxCollection()
     */
    public void setBoxCollection(java.util.Collection boxCollection)
    {
        this.boxCollection = boxCollection;
    }

    /**
     * Returns the unit in which this collection resides.
	 * @hibernate.many-to-one column="UNIT_ID"
	 * class="edu.wustl.catissuecore.domain.Unit"
     * @return the unit in which this collection resides.
     * @see #setUnit(Unit)
     */
    public Unit getUnit()
    {

        return unit;
    }

    /**
     * Sets the unit in which this collection resides.
     * @param unit the unit in which this collection resides.
     * @see #getUnit()
     */
    public void setUnit(Unit unit)
    {
        this.unit = unit;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}