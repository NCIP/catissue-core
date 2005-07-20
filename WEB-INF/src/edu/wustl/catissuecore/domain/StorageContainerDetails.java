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
	protected String key;
	
	/**
     * Value assigned to the parameter.
     */
	protected String value;
	
	/**
     * Type of the defined key. It could be user defined, pre defined, CDE.
     */
	protected String type;

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
     * @see #setKey(String)
     * @hibernate.property name="key" type="string" 
     * column="KEY" length="50"
     */
	public String getKey()
	{
		return key;
	}

	/**
     * Sets the name of parameter.
     * @param key the name of parameter.
     * @see #getKey()
     */
	public void setKey(String key)
	{
		this.key = key;
	}

	/**
     * Returns the value of parameter. 
     * @return The value of parameter.
     * @see #setValue(String)
     * @hibernate.property name="value" type="string" 
     * column="VALUE" length="50"
     */
	public String getValue()
	{
		return value;
	}

	/**
     * Sets the value of parameter.
     * @param value the value of parameter.
     * @see #getValue()
     */
	public void setValue(String value)
	{
		this.value = value;
	}
}