/**
 * <p>Title: InfectionAgent Class>
 * <p>Description:  Models the infection agent information. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;

/**
 * Models the infection agent information.
 * @hibernate.class table="CATISSUE_INFECTION_AGENT" 
 * @author gautam_shetty
 */
public class InfectionAgent extends AbstractDomainObject implements Serializable
{

    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each infection agent.
     * */
    protected Long identifier;

    /**
     * Name of the infection.
     */
    protected String name;

    /**
     * 
     */
    private Collection accessionCollection = new HashSet();

    /**
     * Returns the unique identifier assigned to this infection agent.
     * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the unique identifier assigned to this infection agent.
     * @see #setIdentifier(int)
     * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets the unique identifier assigned to this infection agent.
     * @param identifier the unique identifier assigned to this infection agent.
     * @see #getIdentifier()
     */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the name of the infection.
     * @hibernate.property name="name" type="string" column="NAME" length="50"
     * @return the name of the infection.
     * @see #setName(String)
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the infection.
     * @param name the name of the infection.
     * @see #getName()
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
	 * @hibernate.set name="accessionCollection" table="CATISSUE_ACCESSION_INFECTION"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="INFECTION_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.Accession" column="ACCESSION_ID"
	 * @see #setAccessionCollection(Collection)
	 */
    public Collection getAccessionCollection()
    {
        return accessionCollection;
    }

    /**
     * TODO
     * @param accessionCollection
     */
    public void setAccessionCollection(Collection accessionCollection)
    {
        this.accessionCollection = accessionCollection;
    }

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm)
    {

    }
}