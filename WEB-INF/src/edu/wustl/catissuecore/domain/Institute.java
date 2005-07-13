/**
 * <p>Title: Institute Class>
 * <p>Description:  Models the Institute information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Kapil Kaveeshwar
 * @version 1.00
 */
package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.InstituteForm;
import edu.wustl.common.util.logger.Logger;

/**
 * @hibernate.class table="CATISSUE_INSTITUTE"
 */
public class Institute extends AbstractDomainObject
{
	/**
	 * id used by hibernate for as unique identifier
	 * */
	protected Long identifier;
	
	/**
	 * A string containing the name of the institute.
	 */
	protected String name = "";
	
	/**
	 * Type of the institute.
	 */
	protected String type = "";
	
	/**
	 * Address of institute.
	 * */
	protected Address address = new Address();
	
	/**
	 * Initialize a new institute instance. 
	 * Note: Invoked by hibernate through reflection API.  
	 */
	public Institute()
	{
		
	}
	
	public Institute(InstituteForm form)
	{
		setAllValues(form);
	}
	
	/**
	 * Returns the unique identifier assigned to institute.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return returns a unique identifier assigned to the institute.
     * @see #setIdentifier(int)
	 * */
	public Long getIdentifier()
	{
		return identifier;
	}
	
	/**
	 * Sets an identifier for the institute.
	 * @param identifier Unique identifier to be assigned to the institute.
	 * @see #getIdentifier()
	 * */
	public void setIdentifier(Long identifier)
	{
		this.identifier = identifier;
	}
	
	/**
	 * Returns the name of the institute.
	 * @hibernate.property name="name" type="string" 
     * column="NAME" length="50" not-null="true"
	 * @return Returns the name of the institute. 
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Sets the name of the institute.
	 * @param name Name of the institute.
	 * @see #getName()
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	/**
	 * Returns the type of the institute.
	 * @hibernate.property name="type" type="string" 
     * column="TYPE" length="50"
	 * @return Returns the type of the institute. 
	 * @see #setType(String)
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * Sets the type of the institute.
	 * @param type Type of the institute.
	 * @see #getType()
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * Returns the address of the institute.
	 * @hibernate.many-to-one column="ADDRESS_ID" 
	 * class="edu.wustl.catissuecore.domain.Address" constrained="true"
	 * @return Returns the Address of the institute.
	 * @see #setAddress(Address)
	 */
	public Address getAddress()
	{
		return address;
	}
	/**
	 * Sets the address of the institute.
	 * @param address Address of the institute.
	 * @see #getAddress()
	 */
	public void setAddress(Address address)
	{
		this.address = address;
	}
	
	
	/**
     * This function Copies the data from an UserForm object to a User object.
     * @param user An UserForm object containing the information about the user.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
            InstituteForm iForm = (InstituteForm) abstractForm;
            this.identifier = new Long(iForm.getIdentifier());
            this.name = iForm.getName();
            this.type = iForm.getType();
            
            address.setStreet(iForm.getStreet());
            address.setCity(iForm.getCity());
            address.setState(iForm.getState());
            address.setCountry(iForm.getCountry());
            address.setZip(iForm.getZip());
            address.setPhone(iForm.getPhone());
            address.setFax(iForm.getFax());
	        
        }
        catch (Exception excp)
        {
            Logger.out.error(excp.getMessage());
        }
    }
}