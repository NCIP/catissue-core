/**
 * <p>Title: Specimen Class>
 * <p>Description:  Models the Specimen information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the Specimen information.
 * 
 * @hibernate.class table="CATISSUE_SPECIMEN"
 * @author kapil_kaveeshwar
 */
public class Specimen extends AbstractDomainObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * identifier is a unique id assigned to each Participant.
	 */
	protected Long identifier;
	
	/**
	 * Site where the specimen is anatomically derived from.
	 */
	protected String tissueSite;
	
	/**
	 * Specifies bilateral sites, left or right.
	 */
	protected String tissueSide;
	
	/**
	 * Type of specimen.
	 */
	protected String type;
	
	/**
	 * Identifier for the way in which the specimen is collected and processed.
	 */
	protected String processIdentifier;
	
	/**
	 * Quantity of specimen received.
	 */
	protected Double quantity;
	
	/**
	 * Units of quantity (e.g. ml, g, or no units). 
	 */
	protected String quantityUnit;
	
	/**
	 * Quality of specimen upon receipt.
	 */
	protected String receivedQuality;
	
	/**
	 * Specifies whether histological / cytological review of the specimen 
	 * match the diagnosis of record?
	 */
	protected Boolean reviewed;
	
	/**
	 * Date specimen was reviewed.
	 */
	protected Date reviewedDate;
	
	/**
	 * Technician who reviewed the Biospecimen.
	 */
	protected User reviewedBy;
	
	/**
	 * Percentage neoplastic cellularity of the specimen.
	 */
	protected Double neoplasticCellularityPercentage;
	
	/**
	 * Percentage necrosis of the specimen
	 */
	protected Double necrosisPercentage;
	
	/**
	 * Percentage lymphocytic cellularity of the specimen
	 */
	protected Double lymphocyticPercentage;
	
	/**
	 * Percentage total cellularity of the specimen.
	 */
	protected Double totalCellularityPercentage;
	
	/**
	 * Histological Quality of the specimen.
	 */
	protected String histologicalQuality;
	
	/**
	 * Specifies whether high quality samples been successfully derived from this specimen?
	 */
	protected Boolean sampleDerived;
	
	/**
	 * Specifies if this specimen is still physically available.
	 */
	protected Boolean available;
	
	/**
	 * Quantity of specimen still available.
	 */
	protected Double availableQuantity;
	
	/**
	 * Comments on specimen.
	 */
	protected String comments;

	/**
	 * Activity Status of specimen, it could be CLOSED, ACTIVE, DISABLED
	 */
	protected ActivityStatus activityStatus;

	/**
	 * Bar code for the specimen.
	 */
	protected String barcode;
	
	/**
	 * Accession through which this specimen was collected.
	 */
	protected Accession accession;
	
	/**
	 * Collection of segments associated with this specimen.
	 */
	protected Collection segmentCollection = new HashSet();
	
	/**
	 * Collection of samples associated with this specimen.
	 */
	protected Collection sampleCollection = new HashSet();

	/**
	 * Returns the identifier assigned to Specimen.
	 * @return Long representing the id assigned to specimen.
	 * @see #setIdentifier(Long)
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 */
	public Long getIdentifier()
	{
		return identifier;
	}

	/**
	 * Sets the identifier to the Specimen.
	 * @param identifier the identifier to the Specimen.
	 * @see #getIdentifier()
	 */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}

	/**
	 * Returns the site where the specimen is anatomically derived from.
	 * @hibernate.property name="tissueSite" type="string" column="TISSUE_SITE" length="50"
	 * @return the site where the specimen is anatomically derived from.
	 * @see #setTissueSite(String)
	 */
	public String getTissueSite()
	{
		return tissueSite;
	}

	/**
	 * Sets the site where the specimen is anatomically derived from.
	 * @param tissueSite the site where the specimen is anatomically derived from.
	 * @see #getTissueSite()
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * Returns the bilateral sites, left or right.
	 * @hibernate.property name="tissueSide" type="string" column="TISSUE_SIDE" length="50"
	 * @return the bilateral sites, left or right.
	 * @see #setTissueSide(String)
	 */
	public String getTissueSide()
	{
		return tissueSide;
	}

	/**
	 * Sets the bilateral sites, left or right.
	 * @param tissueSide the bilateral sites, left or right.
	 * @see #getTissueSide()
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
	}

	/**
	 * Returns the type of the specimen.
	 * @hibernate.property name="type" type="string" column="SPECIMEN_TYPE" length="50"
	 * @return the type of the specimen.
	 * @see #setType(String)
	 */
	public String getType()
	{
		return type;
	}

	/**
	 * Sets the type of the specimen.
	 * @param type the type of the specimen.
	 * @see #getType()
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Returns the identifier for the way in which the specimen is collected and processed.
	 * @hibernate.property name="processIdentifier" type="string" column="PROCESS_IDENTIFIER" length="50"
	 * @return the identifier for the way in which the specimen is collected and processed.
	 * @see #setProcessIdentifier(String)
	 */
	public String getProcessIdentifier()
	{
		return processIdentifier;
	}

	/**
	 * Sets the identifier for the way in which the specimen is collected and processed.
	 * @param processIdentifier the identifier for the way in which the specimen is collected and processed.
	 * @see #getProcessIdentifier()
	 */
	public void setProcessIdentifier(String processIdentifier)
	{
		this.processIdentifier = processIdentifier;
	}

	/**
	 * Returns the quantity of specimen received.
	 * @hibernate.property name="quantity" type="double" column="RECEIVED_QUANTITY" length="50"
	 * @return the quantity of specimen received.
	 * @see #setQuantity(Double)
	 */
	public Double getQuantity()
	{
		return quantity;
	}

	/**
	 * Sets the quantity of specimen received.
	 * @param quantity the quantity of specimen received.
	 * @see #getQuantity()
	 */
	public void setQuantity(Double quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * Returns the units of quantity (e.g. ml, g, or no units).
	 * @hibernate.property name="quantityUnit" type="double" column="QUANTITY_UNIT" length="50"
	 * @return the units of quantity (e.g. ml, g, or no units).
	 * @see #setQuantityUnit(String)
	 */
	public String getQuantityUnit()
	{
		return quantityUnit;
	}

	/**
	 * Sets the units of quantity (e.g. ml, g, or no units).
	 * @param quantityUnit the units of quantity (e.g. ml, g, or no units).
	 * @see #getQuantityUnit()
	 */
	public void setQuantityUnit(String quantityUnit)
	{
		this.quantityUnit = quantityUnit;
	}

	/**
	 * Returns the quality of specimen upon receipt.
	 * @hibernate.property name="receivedQuality" type="string" column="RECEIVED_QUALITY" length="50"
	 * @return the quality of specimen upon receipt.
	 * @see #setReceivedQuality(String)
	 */
	public String getReceivedQuality()
	{
		return receivedQuality;
	}

	/**
	 * Sets the quality of specimen upon receipt.
	 * @param receivedQuality the quality of specimen upon receipt.
	 * @see #getReceivedQuality()
	 */
	public void setReceivedQuality(String receivedQuality)
	{
		this.receivedQuality = receivedQuality;
	}

	/**
	 * Returns the reviewed status of the specimen.
	 * @hibernate.property name="receivedQuality" type="boolean" column="REVIEWED"
	 * @return the reviewed status of the specimen.
	 * @see #setReviewed(Boolean)
	 */
	public Boolean getReviewed()
	{
		return reviewed;
	}

	/**
	 * Sets the reviewed status of the specimen.
	 * @param reviewed the reviewed status of the specimen.
	 * @see #getReviewed()
	 */
	public void setReviewed(Boolean reviewed)
	{
		this.reviewed = reviewed;
	}

	/**
	 * Returns the date specimen was reviewed.
	 * @hibernate.property name="reviewedDate" type="date" column="REVIEWED_DATE"
	 * @return the date specimen was reviewed.
	 * @see #setReviewedDate(java.util.Date)
	 */
	public java.util.Date getReviewedDate()
	{
		return reviewedDate;
	}

	/**
	 * Sets the date specimen was reviewed.
	 * @param reviewedDate the date specimen was reviewed.
	 * @see #getReviewedDate()
	 */
	public void setReviewedDate(java.util.Date reviewedDate)
	{
		this.reviewedDate = reviewedDate;
	}

	/**
	 * Returns the technician who reviewed the Biospecimen.
	 * @hibernate.many-to-one column="REVIEWED_BY" class="edu.wustl.catissuecore.domain.User"
	 * constrained="true"
	 * @return the technician who reviewed the Biospecimen.
	 * @see #setReviewedBy(User)
	 */
	public User getReviewedBy()
	{
		return reviewedBy;
	}

	/**
	 * Sets the technician who reviewed the Biospecimen.
	 * @param reviewedBy the technician who reviewed the Biospecimen.
	 * @see #getReviewedBy()
	 */
	public void setReviewedBy(User reviewedBy)
	{
		this.reviewedBy = reviewedBy;
	}

	/**
	 * Returns the percentage neoplastic cellularity of the specimen.
	 * @hibernate.property name="neoplasticCellularityPercentage" type="double"
	 * column="NEOPLASTIC_CELLULARITY_PERCENT" length="50"
	 * @return the percentage neoplastic cellularity of the specimen.
	 * @see #setNeoplasticCellularityPercentage(Double)
	 */
	public Double getNeoplasticCellularityPercentage()
	{
		return neoplasticCellularityPercentage;
	}

	/**
	 * Sets the percentage neoplastic cellularity of the specimen.
	 * @param neoplasticCellularityPercentage the percentage neoplastic cellularity of the specimen.
	 * @see #getNeoplasticCellularityPercentage()
	 */
	public void setNeoplasticCellularityPercentage(Double neoplasticCellularityPercentage)
	{
		this.neoplasticCellularityPercentage = neoplasticCellularityPercentage;
	}

	/**
	 * Returns the percentage necrosis of the specimen.
	 * @hibernate.property name="necrosisPercentage" type="double" column="NECROSIS_PERCENT"
	 * length="50"
	 * @return the percentage necrosis of the specimen.
	 * @see #setNecrosisPercentage(Double)
	 */
	public Double getNecrosisPercentage()
	{
		return necrosisPercentage;
	}

	/**
	 * Sets the percentage necrosis of the specimen.
	 * @param necrosisPercentage the percentage necrosis of the specimen.
	 * @see #getNecrosisPercentage()
	 */
	public void setNecrosisPercentage(Double necrosisPercentage)
	{
		this.necrosisPercentage = necrosisPercentage;
	}

	/**
	 * Returns the percentage lymphocytic cellularity of the specimen.
	 * @hibernate.property name="lymphocyticPercentage" type="double"
	 *                     column="LYMPHOCYTIC_PERCENT" length="50"
	 * @return the percentage lymphocytic cellularity of the specimen.
	 * @see #setLymphocyticPercentage(Double)
	 */
	public Double getLymphocyticPercentage()
	{
		return lymphocyticPercentage;
	}

	/**
	 * Sets the percentage lymphocytic cellularity of the specimen.
	 * @param lymphocyticPercentage the percentage lymphocytic cellularity of the specimen.
	 * @see #getLymphocyticPercentage()
	 */
	public void setLymphocyticPercentage(Double lymphocyticPercentage)
	{
		this.lymphocyticPercentage = lymphocyticPercentage;
	}

	/**
	 * Returns the percentage total cellularity of the specimen.
	 * @hibernate.property name="totalCellularityPercentage" type="double"
	 *                     column="TOTAL_CELLULARITY_PERCENT" length="50"
	 * @return the percentage total cellularity of the specimen.
	 * @see #setTotalCellularityPercentage(Double)
	 */
	public Double getTotalCellularityPercentage()
	{
		return totalCellularityPercentage;
	}

	/**
	 * Sets the percentage total cellularity of the specimen.
	 * @param totalCellularityPercentage the percentage total cellularity of the specimen.
	 * @see #getTotalCellularityPercentage()
	 */
	public void setTotalCellularityPercentage(Double totalCellularityPercentage)
	{
		this.totalCellularityPercentage = totalCellularityPercentage;
	}

	/**
	 * Returns the histological quality of the specimen.
	 * @hibernate.property name="histologicalQuality" type="string" column="HISTOLOGICAL_QUALITY"
	 *                     length="50"
	 * @return the histological quality of the specimen.
	 * @see #setHistologicalQuality(String)
	 */
	public String getHistologicalQuality()
	{
		return histologicalQuality;
	}

	/**
	 * Sets the histological quality of the specimen.
	 * @param histologicalQuality the histological quality of the specimen.
	 * @see #getHistologicalQuality()
	 */
	public void setHistologicalQuality(String histologicalQuality)
	{
		this.histologicalQuality = histologicalQuality;
	}

	/**
	 * Returns the status whether high quality samples have been successfully derived from this specimen?
	 * @hibernate.property name="sampleDerived" type="boolean" column="SAMPLE_DERIVED"
	 * @return the status whether high quality samples have been successfully derived from this specimen?
	 * @see #setSampleDerived(Boolean)
	 */
	public Boolean getSampleDerived()
	{
		return sampleDerived;
	}

	/**
	 * Sets the status whether high quality samples have been successfully derived from this specimen?
	 * @param sampleDerived the status whether high quality samples have been successfully derived from this specimen?
	 * @see #getSampleDerived()
	 */
	public void setSampleDerived(Boolean sampleDerived)
	{
		this.sampleDerived = sampleDerived;
	}

	/**
	 * Returns the status whether this specimen is physically available.
	 * @hibernate.property name="available" type="boolean" column="AVAILABLE" length="50"
	 * @return the status whether this specimen is physically available.
	 * @see #getAvailable()
	 */
	public Boolean getAvailable()
	{
		return available;
	}

	/**
	 * Sets the status whether this specimen is physically available.
	 * @param available the status whether this specimen is physically available.
	 * @see #getAvailable()
	 */
	public void setAvailable(Boolean available)
	{
		this.available = available;
	}

	/**
	 * Returns the available quantity of the specimen.
	 * @hibernate.property name="availableQuantity" type="double" column="AVAILABLE_QUANTITY" 
	 * length="50"
	 * @return the available quantity of the specimen.
	 * @see #setAvailableQuantity(Double)
	 */
	public Double getAvailableQuantity()
	{
		return availableQuantity;
	}

	/**
	 * Sets the available quantity of the specimen.
	 * @param availableQuantity the available quantity of the specimen.
	 * @see #getAvailableQuantity()
	 */
	public void setAvailableQuantity(Double availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}

	/**
	 * Returns the comments on the specimen. 
	 * @hibernate.property name="comments" type="string" column="COMMENTS" length="200"
	 * @return the comments on the specimen.
	 * @see #setComments(String)
	 */
	public String getComments()
	{
		return comments;
	}

	/**
	 * Sets the comments on the specimen.
	 * @param commnets the comments on the specimen.
	 * @see #getComments()
	 */
	public void setComments(String commnets)
	{
		this.comments = commnets;
	}

	/**
	 * Returns the activity status of the Specimen.
	 * @hibernate.many-to-one column="ACTIVITY_STATUS_ID"
	 * class="edu.wustl.catissuecore.domain.ActivityStatus"
	 * constrained="true"
	 * @return Returns the activity status of the Specimen.
	 * @see #setActivityStatus(ActivityStatus)
	 */
	public ActivityStatus getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * Sets the activity status of the Specimen.
	 * @param activityStatus activity status of the Specimen.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(ActivityStatus activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Returns the barcode of the specimen.
	 * @hibernate.property name="barcode" type="string" column="BARCODE" length="50"
	 * @return the barcode of the specimen.
	 * @see #setBarcode(String)
	 */
	public String getBarcode()
	{
		return barcode;
	}

	/**
	 * Sets the barcode of the specimen.
	 * @param barCode the barcode of the specimen.
	 * @see #getBarcode()
	 */
	public void setBarcode(String barCode)
	{
		this.barcode = barCode;
	}

	/**
	 * Returns the accession associated with the specimen.
	 * @hibernate.many-to-one column="ACCESSION_ID"
	 * class="edu.wustl.catissuecore.domain.Accession"
	 * @return the accession associated with the specimen.
	 * @see #setAccession(Accession)
	 */
	public Accession getAccession()
	{
		return accession;
	}

	/**
	 * Sets the accession associated with the specimen.
	 * @param accession the accession associated with the specimen.
	 * @see #getAccession()
	 */
	public void setAccession(Accession accession)
	{
		this.accession = accession;
	}

	/**
	 * Returns the collection of segments derived from this specimen.
	 * @return the collection of segments derived from this specimen.
	 * @hibernate.set name="segmentCollection" table="CATISSUE_SEGMENT"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Segment"
	 * @see #setSegmentCollection(Collection)
	 */
	public java.util.Collection getSegmentCollection()
	{
		return segmentCollection;
	}

	/**
	 * Sets the collection of segments derived from this specimen.
	 * @param segmentCollection the collection of segments derived from this specimen.
	 * @see #getSegmentCollection()
	 */
	public void setSegmentCollection(java.util.Collection segmentCollection)
	{
		this.segmentCollection = segmentCollection;
	}

	/**
	 * Returns the collection of sample derived from this specimen.
	 * @return the collection of sample derived from this specimen.
	 * @hibernate.set name="sampleCollection" table="CATISSUE_SAMPLE"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Sample"
	 * @see #setSampleCollection(Collection)
	 */
	public java.util.Collection getSampleCollection()
	{
		return sampleCollection;
	}

	/**
	 * Sets the collection of sample derived from this specimen.
	 * @param sampleCollection the collection of sample derived from this specimen.
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