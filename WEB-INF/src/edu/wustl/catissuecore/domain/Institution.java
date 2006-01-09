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
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier;
	
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
	 * Returns the unique systemIdentifier assigned to institution.
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_INSTITUTION_SEQ"
	 * @return A unique systemIdentifier assigned to the institution.
	 * @see #setIdentifier(int)
	 * */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * Sets an systemIdentifier for the institution.
	 * @param systemIdentifier Unique systemIdentifier to be assigned to the institution.
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
	
	
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {
		InstitutionForm instituteForm = (InstitutionForm)abstractForm;
		
		this.name = instituteForm.getName().trim() ;
    }
}