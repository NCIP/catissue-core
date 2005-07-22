/**
 * <p>Title: Department Class</p>
 * <p>Description: A department to which a User belongs to.  </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
/**
 * A department to which a User belongs to.
 *  * @hibernate.class table="CATISSUE_DEPARTMENT"
 */
public class Department extends AbstractDomainObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique systemIdentifier.
	 */
	protected Long systemIdentifier;

	/**
	 * Name of the department.
	 */
	protected String name;

	/**
	 * Returns the systemIdentifier assigned to department.
	 * 
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @return a unique systemIdentifier assigned to the department.
	 */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * Sets an systemIdentifier for the department.
	 * 
	 * @param systemIdentifier Unique systemIdentifier to be assigned to the department.
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
	 * Returns the name of the department.
	 * 
	 * @hibernate.property name="name" type="string" column="NAME" length="50"
	 * not-null="true" unique="true"
	 * @return name of the department.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of the department.
	 * 
	 * @param name Name of the department.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) {
		// TODO Auto-generated method stub

	}
}