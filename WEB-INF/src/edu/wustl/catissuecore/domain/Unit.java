/**
 * <p>Title: Unit Class>
 * <p>Description:  Unit is a location within a Site that holds a collection of biospecimens.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on May 5, 2005
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Unit is a location within a Site that holds a collection of biospecimens.
 * @hibernate.class table="CATISSUE_UNIT"
 * @author gautam_shetty
 */
public class Unit extends AbstractDomainObject implements Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each Unit.
     * */
    protected Long identifier;

    /**
     * Type of storage Unit.
     */
    protected String type;

    /**
     * The status of the UNIT ("Full", "Open", "Inactive").
     */
    protected String activityStatus;

    /**
     * Barcode of the Unit.
     */
    protected String barCode;

    /**
     * The capacity of the Unit.
     */
    private Capacity capacity;

    /**
     * Address of this Unit.
     */
    private Address address;

    /**
     * The Site in which this Unit resides.
     */
    private Site site;

    /**
     * Collection of storage collections present in this Unit.
     */
    private Collection collectionCollection = new HashSet();

    /**
	 * Returns the unique identifier assigned to this Unit.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the unique identifier assigned to this Unit.
     * @see #setIdentifier(Long)
	 * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
	 * Sets an identifier to this unit.
	 * @param identifier identifier to this unit.
	 * @see #getIdentifier()
	 * */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the type of the Unit.
     * @hibernate.property name="type" type="string" column="TYPE" length="50"
     * @return the type of the Unit.
     * @see #setType(String)
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type of the Unit.
     * @param type the type of the Unit.
     * @see #getType()
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Returns the activity status of the Unit.
     * @hibernate.many-to-one column="ACTIVITY_STATUS_ID" 
     * class="edu.wustl.catissuecore.domain.ActivityStatus" constrained="true"
     * @return Returns the activity status of the Unit.
     * @see #setActivityStatus(ActivityStatus)
     */
    public String getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the Unit.
     * @param activityStatus the activity status of the Unit.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Returns the barcode assigned to the Unit.
     * @hibernate.property name="barCode" type="string" 
     * column="BAR_CODE" length="100"
     * @return the barcode assigned to the Unit.
     * @see #setBarCode(String)
     */
    public String getBarCode()
    {
        return barCode;
    }

    /**
     * Sets the barcode assigned to this unit.
     * @param barCode the barcode assigned to this unit.
     * @see #getBarCode()
     */
    public void setBarCode(String barCode)
    {
        this.barCode = barCode;
    }

    /**
     * Returns the capacity of the unit.
	 * @hibernate.many-to-one column="CAPACITY_ID" 
	 * class="edu.wustl.catissuecore.domain.Capacity" constrained="true"
     * @return the capacity of the unit.
     * @see #setCapacity(Capacity)
     */
    public Capacity getCapacity()
    {
        return capacity;
    }

    /**
     * Sets the capacity of the unit.
     * @param capacity the capacity of the unit.
     * @see #getCapacity()
     */
    public void setCapacity(Capacity capacity)
    {
        this.capacity = capacity;
    }

    /**
     * Returns the address of this Unit.
     * @hibernate.many-to-one column="ADDRESS_IDENTIFIER" 
     * class="edu.wustl.catissuecore.domain.Address" constrained="true"
     * @return the address of this Unit.
     */
    public Address getAddress()
    {
        return address;
    }

    /**
     * Sets the address of this unit.
     * @param address the address of this unit.
     * @see #getAddress()
     */
    public void setAddress(Address address)
    {
        this.address = address;
    }

    /**
     * Returns the Site to which this Unit resides.
     * @hibernate.many-to-one column="SITE_IDENTIFIER" 
     * class="edu.wustl.catissuecore.domain.Site" constrained="true"
     * @return the Site to which this Unit resides.
     */
    public Site getSite()
    {
        return site;
    }

    /**
     * Sets the Site to which this Unit resides.
     * @param site the Site to which this Unit resides.
     * @see #getSite()
     */
    public void setSite(Site site)
    {
        this.site = site;
    }

    /**
	 * Returns the collection of collection storage units present in this Unit.
	 * @return the collection of collection storage units present in this Unit.
	 * @hibernate.set name="collectionCollection" table="CATISSUE_COLLECTION"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="UNIT_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Collection"
	 * @see #setSampleCollection(Collection)
	 */
    public Collection getCollectionCollection()
    {
        return collectionCollection;
    }

    /**
     * Sets the collection of collection storage units present in this Unit.
     * @param collectionCollection the collection of collection storage units present in this Unit.
     * @see #getCollectionCollection()
     */
    public void setCollectionCollection(Collection collectionCollection)
    {
        this.collectionCollection = collectionCollection;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}