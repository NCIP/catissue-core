/**
 * <p>Title: Roles Class>
 * <p>Description:  Models the roles that are defined in caTISSUE Core system</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author kapil_kaveeshwar
 * @version 1.00
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;




/**
 * Models the Roles that can be defined in caTISSUE Core system
 * @hibernate.class table="CATISSUE_ROLE"
 * @author aarti_sharma
 * 
 */
public class Role extends AbstractDomainObject
{
	/**
	 * id used by hibernate for as unique identifier
	 * */
	protected Long identifier;
	
	/**
	 * Name of the Role
	 */
	private String name;
	
	/**
	 * Initialize a new Role instance. 
	 * Note: Hibernate invokes this constructor through reflection API.  
	 */
	public Role() 
	{
		
	}
	public Role(String name)
	{
	    this.name = name;
	}
	/**
	 * Returns the unique identifier assigned to role.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native" 
     * @return returns a unique identifier assigned to the role.
     * @see #setIdentifier(int)
	 * */
	public Long getIdentifier()
	{
		return (this.identifier);
	}
	
	/**
	 * Sets an identifier for the role.
	 * @param identifier identifier for the role.
	 * @see #getIdentifier()
	 * */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}
	
	/**
	 * Returns the name of the role.
	 * @hibernate.property name="name" type="string" 
     * column="NAME" length="50" not-null="true" unique="true"
	 * @return Returns the name of the role. 
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Sets the name of the role.
	 * @param name Name of the role.
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	

	public int hashCode()
	{
	    return 1;
	}
	
	
	public boolean equals(Object obj)
	{
	    if(obj instanceof Role)
	    {
	        Role role = (Role)obj;
	        if(role.getName().equals(name))           	
	            return true;
	    }
	    return false;
	}
	
	
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}