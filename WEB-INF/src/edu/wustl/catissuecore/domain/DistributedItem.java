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

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.exception.AssignDataException;

/**
 * A specimen that is distributed.
 * @hibernate.class table="CATISSUE_DISTRIBUTED_ITEM"
 * @author Aniruddha Phadnis
 */
public class DistributedItem extends AbstractDomainObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * System generated unique systemIdentifier.
     */
	protected Long systemIdentifier;
	
	/**
     * Amount distributed.
     */
	protected Double quantity;
	
	/**
     * A single unit of tissue, body fluid, or derivative biological macromolecule that is 
     * collected or created from a Participant
     */
	protected Specimen specimen = new Specimen();
	
	/**
     * An event that results in transfer of a specimen from a Repository to a Laboratory.
     */
	protected Distribution distribution = new Distribution();

	/**
     * Returns the system generated unique systemIdentifier.
     * @return Long System generated unique systemIdentifier.
     * @see #setSystemIdentifier(Long)
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets the system generated unique systemIdentifier.
     * @param systemIdentifier the system generated unique systemIdentifier.
     * @see #getSystemIdentifier()
     */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
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
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
				
	}
	
	public String toString()
	{
		return systemIdentifier+" "+quantity+" "+specimen.getSystemIdentifier();		
	}
}