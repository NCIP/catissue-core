/**
 * <p>Title: Capacity Class>
 * <p>Description:  Capacity class represents the capacity of the container.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Capacity class represents the capacity of the container.
 * @author gautam_shetty
 * @hibernate.class table="CATISSUE_CAPACITY"
 */
public class Capacity extends AbstractDomainObject
{
    
	protected Long id;
	
	protected Integer oneDimensionCapacity;
	
	protected Integer twoDimensionCapacity;
	
	public Capacity()
	{

	}
	
	public Capacity(Capacity storageContainerCapacity)
	{
		oneDimensionCapacity = storageContainerCapacity.oneDimensionCapacity;
		twoDimensionCapacity = storageContainerCapacity.twoDimensionCapacity;
	}
	
	/**
     * @see edu.wustl.common.domain.AbstractDomainObject#getId()
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_CAPACITY_SEQ"
     */
    public Long getId()
    {
        return this.id;
    }
	
	/**
     * (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setId(java.lang.Long)
     */
    public void setId(Long id)
    {
        this.id = id;
    }
	
    /**
     * @return Returns the oneDimensionCapacity.
     * @hibernate.property name="oneDimensionCapacity" type="int" 
     * column="ONE_DIMENSION_CAPACITY" length="30"
     */
    public Integer getOneDimensionCapacity()
    {
        return oneDimensionCapacity;
    }
    
    /**
     * @param oneDimensionCapacity The oneDimensionCapacity to set.
     */
    public void setOneDimensionCapacity(Integer oneDimensionCapacity)
    {
        this.oneDimensionCapacity = oneDimensionCapacity;
    }

    /**
     * @return Returns the twoDimensionCapacity.
     * @hibernate.property name="twoDimensionCapacity" type="int" 
     * column="TWO_DIMENSION_CAPACITY" length="30"
     */
    public Integer getTwoDimensionCapacity()
    {
        return twoDimensionCapacity;
    }
    
    /**
     * @param twoDimensionCapacity The twoDimensionCapacity to set.
     */
    public void setTwoDimensionCapacity(Integer twoDimensionCapacity)
    {
        this.twoDimensionCapacity = twoDimensionCapacity;
    }
    
    /** (non-Javadoc)
     * @see edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm arg0)
            throws AssignDataException
    {
    }
}