/**
 * <p>Title: Institution Class</p>
 * <p>Description:  An institution to which a user belongs to.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.InstitutionForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * An institution to which a user belongs to.
 * @hibernate.class table="CATISSUE_INSTITUTION"
 */
public class Institution extends AbstractDomainObject implements Serializable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * System generated unique id.
	 */
	protected Long id;
	
	/**
	 * Name of the Institution.
	 */
	protected String name;

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 * */
	public Institution()
	{
		
	}
	
	public Institution(AbstractActionForm form)
	{
		setAllValues(form);
	}
	
	/**
	 * Returns the unique id assigned to institution.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_INSTITUTION_SEQ"
	 * @return A unique id assigned to the institution.
	 * @see #setIdentifier(int)
	 * */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets an id for the institution.
	 * @param id Unique id to be assigned to the institution.
	 * @see #getIdentifier()
	 * */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Returns the name of the institution.
	 * @hibernate.property name="name" type="string" 
	 * column="NAME" length="255" not-null="true" unique="true"
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
	
	
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {
		InstitutionForm instituteForm = (InstitutionForm)abstractForm;
		
		this.name = instituteForm.getName().trim() ;
    }
    
    /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel() {		
		return this.name;
	}
}