/**
 * <p>Title: CancerResearchGroup Class</p>
 * <p>Description: A collection of scientist and/or clinician users with a common research objective related to biospecimen collection and utilization.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * A collection of scientist and/or clinician users with a common research objective related to biospecimen collection and utilization.
 * @hibernate.class table="CATISSUE_CANCER_RESEARCH_GROUP"
 */
public class CancerResearchGroup extends AbstractDomainObject implements Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique identifier.
	 */
	protected Long systemIdentifier;
	
	/**
	 * Name of the cancer research group.
	 */
	protected String name;

	/**
	 * Returns the unique identifier of the cancer research group.
	 * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long"
	 * length="30" unsaved-value="null" generator-class="native"
	 * @return a unique systemIdentifier assigned to the cancer research group.
	 */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
	 * Sets an identifier for the cancer research group.
	 * 
	 * @param systemIdentifier Unique identifier to be assigned to the cancer research group.
	 */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
	 * Returns the name of the cancer research group.
	 * @hibernate.property name="name" type="string" column="NAME" length="50"
	 * not-null="true" unique="true"
	 * @return name of the department.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name of the cancer research group.
	 * 
	 * @param name Name of the cancer research group.
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