/**
 * <p>Title: Institution Class</p>
 * <p>Description:  An institution to which a user belongs to.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

/**
 * An institution to which a user belongs to.
 * @hibernate.class table="CATISSUE_INSTITUTION"
 */
public class Institution implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * System generated unique identifier.
	 */
	protected Long systemIdentifier;
	
	/**
	 * Name of the Institution.
	 */
	protected String name;

	/**
	 * Returns the unique identifier assigned to institution.
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @return A unique identifier assigned to the institution.
	 * @see #setIdentifier(int)
	 * */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * Sets an identifier for the institution.
	 * @param identifier Unique identifier to be assigned to the institution.
	 * @see #getIdentifier()
	 * */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
	 * Returns the name of the institution.
	 * @hibernate.property name="name" type="string" 
	 * column="NAME" length="50" not-null="true" unique="true"
	 * @return Returns the name of the institution. 
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of the institution.
	 * @param name Name of the institution.
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}