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

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Attributes of a Storage Container in the form of key-value pair.
 * @hibernate.class table="CATISSUE_STORAGE_CONT_DETAILS"
 * @author Aniruddha Phadnis
 */
public class StorageContainerDetails extends AbstractDomainObject implements java.io.Serializable
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
	
	public StorageContainerDetails()
	{
		
	}
	
	public StorageContainerDetails(StorageContainerDetails storageContainerDetails)
	{
		parameterName = storageContainerDetails.parameterName;
		parameterValue = storageContainerDetails.parameterValue;
	}
	
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
     * column="PARAMETER_NAME" length="50"
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
     * @hibernate.many-to-one column="STORAGE_CONTAINER_ID"  class="edu.wustl.catissuecore.domain.StorageContainer" constrained="true"
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

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
		
	}
}