/**
 * <p>Title: SpecimenRequirement Class</p>
 * <p>Description:  Required generic attributes for a Specimen associated with a Collection or Distribution Protocol. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author mandar_deshmukh
 * Required generic attributes for a Specimen associated with a Collection or Distribution Protocol.
 * @hibernate.class table="CATISSUE_SPECIMEN_REQUIREMENT"
 */
public class SpecimenRequirement  extends AbstractDomainObject implements java.io.Serializable
{
	
    private static final long serialVersionUID = 1234567890L;
	
	/**
	 * System generated unique id.
	 */
	protected Long id;
	
	/**
	 * Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String specimenType;
	
	/**
	 * Anatomic site from which the specimen was derived.
	 */
	public String tissueSite;
	
	/**
	 * Histopathological character of the specimen e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	protected String pathologyStatus;
	
	/**
	 * Collection of studies associated with the CollectionProtocol.
	 */
	protected Collection distributionProtocolCollection = new HashSet();
	
	/**
	 * Collection of studies associated with the CollectionProtocol.
	 */
	protected Collection collectionProtocolEventCollection = new HashSet();
	
	//Change for API Search   --- Ashwin 04/10/2006
	protected Quantity quantity = new Quantity();
	
	protected String specimenClass; 
	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 **/
	public SpecimenRequirement()
	{
		super();
	}
	
	/**
	 * Returns the id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SPECIMEN_REQ_SEQ"
	 * @return Returns the id.
	 */
	public Long getId()
	{
		return id;
	}
	
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
	
	/**
	 * Returns the specimenType.
	 * @hibernate.property name="specimenType" type="string"
	 * column="SPECIMEN_TYPE" length="50"
	 * @return Returns the specimenType.
	 */
	public String getSpecimenType()
	{
		return specimenType;
	}

	/**
	 * @param specimenType The specimenType to set.
	 */
	public void setSpecimenType(String specimenType)
	{
		this.specimenType = specimenType;
	}

	/**
	 * Returns the tissueSite.
	 * @hibernate.property name="tissueSite" type="string"
	 * column="TISSUE_SITE" length="150"
	 * @return Returns the tissueSite.
	 */
	public String getTissueSite()
	{
		return tissueSite;
	}

	/**
	 * @param tissueSite The tissueSite to set.
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * Returns the pathologyStatus.
	 * @hibernate.property name="pathologyStatus" type="string"
	 * column="PATHOLOGY_STATUS" length="50"
	 * @return Returns the pathologyStatus.
	 */
	public String getPathologyStatus()
	{
		return pathologyStatus;
	}

	/**
	 * @param pathologyStatus 
	 * The pathologyStatus to set.
	 */
	public void setPathologyStatus(String pathologyStatus)
	{
		this.pathologyStatus = pathologyStatus;
	}
	
	/**
	 * Returns the collection of Studies for this Protocol requirement.
	 * @hibernate.set name="distributionProtocolCollection" table="CATISSUE_DISTRIBUTION_SPE_REQ" 
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_REQUIREMENT_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.DistributionProtocol" column="DISTRIBUTION_PROTOCOL_ID"
	 * @return Returns the collection of Studies for this Protocol.
	 */
	public Collection getDistributionProtocolCollection()
	{
		return distributionProtocolCollection;
	}

	/**
	 * @param studyCollection The studyCollection to set.
	 */
	public void setDistributionProtocolCollection(Collection distributionProtocolCollection)
	{
		this.distributionProtocolCollection = distributionProtocolCollection;
	}
	
	/**
	 * @hibernate.set name="collectionProtocolCollection" table="CATISSUE_COLL_SPECIMEN_REQ" 
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_REQUIREMENT_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.CollectionProtocolEvent" column="COLLECTION_PROTOCOL_EVENT_ID"
	 * @return Returns the collection of Studies for this Protocol.
	 */
	public Collection getCollectionProtocolEventCollection() 
	{
		return collectionProtocolEventCollection;
	}
	
	/**
	 * @param collectionProtocolCollection The collectionProtocolCollection to set.
	 */
	public void setCollectionProtocolEventCollection(Collection collectionProtocolEventCollection) 
	{
		this.collectionProtocolEventCollection = collectionProtocolEventCollection;
	}
	
    /**
     * @return Returns the quantity.
     * @hibernate.many-to-one column="QUANTITY_ID" class="edu.wustl.catissuecore.domain.Quantity"
     * constrained="true"
     */
    public Quantity getQuantity()
    {
        return quantity;
    }
    
    /**
     * @param quantity The quantity to set.
     */
    public void setQuantity(Quantity quantity)
    {
        this.quantity = quantity;
    }
    
    /**
     * @return Returns the specimenClass.
     * @hibernate.property name="specimenClass" type="string" column="SPECIMEN_CLASS" length="50"
     */
    public String getSpecimenClass()
    {
        return specimenClass;
    }
    
    /**
     * @param specimenClass The specimenClass to set.
     */
    public void setSpecimenClass(String specimenClass)
    {
        this.specimenClass = specimenClass;
    }
    
	public String toString()
	{
		return "SR "+this.getClass().getName()+" : "+specimenType+" | "+ tissueSite;
	}
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
		//Change for API Search   --- Ashwin 04/10/2006
    	if (SearchUtil.isNullobject(quantity))
    	{
    		quantity = new Quantity();
    	}
	}
	
}