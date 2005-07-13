/**
 * <p>Title: Segment Class>
 * <p>Description:  Models the Accession information.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the Segment information.
 * @hibernate.class table="CATISSUE_SEGMENT"
 * @author kapil_kaveeshwar
 */
public class Segment extends AbstractDomainObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * Unique identifier assigned to each Segment.
	 */
	protected Long identifier;
	
	/**
	 * Date segment was created from specimen.
	 */
	protected Date creationDate;
	
	/**
	 * Technician who created the Segment.
	 */
	protected User createdBy;
	
	/**
	 * Original quantity of segment.
	 */
	protected Double quantity;
	
	/**
	 * Units of quantity (e.g. ml, g, or no units).
	 */
	protected String quantityUnit;
	
	/**
	 * Specifies if this segment is still physically available.
	 */
	protected Boolean available;
	
	/**
	 * Quantity of segment still available.
	 */
	protected Double availableQuantity;
	
	/**
	 * Activity Status of specimen, it could be CLOSED, ACTIVE, DISABLED
	 */
	protected ActivityStatus activityStatus;
	
	/**
	 * Bar code for the segment.
	 */
	protected String barcode;
	
	/**
	 * Specimen associated with this segment.
	 */
	protected Specimen specimen;
	
	/**
	 * Collection of samples derived from this Segment.
	 */
	protected Collection sampleCollection = new HashSet();

	/**
	 * Returns the identifier assigned to Specimen.
	 * @return Long representing the id assigned to specimen.
	 * @see #setIdentifier(Long)
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
	 *               unsaved-value="null" generator-class="native"
	 */
	public Long getIdentifier()
	{
		return identifier;
	}

	/**
	 * Sets the identifier assigned to Specimen.
	 * @param identifier the identifier assigned to Specimen.
	 * @see #getIdentifier()
	 */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * Returns the date segment was created from specimen.
	 * @hibernate.property name="creationDate" type="date" 
	 * column="CREATION_DATE" length="50"
	 * @return the date segment was created from specimen.
	 * @see #setCreationDate(java.util.Date)
	 */
	public java.util.Date getCreationDate()
	{
		return creationDate;
	}

	/**
	 * Sets the date segment was created from specimen.
	 * @param creationDate the date segment was created from specimen.
	 * @see #getCreationDate()
	 */
	public void setCreationDate(java.util.Date creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
	 * Returns technician who creates the Segment.
	 * @hibernate.many-to-one column="CREATED_BY" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
	 * @return technician who creates the Segment.
	 * @see #setCreatedBy(User)
	 */
	public User getCreatedBy()
	{
		return createdBy;
	}
	
	/**
	 * Sets technician who creates the Segment.
	 * @param createdBy technician who creates the Segment.
	 * @see #getCreatedBy()
	 */
	public void setCreatedBy(User createdBy)
	{
		this.createdBy = createdBy;
	}

	/**
	 * Returns the original quantity of segment
	 * @hibernate.property name="quantity" type="double" 
	 * column="QUANTITY" length="50"
	 * @return the original quantity of segment
	 * @see #setQuantity(Double)
	 */
	public Double getQuantity()
	{
		return quantity;
	}

	/**
	 * Sets the original quantity of segment
	 * @param quantity the original quantity of segment
	 * @see #getQuantity()
	 */
	public void setQuantity(Double quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * Returns the unit of quantity (e.g. ml, g, or no units).
	 * @hibernate.property name="quantityUnit" type="string" 
	 * column="QUANTITY_UNIT" length="50"
	 * @return the unit of quantity (e.g. ml, g, or no units).
	 * @see #setQuantityUnit(String)
	 */
	public String getQuantityUnit()
	{
		return quantityUnit;
	}

	/**
	 * Sets the unit of quantity (e.g. ml, g, or no units).
	 * @param quantityUnit the unit of quantity (e.g. ml, g, or no units).
	 * @see #getQuantityUnit()
	 */
	public void setQuantityUnit(String quantityUnit)
	{
		this.quantityUnit = quantityUnit;
	}

	/**
	 * Returns the status whether th segment is physically available. 
	 * @hibernate.property name="available" type="boolean" 
	 * column="AVAILABLE" length="50"
	 * @return the status whether th segment is physically available.
	 * @see #setAvailable(Boolean)
	 */
	public Boolean getAvailable()
	{
		return available;
	}

	/**
	 * Sets the status whether th segment is physically available.
	 * @param available the status whether th segment is physically available.
	 * @see #getAvailable()
	 */
	public void setAvailable(Boolean available)
	{
		this.available = available;
	}

	/**
	 * Returns the quantity of segment still available.
	 * @hibernate.property name="availableQuantity" type="double" 
	 * column="AVAILABLE_QUANTITY" length="50"
	 * @return the quantity of segment still available.
	 * @see #setAvailableQuantity(Double)
	 */
	public Double getAvailableQuantity()
	{
		return availableQuantity;
	}

	public void setAvailableQuantity(Double availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}

	/**
	 * Returns the activity status of the segment.
	 * @hibernate.many-to-one column="ACTIVITY_STATUS_ID"
	 * class="edu.wustl.catissuecore.domain.ActivityStatus"
	 * constrained="true"
	 * @return Returns the activity status of the segment.
	 * @see #setActivityStatus(ActivityStatus)
	 */
	public ActivityStatus getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * Sets the activity status of the segment.
	 * @param activityStatus activity status of the segment.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(ActivityStatus activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Returns the barcode assigned to the segment.
	 * @hibernate.property name="barcode" type="string" column="BARCODE" length="50"
	 * @return the barcode assigned to the segment.
	 * @see #setBarcode(java.lang.String)
	 */
	public java.lang.String getBarcode()
	{
		return barcode;
	}

	/**
	 * Sets the barcode assigned to the segment.
	 * @param barCode the barcode assigned to the segment.
	 * @see #getBarcode()
	 */
	public void setBarcode(java.lang.String barCode)
	{
		this.barcode = barCode;
	}

	/**
	 * Returns the sepcimen associated with the segment.
	 * @hibernate.many-to-one column="SPECIMEN_ID"
	 * class="edu.wustl.catissuecore.domain.Specimen"
	 * @return the sepcimen associated with the segment.
	 * @see #setSpecimen(Specimen)
	 */
	public Specimen getSpecimen()
	{
		return specimen;
	}

	/**
	 * Sets the sepcimen associated with the segment.
	 * @param specimen the sepcimen associated with the segment.
	 * @see #getSpecimen()
	 */
	public void setSpecimen(Specimen specimen)
	{
		this.specimen = specimen;
	}
	
	/**
	 * Returns the collection of samples derived from this segment.
	 * @return the collection of samples derived from this segment.
	 * @hibernate.set name="sampleCollection" table="CATISSUE_SAMPLE"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SEGMENT_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Sample"
	 * @see #setSampleCollection(Collection)
	 */
	public Collection getSampleCollection()
	{
		return sampleCollection;
	}

	/**
	 * Sets the collection of samples derived from this segment.
	 * @param sampleCollection the collection of samples derived from this segment.
	 * @see #getSampleCollection()
	 */
	public void setSampleCollection(java.util.Collection sampleCollection)
	{
		this.sampleCollection = sampleCollection;
	}
	
	
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}