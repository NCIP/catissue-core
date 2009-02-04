/**
 * <p>Title: ExternalIdentifier Class>
 * <p>Description: A pre-existing, externally defined 
 * id associated with a specimen.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * A pre-existing, externally defined 
 * id associated with a specimen.
 * @hibernate.class table="CATISSUE_EXTERNAL_IDENTIFIER"
 * @author gautam_shetty
 */
public class ExternalIdentifier extends AbstractDomainObject implements java.io.Serializable
{
    private static final long serialVersionUID = 1234567890L;

    /**
     * System generated unique id.
     */
    protected Long id;

    /**
     * Name of the legacy id.
     */
    protected String name;

    /**
     * Value of the legacy id.
     */
    protected String value;
    
    protected Specimen specimen;
	
    /**
     * Returns the system generated unique id.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_EXTERNAL_ID_SEQ"
     * @return the system generated unique id.
     * @see #setId(Long)
     * */
    public Long getId()
    {
        return id;
    }

    /**
     * Sets the system generated unique id.
     * @param id the system generated unique id.
     * @see #getId()
     * */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * Returns the name of the legacy id.
     * @hibernate.property name="name" type="string" 
     * column="NAME" length="255"
     * @return the name of the legacy id.
     * @see #setName(String)
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the legacy id.
     * @param name the name of the legacy id.
     * @see #getName()
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the value of the legacy id.
     * @hibernate.property name="value" type="string" 
     * column="VALUE" length="255"
     * @return the value of the legacy id.
     * @see #setValue(String)
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the value of the legacy id.
     * @param value the value of the legacy id.
     * @see #getValue()
     */
    public void setValue(String value)
    {
        this.value = value;
    }
    
	/**
     * @hibernate.many-to-one column="SPECIMEN_ID"  class="edu.wustl.catissuecore.domain.Specimen" constrained="true"
	 * @see #setParticipant(Site)
     */
	public Specimen getSpecimen() 
	{
		return specimen;
	}
	
	/**
	 * @param specimen The specimen to set.
	 */
	public void setSpecimen(Specimen specimen) 
	{
		this.specimen = specimen;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
	
		
	}
	
}