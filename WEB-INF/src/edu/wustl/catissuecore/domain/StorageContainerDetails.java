/**
 * <p>Title: StorageContainerDetails Class>
 * <p>Description:  Attributes of a Storage Container in the form of key-value pair </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Apr 7, 2005
 */
package edu.wustl.catissuecore.domain;

/**
 * Attributes of a Storage Container in the form of key-value pair.
 * @hibernate.class table="CATISSUE_STORAGE_CONTAINER_DETAILS"
 * @author Aniruddha Phadnis
 */
public class StorageContainerDetails implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
     * System generated unique identifier.
     */
	protected Long systemIdentifier;
	
	/**
     * Name of parameter.
     */
	protected String parameterName;
	
	/**
     * Value assigned to the parameter.
     */
	protected String parameterValue;
	
	protected StorageContainer storageContainer; 
	/**
     * Returns System generated unique identifier.
     * @return Long System generated unique identifier.
     * @see #setSystemIdentifier(Long)
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets unique identifier.
     * @param systemIdentifier Identifier to be set.
     * @see #getSystemIdentifier()
     */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
     * Returns the name of parameter. 
     * @return Name of parameter.
     * @see #setParameterName(String)
     * @hibernate.property name="parameterName" type="string" 
     * column="ARAMETER_NAME" length="50"
     */
	public String getParameterName()
	{
		return parameterName;
	}

	/**
     * Sets the name of parameter.
     * @param parameterName the name of parameter.
     * @see #getParameterName()
     */
	public void setParameterName(String parameterName)
	{
		this.parameterName = parameterName;
	}

	/**
     * Returns the value of parameter. 
     * @return The value of parameter.
     * @see #setValue(String)
     * @hibernate.property name="parameterValue" type="string" 
     * column="VALUE" length="50"
     */
	public String getParameterValue()
	{
		return parameterValue;
	}

	/**
     * Sets the value of parameter.
     * @param value the value of parameter.
     * @see #getValue()
     */
	public void setParameterValue(String value)
	{
		this.parameterValue = value;
	}
	
	/**
	 * @return Returns the storageContainer.
	 */
	public StorageContainer getStorageContainer()
	{
		return storageContainer;
	}
	/**
	 * @param storageContainer The storageContainer to set.
	 */
	public void setStorageContainer(StorageContainer storageContainer)
	{
		this.storageContainer = storageContainer;
	}
}