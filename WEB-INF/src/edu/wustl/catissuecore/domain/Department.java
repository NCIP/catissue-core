/**
 * <p>Title: Department Class>
 * <p>Description:  Models the Department information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;


/**
 * Models the Department information.
 * @hibernate.class table="CATISSUE_DEPARTMENT"
 */
public class Department extends AbstractDomainObject
{
	/**
	 * id used by hibernate for as unique identifier
	 * */
	protected Long identifier;
	
	/**
	 * A string containing the name of the department.
	 */
	protected String name = "";
	
	/**
	 * Initialize a new department instance. 
	 * Note: Invoked by hibernate through reflection API.  
	 */
	public Department()
	{
	}
	
	/**
	 * Returns the identifier assigned to department.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return returns a unique identifier assigned to the department.
     * @see #setIdentifier(int)
	 * */
	public Long getIdentifier()
	{
		return identifier;
	}
	
	/**
	 * Sets an identifier for the department.
	 * @param identifier Unique identifier to be assigned to the department.
	 * @see #getIdentifier()
	 * */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}
	
	/**
	 * Returns the name of the department.
	 * @hibernate.property name="name" type="string" 
     * column="NAME" length="50" not-null="true"
	 * @return Returns the name of the department. 
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Sets the name of the department.
	 * @param name Name of the department.
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}