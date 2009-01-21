/**
 * <p>Title: SpecimenRequirement Class</p>
 * <p>Description:  Required generic attributes for a Specimen associated with a
 * Collection or Distribution Protocol. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author mandar_deshmukh
 * Required generic attributes for a Specimen associated with a Collection or Distribution Protocol.
 * @hibernate.class table="CATISSUE_SPECIMEN_REQUIREMENT"
 */
public class DistributionSpecimenRequirement extends AbstractDomainObject implements java.io.Serializable
{
	/**
	 * Serial Version ID.
	 */
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
	 * Histopathological character of the specimen e.g. Non-Malignant, Malignant,
	 * Non-Malignant Diseased, Pre-Malignant.
	 */
	protected String pathologyStatus;

	/**
	 * Patch Id : Collection_Event_Protocol_Order_7 (Changed From HashSet to LinkedHashSet)
	 * Description : To get the specimen requirement in order
	 */
	/**
	 * Collection of studies associated with the CollectionProtocol.
	 */
	protected DistributionProtocol distributionProtocol = new DistributionProtocol();

	/**
	 * quantity.
	 */
	protected Double quantity = new Double(0);

	/**
	 * specimenClass.
	 */
	protected String specimenClass;

	/**
	 * Default Constructor.
	 * NOTE: Do not delete this constructor. Hibernate uses this by reflection API.
	 */
	public DistributionSpecimenRequirement()
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
	 * @param identifier The id to set.
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
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
	 * @param pathologyStatus String.
	 * The pathologyStatus to set.
	 */
	public void setPathologyStatus(String pathologyStatus)
	{
		this.pathologyStatus = pathologyStatus;
	}

	/**
	 * Returns the collection of Studies for this Protocol requirement.
	 * @hibernate.collection-many-to-one class="edu.wustl.catissuecore.domain.
	 * DistributionProtocol" column="DISTRIBUTION_PROTOCOL_ID"
	 * @return Returns the distribution protocol for this Specimen Requirement.
	 */
	public DistributionProtocol getDistributionProtocol()
	{
		return distributionProtocol;
	}

	/**
	 * @param distributionProtocol The distribution protocol to set.
	 */
	public void setDistributionProtocol(DistributionProtocol distributionProtocol)
	{
		this.distributionProtocol = distributionProtocol;
	}

	/**
	 * @return Returns the quantity.
	 * @hibernate.property name="quantity" type="double"
	 * column="QUANTITY"
	 */
	public Double getQuantity()
	{
		return quantity;
	}

	/**
	 * @param quantity The quantity to set.
	 */
	public void setQuantity(Double quantity)
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

	/**
	 * To String.
	 * @return String.
	 */
	public String toString()
	{
		return "DSR " + this.getClass().getName() + " : " + specimenType + " | " + tissueSite;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.
	 * wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	/**
	 * Set All Values.
	 * @param abstractForm IValueObject.
	 * @throws AssignDataException AssignDataException.
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		//Change for API Search   --- Ashwin 04/10/2006
		if (SearchUtil.isNullobject(quantity))
		{
			quantity = new Double(0);
		}
	}
}