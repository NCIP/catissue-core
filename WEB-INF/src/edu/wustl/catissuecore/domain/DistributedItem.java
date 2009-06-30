/**
 * <p>Title: DistributedItem Class>
 * <p>Description:  A specimen that is distributed. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * A specimen that is distributed.
 * @hibernate.class table="CATISSUE_DISTRIBUTED_ITEM"
 * @author Aniruddha Phadnis
 */
public class DistributedItem extends AbstractDomainObject implements java.io.Serializable
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
	 * Amount distributed.
	 */
	protected Double quantity;

	/**
	 * A single unit of tissue, body fluid, or derivative biological
	 * macromolecule that is collected or created from a Participant.
	 */
	protected Specimen specimen;

	// Change for API Search   --- Ashwin 04/10/2006
	/**
	 * An event that results in transfer of a specimen from a Repository to a Laboratory.
	 */
	protected Distribution distribution;

	/**
	 * Double.
	 */
	transient private Double previousQuantity;

	/**
	 * SpecimenArray.
	 */
	protected SpecimenArray specimenArray;

	/**
	 * This string will have the status Complete/View depending upon
	 * the Admin Verification.
	 */
	protected String verificationKey = "View";

	/**
	 * @hibernate.many-to-one column="SPECIMEN_ARRAY_ID" cascade="none"
	 * class="edu.wustl.catissuecore.domain.SpecimenArray" contrained="false"
	 * @return the specimenArray
	 */
	public SpecimenArray getSpecimenArray()
	{
		return specimenArray;
	}

	/**
	 * @param specimenArray the specimenArray to set
	 */
	public void setSpecimenArray(SpecimenArray specimenArray)
	{
		this.specimenArray = specimenArray;
	}

	/**
	 * Returns the system generated unique id.
	 * @return Long System generated unique id.
	 * @see #setId(Long)
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_DISTRIBUTED_ITEM_SEQ"
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets the system generated unique id.
	 * @param identifier the system generated unique id.
	 * @see #getId()
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the amount distributed.
	 * @return The amount distributed.
	 * @see #setQuantity(Double)
	 * @hibernate.property name="quantity" type="double"
	 * column="QUANTITY" length="30"
	 */
	public Double getQuantity()
	{
		return quantity;
	}

	/**
	 * Sets the amount to be distributed.
	 * @param quantity the amount to be distributed.
	 * @see #getQuantity()
	 */
	public void setQuantity(Double quantity)
	{
		this.quantity = quantity;
	}

	/**
	 * Returns the specimen.
	 * @hibernate.many-to-one column="SPECIMEN_ID"
	 * class="edu.wustl.catissuecore.domain.Specimen" constrained="true"
	 * @return the specimen of the distributed item.
	 * @see #setSpecimen(Specimen)
	 */
	public Specimen getSpecimen()
	{
		return specimen;
	}

	/**
	 * Sets the specimen of this distributed item.
	 * @param specimen the specimen of this distributed item.
	 * @see #getSpecimen()
	 */
	public void setSpecimen(Specimen specimen)
	{
		this.specimen = specimen;
	}

	/**
	 * Returns the distribution.
	 * @hibernate.many-to-one column="DISTRIBUTION_ID"
	 * class="edu.wustl.catissuecore.domain.Distribution" constrained="true"
	 * @return The distribution of this distributed item.
	 * @see #setSpecimen(Specimen)
	 */
	public edu.wustl.catissuecore.domain.Distribution getDistribution()
	{
		return distribution;
	}

	/**
	 * Sets the distribution of this distributed item.
	 * @param distribution distribution of this distributed item.
	 * @see #getDistribution()
	 */
	public void setDistribution(edu.wustl.catissuecore.domain.Distribution distribution)
	{
		this.distribution = distribution;
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
		if (SearchUtil.isNullobject(specimen))
		{
			specimen = new Specimen();
		}

		if (SearchUtil.isNullobject(distribution))
		{
			distribution = new Distribution();
		}
	}

	/**
	 * To String.
	 * @return String.
	 */
	public String toString()
	{
		StringBuffer strVal = new StringBuffer();
		strVal.append(id).append(" ").append(quantity).append(" ");

		if (specimen != null && specimen.getId() != null)
		{
			strVal.append(specimen.getId());
		}

		if (specimenArray != null && specimenArray.getId() != null)
		{
			strVal.append(specimenArray.getId());
		}

		return strVal.toString();
	}

	/**
	 * @return Returns the previousQuantity.
	 */
	public Double getPreviousQuantity()
	{
		return previousQuantity;
	}

	/**
	 * @param previousQuantity The previousQuantity to set.
	 */
	public void setPreviousQuantity(Double previousQuantity)
	{
		this.previousQuantity = previousQuantity;
	}

	/**
	 * @return Returns the verificationKey Status view/complete
	 */
	public String getVerificationKey()
	{
		return verificationKey;
	}

	/**
	 * @param verificationKey Returns the verificationKey Status view/complete
	 */
	public void setVerificationKey(String verificationKey)
	{
		this.verificationKey = verificationKey;
	}
}