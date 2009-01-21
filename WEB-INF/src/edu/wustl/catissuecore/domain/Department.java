/**
 * <p>Title: Department Class</p>
 * <p>Description: A department to which a User belongs to.  </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;


/**
 * A department to which a User belongs to.
 *  * @hibernate.class table="CATISSUE_DEPARTMENT"
 */

public class Department extends AbstractDomainObject implements java.io.Serializable
{
	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * Name of the department.
	 */
	protected String name;

	/**
	 * Default Constructor.
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 */
	public Department()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm.
	 */
	public Department(AbstractActionForm form)
	{
		super();
		setAllValues(form);
	}

	/**
	 * Returns the id assigned to department.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_DEPARTMENT_SEQ"
	 * @return a unique id assigned to the department.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets an id for the department.
	 * @param identifier Unique id to be assigned to the department.
	 */
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the name of the department.
	 * @hibernate.property name="name" type="string" column="NAME" length="255"
	 * not-null="true" unique="true"
	 * @return name of the department.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of the department.
	 * @param name Name of the department.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(
	 * edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	/**
	 * Set All Values.
	 * @param abstractForm IValueObject.
	 */
	public void setAllValues(IValueObject abstractForm)
	{
		DepartmentForm departmentForm = (DepartmentForm) abstractForm;
		this.name = departmentForm.getName().trim();
	}

	/**
	* Returns message label to display on success add or edit.
	* @return String
	*/
	public String getMessageLabel()
	{
		return this.name;
	}
}