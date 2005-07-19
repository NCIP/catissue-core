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

/**
 * @author mandar_deshmukh
 * Required generic attributes for a Specimen associated with a Collection or Distribution Protocol.
 * @hibernate.class table="CATISSUE_SPECIMEN_REQUIREMENT"
 */
public abstract class SpecimenRequirement implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique identifier.
	 */
	protected Long systemIdentifier;
	
	/**
	 * Type of specimen. e.g. Serum, Plasma, Blood, Fresh Tissue etc.
	 */
	protected String specimenType;
	
	/**
	 * Anatomic site from which the specimen was derived.
	 */
	protected String tissueSite;
	
	/**
	 * For bilateral sites, left or right.
	 */
	protected String tissueSide;
	
	/**
	 * Histopathological character of the specimen e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 */
	protected String pathologyStatus;

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
	 * column="TISSUE_SITE" length="50"
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
	 * Returns the tissueSide.
	 * @hibernate.property name="tissueSide" type="string"
	 * column="TISSUE_SIDE" length="50"
	 * @return Returns the tissueSide.
	 */
	public String getTissueSide()
	{
		return tissueSide;
	}

	/**
	 * @param tissueSide The tissueSide to set.
	 */
	public void setTissueSide(String tissueSide)
	{
		this.tissueSide = tissueSide;
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
}