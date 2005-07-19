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
import edu.wustl.catissuecore.domain.StorageContainerCapacity;

/**
 * Type of the storage container e.g. Freezer, Box etc.
 * @hibernate.class table="CATISSUE_STORAGE_TYPE"
 */
public class StorageType implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * System generated unique identifier.
     */
	protected Integer systemIdentifier;
	
	/**
     * Text name assigned to the container type
     */
	protected String type;
	
	/**
     * Default temperature of current type of storage container.
     */
	protected Double defaultTempratureInCentigrade;

	/**
     * Default capacity of a storage container.
     */
	private StorageContainerCapacity defaultStorageCapacity;
	
	/**
     * Returns System generated unique identifier.
     * @return Integer System generated unique identifier.
     * @see #setSystemIdentifier(Integer)
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="int" length="30"
     * unsaved-value="null" generator-class="native" 
     * */
	public Integer getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets system generated unique identifier.
     * @param systemIdentifier identifier of the Storagetype to be set.
     * @see #getSystemIdentifier()
     * */
	public void setSystemIdentifier(Integer systemIdentifier)
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
}