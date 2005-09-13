/**
 * <p>Title: StorageContainerCapacity Class
 * <p>Description:  Capacity defined for a storage container. </p>
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
 * Capacity defined for a storage container.
 * @hibernate.class table="CATISSUE_STORAGE_CONTAINER_CAPACITY"
 * @author Aniruddha Phadnis
 */
public class StorageContainerCapacity extends AbstractDomainObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * System generated unique systemIdentifier.
     */
	protected Long systemIdentifier;
	
	/**
     * Number of objects that can be stored in dimension one.
     */
	protected Integer oneDimensionCapacity;
	
	/**
     * Number of objects that can be stored in dimension two.
     */
	protected Integer twoDimensionCapacity;

	/**
     * Returns System generated unique systemIdentifier.
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
     * Sets unique systemIdentifier.
     * @param systemIdentifier Identifier to be set.
     * @see #getSystemIdentifier()
     */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
     * Returns number of objects that can be stored in dimension one.
     * @return Number of objects that can be stored in dimension one.
     * @see #setOneDimensionCapacity(Integer)
     * @hibernate.property name="oneDimensionCapacity" type="int" 
     * column="ONE_DIMENSION_CAPACITY" length="30"
     */
	public Integer getOneDimensionCapacity()
	{
		return oneDimensionCapacity;
	}

	/**
     * Sets number of objects that can be stored in dimension one.
     * @param oneDimensionCapacity number of objects that can be stored in dimension one.
     * @see #getOneDimensionCapacity()
     */
	public void setOneDimensionCapacity(Integer oneDimensionCapacity)
	{
		this.oneDimensionCapacity = oneDimensionCapacity;
	}

	/**
     * Returns number of objects that can be stored in dimension two.
     * @return Number of objects that can be stored in dimension two.
     * @see #setTwoDimensionCapacity(Integer)
     * @hibernate.property name="twoDimensionCapacity" type="int" 
     * column="TWO_DIMENSION_CAPACITY" length="30"
     */
	public Integer getTwoDimensionCapacity()
	{
		return twoDimensionCapacity;
	}

	/**
     * Sets number of objects that can be stored in dimension two.
     * @param oneDimensionCapacity number of objects that can be stored in dimension two.
     * @see #getTwoDimensionCapacity()
     */
	public void setTwoDimensionCapacity(Integer twoDimensionCapacity)
	{
		this.twoDimensionCapacity = twoDimensionCapacity;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
		
	}
}