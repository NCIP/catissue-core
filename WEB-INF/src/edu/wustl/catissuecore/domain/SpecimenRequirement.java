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

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.exception.AssignDataException;

/**
 * @author mandar_deshmukh
 * Required generic attributes for a Specimen associated with a Collection or Distribution Protocol.
 * @hibernate.class table="CATISSUE_SPECIMEN_REQUIREMENT"
 * @hibernate.discriminator column="SPECIMEN_CLASS" 
 */
public abstract class SpecimenRequirement  extends AbstractDomainObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier;
	
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
	
	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 **/
	public SpecimenRequirement()
	{
	}
	
	/**
	 * Returns the systemIdentifier.
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @return Returns the systemIdentifier.
	 */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * @param systemIdentifier The systemIdentifier to set.
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
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
	 * @hibernate.set name="distributionProtocolCollection" table="CATISSUE_DISTRIBUTION_SPECIMEN_REQUIREMENT" 
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
	 * @hibernate.set name="collectionProtocolCollection" table="CATISSUE_COLLECTION_SPECIMEN_REQUIREMENT" 
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
	
	public String toString()
	{
		return "SR "+this.getClass().getName()+" : "+specimenType+" | "+ tissueSite;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
	}
}