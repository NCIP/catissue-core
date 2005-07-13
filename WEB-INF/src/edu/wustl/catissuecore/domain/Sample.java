/**
 * <p>Title: Accession Class>
 * <p>Description:  Models the Accession information.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Date;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the Segment information.
 * @hibernate.class table="CATISSUE_SAMPLE"
 * @author kapil_kaveeshwar
 */
public class Sample extends AbstractDomainObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * The unique identifier assigned to sample.
	 */
	protected Long identifier;
	
	/**
	 * Date sample was created.
	 */
	protected Date creationDate;
	
	/**
	 * Technician who created the sample.
	 */
	protected User createdBy;
	
	/**
	 * Type of sample.
	 */
	protected String type;
	
	/**
	 * Quality control index of sample.
	 */
	protected String quality;
	
	/**
	 * URL to gel image of sample.
	 */
	protected String gelImageURL;
	
	/**
	 * Lane number of gel corresponding to sample.
	 */
	protected String laneNumber;
	
	/**
	 * Total quantity of sample.
	 */
	protected Double quantity;
	
	/**
	 * Units of quantity (e.g. ml, g, or no units).
	 */
	protected String quantityUnit;
	
	/**
	 * Concentration of sample.
	 */
	protected Double concentration;
	
	/**
	 * Specifies if this sample still physically available.
	 */
	protected Boolean available;
	
	/**
	 * Quantity of sample available.
	 */
	protected Double availableQuantity;
	
	/**
	 * Activity Status of specimen, it could be CLOSED, ACTIVE, DISABLED.
	 */
	protected ActivityStatus activityStatus;
	
	/**
	 * Bar code for the sample.
	 */
	protected String barcode;
	
	/**
	 * Segment whom this sample is derived. 
	 */
	private Segment segment;
	
	/**
	 * Specimen associated with this sample.
	 */
	private Specimen specimen;
	
	/**
	 * Returns the identifier assigned to sample.
	 * @return Long representing the id assigned to sample.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @see #setIdentifier(Long)
	 */
	public Long getIdentifier()
	{
		return identifier;
	}

	/**
	 * Sets the identifier assigned to sample.
	 * @param identifier the identifier assigned to sample.
	 * @see #getIdentifier()
	 */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * Returns the date sample was created.
	 * @hibernate.property name="creationDate" type="date" 
	 * column="CREATION_DATE" length="50"
	 * @return the date sample was created.
	 * @see #setCreationDate(java.util.Date)
	 */
	public java.util.Date getCreationDate()
	{
		return creationDate;
	}

	/**
	 * Sets the date sample was created.
	 * @param creationDate the date sample was created.
	 * @see #getCreationDate()
	 */
	public void setCreationDate(java.util.Date creationDate)
	{
		this.creationDate = creationDate;
	}

	/**
	 * Returns the technician who created the sample. 
	 * @hibernate.many-to-one column="CREATED_BY" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
	 * @return the technician who created the sample.
	 * @see #setCreatedBy(User)
	 */
	public User getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * Sets the technician who created the sample.
	 * @param createdBy the technician who created the sample.
	 * @see #getCreatedBy()
	 */
	public void setCreatedBy(User createdBy)
	{
		this.createdBy = createdBy;
	}

	/**
	 * Returns the type of sample.
	 * @hibernate.property name="type" type="string" 
	 * column="TYPE" length="50"
	 * @return the type of sample.
	 * @see #setType(String)
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Sets the type of sample.
	 * @param type the type of sample.
	 * @see #getType()
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Returns the quality control index of sample.
	 * @hibernate.property name="quality" type="string" 
	 * column="QUALITY" length="50"
	 * @return the quality control index of sample.
	 * @see #setQuality(String)
	 */
	public String getQuality()
	{
		return quality;
	}

	/**
	 * Sets the quality control index of sample.
	 * @param quality the quality control index of sample.
	 * @see #getQuality()
	 */
	public void setQuality(String quality)
	{
		this.quality = quality;
	}

	/**
	 * Returns the URL to gel image of sample.
	 * @hibernate.property name="gelImageURL" type="string" 
	 * column="GEL_IMAGE_URL" length="100"
	 * @return the URL to gel image of sample.
	 * @see #setGelImageURL(String)
	 */
	public String getGelImageURL()
	{
		return gelImageURL;
	}

	/**
	 * Sets the URL to gel image of sample.
	 * @param gelImageURL the URL to gel image of sample.
	 * @see #getGelImageURL()
	 */
	public void setGelImageURL(String gelImageURL)
	{
		this.gelImageURL = gelImageURL;
	}

	/**
	 * Returns the lane number of gel corresponding to sample.
	 * @hibernate.property name="laneNumber" type="string" 
	 * column="LANE_NUMBER" length="50"
	 * @return the lane number of gel corresponding to sample.
	 * @see #setLaneNumber(String)
	 */
	public String getLaneNumber()
	{
		return laneNumber;
	}

	/**
	 * Sets the lane number of gel corresponding to sample.
	 * @param laneNumber the lane number of gel corresponding to sample.
	 * @see #getLaneNumber()
	 */
	public void setLaneNumber(String laneNumber)
	{
		this.laneNumber = laneNumber;
	}

	/**
	 * Returns the quantity of the sample.
	 * @hibernate.property name="quantity" type="double" 
	 * column="QUANTITY" length="50"
	 * @return the quantity of the sample.
	 * @see #setQuantity(Double)
	 */
	public Double getQuantity()
	{
		return quantity;
	}

	/**
	 * Sets the quantity of the sample.
	 * @param quantity the quantity of the sample.
	 * @see #getQuantity()
	 */
	public void setQuantity(Double quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * Returns the unit of quantity.
	 * @hibernate.property name="quantityUnit" type="string" 
	 * column="QUANTITY_UNIT" length="50"
	 * @return the unit of quantity.
	 * @see #setQuantityUnit(java.lang.String)
	 */
	public java.lang.String getQuantityUnit()
	{
		return quantityUnit;
	}

	/**
	 * Sets the unit of quantity.
	 * @param quantityUnit the unit of quantity.
	 * @see #getQuantityUnit()
	 */
	public void setQuantityUnit(java.lang.String quantityUnit)
	{
		this.quantityUnit = quantityUnit;
	}

	/**
	 * Returns the concentration of the sample.
	 * @hibernate.property name="concentration" type="double" 
	 * column="CONCENTRATION" length="50"
	 * @return the concentration of the sample.
	 * @see #setConcentration(Double)
	 */
	public Double getConcentration()
	{
		return concentration;
	}

	/**
	 * Sets the concentration of the sample.
	 * @param concentration the concentration of the sample.
	 * @see #getConcentration()
	 */
	public void setConcentration(Double concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * Returns whether this sample is still physically available.
	 * @hibernate.property name="available" type="boolean" 
	 * column="AVAILABLE" length="50"
	 * @return whether this sample is still physically available.
	 * @see #setAvailable(Boolean)
	 */
	public Boolean getAvailable()
	{
		return available;
	}

	/**
	 * Sets whether this sample is still physically available.
	 * @param available whether this sample is still physically available.
	 * @see #getAvailable()
	 */
	public void setAvailable(Boolean available)
	{
		this.available = available;
	}

	/**
	 * Returns the available quantity of the sample.
	 * @hibernate.property name="availableQuantity" type="double" 
	 * column="AVAILABLE_QUANTITY" length="50"
	 * @return the available quantity of the sample.
	 * @see #setAvailableQuantity(Double)
	 */
	public Double getAvailableQuantity()
	{
		return availableQuantity;
	}

	/**
	 * Sets the available quantity of the sample.
	 * @param availableQuantity the available quantity of the sample.
	 * @see #getAvailableQuantity()
	 */
	public void setAvailableQuantity(Double availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}

	/**
	 * Returns the activity status of the sample.
	 * @hibernate.many-to-one column="ACTIVITY_STATUS_ID"
	 * class="edu.wustl.catissuecore.domain.ActivityStatus"
	 * constrained="true"
	 * @return Returns the activity status of the sample.
	 * @see #setActivityStatus(ActivityStatus)
	 */
	public ActivityStatus getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * Sets the activity status of the sample.
	 * @param activityStatus activity status of the sample.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(ActivityStatus activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Returns the barcode of this sample.
	 * @hibernate.property name="barcode" type="string" column="BARCODE" length="50"
	 * @return the barcode of this sample.
	 * @see #setBarcode(java.lang.String)
	 */
	public java.lang.String getBarcode()
	{
		return barcode;
	}

	/**
	 * Sets the barcode of this sample.
	 * @param barCode the barcode of this sample.
	 * @see #getBarcode()
	 */
	public void setBarcode(java.lang.String barCode)
	{
		this.barcode = barCode;
	}

	
	/**
	 * Returns the segment associated with the sample.
	 * @hibernate.many-to-one column="SEGMENT_ID"
	 * class="edu.wustl.catissuecore.domain.Segment"
	 * @return the Segment associated with the sample.
	 * @see #setSegment(Segment)
	 */
	public Segment getSegment()
	{
		return segment;
	}

	/**
	 * Sets the segment associated with the sample.
	 * @param segment the segment associated with the sample.
	 * @see #getSegment()
	 */
	public void setSegment(Segment segment)
	{
		this.segment = segment;
	}
	
	/**
	 * Returns the specimen associated with the sample.
	 * @hibernate.many-to-one column="SPECIMEN_ID"
	 * class="edu.wustl.catissuecore.domain.Specimen"
	 * @return the specimen associated with the sample.
	 * @see #setSpecimen(Specimen)
	 */
	public Specimen getSpecimen()
	{
		return specimen;
	}
	
	/**
	 * Sets the specimen associated with the sample.
	 * @param specimen the specimen associated with the sample.
	 * @see #getSpecimen() 
	 */
	public void setSpecimen(Specimen specimen)
	{
		this.specimen = specimen;
	}
	
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}