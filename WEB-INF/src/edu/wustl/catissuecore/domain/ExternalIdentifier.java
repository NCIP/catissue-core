/**
 * <p>Title: ExternalIdentifier Class>
 * <p>Description: A pre-existing, externally defined 
 * identifier associated with a specimen.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;

/**
 * A pre-existing, externally defined 
 * identifier associated with a specimen.
 * @hibernate.class table="CATISSUE_EXTERNAL_IDENTIFIER"
 * @author gautam_shetty
 */
public class ExternalIdentifier implements Serializable
{
    private static final long serialVersionUID = 1234567890L;

    /**
     * System generated unique identifier.
     */
    protected Long systemIdentifier;

    /**
     * Name of the legacy identifier.
     */
    protected String name;

    /**
     * Value of the legacy identifier.
     */
    protected String value;
    
    protected Specimen specimen;
	
    /**
     * Returns the system generated unique identifier.
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the system generated unique identifier.
     * @see #setSystemIdentifier(Long)
     * */
    public Long getSystemIdentifier()
    {
        return systemIdentifier;
    }

    /**
     * Sets the system generated unique identifier.
     * @param identifier the system generated unique identifier.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(Long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }

    /**
     * Returns the name of the legacy identifier.
     * @hibernate.property name="name" type="string" 
     * column="NAME" length="50"
     * @return the name of the legacy identifier.
     * @see #setName(String)
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the legacy identifier.
     * @param name the name of the legacy identifier.
     * @see #getName()
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the value of the legacy identifier.
     * @hibernate.property name="value" type="string" 
     * column="VALUE" length="50"
     * @return the value of the legacy identifier.
     * @see #setValue(String)
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the value of the legacy identifier.
     * @param value the value of the legacy identifier.
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
}