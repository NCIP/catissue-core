/**
 * <p>Title: CancerResearchGroup Class</p>
 * <p>Description: A collection of scientist and/or clinician users with a common
 * research objective related to biospecimen collection and utilization.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.CancerResearchGroupForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * A collection of scientist and/or clinician users with a common research objective related
 * to biospecimen collection and utilization.
 * @hibernate.class table="CATISSUE_CANCER_RESEARCH_GROUP"
 */
public class CancerResearchGroup extends AbstractDomainObject implements Serializable
{

	/**
	 * Serial Id for class.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * Name of the cancer research group.
	 */
	protected String name;

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 */
	public CancerResearchGroup()
	{
		super();
		// Default Constructor, required for Hibernate
	}

	/**
	 * Parameterized constructor.
	 * @param form AbstractActionForm.
	 */
	public CancerResearchGroup(AbstractActionForm form)
	{
		super();
		setAllValues(form);
	}

	/**
	 * Returns the unique id of the cancer research group.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_CANCER_RES_GRP_SEQ"
	 * @return a unique id assigned to the cancer research group.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * Sets an id for the cancer research group.
	 * @param identfier Unique id to be assigned to the cancer research group.
	 */
	public void setId(Long identfier)
	{
		this.id = identfier;
	}

	/**
	 * Returns the name of the cancer research group.
	 * @hibernate.property name="name" type="string" column="NAME" length="255"
	 * not-null="true" unique="true"
	 * @return name of the department.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of the cancer research group.
	 * @param name Name of the cancer research group.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues
	 * (edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	/**
	 * Set all values on the form object.
	 * @param abstractForm IValueObject.
	 */
	public void setAllValues(IValueObject abstractForm)
	{
		final CancerResearchGroupForm cancerResearchGroupForm = (CancerResearchGroupForm) abstractForm;
		this.name = cancerResearchGroupForm.getName().trim();
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String type label.
	 */
	public String getMessageLabel()
	{
		return this.name;
	}
}