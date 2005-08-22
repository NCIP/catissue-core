/**
 * <p>Title: StorageType Class
 * <p>Description:  Type of the storage container e.g. Freezer, Box etc. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.domain.StorageContainerCapacity;
import edu.wustl.catissuecore.actionForm.StorageTypeForm;
import edu.wustl.common.util.logger.Logger;

/**
 * Type of the storage container e.g. Freezer, Box etc.
 * @hibernate.class table="CATISSUE_STORAGE_TYPE"
 */
public class StorageType extends AbstractDomainObject implements Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * System generated unique systemIdentifier.
     */
	protected Long systemIdentifier;
	
	/**
     * Text name assigned to the container type
     */
	protected String type;
	
	/**
     * Default temperature of current type of storage container.
     */
	protected Double defaultTempratureInCentigrade;
	
	/**
     * Human understandable name assigned to dimension one.
     */
	protected String oneDimensionLabel;
	
	/**
     * Human understandable name assigned to dimension two.
     */
	protected String twoDimensionLabel;

	/**
     * Default capacity of a storage container.
     */
	private StorageContainerCapacity defaultStorageCapacity = new StorageContainerCapacity();
	
	//DO Not delete required by hibernate
	public StorageType()
	{
		
	}
	
	public StorageType(AbstractActionForm abstractForm)
	{
		setAllValues(abstractForm);
	}
	
	/**
     * Returns System generated unique systemIdentifier.
     * @return System generated unique systemIdentifier.
     * @see #setSystemIdentifier(Integer)
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     * */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets system generated unique systemIdentifier.
     * @param systemIdentifier systemIdentifier of the Storagetype to be set.
     * @see #getSystemIdentifier()
     * */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
     * Returns the type of storage container.
     * @hibernate.property name="type" type="string" 
     * column="TYPE" length="50" not-null="true" unique="true"
     * @return The type of storage container.
     * @see #setType(String)
     */
	public String getType()
	{
		return type;
	}

	/**
     * Sets the type of storage container.
     * @param type The type of storage container to be set.
     * @see #getType()
     */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
     * Returns the default temperature of a storage container in centigrade.
     * @hibernate.property name="defaultTempratureInCentigrade" type="double" 
     * column="DEFAULT_TEMPERATURE_IN_CENTIGRADE" length="30"
     * @return The default temperature of a storage container in centigrade.
     * @see #setDefaultTempratureInCentigrade(Double)
     */
	public Double getDefaultTempratureInCentigrade()
	{
		return defaultTempratureInCentigrade;
	}

	/**
     * Sets the default temperature of a storage container in centigrade.
     * @param defaultTempratureInCentigrade temperature of a storage container in centigrade to be set.
     * @see #getDefaultTempratureInCentigrade()
     */
	public void setDefaultTempratureInCentigrade(Double defaultTempratureInCentigrade)
	{
		this.defaultTempratureInCentigrade = defaultTempratureInCentigrade;
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

	/**
	 * Returns the default capacity of a storage container.
	 * @return The default capacity of a storage container.
	 * @hibernate.many-to-one column="STORAGE_CONTAINER_CAPACITY_ID"
	 * class="edu.wustl.catissuecore.domain.StorageContainerCapacity" constrained="true"
	 * @see #setDefaultStorageCapacity(StorageContainerCapacity)
	 */
	public StorageContainerCapacity getDefaultStorageCapacity()
	{
		return defaultStorageCapacity;
	}

	/**
     * Sets the default storage capacity.
     * @param defaultStorageCapacity capacity  of a storage container to be set.
     * @see #getDefaultStorageCapacity
     * ()
     */
	public void setDefaultStorageCapacity(StorageContainerCapacity defaultStorageCapacity)
	{
		this.defaultStorageCapacity = defaultStorageCapacity;
	}
	
	/**
	 * This function Copies the data from a StorageTypeForm object to a StorageType object.
	 * @param storageTypeForm A StorageTypeForm object containing the information about the StorageType.  
	 * */
	public void setAllValues(AbstractActionForm abstractForm)
	{
	    try
	    {
	        StorageTypeForm storageTypeForm = (StorageTypeForm) abstractForm;
	        
	        this.systemIdentifier = new Long(storageTypeForm.getSystemIdentifier());
	        this.type = storageTypeForm.getType();
	        this.defaultTempratureInCentigrade = new Double(storageTypeForm.getDefaultTemperature());
	        this.oneDimensionLabel = storageTypeForm.getOneDimensionLabel();
	        this.twoDimensionLabel = storageTypeForm.getTwoDimensionLabel();
	        
	        defaultStorageCapacity.setSystemIdentifier(systemIdentifier);
	        defaultStorageCapacity.setOneDimensionCapacity(new Integer(storageTypeForm.getOneDimensionCapacity()));
	        defaultStorageCapacity.setTwoDimensionCapacity(new Integer(storageTypeForm.getTwoDimensionCapacity()));
	    }
	    catch(Exception excp)
	    {
	        Logger.out.error(excp.getMessage());
	    }
	}
}