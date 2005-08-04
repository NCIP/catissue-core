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

import java.io.Serializable;

/**
 * Capacity defined for a storage container.
 * @hibernate.class table="CATISSUE_STORAGE_CONTAINER_CAPACITY"
 * @author Aniruddha Phadnis
 * TODO Need to shift oneDimensionLabel and twoDimensionLabel to StorageType class
 */
public class StorageContainerCapacity implements Serializable
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
     * Human understandable name assigned to dimension one.
     */
	protected String oneDimensionLabel;
	
	/**
     * Human understandable name assigned to dimension two.
     */
	protected String twoDimensionLabel;

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

	/**
     * Returns human understandable name assigned to dimension one.
     * @return Human understandable name assigned to dimension one.
     * @see #setOneDimensionLabel(String)
     * @hibernate.property name="oneDimensionLabel" type="string" 
     * column="ONE_DIMENSION_LABEL" length="50"
     */
	public String getOneDimensionLabel()
	{
		return oneDimensionLabel;
	}

	/**
     * Sets human understandable name assigned to dimension one.
     * @param oneDimensionLabel human understandable name assigned to dimension one.
     * @see #getOneDimensionLabel()
     */
	public void setOneDimensionLabel(String oneDimensionLabel)
	{
		this.oneDimensionLabel = oneDimensionLabel;
	}

	/**
     * Returns human understandable name assigned to dimension two.
     * @return Human understandable name assigned to dimension two.
     * @see #setTwoDimensionLabel(String)
     * @hibernate.property name="twoDimensionLabel" type="string" 
     * column="TWO_DIMENSION_LABEL" length="50"
     */
	public String getTwoDimensionLabel()
	{
		return twoDimensionLabel;
	}

	/**
     * Sets human understandable name assigned to dimension two.
     * @param oneDimensionLabel human understandable name assigned to dimension two.
     * @see #getTwoDimensionLabel()
     */
	public void setTwoDimensionLabel(String twoDimensionLabel)
	{
		this.twoDimensionLabel = twoDimensionLabel;
	}
}